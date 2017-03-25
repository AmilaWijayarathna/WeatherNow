package matrix.weathernow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Gohere extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gohere);


        Button LogIn=(Button) findViewById(R.id.LogIn);
        Button  SignIn=(Button) findViewById(R.id.SignIn);
        LogIn.setOnClickListener(this);
        SignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LogIn:
                startActivity(new Intent(this,Login.class));


                break;
            case R.id.SignIn:
                startActivity(new Intent(this, Register.class));
                break;



        }
    }
}

