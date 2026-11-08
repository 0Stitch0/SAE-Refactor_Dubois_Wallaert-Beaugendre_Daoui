package universite_paris8.iut.mdubois.killer_princess.controleur.chrono;

import javafx.animation.Timeline;

//classe de base de tous les chronomètres du jeu, arrêter, pause, reprendre.

public abstract class ChronoJeu {
    protected Timeline timeline = new Timeline();

    public void arreter() {
        timeline.stop();
    }

    public void pause() {
        timeline.pause();
    }

    public void reprendre() {
        if(timeline.getStatus() == Timeline.Status.PAUSED){
            timeline.play();
        }
    }

    public boolean enCours() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }
}