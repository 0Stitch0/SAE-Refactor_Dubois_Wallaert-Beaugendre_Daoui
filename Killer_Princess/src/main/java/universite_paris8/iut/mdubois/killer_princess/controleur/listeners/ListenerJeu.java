package universite_paris8.iut.mdubois.killer_princess.controleur.listeners;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mdubois.killer_princess.controleur.*;
import universite_paris8.iut.mdubois.killer_princess.modele.*;
import universite_paris8.iut.mdubois.killer_princess.vue.*;

public class ListenerJeu  {
    private final StategieListener[] strategies;

    public ListenerJeu(Jeu partie, VuePiege vuePiege, VueEnnemis vueEnnemi, Pane paneEntite, DragAndDrop gnd) {
        strategies = new StategieListener[]{
                new ListenerPiege(partie, vuePiege, paneEntite, gnd),
                new ListenerEnnemis(partie, vueEnnemi, paneEntite)
        };
    }

    public void activer() {
        for (StategieListener strategie : strategies) {
            strategie.activer();
        }
    }
}

