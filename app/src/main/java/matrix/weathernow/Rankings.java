package matrix.weathernow;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Rankings extends Fragment {


    //View rank;
    ListView list;
    public Rankings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rank =  inflater.inflate(R.layout.fragment_rankings, container, false);
        final ListView list = (ListView) rank.findViewById(R.id.rankList);
        Button update = (Button)  rank.findViewById(R.id.rankButton);
        final ArrayList<HashMap<String, String>> rankList = new ArrayList<>();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListner = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject jsonResponse  = new JSONObject(response);
                            JSONArray ranks = jsonResponse.getJSONArray("leaderboard");
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                for (int i = 0; i < ranks.length(); i++) {
                                    JSONObject c = ranks.getJSONObject(i);

                                    String username = c.getString("username");
                                    String score = c.getString("rate");

                                    HashMap<String, String> rank = new HashMap<>();
                                    rank.put("username", username);
                                    rank.put("rate", score);

                                    rankList.add(rank);

                                    ListAdapter adapter = new SimpleAdapter(
                                            getContext(), rankList,
                                            R.layout.ranking_list, new String[]{"username", "rate",},
                                            new int[]{R.id.name, R.id.score});

                                    list.setAdapter(adapter);
                                    Toast.makeText(getContext(),"Updated", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Updating Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                };
                RankRequest rankRequest = new RankRequest( responseListner);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(rankRequest);
            }
        });



        return rank;

    }


}
