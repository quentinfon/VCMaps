package fr.qfondev.vcmaps.vue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

import fr.qfondev.vcmaps.R;
import fr.qfondev.vcmaps.modele.RepereLieux;

public class AjoutLieuxActivity extends AppCompatActivity {

    private EditText rechercheLieu, nomLieu;
    private ImageButton validerLieu;
    private LinearLayout page;
    private Switch utilisationGPS;
    private RelativeLayout barreDeRecherche;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static Location position;
    private FusedLocationProviderClient fusedLocationClient;
    public static Spinner listeGroupeW;
    public static ArrayList<String> listeGroupes;
    private Button ajoutGroupe;
    public static final String FICHIERGOUPES = "groupes.txt";
    public static Context ctx;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_lieux);

        barreDeRecherche = (RelativeLayout) findViewById(R.id.barreDeRecherche);
        rechercheLieu = (EditText) findViewById(R.id.input_rechercheLieu);
        nomLieu = (EditText) findViewById(R.id.nomLieu);
        validerLieu = (ImageButton) findViewById(R.id.imgValidation);
        page = (LinearLayout) findViewById(R.id.pageCrea);
        utilisationGPS = (Switch) findViewById(R.id.positionGPS);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        listeGroupeW = (Spinner) findViewById(R.id.listeGroupes);
        ajoutGroupe = (Button) findViewById(R.id.btnNewGrp);
        ctx = this;



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                position = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };


        ajoutGroupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grpAct = new Intent(AjoutLieuxActivity.this,AjoutGroupesActivity.class);
                startActivity(grpAct);
            }

        });

        utilisationGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    barreDeRecherche.setVisibility(View.INVISIBLE);
                }
                else{
                    barreDeRecherche.setVisibility(View.VISIBLE);
                }
            }
        });

        validerLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utilisationGPS.isChecked()){

                    try {

                        requeteGPS();

                        if(position == null){
                            fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(AjoutLieuxActivity.this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                System.out.println("impossible de trouver votre position");
                                            }
                                            else{
                                                position = location;
                                            }
                                        }
                                    });
                        }

                        String nomL = nomLieu.getText().toString();


                        RepereLieux nAdresse = new RepereLieux(nomL, position.getLatitude(), position.getLongitude(), "defaut");

                        MenuPrincipalActivity.db.addRepere(nAdresse);
                        MenuPrincipalActivity.listeLieux.add(nAdresse);
                        MenuPrincipalActivity.affichage(MenuPrincipalActivity.MenuContext);

                        finish();
                    } catch (Exception e) {
                    }

                }
                else{
                    localise();
                }
            }

        });


        initialisationGroupes(AjoutLieuxActivity.this);
    }

    public static void initialisationGroupes(Context ctx){

        listeGroupes = new ArrayList<String>();

        /*Recuperation des groupes sauvegarder*/
        try {
            StringBuilder text = new StringBuilder();
            FileInputStream fis = ctx.openFileInput(FICHIERGOUPES);
            BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(fis)));
            String line;
            while ((line = br.readLine()) != null) {

                String groupNom = line;

                listeGroupes.add(groupNom);

            }
            br.close();
            fis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (RepereLieux repere: MenuPrincipalActivity.listeLieux) {

            if (listeGroupes.indexOf(repere.getGroupe()) == -1){
                listeGroupes.add(repere.getGroupe());
                System.out.println("Ajout de "+repere.getGroupe());
            }

        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1, listeGroupes);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listeGroupeW.setAdapter(adp);

    }

    public static void enregistrerGroupes(Context ctx){

        String tousLesGroupes = "";
        for(String nomGrp: AjoutLieuxActivity.listeGroupes){
            tousLesGroupes += nomGrp;
            tousLesGroupes+="\n";
        }

        File mydir = ctx.getFilesDir(); //get your internal directory
        File myFile = new File(mydir, FICHIERGOUPES);
        myFile.delete();

        try {
            FileOutputStream outputStream = ctx.openFileOutput(MenuPrincipalActivity.FICHIER, Context.MODE_PRIVATE);
            outputStream.write(tousLesGroupes.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void requeteGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 50, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                requeteGPS();
                break;
            default:
                break;
        }
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

            RepereLieux nAdresse = new RepereLieux(nomL, adresse.getLatitude(),adresse.getLongitude(), "defaut");

            MenuPrincipalActivity.listeLieux.add(nAdresse);

            MenuPrincipalActivity.db.addRepere(nAdresse);
            MenuPrincipalActivity.affichage(MenuPrincipalActivity.MenuContext);
            finish();

        }
    }

    protected void onPause(){
        page.setVisibility(View.GONE);
        super.onPause();
    }

    protected void onResume(){
        page.setVisibility(View.VISIBLE);
        super.onResume();
    }
}
