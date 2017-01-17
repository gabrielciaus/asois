package data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Gabriel Nicolae on 16.01.2017.
 */

public class CityPreference {
    SharedPreferences sharedPreferences;

    public CityPreference(Activity activity){
        sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return sharedPreferences.getString("city", "Bucharest,ro");
    }

    public void setCity(String city){
        sharedPreferences.edit().putString("city", city).commit();
    }

}
