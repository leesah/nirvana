package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.ui.medication.StrategyEditFragment;

import static name.leesah.nirvana.R.layout.in_development;

//TODO
public class NTimesADayEditFragment extends StrategyEditFragment.Reminding {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(in_development, container, false);
    }

    @NonNull
    @Override
    protected RemindingStrategy readStrategy() {
        return null;
    }

    @Override
    protected void updateView(RemindingStrategy strategy) {

    }
}
