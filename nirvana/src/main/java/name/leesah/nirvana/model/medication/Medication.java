package name.leesah.nirvana.model.medication;

import android.support.annotation.Nullable;

import org.joda.time.Period;

import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;
import name.leesah.nirvana.utils.IdentityHelper;

import static name.leesah.nirvana.utils.IdentityHelper.*;

/**
 * Created by sah on 2016-12-07.
 */

public class Medication {

    private final int id;
    private final String name;
    private final String manufacturer;
    private final DosageForm form;
    private RepeatingStrategy repeatingStrategy;
    private RemindingStrategy remindingStrategy;
    private StartingStrategy startingStrategy;
    private StoppingStrategy stoppingStrategy;

    public Medication(String name, String manufacturer, DosageForm form, RemindingStrategy remindingStrategy, RepeatingStrategy repeatingStrategy, StartingStrategy startingStrategy, StoppingStrategy stoppingStrategy) {
        this.id = uniqueInt();
        this.name = name;
        this.manufacturer = manufacturer;
        this.form = form;
        this.repeatingStrategy = repeatingStrategy;
        this.remindingStrategy = remindingStrategy;
        this.startingStrategy = startingStrategy;
        this.stoppingStrategy = stoppingStrategy;
    }

    public void setRemindingStrategy(RemindingStrategy remindingStrategy) {
        this.remindingStrategy = remindingStrategy;
    }

    public void setRepeatingStrategy(RepeatingStrategy repeatingStrategy) {
        this.repeatingStrategy = repeatingStrategy;
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

    public RepeatingStrategy getRepeatingStrategy() {
        return repeatingStrategy;
    }

    public RemindingStrategy getRemindingStrategy() {
        return remindingStrategy;
    }

    public StartingStrategy getStartingStrategy() {
        return startingStrategy;
    }

    public StoppingStrategy getStoppingStrategy() {
        return stoppingStrategy;
    }

    @Override
    public String toString() {
        return String.format("Medication {id=[%d], n=[%s]}", id, name);
    }

}
