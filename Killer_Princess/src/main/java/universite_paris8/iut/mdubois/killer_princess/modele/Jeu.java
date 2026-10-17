
package universite_paris8.iut.mdubois.killer_princess.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public Jeu(int modeJeu) {
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
        this.x_Porte = porte.getColonne() * tailleTuile;
        this.y_Porte = porte.getLigne() * tailleTuile;
    }

    // Vérifie si la case est déjà occupée par un piège
    public boolean caseOccupee(int ligne, int colonne) {
        boolean caseOcc = false;
        int i = 0;

        while (i < listePieges.size() && !caseOcc) {
            if (listePieges.get(i).getX() == colonne
                    && listePieges.get(i).getY() == ligne) {
                caseOcc = true;
            }
            i++;
        }

        return caseOcc;
    }

    // Tente de placer un piège sur la carte
    public boolean tenterPlacerPiege(
            String type, int col, int lig, FabriqueInstance fabriquePiege) {

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
        // Déplacement des ennemis
        for (int i = 0; i < listeEnnemis.size(); i++) {
            Ennemi e = listeEnnemis.get(i);
            if (e != null) {
                e.mouvementAutoVersObjctif();
            }
        }

        // Récupération des ennemis vivants pour les attaques
        ArrayList<Ennemi> ennemisVivants = new ArrayList<>();
        for (Ennemi e : listeEnnemis) {
            if (!e.estMort()) {
                ennemisVivants.add(e);
            }
        }

        // Les pièges attaquent les ennemis vivants
        for (Piege p : listePieges) {
            p.attaqueE(ennemisVivants);
        }

        // Suppression des ennemis morts et récupération de leur prime
        for (int i = 0; i < listeEnnemis.size(); i++) {
            Ennemi e = listeEnnemis.get(i);
            if (e.estMort()) {
                supprimerEnnemi(e);
                ajouterOr(e.getPrime());
                i--;
            }
        }

        // Suppression des pièges détruits
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
        // ennemisTues++;
    }

    public void affichage() {
        for (int i = 0; i < listePieges.size(); i++) {
            Piege p = listePieges.get(i);
            System.out.println("cout : " + p.getCout()
                    + " niveau : " + p.getNiveau()
                    + " degat " + p.getDegat());
        }
    }

    public void AmeliorerPiege() {
        Piege piege = getPiegeSelectionne();

        if (piege != null) {
            int cout = piege.getCoutAmelioration();
            if (this.or.get() >= cout && piege.getNiveau() < piege.getNivMax()) {
                depenserOr(cout);
                piege.amelioration();
            } else {
                System.out.println("Pas assez d'or pour améliorer !");
            }
            deselectionner();
        }
    }

    public void VendrePiege() {
        Piege piege = getPiegeSelectionne();

        if (piege != null) {
            ajouterOr(piege.prixVente());
            deselectionner();
            retirerPiege(piege);
        }
    }

    public Ennemi trouverEnnemiLePlusProche() {
        Ennemi plusProche = null;

        if (!this.listeEnnemis.isEmpty()) {
            double distanceMin = Double.MAX_VALUE;

            for (Ennemi e : this.listeEnnemis) {
                int col = (int) (e.getX() / tailleTuile);
                int lig = (int) (e.getY() / tailleTuile);
                Case caseEnnemi = this.carte.getCaseAlgo(col, lig);

                if (caseEnnemi != null) {
                    Bfs bfs = new Bfs(this.carte, porte);
                    ArrayList<Case> chemin = bfs.cheminVersSource(caseEnnemi);

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

        if (plusProche != null /*&& ennemisTues>=15*/) {
            this.listeEnnemis.remove(plusProche);
            ajouterOr(plusProche.getPrime());
            this.ennemisTues = 1;
        }
    }

    // Conditions de victoire et de défaite
    public boolean estPartieGagnee() {
        return this.gestionnaireVagues.vagueTerminee()
                && this.vague.get() >= this.gestionnaireVagues.getNbTotalVagues()
                && this.listeEnnemis.isEmpty();
    }

    public boolean estPartiePerdue() {
        if (listeEnnemis != null) {
            for (Ennemi e : listeEnnemis) {
                if (e.getX() == x_Porte && e.getY() == y_Porte) {
                    return true;
                }
            }
        }
        return false;
    }

    // Gestion des listes
    public void ajouterPiege(Piege p) {
        listePieges.add(p);
    }

    public void ajouterEnnemi(Ennemi e) {
        listeEnnemis.add(e);
    }

    public void retirerPiege(Piege p) {
        listePieges.remove(p);
    }

    public void retirerEnnemi(Ennemi e) {
        listeEnnemis.remove(e);
    }

    // Sélection d'un piège
    public void selectionnerPiege(Piege p) {
        this.piegeSelectionne = p;
        if (p != null) {
            System.out.println("Piège sélectionné (Niveau "
                    + p.getNiveau() + ")");
        }
    }

    public void deselectionner() {
        this.piegeSelectionne = null;
    }

    // Gestion de l'or
    public void ajouterOr(int valeur) {
        if (valeur > 0) {
            this.or.set(this.or.get() + valeur);
        } else {
            System.out.println("valeur invalide");
        }
    }

    public void depenserOr(int valeur) {
        if (or.get() >= valeur) {
            or.set(or.get() - valeur);
        }
    }

    // Getters et setters
    public IntegerProperty getOr() {
        return this.or;
    }

    public IntegerProperty getVague() {
        return this.vague;
    }

    public void setOr(int valeur) {
        this.or.set(valeur);
    }

    public int getModeJeu() {
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

        if (i >= listePieges.size()) {
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
