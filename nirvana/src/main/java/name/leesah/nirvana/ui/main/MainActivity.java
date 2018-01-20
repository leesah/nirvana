package name.leesah.nirvana.ui.main;

import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;

import name.leesah.nirvana.R;
import name.leesah.nirvana.ui.settings.SettingsFragment;
import name.leesah.nirvana.ui.settings.treatment.TreatmentSettingsFragment;

import static android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.R.color.accent;
import static name.leesah.nirvana.R.string.notification_channel_description_reminder;
import static name.leesah.nirvana.R.string.notification_channel_id_reminder;
import static name.leesah.nirvana.R.string.notification_channel_name_reminder;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_NAV_POS = "name.leesah.nirvana:key:NAVIGATION_POSITION";
    private final MedicationListFragment medicationListFragment = new MedicationListFragment();
    private final RemindersOfDayFragment remindersOfDayFragment = new RemindersOfDayFragment();
    private final SettingsFragment settingsFragment = new SettingsFragment();
    private final TreatmentSettingsFragment treatmentSettingsFragment = new TreatmentSettingsFragment();
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeNotificationChannel();
        initializeSettingsFragment();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this::onNavigation);

        showDefaultView();
    }

    private void initializeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel reminderChannel = new NotificationChannel(getString(notification_channel_id_reminder), getString(notification_channel_name_reminder), IMPORTANCE_HIGH);
            reminderChannel.setDescription(getString(notification_channel_description_reminder));
            reminderChannel.setLightColor(getColor(accent));
            reminderChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            getSystemService(NotificationManager.class).createNotificationChannel(reminderChannel);
        }
    }

    private void showDefaultView() {
        boolean newUser = pharmacist(this).getMedications().isEmpty();
        navigation.setSelectedItemId(newUser ? R.id.navigation_medications : R.id.navigation_reminders);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        restoreStatus(savedInstanceState);
    }

    private void restoreStatus(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt(KEY_NAV_POS, 0);
            navigation.setSelectedItemId(position);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_NAV_POS, navigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    private boolean onNavigation(MenuItem item) {
        Bundle params = new Bundle();
        params.putCharSequence("item_title", item.getTitle());
        FirebaseAnalytics.getInstance(this).logEvent("main_navigate", params);

        switch (item.getItemId()) {
            case R.id.navigation_medications:
                clearBackStackAndReplaceFragment(medicationListFragment);
                return true;

            case R.id.navigation_reminders:
                clearBackStackAndReplaceFragment(remindersOfDayFragment);
                return true;

            case R.id.navigation_settings:
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment != treatmentSettingsFragment)
                    clearBackStackAndReplaceFragment(settingsFragment);
                return true;

            default:
                return false;
        }
    }

    private Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(R.id.content_main);
    }

    private void clearBackStackAndReplaceFragment(Fragment fragment) {
        getFragmentManager().popBackStack(null, POP_BACK_STACK_INCLUSIVE);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment)
                .commit();
    }

    private void initializeSettingsFragment() {
        settingsFragment.setTreatmentListener(preference -> showTreatmentSettings());
    }

    private boolean showTreatmentSettings() {
        replaceFragmentAndAddToBackStack(treatmentSettingsFragment);
        return true;
    }

    private void replaceFragmentAndAddToBackStack(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

}
