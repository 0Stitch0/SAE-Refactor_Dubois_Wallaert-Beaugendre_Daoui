package universite_paris8.iut.mdubois.killer_princess.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Destructible extends Piege {

    public Destructible(double x, double y, Jeu partie, int vie, int degat, String nom, int prime, int cout){
        super(x, y, partie, vie, degat, nom, prime, cout);
    }

    public void pdvPerdus(int degats){
        if (degats>0) {
            enleverPointDeVie(degats);
        } else {
            System.out.println("valeur invalide");
        }
    }

    public boolean enPlace(){
        return getPdv() > 0;
    }

    public int getDegat() {
        return super.getDegats();
    }

    public IntegerProperty getPdvProperty() {
        return pdvProperty();
    }
}