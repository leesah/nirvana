package name.leesah.nirvana.model.medication;

import org.joda.time.Period;

import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;

/**
 * Created by sah on 2016-12-07.
 */
public class MedicationBuilder {
    private String name;
    private String manufacturer;
    private DosageForm form;
    private boolean delayed = false;
    private RepeatingStrategy repeatingStrategy;
    private RemindingStrategy remindingStrategy;
    private Period delayedBy;

    public Medication build() {
        return new Medication(name, manufacturer == null ? "" : manufacturer, form, delayed, delayedBy, repeatingStrategy, remindingStrategy);
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

    public MedicationBuilder setDelayedBy(Period period) {
        this.delayedBy = period;
        return this;
    }

    public MedicationBuilder setRepeatingStrategy(RepeatingStrategy repeatingStrategy) {
        this.repeatingStrategy = repeatingStrategy;
        return this;
    }

    public MedicationBuilder setRemindingStrategy(RemindingStrategy remindingStrategy) {
        this.remindingStrategy = remindingStrategy;
        return this;
    }

}
