package matrix.weathernow;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserUpload extends AppCompatActivity {

    RadioGroup weather, cloud, wind;
    RadioButton weatherType, windType, cloudType;
    Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_upload);
        final String location = getIntent().getStringExtra("location");
        final String latitide = getIntent().getStringExtra("latitude");
        final String longitude = getIntent().getStringExtra("longitude");

        SharedPreferences sharedPref= getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        final String tempUsername = sharedPref.getString("username","");


        weather = (RadioGroup) findViewById(R.id.weather);
        cloud = (RadioGroup) findViewById(R.id.cloud);
        wind = (RadioGroup) findViewById(R.id.wind);
        update = (Button) findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( weather.getCheckedRadioButtonId() == -1 || cloud.getCheckedRadioButtonId() == -1 || wind.getCheckedRadioButtonId() == -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserUpload.this);
                    builder.setMessage("Check all Buttons")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();

                }
                else {
                    int selected1 = weather.getCheckedRadioButtonId();
                    weatherType = (RadioButton) findViewById(selected1);
                    final String getWeather = weatherType.getText().toString();

                    int selected2 = cloud.getCheckedRadioButtonId();
                    cloudType = (RadioButton) findViewById(selected2);
                    final String getCloud = cloudType.getText().toString();

                    int selected3 = wind.getCheckedRadioButtonId();
                    windType = (RadioButton) findViewById(selected3);
                    final String getWind = windType.getText().toString();

                    Toast.makeText(UserUpload.this, tempUsername, Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserUpload.this, location, Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserUpload.this, latitide, Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserUpload.this, longitude, Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserUpload.this, getWeather, Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserUpload.this, getCloud, Toast.LENGTH_SHORT).show();
                    Toast.makeText(UserUpload.this, getWind, Toast.LENGTH_SHORT).show();


                    Response.Listener<String> responsListner = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                Boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UserUpload.this);
                                    builder.setMessage("Update Successful")
                                            .create()
                                            .show();

                                    //Thread.sleep(10000);
                                    new Thread() {
                                        public void run() {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            finish();
                                        }
                                    }.start();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UserUpload.this);
                                    builder.setMessage("Try Again")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    UpdateRequest updateRequest = new UpdateRequest(tempUsername, location, latitide, longitude, getWeather, getCloud, getWind, responsListner);
                    RequestQueue queue = Volley.newRequestQueue(UserUpload.this);
                    queue.add(updateRequest);

                }




            }
        });
    }
}
