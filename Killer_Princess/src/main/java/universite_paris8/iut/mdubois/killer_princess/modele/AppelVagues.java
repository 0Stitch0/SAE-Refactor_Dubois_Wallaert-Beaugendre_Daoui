
package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;
import static universite_paris8.iut.mdubois.killer_princess.modele.Carte.tailleTuile;

public class AppelVagues {
    private Jeu partie;
    private GestionnaireVagues gestionnaireVagues;
    private FabriqueInstance fabriqueEnnemis;

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
            int minLigne = 1;
            int maxLigne = 19;

            caseDepartY = minLigne
                    + (int) (Math.random() * (maxLigne - minLigne + 1));
            caseDepartX = 29;
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
}
