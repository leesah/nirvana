package name.leesah.nirvana.ui.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import name.leesah.nirvana.R;
import name.leesah.nirvana.ui.settings.NotificationSettingsFragment;
import name.leesah.nirvana.ui.settings.SettingsFragment;
import name.leesah.nirvana.ui.settings.treatment.TreatmentSettingsFragment;

import static android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.ui.medication.MedicationActivity.REQUEST_CODE_ADD_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationActivity.REQUEST_CODE_EDIT_MEDICATION;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_NAV_POS = "name.leesah.nirvana:key:NAVIGATION_POSITION";
    private final MedicationListFragment medicationListFragment = new MedicationListFragment();
    private final RemindersOfDayFragment remindersOfDayFragment = new RemindersOfDayFragment();
    private final SettingsFragment settingsFragment = new SettingsFragment();
    private final TreatmentSettingsFragment treatmentSettingsFragment = new TreatmentSettingsFragment();
    private final NotificationSettingsFragment notificationSettingsFragment = new NotificationSettingsFragment();
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeSettingsFragment();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this::onNavigation);

        showDefaultView();
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
        switch (item.getItemId()) {
            case R.id.navigation_medications:
                clearBackStackAndReplaceFragment(medicationListFragment);
                return true;

            case R.id.navigation_reminders:
                clearBackStackAndReplaceFragment(remindersOfDayFragment);
                return true;

            case R.id.navigation_settings:
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment != treatmentSettingsFragment && currentFragment != notificationSettingsFragment)
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
        settingsFragment.setNotificationListener(preference -> showNotificationsSettings());
    }

    private boolean showTreatmentSettings() {
        replaceFragmentAndAddToBackStack(treatmentSettingsFragment);
        return true;
    }

    private boolean showNotificationsSettings() {
        replaceFragmentAndAddToBackStack(notificationSettingsFragment);
        return true;
    }

    private void replaceFragmentAndAddToBackStack(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

}
