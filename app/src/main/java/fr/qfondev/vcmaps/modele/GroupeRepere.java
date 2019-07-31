package fr.qfondev.vcmaps.modele;

public class GroupeRepere {

    private String nom;
    private String description;

    public GroupeRepere(String n){
        this.nom = n;
    }

    public GroupeRepere(String n, String desc){
        this.nom = n;
        this.description = desc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }
}
