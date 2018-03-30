package name.leesah.nirvana.ui.medication.repeating;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.in_development, container, false);
    }
}
