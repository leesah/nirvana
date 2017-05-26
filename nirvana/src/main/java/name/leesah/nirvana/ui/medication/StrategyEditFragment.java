package name.leesah.nirvana.ui.medication;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.reflect.TypeToken;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.text.TextUtils.isEmpty;
import static name.leesah.nirvana.ui.medication.MedicationActivity.STAGING;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;

/**
 * Created by sah on 2017-05-02.
 */

public abstract class StrategyEditFragment<StrategyType> extends Fragment {

    private MenuItem saveButton;
    private boolean saveButtonEnabled = false;
    private final TypeToken<StrategyType> strategyType;
    protected FirebaseAnalytics analytics;

    protected StrategyEditFragment(TypeToken<StrategyType> strategyType) {
        this.strategyType = strategyType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        analytics = FirebaseAnalytics.getInstance(getContext());

        String json = getSharedPreferences().getString(getPreferenceKey(), null);
        if (!isEmpty(json)) updateView(getGson().fromJson(json, strategyType.getType()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_button, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        saveButton = menu.findItem(R.id.save_button);
        saveButton.setEnabled(saveButtonEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button:
                String json = getGson().toJson(readStrategy(), strategyType.getType());
                getSharedPreferences().edit().putString(getPreferenceKey(), json).apply();

                Bundle params = new Bundle();
                params.putString("json", json);
                analytics.logEvent("strategy_save", params);

                getActivity().setResult(RESULT_OK);
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setSaveButtonEnabled(boolean enabled) {
        saveButtonEnabled = enabled;
        if (saveButton != null)
            saveButton.setEnabled(saveButtonEnabled);
    }

    protected abstract SharedPreferences getSharedPreferences();

    @NonNull
    protected abstract StrategyType readStrategy();

    protected abstract void updateView(StrategyType strategy);

    protected abstract String getPreferenceKey();

    public static abstract class Recurring extends StrategyEditFragment<RecurringStrategy> {
        public Recurring() {
            super(new TypeToken<RecurringStrategy>() {
            });
        }

        @Override
        protected String getPreferenceKey() {
            return getString(R.string.pref_key_treatment_recurring);
        }

        @Override
        protected SharedPreferences getSharedPreferences() {
            return getDefaultSharedPreferences(getContext());
        }
    }

    public static abstract class Reminding extends StrategyEditFragment<RemindingStrategy> {
        public Reminding() {
            super(new TypeToken<RemindingStrategy>() {
            });
        }

        @Override
        protected String getPreferenceKey() {
            return getString(R.string.pref_key_medication_reminding);
        }

        @Override
        protected SharedPreferences getSharedPreferences() {
            return getContext().getSharedPreferences(STAGING, MODE_PRIVATE);
        }
    }

    public static abstract class Repeating extends StrategyEditFragment<RepeatingStrategy> {
        public Repeating() {
            super(new TypeToken<RepeatingStrategy>() {
            });
        }

        @Override
        protected String getPreferenceKey() {
            return getString(R.string.pref_key_medication_repeating);
        }

        @Override
        protected SharedPreferences getSharedPreferences() {
            return getContext().getSharedPreferences(STAGING, MODE_PRIVATE);
        }
    }
}
