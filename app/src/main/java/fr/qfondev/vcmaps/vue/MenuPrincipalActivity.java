package fr.qfondev.vcmaps.vue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import fr.qfondev.vcmaps.R;

import static android.app.PendingIntent.getActivity;

public class MenuPrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mapsBtn;
    private ImageButton ajouterBtn;
    private LinearLayout buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonContainer = (LinearLayout) findViewById(R.id.conteneurBtn);

        mapsBtn = (Button)findViewById(R.id.testMaps);

        mapsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String latitude = "49.0593";
                String longitude = "-1.244730000000004";
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                try{
                    startActivity(mapIntent);
                }catch (Exception e){
                }
            }
        });

        ajouterBtn = (ImageButton)findViewById(R.id.ajouterBtn);

        ajouterBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Button button = new Button(this);
        button.setText("test");
        buttonContainer.addView(button);
    }
}
