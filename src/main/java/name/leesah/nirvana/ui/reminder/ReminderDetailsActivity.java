package name.leesah.nirvana.ui.reminder;

import android.app.Activity;
import android.os.Bundle;

import name.leesah.nirvana.R;

public class ReminderDetailsActivity extends Activity {

    public static final String EXTRA_REMINDER_ID = "name.leesah.nirvana:extra:REMINDER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);
    }
}
