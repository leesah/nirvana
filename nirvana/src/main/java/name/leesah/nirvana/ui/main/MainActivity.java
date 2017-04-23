package name.leesah.nirvana.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.ui.medication.MedicationListFragment;
import name.leesah.nirvana.ui.reminder.RemindersOfDayFragment;
import name.leesah.nirvana.ui.settings.NotificationSettingsFragment;
import name.leesah.nirvana.ui.settings.SettingsFragment;
import name.leesah.nirvana.ui.settings.treatment.TreatmentRepeatingModelSelectFragment;
import name.leesah.nirvana.ui.settings.treatment.TreatmentSettingsFragment;

import static android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_NAV_POS = "navigation position";
    private final MedicationListFragment medicationListFragment = new MedicationListFragment();
    private final RemindersOfDayFragment remindersOfDayFragment = new RemindersOfDayFragment();
    private final SettingsFragment settingsFragment = new SettingsFragment();
    private final TreatmentSettingsFragment treatmentSettingsFragment = new TreatmentSettingsFragment();
    private final NotificationSettingsFragment notificationSettingsFragment = new NotificationSettingsFragment();
    private final TreatmentRepeatingModelSelectFragment treatmentRepeatingModelSelectFragment = new TreatmentRepeatingModelSelectFragment();
    private FragmentManager fragmentManager;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSettingsFragment();

        fragmentManager = getFragmentManager();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this::onNavigation);

        showDefaultView();
    }

    private void showDefaultView() {
        boolean newUser = Pharmacist.getInstance(this).getMedications().isEmpty();
        navigation.setSelectedItemId(newUser ? R.id.navigation_medications : R.id.navigation_reminders);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        restoreStatus(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        return fragmentManager.findFragmentById(R.id.content_main);
    }

    private void clearBackStackAndReplaceFragment(Fragment fragment) {
        fragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, fragment)
                .commit();
    }

    private void initSettingsFragment() {
        settingsFragment.setTreatmentListener(preference -> showTreatmentSettings());
        settingsFragment.setNotificationListener(preference -> showNotificationsSettings());
        treatmentSettingsFragment.setRepeatingModelListener(preference -> showRepeatingModelSettings());
    }

    private boolean showTreatmentSettings() {
        replaceFragmentAndAddToBackStack(treatmentSettingsFragment);
        return true;
    }

    private boolean showNotificationsSettings() {
        replaceFragmentAndAddToBackStack(notificationSettingsFragment);
        return true;
    }

    private boolean showRepeatingModelSettings() {
        replaceFragmentAndAddToBackStack(treatmentRepeatingModelSelectFragment);
        return true;
    }

    private void replaceFragmentAndAddToBackStack(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

}
