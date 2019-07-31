package fr.qfondev.vcmaps.vue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.qfondev.vcmaps.R;
import fr.qfondev.vcmaps.modele.MyDatabaseHelper;
import fr.qfondev.vcmaps.modele.RepereLieux;

import static android.app.PendingIntent.getActivity;

public class MenuPrincipalActivity extends AppCompatActivity {

    private Button mapsBtn;
    private ImageButton ajouterBtn, suppBtn;
    public static LinearLayout buttonContainer;
    private TextView test;
    private File mFile = null;
    public static final String FICHIER = "lieux.txt";
    public static List<RepereLieux> listeLieux;
    public static Context MenuContext;
    public static MyDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MenuContext = this;

        ajouterBtn = (ImageButton) findViewById(R.id.ajouterBtn);
        suppBtn = (ImageButton) findViewById(R.id.supprimerBtn);
        buttonContainer = (LinearLayout) findViewById(R.id.conteneurBtn);
        listeLieux = new ArrayList<RepereLieux>();

        if(db == null)
            db = new MyDatabaseHelper(this);

        mFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/ " + getPackageName() + "/files/" + FICHIER);


        ajouterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(MenuPrincipalActivity.this,AjoutLieuxActivity.class);
                startActivity(main);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        suppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent suppActy = new Intent(MenuPrincipalActivity.this,SuppressionLieuxActivity.class);
                startActivity(suppActy);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });



        /*Recuperation des lieux sauvegarder*/
        listeLieux = db.getAllLieux();


        affichage(this);



    }

    public static void trierAlphabetiquement(){
        Collections.sort(listeLieux, new Comparator<RepereLieux>() {
            @Override
            public int compare(final RepereLieux repere1, final RepereLieux repere2) {
                return repere1.getNom().compareTo(repere2.getNom());
            }
        });
    }



    public static void affichage(Context ctx){

        buttonContainer.removeAllViews();
        trierAlphabetiquement();

        for (int i = 0; i <listeLieux.size(); i++){

            try{
                ajouterUnBouton(listeLieux.get(i).getNom(), listeLieux.get(i).getLatitude(), listeLieux.get(i).getLongitude(), ctx);
            }catch (Exception e){

            }
        }

    }

    public static void ajouterUnBouton(String nom, final double lat, final double lon, final Context ctx){
        Button btn = new Button(ctx);
        btn.setTypeface(Typeface.SANS_SERIF);
        btn.setText(nom);
        btn.setBackground(ctx.getResources().getDrawable(R.drawable.design_btn));
        btn.setTextSize(30);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                try {
                    ctx.startActivity(mapIntent);
                } catch (Exception e) {
                }
            }
        });

        buttonContainer.addView(btn);

    }


}
