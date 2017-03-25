package matrix.weathernow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);

        final Button login = (Button) findViewById(R.id.login);

        SharedPreferences sharedPref= getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String tempUsername = sharedPref.getString("username","");
        String tempPassword = sharedPref.getString("password","");

        if(!tempPassword.equals("")) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {

                            Toast.makeText(getBaseContext(), "Login Successfull", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login.this, Navigation.class);
                            Login.this.startActivity(intent);


                        } else {
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                        builder.setMessage("Login Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();*/
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            LoginRequest loginRequest = new LoginRequest(tempUsername, tempPassword, responseListener);
            RequestQueue queue = Volley.newRequestQueue(Login.this);
            queue.add(loginRequest);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nusername = username.getText().toString();
                final String npassword = password.getText().toString();

                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                SharedPreferences sharedPref= getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor =sharedPref.edit();
                                editor.putString("username", username.getText().toString());
                                editor.putString("password", password.getText().toString());
                                editor.apply();
                                Toast.makeText( getBaseContext(),"Login Data saved.\nLogin Successfull", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Login.this, Navigation.class);
                                Login.this.startActivity(intent);



                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(nusername, npassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(loginRequest);
            }
        });
    }



}
