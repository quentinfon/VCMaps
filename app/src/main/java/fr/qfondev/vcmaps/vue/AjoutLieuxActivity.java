package fr.qfondev.vcmaps.vue;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.qfondev.vcmaps.R;

public class AjoutLieuxActivity extends AppCompatActivity {

    private EditText rechercheLieu, nomLieu;
    private ImageButton validerLieu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_lieux);

        rechercheLieu = (EditText) findViewById(R.id.input_rechercheLieu);
        nomLieu = (EditText) findViewById(R.id.nomLieu);
        validerLieu = (ImageButton) findViewById(R.id.imgValidation);

        validerLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localise();
            }

        });
    }

    private void localise(){
        String recherche = rechercheLieu.getText().toString();
        String nomL = nomLieu.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(recherche, 1);
        }catch(IOException e){

        }

        if(list.size()>0){
            Address adresse = list.get(0);

            String nouvelleAdresse = nomL+"#"+adresse.getLatitude()+"#"+adresse.getLongitude();
            MenuPrincipalActivity.listeLieux.add(nouvelleAdresse);

            System.out.println("AJOUT DE :");
            System.out.println(MenuPrincipalActivity.listeLieux);

            String tousLesLieux = "";
            for(String lieu: MenuPrincipalActivity.listeLieux){
                tousLesLieux += lieu;
                tousLesLieux+="\n";
            }


            try {
                FileOutputStream outputStream = openFileOutput(MenuPrincipalActivity.FICHIER, Context.MODE_PRIVATE);
                outputStream.write(tousLesLieux.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent main = new Intent(getApplicationContext(),MenuPrincipalActivity.class);
            startActivity(main);
            finish();

        }
    }
}
