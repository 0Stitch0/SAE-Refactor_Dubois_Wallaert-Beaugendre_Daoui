
package universite_paris8.iut.mdubois.killer_princess.modele;

import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;

import java.util.ArrayList;
import static universite_paris8.iut.mdubois.killer_princess.modele.Carte.tailleTuile;

public class AppelVagues {

    private static final int MIN_LIGNE_DEPART = 1;
    private static final int MAX_LIGNE_DEPART = 19;
    private static final int COLONNE_DEPART = 29;

    private final Jeu partie;
    private final GestionnaireVagues gestionnaireVagues;
    private final FabriqueInstance fabriqueEnnemis;

    public AppelVagues(Jeu partie) {
        this.partie = partie;
        this.gestionnaireVagues = partie.getGestionnaireVagues();
        this.fabriqueEnnemis = new FabriqueInstance(partie);
    }

    public ArrayList<Ennemi> spawnUnGroupe() {
        GroupeParVague groupeMetier =
                gestionnaireVagues.getVagueActuelle().appelrochainGroupe();

        if (groupeMetier == null) {
            if (gestionnaireVagues.vagueTerminee()) {
                gestionnaireVagues.vagueSuivante();
            }
            return null;
        }

        ArrayList<Ennemi> ennemisCrees = new ArrayList<>();

        for (String type : groupeMetier.getTypesEnnemis()) {
            Ennemi ennemi = creerEnnemi(type, groupeMetier);
            partie.ajouterEnnemi(ennemi);
            ennemisCrees.add(ennemi);
        }

        return ennemisCrees;
    }

    private Ennemi creerEnnemi(String type, GroupeParVague groupeMetier) {
        int caseDepartX;
        int caseDepartY;

        if (partie.getModeJeu() == 1) {
            caseDepartY = genererLigneDepartAleatoire();
            caseDepartX = COLONNE_DEPART;
        } else {
            Case caseDepart =
                    partie.getCarte().trouverTuile(groupeMetier.getIdEntree());

            caseDepartX = caseDepart.getColonne();
            caseDepartY = caseDepart.getLigne();
        }

        int pixelX = caseDepartX * tailleTuile;
        int pixelY = caseDepartY * tailleTuile;

        return fabriqueEnnemis.creerInstanceEnnemi(type, pixelX, pixelY);
    }

    private int genererLigneDepartAleatoire() {
        return MIN_LIGNE_DEPART
                + (int) (Math.random()
                * (MAX_LIGNE_DEPART - MIN_LIGNE_DEPART + 1));
    }
}
