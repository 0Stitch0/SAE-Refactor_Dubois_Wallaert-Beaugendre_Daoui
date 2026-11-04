package universite_paris8.iut.mdubois.killer_princess.controleur.listeners;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mdubois.killer_princess.controleur.*;
import universite_paris8.iut.mdubois.killer_princess.modele.*;
import universite_paris8.iut.mdubois.killer_princess.vue.*;

public class ListenerJeu {
    private final Jeu partie;
    private final VuePiege vuePiege;
    private final VueEnnemis vueEnnemis;
    private final Pane paneEntitee;
    private final DragAndDrop gnd;

    public ListenerJeu(Jeu partie, VuePiege vuePiege, VueEnnemis vueEnnemi, Pane paneEntite, DragAndDrop gnd) {
        this.partie = partie;
        this.vuePiege = vuePiege;
        this.vueEnnemis = vueEnnemi;
        this.paneEntitee = paneEntite;
        this.gnd = gnd;
    }

    public void activer() {
        listenerPiege();
        listenerEnnemies();
    }

    public void listenerPiege(){
        partie.getListePieges().addListener((ListChangeListener<Piege>) change -> {
            while (change.next()) {
                //Un piège est ajouté via drag and drop
                if (change.wasAdded()) {
                    for (Piege nouveauPiege : change.getAddedSubList()) {
                        vuePiege.afficherPiegeSurMap(nouveauPiege, nouveauPiege.getNom(), paneEntitee, gnd.getGestionnaireClic());
                    }
                }
                //Un piège est vendu ou détruit pas ennemis
                if (change.wasRemoved()) {
                    for (Piege piegeSupprime : change.getRemoved()) {
                        vuePiege.supprimerPiegeDeLaMap(piegeSupprime, paneEntitee);
                    }
                }
            }
        });
    }

    public void listenerEnnemies(){
        partie.getListeEnnemis().addListener((ListChangeListener<Ennemi>) change -> {
            while (change.next()) {
                //Un ennemi apparait pendant les vagues
                if (change.wasAdded()) {
                    for (Ennemi nouvelEnnemi : change.getAddedSubList()) {
                        vueEnnemis.creerVuePourEnnemi(nouvelEnnemi);
                        // Change image si attaque ou run
                        nouvelEnnemi.attaqueProperty().addListener((obs, oldVal, newVal) -> {
                            if(newVal){
                                vueEnnemis.imageEnnemieEnAttaque(nouvelEnnemi);
                            }
                            else{
                                vueEnnemis.imageEnnemieEnRun(nouvelEnnemi);
                            }
                        });
                    }
                }
                //Un ennemi meurt
                if (change.wasRemoved()) {
                    for (Ennemi ennemiMort : change.getRemoved()) {
                        vueEnnemis.supprimerEnnemiDeLaMap(ennemiMort, paneEntitee);
                    }
                }
            }
        });
    }
}
