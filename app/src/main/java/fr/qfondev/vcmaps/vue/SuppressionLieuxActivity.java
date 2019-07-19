package fr.qfondev.vcmaps.vue;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.qfondev.vcmaps.R;

public class SuppressionLieuxActivity extends AppCompatActivity {

    private ImageButton retourNav;
    private LinearLayout buttonContainer;
    private File mFile = null;
    private ArrayList<String> lieuxSupp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppression);


        retourNav = (ImageButton) findViewById(R.id.retourNavigation);
        buttonContainer = (LinearLayout) findViewById(R.id.conteneurBtnSupp);

        mFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/ " + getPackageName() + "/files/" + MenuPrincipalActivity.FICHIER);


        retourNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getApplicationContext(),MenuPrincipalActivity.class);
                startActivity(main);
                finish();
            }
        });
        lieuxSupp = new ArrayList<String>();

        /*Recuperation des lieux sauvegarder*/
        try {
            StringBuilder text = new StringBuilder();
            FileInputStream fis = this.openFileInput(MenuPrincipalActivity.FICHIER);
            BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(fis)));
            String line;
            while ((line = br.readLine()) != null) {
                lieuxSupp.add(line);
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        affichage();



    }

    private void affichage(){ ;

        for (int i = 0; i < lieuxSupp.size(); i++){

            String[] infos = lieuxSupp.get(i).split("#");

            try{
                ajouterUnBouton(infos[0], infos[1], infos[2], this);
            }catch (Exception e){

            }
        }

    }

    private void ajouterUnBouton(final String nom, final String lat, final String lon, final Context ctx){
        Button btn = new Button(this);
        btn.setText(nom);
        btn.setBackground(getResources().getDrawable(R.drawable.design_btn_supp));
        btn.setTextSize(30);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] infos;
                String aRetirer = "";

                //detection du boutons a supp
                for (String str: lieuxSupp){
                    infos = str.split("#");
                    if (nom.equals(infos[0]) && lat.equals(infos[1]) && lon.equals(infos[2])){
                        aRetirer = str;
                    }
                }


                MenuPrincipalActivity.listeLieux.remove(aRetirer);
                MenuPrincipalActivity.enregistrerLieux(ctx);
                Intent menu = new Intent(getApplicationContext(),MenuPrincipalActivity.class);
                startActivity(menu);
                finish();


            }
        });

        buttonContainer.addView(btn);

    }


}
