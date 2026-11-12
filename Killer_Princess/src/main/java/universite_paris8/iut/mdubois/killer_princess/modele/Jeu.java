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

    private final IntegerProperty or;
    private final ObservableList<Piege> listePieges;
    private final ObservableList<Ennemi> listeEnnemis;
    private static final int OR_INITIAL = 200;

    private final Carte carte;
    private Piege piegeSelectionne;
    private final GestionnaireVagues gestionnaireVagues;
    private final IntegerProperty vague;
    private int ennemisTues;
    private final Case porte;
    private final int x_Porte;
    private final int y_Porte;
    private final int modeJeu;

    public Jeu(int modeJeu) {
        this.or = new SimpleIntegerProperty(OR_INITIAL);
        this.vague = new SimpleIntegerProperty(1);
        this.listePieges = FXCollections.observableArrayList();
        this.listeEnnemis = FXCollections.observableArrayList();

        this.carte = new Carte(modeJeu);
        this.gestionnaireVagues = new GestionnaireVagues();

        this.ennemisTues = 0;
        this.modeJeu = modeJeu;

        this.porte = carte.trouverTuile(100);
        this.x_Porte = porte.getColonne() * tailleTuile;
        this.y_Porte = porte.getLigne() * tailleTuile;
    }

    // Vérifie si une case est déjà occupée par un piège
    public boolean caseOccupee(int ligne, int colonne) {
        for (Piege piege : listePieges) {
            if (piege.getX() == colonne
                    && piege.getY() == ligne) {
                return true;
            }
        }
        return false;
    }

    // Tente de placer un piège sur la carte
    public boolean tenterPlacerPiege(String type, int col, int lig, FabriqueInstance fabriquePiege) {
        if (!caseValidePourPiege(type, col, lig)) {
            return false;
        }
        int cout = fabriquePiege.getCoutPour(type);

        if (!aAssezOr(cout)) {
            return false;
        }

        Piege nouveauPiege = fabriquePiege.creerInstancePiege(
                type, col * tailleTuile, lig * tailleTuile);

        if (nouveauPiege == null) {
            return false;
        }
        ajouterPiege(nouveauPiege);
        depenserOr(cout);
        return true;
    }

    // Vérifie que la case est libre et autorisée pour ce piège
    private boolean caseValidePourPiege(String type, int col, int lig) {

        return !caseOccupee(lig, col)
                && carte.peutPoserPiege(
                        col * tailleTuile, lig * tailleTuile, type);
    }

    // Vérifie que le joueur possède assez d'or
    private boolean aAssezOr(int cout) {
        return getOr().get() >= cout;
    }

    // Met à jour les ennemis et les pièges
    public void mettreAJour() {
        deplacerEnnemis();
        ArrayList<Ennemi> ennemisVivants = obtenirEnnemisVivants();

        faireAttaquerPieges(ennemisVivants);
        supprimerEnnemisMorts();
        supprimerPiegesDetruits();
    }

    // Déplace les ennemis présents sur la carte
    private void deplacerEnnemis() {
        for (Ennemi e : listeEnnemis) {
            if (e != null) {
                e.mouvementAutoVersObjctif();
            }
        }
    }

    // Récupère les ennemis vivants
    private ArrayList<Ennemi> obtenirEnnemisVivants() {
        ArrayList<Ennemi> ennemisVivants = new ArrayList<>();

        for (Ennemi e : listeEnnemis) {
            if (e != null && !e.estMort()) {
                ennemisVivants.add(e);
            }
        }
        return ennemisVivants;
    }

    // Fait attaquer les pièges contre les ennemis vivants
    private void faireAttaquerPieges(
            ArrayList<Ennemi> ennemisVivants) {

        for (Piege p : listePieges) {
            p.attaqueE(ennemisVivants);
        }
    }

    // Supprime les ennemis morts et récupère leur prime
    private void supprimerEnnemisMorts() {
        for (int i = 0; i < listeEnnemis.size(); i++) {
            Ennemi e = listeEnnemis.get(i);

            if (e.estMort()) {
                supprimerEnnemi(e);
                ajouterOr(e.getPrime());
                i--;
            }
        }
    }

    // Supprime les pièges détruits
    private void supprimerPiegesDetruits() {
        for (int i = 0; i < listePieges.size(); i++) {
            if (!listePieges.get(i).enPlace()) {
                retirerPiege(listePieges.get(i));
                i--;
            }
        }
    }

    public void passerAVagueSuivante() {
        this.vague.set(this.vague.get() + 1);
        System.out.println("Passage à la vague numéro : " + this.vague.get());
        this.gestionnaireVagues.vagueSuivante();
    }

    public void supprimerEnnemi(Ennemi e) {
        listeEnnemis.remove(e);
    }

    public void affichage() {
        for (Piege p : listePieges) {
            System.out.println(
                    "cout : " + p.getCout()
                    + " niveau : " + p.getNiveau()
                    + " degat " + p.getDegats()
            );
        }
    }

    // Améliore le piège sélectionné
    public void AmeliorerPiege() {
        Piege piege = getPiegeSelectionne();

        if (piege != null) {
            int cout = piege.getCout();

            if (peutAmeliorer(piege, cout)) {
                depenserOr(cout);
                piege.amelioration();
            } else {
                System.out.println("Pas assez d'or pour améliorer !");
            }
            deselectionner();
        }
    }

    // Vérifie si le piège peut être amélioré
    private boolean peutAmeliorer(Piege piege, int cout) {
        return this.or.get() >= cout && piege.getNiveau() < piege.getNivMax();
    }

    // Vend le piège sélectionné
    public void VendrePiege() {
        Piege piege = getPiegeSelectionne();

        if (piege != null) {//si le piège n'est pas null
            ajouterOr(piege.prixVente());       //ajoute au crédit du joueur le prix de la vente
            deselectionner();
            retirerPiege(piege);            //retire le piège de la liste
        }
    }

    public Ennemi trouverEnnemiLePlusProche(){
        Ennemi plusProche = null;
        double distanceMin = Double.MAX_VALUE;

        //parcours la liste d'ennemis et trouve leur case
        for (Ennemi e : this.listeEnnemis) {
            double distance = calculerDistanceDepuisPorte(e);

            if (distance < distanceMin) {
                distanceMin = distance;
                plusProche = e;
            }
        }
        return plusProche;
    }

    // Calcule la longueur du chemin entre un ennemi et la porte avec BFS
    private double calculerDistanceDepuisPorte(Ennemi e) {
        int col = (int) (e.getX() / tailleTuile);
        int lig = (int) (e.getY() / tailleTuile);
        Case caseEnnemi = this.carte.getCaseAlgo(col, lig);

        if (caseEnnemi == null) {
            return Double.MAX_VALUE;
        }
        Bfs bfs = new Bfs(this.carte, porte);
        ArrayList<Case> chemin = bfs.cheminVersSource(caseEnnemi);

        if (chemin == null) {
            return Double.MAX_VALUE;
        }
        return chemin.size();
    }

    public void declencherBouleDeFeu() {
        Ennemi plusProche = trouverEnnemiLePlusProche();

        if (plusProche != null /*&& ennemisTues >= 15*/) {
            this.listeEnnemis.remove(plusProche);
            ajouterOr(plusProche.getPrime());
            this.ennemisTues = 1;
        }
    }

    // Conditions de victoire et de défaite
    public boolean estPartieGagnee() {
        return this.gestionnaireVagues.vagueTerminee()
                && this.vague.get()
                >= this.gestionnaireVagues.getNbTotalVagues()
                && this.listeEnnemis.isEmpty();
    }

    public boolean estPartiePerdue() {
        for (Ennemi e : listeEnnemis) {
            if (e != null && e.getX() == x_Porte
                    && e.getY() == y_Porte) {
                return true;
            }
        }
        return false;
    }

    // Gestion des listes
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

    // Sélection d'un piège
    public void selectionnerPiege(Piege p) {
        this.piegeSelectionne = p;
        if (p != null) {
            System.out.println("Piège sélectionné" + " (Niveau " + p.getNiveau() + ")");
        }
    }

    public void deselectionner() {
        this.piegeSelectionne = null;
    }

    // Gestion de l'or
    public void ajouterOr(int valeur){
        if (valeur > 0){
            this.or.set(this.or.get() + valeur);
        } else {
            System.out.println("valeur invalide");
        }
    }

    //à l'achat d'un piège si le crédit est suffisant
    public void depenserOr(int valeur){
        if(or.get() >= valeur) {
            or.set(or.get() - valeur);
        }
    }

    // Getters et setters
    public IntegerProperty getOr() {
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

    public Piege getPiegeEn(int ligne, int colonne) {
        int i = 0;

        while (listePieges.size() > i
                && getXCase(listePieges.get(i)) != ligne
                && getYCase(listePieges.get(i)) != colonne) {
            i++;
        }
        if (i >= listePieges.size()){
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
        return this.listePieges.get(
                this.listePieges.size() - 1);
    }
}