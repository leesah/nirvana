package name.leesah.nirvana.model.treatment;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by sah on 2017-04-06.
 */
public class TreatmentBuilderTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void buildEverlastingTreatment() throws Exception {
        Treatment everlastingTreatment = new TreatmentBuilder().buildEverlastingTreatment();
        LocalDate randomFutureDate = today().plusDays(new Random().nextInt());
        TreatmentCycle cycle = everlastingTreatment.getRepeatingModel().getCycleForDate(everlastingTreatment.getFirstCycle(), randomFutureDate);
        assertNotNull(cycle);
        assertTrue(randomFutureDate.equals(cycle.getFirstDay()));
        assertTrue(randomFutureDate.equals(cycle.getLastDay()));
    }

}