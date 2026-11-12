package universite_paris8.iut.mdubois.killer_princess.modele;

import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;

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
        GroupeParVague groupeMetier = partie.getGestionnaireVagues().getVagueActuelle().appelrochainGroupe();       //cherche le groupe de la vague actuelle de la classe Jeu

        //Si le groupe est null
        if (groupeMetier == null) {     //et que la vague est finie, on passe à la vague suivante
            if (gestionnaireVagues.vagueTerminee()) {
                gestionnaireVagues.vagueSuivante();
            }
            return null;    //on retourne null dans tous les cas
        }

        ArrayList<Ennemi> ennemisCrees = new ArrayList<>();

        for (String type : groupeMetier.getTypesEnnemis()) {
            int caseDepartX, caseDepartY;

            if(partie.getModeJeu() == 1) {
                int minLigne = 1;
                int maxLigne = 19;
                caseDepartY = minLigne + (int) (Math.random() * ((maxLigne - minLigne) + 1));

                caseDepartX = 29;
            }
            else{
                Case caseDepart = partie.getCarte().trouverTuile(groupeMetier.getIdEntree());
                caseDepartX = caseDepart.getColonne();
                caseDepartY = caseDepart.getLigne();
            }

            // Conversion en pixels
            int pixelX = caseDepartX * tailleTuile;
            int pixelY = caseDepartY * tailleTuile;

            Ennemi nouveau = fabriqueEnnemis.creerInstanceEnnemi(type, pixelX, pixelY);
            partie.ajouterEnnemi(nouveau);

            ennemisCrees.add(nouveau);
        }
        return ennemisCrees;
    }
}