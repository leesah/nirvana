package name.leesah.nirvana.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;

import name.leesah.nirvana.utils.AdaptedGsonFactory;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by sah on 2016-12-14.
 */
abstract class DataHolder {
    protected final Context context;
    protected final Resources resources;
    final SharedPreferences preferences;
    final Gson gson;

    DataHolder(Context context) {
        this.context = context;
        this.resources = context.getResources();
        this.preferences = getDefaultSharedPreferences(context);
        this.gson = AdaptedGsonFactory.getGson();
    }

}
