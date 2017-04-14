package name.leesah.nirvana.model.reminder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import name.leesah.nirvana.data.Therapist;

import static org.mockito.Mockito.mock;

/**
 * Created by sah on 2017-04-07.
 */
public class ReminderFactoryTest {
    private Therapist therapist;

    @Before
    public void setUp() throws Exception {
        therapist = mock(Therapist.class);
        Therapist.setInstance(therapist);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createReminders() throws Exception {
    }

    @Test
    public void createReminders1() throws Exception {
    }

}