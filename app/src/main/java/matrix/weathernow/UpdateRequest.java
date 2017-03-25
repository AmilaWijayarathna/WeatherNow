package matrix.weathernow;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amila on 3/20/17.
 */

public class UpdateRequest extends StringRequest {
    private  static final String UPDATE_REQUEST_URL = "http://matrix.projects.mrt.ac.lk:3000/update";
    private Map<String, String> params;
    public UpdateRequest(String name, String location,String latitude,String longitude,String getWeather, String getCloud,String getWind,  Response.Listener<String> listener) {


        super(Method.POST,UPDATE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", name);
        params.put("location", location);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("weather",getWeather);
        params.put("cloud", getCloud);
        params.put("wind", getWind);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
