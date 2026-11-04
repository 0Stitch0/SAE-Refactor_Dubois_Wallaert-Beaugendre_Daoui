package universite_paris8.iut.mdubois.killer_princess.modele;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.util.ArrayList;
import static java.lang.reflect.Array.set;

public abstract class Ennemi extends EnMouvement{

    private Carte carte;
    private boolean esquive;

    public Ennemi(double x, double y, Jeu partie, int vie, int degats, String nom, double vitesse, int prime) {
        super(x, y, partie, vie, degats, nom, vitesse, prime);
        carte = partie.getCarte();
        esquive = false;
    }

    public void mouvementAutoVersObjctif(){
        //faire un if si incincible pour son deplacment et un if pas attaque ou juste il a un degat de fou et nique tout
        super.mouvementBFS();
    }

    public void enleverPdv(int degats){
        if(degats>0){
            enleverPointDeVie(degats);
        }else{
            System.out.println("valeur invalide");
        }
    }

    public boolean estMort(){
        return getPdv()<=0;
    }

    public abstract void attaqueP(ArrayList<Ennemi> ennemiSurCase);
}
