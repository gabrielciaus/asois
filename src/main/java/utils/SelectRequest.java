package utils;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gabriel Nicolae on 16.01.2017.
 */

public class SelectRequest extends StringRequest{

    private static final String SELECT_REQUEST_URL = "https://cgn.000webhostapp.com/select.php";
    private Map<String, String> params;

    public SelectRequest(Response.Listener listener){
        super(Method.POST, SELECT_REQUEST_URL, listener, null);
        params = new HashMap<>();

       /* params.put("temp", temp + "");
        params.put("hum", hum + "");
        params.put("wind", wind + "");
        params.put("press", press + "");*/
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
