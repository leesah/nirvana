package name.leesah.nirvana.ui.reminder;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by sah on 2016-12-08.
 */
public class SchedulingServiceTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void onHandleIntent() throws Exception {

    }

    @Test
    public void midnight(){
        DateTime midnight = new DateTime().withTimeAtStartOfDay().plus(Days.ONE);
    }
}