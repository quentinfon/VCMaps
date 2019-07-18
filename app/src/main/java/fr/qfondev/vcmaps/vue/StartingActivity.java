package fr.qfondev.vcmaps.vue;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import fr.qfondev.vcmaps.R;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_starting);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(getApplicationContext(),MenuPrincipalActivity.class);
                startActivity(main);
                finish();
            }

        };

        new Handler().postDelayed(runnable,2000);
    }


}
