package name.leesah.nirvana.model.medication;

import org.joda.time.ReadablePeriod;

import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;

/**
 * Created by sah on 2016-12-07.
 */
public class MedicationBuilder {
    private String name;
    private String manufacturer;
    private DosageForm form;
    private boolean delayed = false;
    private RepeatingModel repeatingModel;
    private RemindingModel remindingModel;
    private ReadablePeriod delayedBy;

    public Medication build() {
        return new Medication(name, manufacturer, form, delayed, delayedBy, repeatingModel, remindingModel);
    }

    public MedicationBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MedicationBuilder setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public MedicationBuilder setForm(DosageForm form) {
        this.form = form;
        return this;
    }

    public MedicationBuilder setDelayed(boolean delayed) {
        this.delayed = delayed;
        return this;
    }

    public MedicationBuilder setDelayedBy(ReadablePeriod period) {
        this.delayedBy = period;
        return this;
    }

    public MedicationBuilder setRepeatingModel(RepeatingModel repeatingModel) {
        this.repeatingModel = repeatingModel;
        return this;
    }

    public MedicationBuilder setRemindingModel(RemindingModel remindingModel) {
        this.remindingModel = remindingModel;
        return this;
    }

}
