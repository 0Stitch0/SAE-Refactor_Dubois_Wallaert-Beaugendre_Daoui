package universite_paris8.iut.mdubois.killer_princess.modele;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.util.ArrayList;
import static java.lang.reflect.Array.set;

public abstract class Ennemi extends EnMouvement{

    private Jeu partie;
    private Carte carte;
    private IntegerProperty vie;
    private boolean esquive;
    private int prime;

    public Ennemi(double x, double y, Jeu partie, int vie, int degats, String nom, double vitesse, int prime) {
        super(x, y, partie, vie, degats, nom, vitesse, prime);
        this.prime = prime;
        this.partie = partie;
        carte = partie.getCarte();
        esquive = false;
        this.vie = new SimpleIntegerProperty(vie);
    }

    public void mouvementAutoVersObjctif(){
        //faire un if si incincible pour son deplacment et un if pas attaque ou juste il a un degat de fou et nique tout
        super.mouvementBFS();
    }

    public void enleverPdv(int degats){
        if(degats>0){
            this.vie.set(this.vie.get()-degats);
        }else{
            System.out.println("valeur invalide");
        }

    }

    public boolean estMort(){
        return this.vie.get()<=0;
    }

    public void setPdv(int pdv){
        this.vie.set(pdv);
    }

    public IntegerProperty pdvProperty(){
        return vie;
    }

    public int getVie(){
        return vie.get();
    }

    public abstract void attaqueP(ArrayList<Ennemi> ennemiSurCase);
}
