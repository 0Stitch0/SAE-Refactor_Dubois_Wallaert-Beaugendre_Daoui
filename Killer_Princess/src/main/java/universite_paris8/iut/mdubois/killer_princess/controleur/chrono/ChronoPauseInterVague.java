package universite_paris8.iut.mdubois.killer_princess.controleur.chrono;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ChronoPauseInterVague extends ChronoJeu {
    private final ImageView nouvelleVague, trois, deux, un;

    public ChronoPauseInterVague(ImageView nouvelleVague, ImageView trois, ImageView deux, ImageView un) {
        this.nouvelleVague = nouvelleVague;
        this.trois = trois;
        this.deux = deux;
        this.un = un;
    }

    public void lancer(double message, double three, double two, double one, double vague, Runnable finDecompte) {
        //Timers pour l'affichage du décompte juste avant la vague
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(message), event -> {
                    nouvelleVague.setOpacity(1);
                }),
                new KeyFrame(Duration.seconds(three), event -> {
                    nouvelleVague.setOpacity(0);
                    trois.setOpacity(1);
                }),
                new KeyFrame(Duration.seconds(two), event -> {
                    trois.setOpacity(0);
                    deux.setOpacity(1);
                }),
                new KeyFrame(Duration.seconds(one), event -> {
                    deux.setOpacity(0);
                    un.setOpacity(1);
                }),
                new KeyFrame(Duration.seconds(vague), event -> {
                    un.setOpacity(0);
                    finDecompte.run();      //ce qui se passe à la fin du décompte (vague suivante)
                })
        );
        timeline.setCycleCount(1);   //programme le timer de pause entre vagues pour se lancer qu'une seule fois
        timeline.play();             //lance le timer
    }
}
