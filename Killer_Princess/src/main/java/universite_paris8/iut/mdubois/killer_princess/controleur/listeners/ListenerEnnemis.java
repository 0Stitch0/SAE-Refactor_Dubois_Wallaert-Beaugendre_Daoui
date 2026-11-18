package universite_paris8.iut.mdubois.killer_princess.controleur.listeners;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;
import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;
import universite_paris8.iut.mdubois.killer_princess.vue.VueEnnemis;

public class ListenerEnnemis implements StategieListener{
    private final Jeu partie;
    private final VueEnnemis vueEnnemis;
    private final Pane paneEntitee;

    public ListenerEnnemis(Jeu partie, VueEnnemis vueEnnemis, Pane paneEntitee){
        this.partie = partie;
        this.vueEnnemis = vueEnnemis;
        this.paneEntitee = paneEntitee;
    }

    @Override
    public void activer(){
        partie.getListeEnnemis().addListener((ListChangeListener<Ennemi>) change -> {
            while (change.next()){
            //un ennemi apparaît pendant les vagues
                if (change.wasAdded()){//si élément ajouter = true
                    for (Ennemi nouvelEnnemi : change.getAddedSubList()){
                        vueEnnemis.creerVuePourEnnemi(nouvelEnnemi);
                    //change l'image si l'ennemi est en: attaque ou marche
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
            //un ennemi meurt
                if (change.wasRemoved()){
                    for (Ennemi ennemiMort : change.getRemoved()){
                        vueEnnemis.supprimerEnnemiDeLaMap(ennemiMort, paneEntitee);
                    }
                }
            }
        });
    }
}
