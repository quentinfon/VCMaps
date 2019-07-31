package fr.qfondev.vcmaps.vue;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.qfondev.vcmaps.R;
import fr.qfondev.vcmaps.modele.RepereLieux;

public class SuppressionLieuxActivity extends AppCompatActivity {

    private ImageButton retourNav;
    private LinearLayout buttonContainer;
    private File mFile = null;
    private List<RepereLieux> lieuxSupp;

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
                finish();
            }
        });
        lieuxSupp = new ArrayList<RepereLieux>();

        /*Recuperation des lieux sauvegarder*/
        lieuxSupp = MenuPrincipalActivity.db.getAllLieux();


        affichage();



    }

    public void trierAlphabetiquement(){
        Collections.sort(this.lieuxSupp, new Comparator<RepereLieux>() {
            @Override
            public int compare(final RepereLieux repere1, final RepereLieux repere2) {
                return repere1.getNom().compareTo(repere2.getNom());
            }
        });
    }

    private void affichage(){

        trierAlphabetiquement();

        for (int i = 0; i <lieuxSupp.size(); i++){

            try{
                ajouterUnBouton(lieuxSupp.get(i).getNom(), SuppressionLieuxActivity.this);
            }catch (Exception e){

            }
        }

    }

    private void ajouterUnBouton(final String nom, final Context ctx){
        Button btn = new Button(this);
        btn.setText(nom);
        btn.setBackground(getResources().getDrawable(R.drawable.design_btn_supp));
        btn.setTextSize(30);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] infos;
                RepereLieux aRetirer = null;

                //detection du boutons a supp
                for (RepereLieux str: MenuPrincipalActivity.listeLieux){
                    if (nom.equals(str.getNom())){
                        aRetirer = str;
                    }
                }


                MenuPrincipalActivity.db.deleteLieu(aRetirer);
                MenuPrincipalActivity.listeLieux.remove(aRetirer);
                MenuPrincipalActivity.affichage(MenuPrincipalActivity.MenuContext);
                finish();


            }
        });

        buttonContainer.addView(btn);

    }

    protected void onPause(){
        retourNav.setVisibility(View.GONE);
        super.onPause();
    }

    protected void onResume(){
        retourNav.setVisibility(View.VISIBLE);
        super.onResume();
    }

}
