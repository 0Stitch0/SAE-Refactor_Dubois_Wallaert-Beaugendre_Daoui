package universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import universite_paris8.iut.mdubois.killer_princess.controleur.*;
import universite_paris8.iut.mdubois.killer_princess.modele.*;
import universite_paris8.iut.mdubois.killer_princess.vue.*;

public class ModeSurvie implements ModeJeu {
    private final Jeu partie;
    private final FabriqueInstance fabriqueInstance;
    private Timeline chronoVagues;

    public ModeSurvie(Jeu partie, FabriqueInstance fabriqueInstance) {
        this.partie = partie;
        this.fabriqueInstance = fabriqueInstance;
    }

    // Mode survie Vague infini
    @Override
    public void demarrer() {
        System.out.println("La vague infini commence");
        chronoVagues = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            int aleatoireEnnemi;
            int aleatoirePositionY;
            aleatoireEnnemi = (int) (Math.random() * 100) + 1;
            aleatoirePositionY = (int) (Math.random() * 19) + 1;
            String ennemiNom = fabriqueInstance.retournerStringEnnemi(aleatoireEnnemi);
            // 31 est mieux que 32 car ennemie a moitié sur la map
            Ennemi ennemi = fabriqueInstance.creerInstanceEnnemi(ennemiNom, 29 * 31, aleatoirePositionY * 31);
            partie.ajouterEnnemi(ennemi);
        }));
        chronoVagues.setCycleCount(Timeline.INDEFINITE);
        chronoVagues.play();
    }

    @Override
    public void mettreAJour() {
        // rien à faire à chaque frame en survie
    }

    @Override
    public void arreter() {
        if(chronoVagues != null){
            chronoVagues.stop();
        }
    }

    @Override
    public void pause() {
        if(chronoVagues != null){
            chronoVagues.pause();
        }
    }

    @Override
    public void reprendre() {
        if(chronoVagues != null && chronoVagues.getStatus() == Timeline.Status.PAUSED) {
            chronoVagues.play();
        }
    }
}