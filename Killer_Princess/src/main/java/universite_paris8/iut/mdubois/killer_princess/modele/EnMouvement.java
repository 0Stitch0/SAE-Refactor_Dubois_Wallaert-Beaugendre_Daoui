package universite_paris8.iut.mdubois.killer_princess.modele;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.ArrayList;

public abstract class EnMouvement extends Entite {

    private double vitesse;
    private int temps=0;
    private final ObservableMap<Object, Object> properties = FXCollections.observableHashMap();
    public final ObservableMap<Object, Object> getProperties(){
        return this.properties;
    }
    private ArrayList<Case> chemin;
    private int positionActuelle;
    //bindings
    private DoubleProperty cibleXProp;
    private DoubleProperty cibleYProp;
    private DoubleBinding distanceBind;

    public EnMouvement(double x, double y, Jeu partie, int vie, int degats, String nom, double vitesse, int prime){
        super(x, y, partie, vie, degats, nom, prime);
        this.vitesse= vitesse;
        initBFS(x, y); //methode pour a leger le constructeur
        initBindings();
    }

    //initialise les variables utile a BFS
    private void initBFS(double x, double y){
        int colonne= (int)(x/partie.getCarte().getTailleTuile());
        int ligne= (int)(y/partie.getCarte().getTailleTuile());

        Case source= partie.getCarte().getCaseAlgo(colonne, ligne);//source = point d'apparition
        //faire un if avec squelette idRecherche different
        Case cible = partie.getCarte().trouverTuile(100);//cible = case id 100

        Bfs bfs= new Bfs(partie.getCarte(), source);
        chemin= bfs.cheminVersSource(cible);
        if(chemin==null || chemin.isEmpty()){
            System.out.println("Chemin introuvable pour l'ennemi à " + x + ", " + y);
            positionActuelle= 0;
        }else{
            positionActuelle= chemin.size()-1;
        }
    }

    //initialisation des bind pour le deplacment
    private void initBindings(){
        cibleXProp= new SimpleDoubleProperty();
        cibleYProp= new SimpleDoubleProperty();

        DoubleBinding dxBind= cibleXProp.subtract(getXProp());
        DoubleBinding dyBind= cibleYProp.subtract(getYProp());
        distanceBind = Bindings.createDoubleBinding(
                ()->Math.hypot(dxBind.get(), dyBind.get()),
                dxBind, dyBind
        );
    }

    //appel toute les methodes utile au depalcement avec le BFS
    public void mouvementBFS(){
        if(cheminTermine())return;
        mettreAJourCible();
        if(estArriveSurCase()){
            passerCaseSuivante();
            return;
        }
        if (attaquerPiegeEnFace()) return;
        deplacer();
    }

    //chemin trouver depuis la case d'apparaition jusqu'a la case cible
    private boolean cheminTermine(){
        return chemin==null || chemin.isEmpty() || positionActuelle <= 0;//return true ou false pour savoir si peuvent avancer
    }

    //donne les coordonées de la prochaine position pour le deplacement de l'ennemi
    private void mettreAJourCible(){
        Case prochaine= chemin.get(positionActuelle-1);
        cibleXProp.set(prochaine.getColonne()*partie.getCarte().getTailleTuile());
        cibleYProp.set(prochaine.getLigne()*partie.getCarte().getTailleTuile());
    }

    //
    private boolean estArriveSurCase(){
        return distanceBind.get()<0.5;
    }

    private void passerCaseSuivante(){
        getXProp().set(cibleXProp.get());
        getYProp().set(cibleYProp.get());
        positionActuelle--;
    }

    private void deplacer(){
        double distance= distanceBind.get();
        double deplacement= Math.min(getVitesse(), distance);
        double nextX= calculerNextX(distance, deplacement);
        double nextY= calculerNextY(distance, deplacement);
        deplacerX(nextX);
        deplacerY(nextY);
    }

    private double calculerNextX(double distance, double deplacement){
        return getX()+(cibleXProp.get()-getX())/distance*deplacement;
    }

    private double calculerNextY(double distance, double deplacement){
        return getY()+(cibleYProp.get()-getY())/distance*deplacement;
    }

    private void deplacerX(double nextX) {
        if (!partie.getCarte().estObstacle(nextX, getY(),32,32)){
            getXProp().set(nextX);
        }
    }

    private void deplacerY(double nextY){
        if(!partie.getCarte().estObstacle(getX(), nextY,32,32)){
            getYProp().set(nextY);
        }
    }

    // Cherche un piège destructible devant et l'attaque
    private boolean attaquerPiegeEnFace() {
        Destructible piegeDevant = trouverPiegeDevant();
        if (piegeDevant == null) return false;

        effectuerAttaque(piegeDevant);
        return true;
    }

    // Cherche un Destructible sur la case juste devant l'ennemi
    private Destructible trouverPiegeDevant() {
        double devantX = this.getX() + 32;
        double devantY = this.getY();

        for (Piege p : partie.getListePieges()) {
            if (!(p instanceof Destructible)) continue;
            if (Math.abs(p.getX() - devantX) < 50 && Math.abs(p.getY() - devantY) < 32) {
                return (Destructible) p;
            }
        }
        return null;
    }


    // Attaque le piège toutes les 60 frames
    private void effectuerAttaque(Destructible piege) {
        attaqueProperty().set(true);
        if (temps < 60) {
            temps++;        // ← temps de Entite, plus celui de EnMouvement
            return;
        }
        piege.pdvPerdus(getDegats());
        enleviePointDeVie(piege.getDegat());
        temps = 0;

        supprimerSiDetruit(piege);
    }

    // Supprime le piège si détruit et reprend le chemin
    private void supprimerSiDetruit(Destructible piege) {
        if (piege.getPdvProperty().get() <= 0) {
            partie.getListePieges().remove(piege);
            temps = 0;
            System.out.println(piege.getNom() + " détruit !");
        }
    }

    public double getVitesse(){
        return vitesse;
    }

    public abstract void mouvementAutoVersObjctif();
}