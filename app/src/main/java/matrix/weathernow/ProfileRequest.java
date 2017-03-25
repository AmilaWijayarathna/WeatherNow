package matrix.weathernow;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * send username for get user's profile data
 * expectedd results score, total updates, accurate updates
 * Created by amila on 3/13/17.
 */



    public class ProfileRequest extends StringRequest {
        private  static final String PROFILE_REQUEST_URL = "http://matrix.projects.mrt.ac.lk:3000/profileAndroid";
        private Map<String, String> params;
        public ProfileRequest(String username, Response.Listener<String> listener) {


            super(Method.POST,PROFILE_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("username",username);

        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

