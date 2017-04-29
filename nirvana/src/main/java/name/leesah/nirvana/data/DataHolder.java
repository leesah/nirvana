package name.leesah.nirvana.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import name.leesah.nirvana.utils.AdaptedGsonFactory;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by sah on 2016-12-14.
 */
abstract class DataHolder {
    protected final Resources resources;
    final SharedPreferences preferences;
    final Gson gson;

    DataHolder(Context context) {
        resources = context.getResources();
        preferences = getDefaultSharedPreferences(context);
        gson = AdaptedGsonFactory.getGson();
    }

}
