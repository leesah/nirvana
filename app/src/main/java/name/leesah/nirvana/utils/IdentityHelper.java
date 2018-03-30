package name.leesah.nirvana.utils;

import java.util.UUID;

/**
 * Created by sah on 2016-12-11.
 */
public class IdentityHelper {

    public static int uniqueInt() {
        return UUID.randomUUID().hashCode();
    }
}
