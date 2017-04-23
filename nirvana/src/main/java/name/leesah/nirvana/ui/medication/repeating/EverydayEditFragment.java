package name.leesah.nirvana.ui.medication.repeating;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;

/**
 * Created by sah on 2017-03-15.
 */

public class EverydayEditFragment extends RepeatingModelEditFragment {

    private Everyday editingExisting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.everyday, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportValidity(true);
    }

    @Override
    public RepeatingStrategy readModel() {
        return new Everyday();
    }


    public void setEditingExisting(Everyday editingExisting) {
        this.editingExisting = editingExisting;
    }
}
