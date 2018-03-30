package name.leesah.nirvana.model.treatment;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

/**
 * Created by sah on 2017-04-29.
 */
@Keep
public interface Treatment {

    boolean contains(@NonNull LocalDate date);

    @Nullable
    LocalDate getStartDateOf(@NonNull LocalDate date);
}
