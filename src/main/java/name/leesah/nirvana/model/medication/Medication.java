package name.leesah.nirvana.model.medication;

import android.support.annotation.Nullable;

import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;

import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;
import name.leesah.nirvana.utils.IdentityHelper;

/**
 * Created by sah on 2016-12-07.
 */

public class Medication {

    private final int id;
    private final String name;
    private final String manufacturer;
    private final DosageForm form;
    private final boolean delayed;
    private final Period delayedBy;
    private RepeatingModel repeatingModel;
    private RemindingModel remindingModel;

    public Medication(String name, String manufacturer, DosageForm form, boolean delayed, Period delayedBy, RepeatingModel repeatingModel, RemindingModel remindingModel) {
        this.id = IdentityHelper.uniqueInt();
        this.name = name;
        this.manufacturer = manufacturer;
        this.form = form;
        this.delayed = delayed;
        this.delayedBy = delayedBy;
        this.repeatingModel = repeatingModel;
        this.remindingModel = remindingModel;
    }

    public void setRemindingModel(RemindingModel remindingModel) {
        this.remindingModel = remindingModel;
    }

    public void setRepeatingModel(RepeatingModel repeatingModel) {
        this.repeatingModel = repeatingModel;
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

    public RepeatingModel getRepeatingModel() {
        return repeatingModel;
    }

    public RemindingModel getRemindingModel() {
        return remindingModel;
    }

    public boolean isDelayed() {
        return delayed;
    }

    @Nullable
    public Period getDelayedPeriod() {
        return delayedBy;
    }

    @Override
    public String toString() {
        return String.format("Medication {id=[%d], n=[%s]}", id, name);
    }

}
