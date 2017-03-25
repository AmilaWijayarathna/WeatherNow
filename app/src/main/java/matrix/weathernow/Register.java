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

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final EditText etName = (EditText)findViewById(R.id.name);
        final EditText etEmail = (EditText)findViewById(R.id.email);
        final  EditText etPassword = (EditText) findViewById(R.id.password);
        final Button etSignin = (Button)findViewById(R.id.signin);





        etSignin.setOnClickListener(new View.OnClickListener() {
                                        public void onClick (View v){
                                            if(!("".equals(etName.getText().toString()))&&!("".equals(etEmail.getText().toString()))&&!("".equals(etPassword.getText().toString()))){
                                                final String name = etName.getText().toString();
                                                final String email = etEmail.getText().toString();
                                                final String password = etPassword.getText().toString();

                                                if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+[.]+[a-z]+")) {
                                                    Toast.makeText(getBaseContext(),"invalid email!!", Toast.LENGTH_SHORT).show();
                                                }

                                                else {

                                                    Response.Listener<String> responseListner = new Response.Listener<String>() {


                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonResponse = new JSONObject(response);
                                                                boolean success = jsonResponse.getBoolean("success");


                                                                if (success) {

                                                                    SharedPreferences sharedPref= getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor =sharedPref.edit();
                                                                    editor.putString("username", etName.getText().toString());
                                                                    editor.putString("password", etPassword.getText().toString());
                                                                    editor.apply();
                                                                    Toast.makeText( getBaseContext(),"Login Data saved.\nRegister Successfull", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(Register.this, Navigation.class);
                                                                    Register.this.startActivity(intent);


                                                                } else {

                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                                                    builder.setMessage("Register Failed")
                                                                            .setNegativeButton("Retry", null)
                                                                            .create()
                                                                            .show();


                                                                }


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };
                                                    RegisterRequest registerRequest = new RegisterRequest(name, email, password, responseListner);
                                                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                                                    queue.add(registerRequest);
                                                }
                                            }
                                            else{

                                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                                builder.setMessage("All fields are requeired")
                                                        .setNegativeButton("Retry", null)
                                                        .create()
                                                        .show();

                                            }
                                        }
                                    }
        );


    }
    }

