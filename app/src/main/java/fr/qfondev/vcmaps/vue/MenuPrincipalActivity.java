package fr.qfondev.vcmaps.vue;

import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

import fr.qfondev.vcmaps.R;

import static android.app.PendingIntent.getActivity;

public class MenuPrincipalActivity extends AppCompatActivity {

    private Button mapsBtn;
    private ImageButton ajouterBtn, suppBtn;
    private LinearLayout buttonContainer;
    private TextView test;
    private File mFile = null;
    public static final String FICHIER = "lieux.txt";
    public static ArrayList<String> listeLieux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ajouterBtn = (ImageButton) findViewById(R.id.ajouterBtn);
        suppBtn = (ImageButton) findViewById(R.id.supprimerBtn);
        buttonContainer = (LinearLayout) findViewById(R.id.conteneurBtn);
        listeLieux = new ArrayList<String>();

        mFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/ " + getPackageName() + "/files/" + FICHIER);


        ajouterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getApplicationContext(),AjoutLieuxActivity.class);
                startActivity(main);
            }
        });

        suppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent suppActy = new Intent(getApplicationContext(),SuppressionLieuxActivity.class);
                startActivity(suppActy);
            }
        });



        /*Recuperation des lieux sauvegarder*/
        try {
            StringBuilder text = new StringBuilder();
            FileInputStream fis = this.openFileInput(FICHIER);
            BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(fis)));
            String line;
            while ((line = br.readLine()) != null) {
                listeLieux.add(line);
                System.out.println(line);
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        affichage();



    }

    public static void enregistrerLieux(Context ctx){

        String tousLesLieux = "";
        for(String lieu: MenuPrincipalActivity.listeLieux){
            tousLesLieux += lieu;
            tousLesLieux+="\n";
        }

        File mydir = ctx.getFilesDir(); //get your internal directory
        File myFile = new File(mydir, FICHIER);
        myFile.delete();

        try {
            FileOutputStream outputStream = ctx.openFileOutput(MenuPrincipalActivity.FICHIER, Context.MODE_PRIVATE);
            outputStream.write(tousLesLieux.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void affichage(){ ;

        for (int i = 0; i <listeLieux.size(); i++){

            String[] infos = listeLieux.get(i).split("#");

            try{
                ajouterUnBouton(infos[0], infos[1], infos[2]);
            }catch (Exception e){
                System.out.println("CRAAAAASSSHHHHH");
            }
        }

    }

    private void ajouterUnBouton(String nom, final String lat, final String lon){
        Button btn = new Button(this);
        btn.setText(nom);
        btn.setBackground(getResources().getDrawable(R.drawable.design_btn));
        btn.setTextSize(30);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                try {
                    startActivity(mapIntent);
                } catch (Exception e) {
                }
            }
        });

        buttonContainer.addView(btn);

    }


}
