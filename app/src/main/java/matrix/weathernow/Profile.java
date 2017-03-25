package matrix.weathernow;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {


    View prof;
    Navigation n;
    TextView name;

    public Profile() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedPref= getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        final String tempUsername = sharedPref.getString("username","");
        // Inflate the layout for this fragment

        final View prof=  inflater.inflate(R.layout.fragment_profile, container, false);
        final Button updateProfile =(Button) prof.findViewById(R.id.updateProfile);
        final TextView name= (TextView)prof.findViewById(R.id.name) ;
        final TextView score= (TextView)prof.findViewById(R.id.score) ;
        final TextView updated = (TextView)prof.findViewById(R.id.total) ;
        final TextView accurate= (TextView)prof.findViewById(R.id.accurate) ;

        name.setText(tempUsername);




        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListner = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse= new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                score.setText(jsonResponse.getString("rate"));
                                updated.setText(jsonResponse.getString("totalUpdates"));
                                accurate.setText(jsonResponse.getString("accurateUpdates"));

                                Toast.makeText(getActivity(),"Profile updated",Toast.LENGTH_SHORT).show();



                            }
                            else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(n);
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
                ProfileRequest profileRequest = new ProfileRequest(tempUsername, responseListner);
                RequestQueue queue = Volley.newRequestQueue(n);
                queue.add(profileRequest);

            }
        });
        return prof;
    }

}
