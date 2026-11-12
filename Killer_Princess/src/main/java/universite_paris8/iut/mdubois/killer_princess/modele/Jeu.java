package universite_paris8.iut.mdubois.killer_princess.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;
import universite_paris8.iut.mdubois.killer_princess.modele.pieges.Piege;

import java.util.ArrayList;

import static universite_paris8.iut.mdubois.killer_princess.modele.Carte.tailleTuile;

public class Jeu {
    private IntegerProperty or;
    private ObservableList<Piege> listePieges;
    private ObservableList<Ennemi> listeEnnemis;
    private static final int OR_INITIAL = 200;
    private Carte carte;
    private Piege piegeSelectionne;
    private GestionnaireVagues gestionnaireVagues;
    private IntegerProperty vague;
    private int ennemisTues;
    private Case porte;
    private final int x_Porte;
    private final int y_Porte;
    private int modeJeu;

    public Jeu (int modeJeu) {
        this.or = new SimpleIntegerProperty(OR_INITIAL);
        this.vague = new SimpleIntegerProperty(1);
        this.listePieges = FXCollections.observableArrayList();
        this.listeEnnemis = FXCollections.observableArrayList();
        this.carte = new Carte(modeJeu);
        this.gestionnaireVagues = new GestionnaireVagues();
        this.carte = new Carte(modeJeu);
        this.ennemisTues = 0;
        this.modeJeu = modeJeu;
        porte = carte.trouverTuile(100);
        this.x_Porte = porte.getColonne()*tailleTuile;
        this.y_Porte = porte.getLigne()*tailleTuile;

    }



//Vérifie si la case actuelle est déjà occupée par un autre piège

    public boolean caseOccupee(int ligne, int colonne){
        boolean caseOcc = false;
        int i = 0;

        //Boucle qui parcourt les pièges un par un.
        //Elle s'arrête si on a fait le tour de la liste OU si on a trouvé un piège sur la case
        while (i < listePieges.size() && !caseOcc) {

            //On vérifie si le piège actuel (à l'index i) a les mêmes coordonnées que celles passées en paramètre.
            if (listePieges.get(i).getX() == colonne && listePieges.get(i).getY() == ligne) {
                caseOcc = true;         //Si on trouve un piège, on retourne vrai (la case est bien occupée)
            }
            i++;
        }
        //On renvoie le résultat final (true si occupée, false si libre)
        return caseOcc;
    }

    //fonction booléenne qui crée un piège à une certaine coordonnée si c'est possible
    public boolean tenterPlacerPiege(String type, int col, int lig, FabriqueInstance fabriquePiege) {
        //Retourne faux si la case est occupée ou si la case n'est ni de l'herbe ni à l'intérieur du plateau
        if (caseOccupee(lig, col) || !getCarte().peutPoserPiege(col *tailleTuile, lig * tailleTuile, type)) {
            return false;
        }
        //Retourne faux si le coût est supérieur au crédit du joueur
        int cout = fabriquePiege.getCoutPour(type);
        if (getOr().get() < cout) {
            return false;
        }

        //retourne vrai si le piège n'est pas null
        Piege nouveauPiege = fabriquePiege.creerInstancePiege(type, col * tailleTuile, lig * tailleTuile);
        if (nouveauPiege != null) {
            ajouterPiege(nouveauPiege);     //l'ajoute à la liste de pièges
            depenserOr(cout);           //dépense son coût
            return true;
        }
        return false;
    }

    //méthode qui met à jour le jeu et les listes de pièges et ennemis
    public void mettreAJour() {
        //pour les ennemis mouvement ennemis
        for (int i = 0; i < listeEnnemis.size(); i++) {
            Ennemi e = listeEnnemis.get(i);
            if(e != null) {
                //fait avancer l'ennemi si il n'est pas null
                e.mouvementAutoVersObjctif();
                //si l'ennemi est mort on le retire de la liste et ajout sa prime au crédit du joueur
            }
        }

        // piege attaque ennemi en vie
        ArrayList<Ennemi> ennemisVivants = new ArrayList<>();
        for (Ennemi e : listeEnnemis) {
            if (!e.estMort()) ennemisVivants.add(e);
        }
        for (Piege p : listePieges) {
            p.attaqueE(ennemisVivants);
        }

        // retir ennemis mort
        for (int i = 0; i < listeEnnemis.size(); i++) {
            Ennemi e = listeEnnemis.get(i);
            if (e.estMort()) {
                supprimerEnnemi(e);
                ajouterOr(e.getPrime());
                i--;
            }
        }

        // retir piege detruit
        for (int i = 0; i < listePieges.size(); i++) {
            if (!listePieges.get(i).enPlace()) {
                retirerPiege(listePieges.get(i));
                i--;
            }
        }
    }

    public void passerAVagueSuivante() {
        this.vague.set(this.vague.get() + 1);
        System.out.println("Passage à la vague numéro : " + this.vague);
        this.gestionnaireVagues.vagueSuivante();
    }

    /*public boolean estPartiePerdue() {
        return this.listeEnnemis;
    }*/

    public void supprimerEnnemi(Ennemi e) {
        listeEnnemis.remove(e);
        //ennemisTues++;
    }


    public void affichage(){
        for(int i=0; i<listePieges.size(); i++){
            Piege p = listePieges.get(i);
            System.out.println("cout : " + p.getCout() + " " + "niveau : " + p.getNiveau() + " " + "degat " + p.getDegats());
        }
    }

    public void AmeliorerPiege() {
        Piege piege = getPiegeSelectionne();

        if (piege != null) {
            int cout = piege.getCout();
            if (this.or.get() >= cout && piege.getNiveau() < piege.getNivMax()) {   //si l'or possédé est suffisant et que les piège n'a pas atteint son niveau max
                depenserOr(cout);       //crédite le prix de l'amélioration
                piege.amelioration();       //améliore le piège
            } else {
                System.out.println("Pas assez d'or pour améliorer !");
            }
            deselectionner();
        }
    }

    public void VendrePiege() {
        Piege piege = getPiegeSelectionne();

        if (piege != null) {        //si le piège n'est pas null
            ajouterOr(piege.prixVente());       //ajoute au crédit du joueur le prix de la vente
            deselectionner();
            retirerPiege(piege);            //retire le piège de la liste
        }
    }

    public Ennemi trouverEnnemiLePlusProche(){
        Ennemi plusProche = null;
        //S'il y a un ennemi en jeu
        if (!this.listeEnnemis.isEmpty()) {
            double distanceMin = Double.MAX_VALUE;      //initialisation de la distance min à beaucoup

            //parcours la liste d'ennemis et trouve leur case
            for (Ennemi e : this.listeEnnemis) {
                int col = (int) (e.getX() / tailleTuile);
                int lig = (int) (e.getY() / tailleTuile);
                Case caseEnnemi = this.carte.getCaseAlgo(col, lig);

                if (caseEnnemi != null) {
                    //BFS depuis la porte vers l'ennemi
                    Bfs bfs = new Bfs(this.carte, porte);
                    ArrayList<Case> chemin = bfs.cheminVersSource(caseEnnemi);      //calcule du nombre de cases minimal pour le chemin

                    if (chemin != null && chemin.size() < distanceMin) {
                        distanceMin = chemin.size();
                        plusProche = e;
                    }
                }
            }
        }
        return plusProche;
    }

    public void declencherBouleDeFeu() {
        Ennemi plusProche = trouverEnnemiLePlusProche();

        //Si on a trouvé l'ennemi le plus proche
        if (plusProche != null && ennemisTues>=15) {
            //On le "tue" en le retirant directement de la liste d'ennemis
            this.listeEnnemis.remove(plusProche);
            ajouterOr(plusProche.getPrime());       //on gagne l'argent de sa prime
            this.ennemisTues = 1;       //on remet le compteur d'ennemis tués à 0
        }
    }

    //CONDITION DE PARTIE GAGNÉE/PERDUE
    public boolean estPartieGagnee() {
        return this.gestionnaireVagues.vagueTerminee() && this.vague.get() >= this.gestionnaireVagues.getNbTotalVagues() && this.listeEnnemis.isEmpty();
    }

    public boolean estPartiePerdue() {
        if(listeEnnemis!=null){
            for(Ennemi e : listeEnnemis){
                if(e.getX() == x_Porte && e.getY()==y_Porte){
                    return true;
                }
            }
        }
        return false;
    }

    //GESTION DES LISTES
    public void ajouterPiege(Piege p){
        listePieges.add(p);
    }

    public void ajouterEnnemi(Ennemi e){
        listeEnnemis.add(e);
    }

    public void retirerPiege(Piege p){
        listePieges.remove(p);
    }

    public void retirerEnnemi(Ennemi e) {
        listeEnnemis.remove(e);
    }

    //Modifient les attributs de la classe Jeu
    public void selectionnerPiege(Piege p) {
        this.piegeSelectionne = p;
        if (p != null) {
            System.out.println("Piège sélectionné" + " (Niveau " + p.getNiveau() + ")");
        }
    }

    public void deselectionner() {
        this.piegeSelectionne = null;
    }

    //à la mort d'un ennemi
    public void ajouterOr(int valeur){
        if (valeur>0) {
            this.or.set(this.or.get() + valeur);
        } else {
            System.out.println("valeur invalide");
        }
    }

    //à l'achat d'un piège si le crédit est suffisant
    public void depenserOr(int valeur){
        if(or.get() >= valeur) {
            or.set(or.get()-valeur);
        }
    }

    //GETTERS ET SETTERS

    public IntegerProperty getOr(){
        return this.or;
    }

    public IntegerProperty getVague(){
        return this.vague;
}

    public void setOr(int valeur){
        this.or.set(valeur);
    }

    public int getModeJeu(){
        return this.modeJeu;
    }

    public int getXCase(Piege p) {
        int tailleTuile = carte.getTailleTuile();
        int col = (int) (p.getX() / tailleTuile);
        return col;
    }

    public int getYCase(Piege p) {
        int tailleTuile = carte.getTailleTuile();
        int lig = (int) (p.getY() / tailleTuile);
        return lig;
    }

    public Piege getPiegeSelectionne() {
        return this.piegeSelectionne;
    }

    public Piege getPiegeEn(int ligne, int colonne){
        int i=0;
        while(listePieges.size()>i && getXCase(listePieges.get(i))!=ligne && getYCase(listePieges.get(i))!=colonne) {
            i++;
        }
        if(i>=listePieges.size()){
            return null;
        }
        return listePieges.get(i);
    }

    public ObservableList<Piege> getListePieges() {
        return this.listePieges;
    }
    public ObservableList<Ennemi> getListeEnnemis() {
        return this.listeEnnemis;
    }

    public GestionnaireVagues getGestionnaireVagues() {
        return this.gestionnaireVagues;
    }

    public Carte getCarte() {
        return this.carte;
    }

    public Piege getDernierPiegeAjoute() {
        if (this.listePieges.isEmpty()) {
            return null;
        }
        return this.listePieges.get(this.listePieges.size() - 1);
    }
}