package name.leesah.nirvana.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.ui.medication.MedicationListFragment;
import name.leesah.nirvana.ui.reminder.RemindersOfDayFragment;
import name.leesah.nirvana.ui.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_NAV_POS = "navigation position";
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                replaceContent(new MedicationListFragment());
                return true;
            case R.id.navigation_reminders:
                replaceContent(new RemindersOfDayFragment());
                return true;
            case R.id.navigation_settings:
                replaceContent(new SettingsFragment());
                return true;
        }
        return false;
    }

    private void replaceContent(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment)
                .commit();
    }

}
