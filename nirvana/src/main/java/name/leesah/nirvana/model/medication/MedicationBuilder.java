package name.leesah.nirvana.model.medication;

import org.joda.time.Period;

import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;

/**
 * Created by sah on 2016-12-07.
 */
public class MedicationBuilder {
    private String name;
    private String manufacturer;
    private DosageForm form;
    private RemindingStrategy remindingStrategy;
    private RepeatingStrategy repeatingStrategy;
    private StartingStrategy startingStrategy;
    private StoppingStrategy stoppingStrategy;

    public Medication build() {
        if (name == null || form == null
                //|| remindingStrategy == null || repeatingStrategy == null
                || startingStrategy == null || stoppingStrategy == null)
            throw new IllegalArgumentException("Premature invocation on builder.");
        return new Medication(name, manufacturer == null ? "" : manufacturer, form,
                remindingStrategy, repeatingStrategy, startingStrategy, stoppingStrategy);
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

    public MedicationBuilder setRemindingStrategy(RemindingStrategy remindingStrategy) {
        this.remindingStrategy = remindingStrategy;
        return this;
    }

    public MedicationBuilder setRepeatingStrategy(RepeatingStrategy repeatingStrategy) {
        this.repeatingStrategy = repeatingStrategy;
        return this;
    }

    public MedicationBuilder setStartingStrategy(StartingStrategy startingStrategy) {
        this.startingStrategy = startingStrategy;
        return this;
    }

    public MedicationBuilder setStoppingStrategy(StoppingStrategy stoppingStrategy) {
        this.stoppingStrategy = stoppingStrategy;
        return this;
    }
}
