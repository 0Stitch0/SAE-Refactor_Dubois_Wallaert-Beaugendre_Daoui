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
        new ListenerPiege(partie, vuePiege, paneEntitee, gnd).activer();
        new ListenerEnnemis(partie, vueEnnemis, paneEntitee).activer();
    }

}
