package universite_paris8.iut.mdubois.killer_princess.controleur.listeners;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;
import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;
import universite_paris8.iut.mdubois.killer_princess.vue.VueEnnemis;

public class ListenerEnnemis implements StategieListener {
    private final Jeu partie;
    private final VueEnnemis vueEnnemis;
    private final Pane paneEntitee;

    public ListenerEnnemis(Jeu partie, VueEnnemis vueEnnemis, Pane paneEntitee) {
        this.partie = partie;
        this.vueEnnemis = vueEnnemis;
        this.paneEntitee = paneEntitee;
    }

    @Override
    public void activer() {
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
