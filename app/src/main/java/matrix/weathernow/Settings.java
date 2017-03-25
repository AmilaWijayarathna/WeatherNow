package matrix.weathernow;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final   View settings= inflater.inflate(R.layout.fragment_settings, container, false);
        final Button email =(Button) settings.findViewById(R.id.sendMail);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{"teammatrixb14@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT,"");
                email.putExtra(Intent.EXTRA_TEXT,"");

                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email,"Choose email client.."));
            }
        });



        return settings;
    }

}
