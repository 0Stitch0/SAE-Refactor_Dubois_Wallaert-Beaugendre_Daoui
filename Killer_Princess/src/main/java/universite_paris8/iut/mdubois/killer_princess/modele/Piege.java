package universite_paris8.iut.mdubois.killer_princess.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.ArrayList;

public abstract class Piege extends Entite {
    private int cout;
    private int niveau;
    private int nivMax;
    private int intervalle; // l'intervalle entre chaque réinitialisation du compteur de tic
    private int cooldown = 0; // un compteur de tic
    private final ObservableMap<Object, Object> properties = FXCollections.observableHashMap();

    public Piege(double x, double y,Jeu partie, int vie, int degat, String nom, int prime, int cout, int intervalle){
        super(x, y, partie, vie, degat, nom, prime);
        this.cout = cout;
        this.niveau = 1;
        this.nivMax = 3;
        this.intervalle = intervalle;
    }

    public Ennemi EnnemisLePlusProche(ArrayList<Ennemi> ennemis) {
        Ennemi eProche = ennemis.get(0);

        for (int i=1; i<ennemis.size(); i++) {
            if(ennemis.get(i).getY()<eProche.getY()){
                eProche = ennemis.get(i);
            }
        }
        return eProche;
    }

    public void amelioration(){
        this.setDegats(this.getDegats()+2);
        this.niveau++;
    }

    public final ObservableMap<Object, Object> getProperties() {
        return this.properties;
    }

    public int getNiveau(){
        return this.niveau;
    }

    public void setNiveau(int niveau){
        this.niveau = niveau;
    }

    public int getNivMax(){
        return this.nivMax;
    }

    public int getCout(){
        return this.cout;
    }

    public int getIntervalle() {
        return this.intervalle;
    }

    public int prixVente(){
        return this.cout / 2;
    }

    public boolean enPlace() {
        return true;
    }

    protected abstract boolean appliquerEffet(ArrayList<Ennemi> ennemiSurCase); //renvois true si l'effet a été appliqué et déclenche le cooldown

    /**
     * tous les pieges on une attaque qui se differencie
     */
    public void attaqueE(ArrayList<Ennemi> ennemiSurCase) {
        if(cooldown > 0) {
            cooldown--;
            return;
        }
        if(appliquerEffet(ennemiSurCase)) {
            cooldown = intervalle;
        }

    }
}