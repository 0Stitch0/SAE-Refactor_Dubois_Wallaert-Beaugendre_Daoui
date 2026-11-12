package universite_paris8.iut.mdubois.killer_princess.controleur.listeners;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mdubois.killer_princess.controleur.DragAndDrop;
import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;
import universite_paris8.iut.mdubois.killer_princess.modele.pieges.Piege;
import universite_paris8.iut.mdubois.killer_princess.vue.VuePiege;

public class ListenerPiege implements StategieListener {
    private final Jeu partie;
    private final VuePiege vuePiege;
    private final Pane paneEntitee;
    private final DragAndDrop gnd;

    public ListenerPiege(Jeu partie, VuePiege vuePiege, Pane paneEntitee, DragAndDrop gnd) {
        this.partie = partie;
        this.vuePiege = vuePiege;
        this.paneEntitee = paneEntitee;
        this.gnd = gnd;
    }

    @Override
    public void activer() {
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
}