package fr.qfondev.vcmaps.modele;

public class RepereLieux {

    private String nom, groupe;
    private double latitude, longitude;

    public RepereLieux(String nom, double lat, double lon, String grp){
        this.nom = nom;
        this.groupe = grp;
        this.latitude = lat;
        this.longitude = lon;
    }

    public String getNom() {
        return nom;
    }

    public String getGroupe() {
        return groupe;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}
