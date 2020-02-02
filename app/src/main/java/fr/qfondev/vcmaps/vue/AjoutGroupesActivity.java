package fr.qfondev.vcmaps.vue;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import fr.qfondev.vcmaps.R;
import fr.qfondev.vcmaps.modele.GroupeRepere;


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

                    GroupeRepere grp = new GroupeRepere(nomGroupeWidget.getText().toString());

                    AjoutLieuxActivity.listeGroupes.add(grp);
                    MenuPrincipalActivity.db.addGroupe(grp);
                    AjoutLieuxActivity.initialisationGroupes();
                    finish();
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
