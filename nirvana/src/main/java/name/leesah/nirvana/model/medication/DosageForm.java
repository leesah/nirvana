package name.leesah.nirvana.model.medication;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.DatePicker;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import static java.lang.String.format;
import static java.util.Arrays.*;
import static java.util.EnumSet.allOf;
import static org.apache.commons.lang3.text.WordUtils.*;

/**
 * Created by sah on 2016-12-07.
 */
public enum DosageForm {
    TABLET, CAPSULE, INJECTION;

    public String getName(Context context) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(name().toLowerCase(), "string", context.getPackageName());
        if (resId == 0) {
            String msg = format("Missing resource [R.string.%s].", name().toLowerCase());
            Log.wtf(name(), msg);
            throw new IllegalStateException(msg);
        }
        return resources.getString(resId);
    }

    public String getDosageString(Context context, int quantity) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(name().toLowerCase() + "_dosage_formatter", "plurals", context.getPackageName());
        if (resId == 0) {
            String msg = format("Missing resource [R.plurals.%s].", name().toLowerCase() + "_dosage_formatter");
            Log.wtf(name(), msg);
            throw new IllegalStateException(msg);
        }
        return format(resources.getQuantityString(resId, quantity), quantity);
    }

    public static DosageForm withName(Context context, String name) {
        return new ArrayList<>(allOf(DosageForm.class)).stream()
                .filter(form -> form.getName(context).equals(name))
                .reduce((a, b) -> {
                    throw new IllegalStateException(format("[%s] matches more than one dosage forms: [%s, %s].", name, a, b));
                })
                .get();
    }
}
