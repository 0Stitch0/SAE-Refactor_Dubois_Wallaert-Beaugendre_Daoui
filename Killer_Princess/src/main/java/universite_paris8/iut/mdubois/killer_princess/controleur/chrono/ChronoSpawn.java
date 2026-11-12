package universite_paris8.iut.mdubois.killer_princess.controleur.chrono;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ChronoSpawn extends ChronoJeu {

    public void lancer(Duration cadence, int cycles, Runnable action) {
        //À chaque tic de la cadence, on exécute l'action
        KeyFrame actionSpawn = new KeyFrame(cadence, event -> action.run());

        //Initialisation du timeline
        timeline = new Timeline(actionSpawn);
        timeline.setCycleCount(cycles);
        timeline.play();
    }
}