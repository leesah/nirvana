package name.leesah.nirvana.model.medication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import com.google.common.base.MoreObjects;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;

import static android.content.Context.MODE_PRIVATE;
import static name.leesah.nirvana.ui.medication.MedicationActivity.STAGING;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;

/**
 * Created by sah on 2016-12-07.
 */
@Keep
public class Medication implements Comparable<Medication>{

    private final int id;
    private final String name;
    private final String manufacturer;
    private final DosageForm form;
    private final RepeatingStrategy repeatingStrategy;
    private final RemindingStrategy remindingStrategy;
    private final StartingStrategy startingStrategy;
    private final StoppingStrategy stoppingStrategy;

    private Medication(int id, String name, String manufacturer, DosageForm form, RemindingStrategy remindingStrategy, RepeatingStrategy repeatingStrategy, StartingStrategy startingStrategy, StoppingStrategy stoppingStrategy) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.form = form;
        this.repeatingStrategy = repeatingStrategy;
        this.remindingStrategy = remindingStrategy;
        this.startingStrategy = startingStrategy;
        this.stoppingStrategy = stoppingStrategy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public DosageForm getForm() {
        return form;
    }

    public RepeatingStrategy getRepeatingStrategy() {
        return repeatingStrategy;
    }

    public RemindingStrategy getRemindingStrategy() {
        return remindingStrategy;
    }

    public StartingStrategy getStartingStrategy() {
        return startingStrategy;
    }

    public StoppingStrategy getStoppingStrategy() {
        return stoppingStrategy;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("manufacturer", manufacturer)
                .add("form", form)
                .add("repeatingStrategy", repeatingStrategy)
                .add("remindingStrategy", remindingStrategy)
                .add("startingStrategy", startingStrategy)
                .add("stoppingStrategy", stoppingStrategy)
                .toString();
    }

    @Override
    public int compareTo(@NonNull Medication that) {
        return this.getName().compareTo(that.name);
    }

    /**
     * Created by sah on 2016-12-07.
     */
    public static class Builder {
        public static final int INVALID_ID = 0;
        private int id = uniqueInt();
        private String name;
        private String manufacturer;
        private DosageForm form;
        private RemindingStrategy remindingStrategy;
        private RepeatingStrategy repeatingStrategy;
        private StartingStrategy startingStrategy;
        private StoppingStrategy stoppingStrategy;

        public Medication build() {
            if (id == INVALID_ID || name == null || form == null ||
                    remindingStrategy == null || repeatingStrategy == null ||
                    startingStrategy == null || stoppingStrategy == null)
                throw new IllegalArgumentException("Premature invocation on builder.");

            return new Medication(
                    id, name, manufacturer == null ? "" : manufacturer, form,
                    remindingStrategy, repeatingStrategy, startingStrategy, stoppingStrategy);
        }

        private void setId(int id) {
            this.id = id;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public Builder setForm(DosageForm form) {
            this.form = form;
            return this;
        }

        public Builder setRemindingStrategy(RemindingStrategy remindingStrategy) {
            this.remindingStrategy = remindingStrategy;
            return this;
        }

        public Builder setRepeatingStrategy(RepeatingStrategy repeatingStrategy) {
            this.repeatingStrategy = repeatingStrategy;
            return this;
        }

        public Builder setStartingStrategy(StartingStrategy startingStrategy) {
            this.startingStrategy = startingStrategy;
            return this;
        }

        public Builder setStoppingStrategy(StoppingStrategy stoppingStrategy) {
            this.stoppingStrategy = stoppingStrategy;
            return this;
        }

        public Medication buildFromStaged(Context context) {
            SharedPreferences sp = context.getSharedPreferences(STAGING, MODE_PRIVATE);
            setId(
                    sp.getInt(context.getString(R.string.pref_key_medication_id), uniqueInt()));
            setName(
                    sp.getString(context.getString(R.string.pref_key_medication_name), null));
            setManufacturer(
                    sp.getString(context.getString(R.string.pref_key_medication_manufacturer), null));
            setForm(DosageForm.valueOf(
                    sp.getString(context.getString(R.string.pref_key_medication_dosage_form), null)));
            setRemindingStrategy(getGson().fromJson(
                    sp.getString(context.getString(R.string.pref_key_medication_reminding), null), RemindingStrategy.class));
            setRepeatingStrategy(getGson().fromJson(
                    sp.getString(context.getString(R.string.pref_key_medication_repeating), null), RepeatingStrategy.class));
            setStartingStrategy(getGson().fromJson(
                    sp.getString(context.getString(R.string.pref_key_medication_starting), null), StartingStrategy.class));
            setStoppingStrategy(getGson().fromJson(
                    sp.getString(context.getString(R.string.pref_key_medication_stopping), null), StoppingStrategy.class));
            return build();
        }

    }
}
