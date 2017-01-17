package utils;

import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

public class ParseJSON {
    public static String[] ids;
    public static String[] times;
    public static String[] temps;
    public static String[] hums;
    public static String[] winds;
    public static String[] press;

    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id";
    public static final String KEY_TIME = "time";
    public static final String KEY_TEMP = "temp";
    public static final String KEY_HUM = "hum";
    public static final String KEY_WIND = "wind";
    public static final String KEY_PRES = "pres";

    private JSONArray users = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    public void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

            ids = new String[users.length()];
            times = new String[users.length()];
            temps = new String[users.length()];
            hums = new String[users.length()];
            winds = new String[users.length()];
            press = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                ids[i] = jo.getString(KEY_ID);
                times[i] = "Data: " + jo.getString(KEY_TIME);
                temps[i] = "Temp: " + jo.getString(KEY_TEMP) + " °C";
                hums[i] = "Umid: " + jo.getString(KEY_HUM) + " %";
                winds[i] = "Vant: " + jo.getString(KEY_WIND).substring(0,4) + " mps";
                press[i] = "Pres: " + jo.getString(KEY_PRES) + " hPa";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}