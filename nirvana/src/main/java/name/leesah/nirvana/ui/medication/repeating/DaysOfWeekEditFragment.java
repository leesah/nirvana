package name.leesah.nirvana.ui.medication.repeating;

import android.support.annotation.NonNull;

import java.util.EnumSet;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.Weekday;
import name.leesah.nirvana.model.medication.repeating.DaysOfWeek;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.ui.medication.StrategyEditFragment;

/**
 * Created by sah on 2017-04-16.
 */

public class DaysOfWeekEditFragment extends StrategyEditFragment.Repeating {

    @NonNull
    @Override
    protected DaysOfWeek readStrategy() {
        return new DaysOfWeek(EnumSet.allOf(Weekday.class));
    }

    @Override
    protected void updateView(RepeatingStrategy strategy) {

    }

}
