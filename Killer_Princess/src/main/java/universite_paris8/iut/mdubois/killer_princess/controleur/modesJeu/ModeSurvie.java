package universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu;

import javafx.animation.Timeline;
import javafx.util.Duration;
import universite_paris8.iut.mdubois.killer_princess.controleur.chrono.ChronoSpawn;
import universite_paris8.iut.mdubois.killer_princess.modele.*;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;

public class ModeSurvie implements ModeJeu {
    private final Jeu partie;
    private final FabriqueInstance fabriqueInstance;
    private final ChronoSpawn chronoVagues = new ChronoSpawn();

    public ModeSurvie(Jeu partie, FabriqueInstance fabriqueInstance) {
        this.partie = partie;
        this.fabriqueInstance = fabriqueInstance;
    }

    // Mode survie Vague infini
    @Override
    public void demarrer() {
        System.out.println("La vague infini commence");
        chronoVagues.lancer(Duration.seconds(2), Timeline.INDEFINITE, () -> {
            int aleatoireEnnemi;
            int aleatoirePositionY;
            aleatoireEnnemi = (int) (Math.random() * 100) + 1;
            aleatoirePositionY = (int) (Math.random() * 19) + 1;
            String ennemiNom = fabriqueInstance.retournerStringEnnemi(aleatoireEnnemi);
            // 31 est mieux que 32 car ennemie a moitié sur la map
            Ennemi ennemi = fabriqueInstance.creerInstanceEnnemi(ennemiNom, 29 * 31, aleatoirePositionY * 31);
            partie.ajouterEnnemi(ennemi);
        });
    }

    @Override
    public void mettreAJour() {
        // rien à faire à chaque frame en survie
    }

    @Override
    public void arreter() {
        chronoVagues.arreter();
    }

    @Override
    public void pause() {
        chronoVagues.pause();
    }

    @Override
    public void reprendre() {
        chronoVagues.reprendre();
    }
}