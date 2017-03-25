package matrix.weathernow;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by amila on 3/21/17.
 */

public class RankRequest extends StringRequest {
    private static final String PROFILE_REQUEST_URL = "http://matrix.projects.mrt.ac.lk:3000/leaderboard";

    public RankRequest( Response.Listener<String> listener) {


        super(Request.Method.GET, PROFILE_REQUEST_URL, listener, null);


    }
}
