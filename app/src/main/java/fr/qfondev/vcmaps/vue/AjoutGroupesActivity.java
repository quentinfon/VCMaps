package fr.qfondev.vcmaps.vue;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import fr.qfondev.vcmaps.R;


public class AjoutGroupesActivity extends AppCompatActivity {


    private ImageButton validation;
    private EditText nomGroupeWidget;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_groupe);

        nomGroupeWidget = (EditText) findViewById(R.id.nomGroupe);
        validation = (ImageButton) findViewById(R.id.imgValidationGrp);

        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nomGroupeWidget.getText().toString() != null & !nomGroupeWidget.getText().toString().equals("")){
                    AjoutLieuxActivity.listeGroupes.add(nomGroupeWidget.getText().toString());
                    AjoutLieuxActivity.enregistrerGroupes(AjoutLieuxActivity.ctx);
                    AjoutLieuxActivity.initialisationGroupes(AjoutLieuxActivity.ctx);
                }
            }
        });


    }


    protected void onPause(){
        super.onPause();
    }

    protected void onResume(){
        super.onResume();
    }
}
