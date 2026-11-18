package universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu;

import javafx.animation.Timeline;
import javafx.util.Duration;
import universite_paris8.iut.mdubois.killer_princess.controleur.chrono.ChronoSpawn;
import universite_paris8.iut.mdubois.killer_princess.modele.*;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.TypeEnnemi;

public class ModeSurvie implements ModeJeu{
    private final Jeu partie;
    private final FabriqueInstance fabriqueInstance;
    private final ChronoSpawn chronoVagues = new ChronoSpawn();

    public ModeSurvie(Jeu partie, FabriqueInstance fabriqueInstance){
        this.partie = partie;
        this.fabriqueInstance = fabriqueInstance;
    }

//mode survie = Vague infini
    @Override
    public void demarrer(){
        System.out.println("La vague infini commence");
        chronoVagues.lancer(Duration.seconds(2), Timeline.INDEFINITE, () -> {
            int aleatoireEnnemi;
            int aleatoirePositionY;
            aleatoireEnnemi = (int)(Math.random() * 100) + 1;
            aleatoirePositionY = (int)(Math.random() * 19) + 1;
            TypeEnnemi typeEnnemi = fabriqueInstance.retournerTypeEnnemi(aleatoireEnnemi);
            //31 est mieux que 32 car ennemie à moitié sur la carte
            Ennemi ennemi = fabriqueInstance.creerInstanceEnnemi(typeEnnemi, 29 * 31, aleatoirePositionY * 31);
            partie.ajouterEnnemi(ennemi);
        });
    }

    @Override
    public void mettreAJour(){
        //rien à faire à chaque frame en survie, doit la laisser meme si vide
    }

//pour arrêter le chrono
    @Override
    public void arreter(){
        chronoVagues.arreter();
    }

//pour mettre en pause le chrono
    @Override
    public void pause(){
        chronoVagues.pause();
    }

//pour reprendre le chrono là où on en était
    @Override
    public void reprendre(){
        chronoVagues.reprendre();
    }
}