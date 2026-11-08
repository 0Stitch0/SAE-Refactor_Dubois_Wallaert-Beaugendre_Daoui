package universite_paris8.iut.mdubois.killer_princess.controleur.chrono;

import javafx.animation.Timeline;
import universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu.ModeJeu;


public class GestionnairePause {
    private final Timeline gameLoop;
    private final ModeJeu modeJeu;
    private boolean isPaused = false;

    public GestionnairePause(Timeline gameLoop, ModeJeu modeJeu) {
        this.gameLoop = gameLoop;
        this.modeJeu = modeJeu;
    }

    // Met en pause si le jeu tourne, reprend s'il est en pause
    public void basculer() {
        isPaused = !isPaused;
        if (isPaused) {
            gameLoop.pause();
            modeJeu.pause();
        } else {                //sinon (booléen faux) on remet en marche la boucle et le mode de jeu
            gameLoop.play();
            modeJeu.reprendre();
        }
    }

    // Arrête complètement la boucle et le mode de jeu (fin de partie)
    public void arreter() {
        gameLoop.stop();
        modeJeu.arreter();
    }

    public boolean isPaused() {
        return isPaused;
    }
}