package matrix.weathernow;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amila on 3/22/17.
 */

public class RegisterRequest extends StringRequest {
    private  static final String REGISTER_REQUEST_URL = "http://matrix.projects.mrt.ac.lk:3000/signup";
    private Map<String, String> params;
    public RegisterRequest(String name, String email,String password, Response.Listener<String> listener) {


        super(Method.POST,REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username",name);
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
