package universite_paris8.iut.mdubois.killer_princess.modele;

import javafx.beans.property.*;

import java.util.ArrayList;

public class Entite {

    Jeu partie;
    private DoubleProperty xProp; //x =  droite gauche en deplacement
    private DoubleProperty yProp; //y = haut bas en deplacement
    private BooleanProperty attaque;
    private IntegerProperty pdv;
    private String nom;
    private int vie, degats, prime;

    //mettre pointdevie
    public Entite(double x, double y, Jeu partie, int vie, int degats, String nom, int prime) {
        xProp= new SimpleDoubleProperty(x);//quand initial = point depart
        yProp=new SimpleDoubleProperty(y);
        this.partie=partie;
        this.vie = vie;
        this.pdv = new SimpleIntegerProperty(vie);
        this.degats = degats;
        this.nom = nom;
        this.attaque = new SimpleBooleanProperty(false);
        this.prime = prime;
    }

    // l'ennemie perd des points de vie
    public void enleverPointDeVie(int degat){
        pdv.set(pdv.get() - degat);
    }

    public BooleanProperty attaqueProperty() {
        return attaque;
    }

    public void setX(double x){
        xProp.set(x);
    }

    public void setY(double y){
        yProp.set(y);
    }

    public double getX(){
        return getXProp().getValue();
    }

    public double getY(){
        return getYProp().getValue();
    }

    public DoubleProperty getXProp(){
        return xProp;
    }

    public DoubleProperty getYProp(){
        return yProp;
    }

    public String getNom() {
        return this.nom;
    }

    public int getPrime(){
        return this.prime;
    }

    public IntegerProperty pdvProperty() {
        return pdv;
    }

    public int getPdv() {
        return pdv.get();
    }

    // maximum de la barre de vie
    public int getVie(){
        return vie;
    }

    public int getDegats(){
        return degats;
    }
}

/*
    public void colisionDetect(double nextPosiX, double nextPosiY){
        boolean collisionY = false;
        boolean collisionX = false;
        // collision obstacle : tester X et Y séparément
        if(partie.getCarte().estObstacle(nextPosiX, getY(), largEntit, hautEntit)){
            collisionX = true;
        }
        if(partie.getCarte().estObstacle(getX(), nextPosiY, largEntit, hautEntit)){
            collisionY = true;
        }
        if(!collisionX){
            xProp.set(nextPosiX);
        }
        if(!collisionY){
            yProp.set(nextPosiY);
        }
    }
 */