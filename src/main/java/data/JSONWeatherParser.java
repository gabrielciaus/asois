package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Location;
import model.Weather;
import utils.Utils;

/**
 * Created by Gabriel Nicolae on 10.01.2017.
 */

public class JSONWeatherParser {

    public static Weather getWeather(String data){
        Weather weather = new Weather();
        // create JSON from data

        try {
            JSONObject jsonObject = new JSONObject(data);

            Location location = new Location();
            JSONObject coorObj = Utils.getObject("coord", jsonObject);
            location.setLatitude(Utils.getFloat("lat" , coorObj));
            location.setLongitude(Utils.getFloat("lon", coorObj));

            // Get sys obj
            JSONObject sysObject = Utils.getObject("sys", jsonObject);
            location.setCountry(Utils.getString("country", sysObject));
            location.setLastUpdate(Utils.getInt("dt", jsonObject));
            location.setSunrise(Utils.getInt("sunrise", sysObject));
            location.setSunset(Utils.getInt("sunset", sysObject));
            location.setCity(Utils.getString("name", jsonObject));
            weather.location = location;

            // get the weather objs
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherID(Utils.getInt("id", jsonWeather));
            weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
            weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));

            JSONObject mainObj = Utils.getObject("main", jsonObject);
            weather.currentCondition.setHumidity(Utils.getInt("humidity", mainObj));
            weather.currentCondition.setPressure(Utils.getInt("pressure", mainObj));
            weather.currentCondition.setMinTemp(Utils.getFloat("temp_min", mainObj));
            weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max", mainObj));
            weather.currentCondition.setTemperature(Utils.getDouble("temp", mainObj));

            JSONObject windObjs = Utils.getObject("wind", jsonObject);
            weather.wind.setSpeed(Utils.getFloat("speed", windObjs));
            weather.wind.setDegree(Utils.getFloat("deg", windObjs));

            JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all", cloudObj));

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
