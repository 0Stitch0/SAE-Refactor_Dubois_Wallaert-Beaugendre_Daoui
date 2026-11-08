package universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import universite_paris8.iut.mdubois.killer_princess.controleur.spawn.StrategieApparition;
import universite_paris8.iut.mdubois.killer_princess.modele.*;

public class ModeVagues implements ModeJeu {
    private final Jeu partie;
    private final StrategieApparition strategieApparition;
    private final ImageView nouvelleVague, trois, deux, un;
    private Timeline chronoVagues, pauseInterVague;
    private boolean modeRapideDeclenche = false;

    public ModeVagues(Jeu partie, StrategieApparition strategieApparition,
                      ImageView nouvelleVague, ImageView trois, ImageView deux, ImageView un) {
        this.partie = partie;
        this.strategieApparition = strategieApparition;
        this.nouvelleVague = nouvelleVague;
        this.trois = trois;
        this.deux = deux;
        this.un = un;
    }

    @Override
    public void demarrer() {
        if(partie.getGestionnaireVagues().getVagueActuelle().getNum()<partie.getGestionnaireVagues().getNbTotalVagues()) {
            SpawnVagues();
        }
    }

    @Override
    public void mettreAJour() {
        skipTimerVagues();
    }

    @Override
    public void arreter() {
        if (chronoVagues != null) chronoVagues.stop();
        if (pauseInterVague != null) pauseInterVague.stop();
    }

    @Override
    public void pause() {
        if (chronoVagues != null) chronoVagues.pause();
        if (pauseInterVague != null) pauseInterVague.pause();
    }

    @Override
    public void reprendre() {
        if (chronoVagues != null && chronoVagues.getStatus() == Timeline.Status.PAUSED) {
            chronoVagues.play();
        }
        if (pauseInterVague != null && pauseInterVague.getStatus() == Timeline.Status.PAUSED) {
            pauseInterVague.play();
        }
    }

    //Lance le chronomètre de spawn de la vague actuelle
    public void SpawnVagues() {
        if (chronoVagues == null || chronoVagues.getStatus() != Timeline.Status.RUNNING) {
            System.out.println("La vague commence !");

            //La cadence et le nombre de cycles viennent de la stratégie (selon la map)
            Duration cadence = strategieApparition.getCadence();
            int cycles = strategieApparition.getNombreCycles();

            //À chaque tic de la cadence, on appelle gererApparitionEnnemi
            KeyFrame actionSpawn = new KeyFrame(cadence, event -> gererApparitionEnnemi());

            //Initialisation de chronovagues
            chronoVagues = new Timeline(actionSpawn);
            chronoVagues.setCycleCount(cycles);
            chronoVagues.play();
        }
    }

    private void gererApparitionEnnemi() {
        //Le spawn dépend de la map, c'est la stratégie qui s'en occupe
        boolean vagueTermineeEtFileVide = strategieApparition.faireApparaitre();

        //Gestion unique de fin de vague pour les 2 maps (Pour les deux maps)
        if (vagueTermineeEtFileVide) {
            if (chronoVagues != null) {
                chronoVagues.stop();
            }

            //S'il reste des vagues et qu'il y a des ennemis en vie -> Pause de 30s
            if (partie.getVague().get() < partie.getGestionnaireVagues().getNbTotalVagues() && !partie.getListeEnnemis().isEmpty()) {
                if (pauseInterVague != null) pauseInterVague.stop(); // évite les conflits
                declencherPauseInterVague(26, 27, 28, 29, 30);
            }
        }
    }

    private void declencherPauseInterVague(double message, double three, double two, double one, double vague) {
        if (partie.getGestionnaireVagues().vagueTerminee() && partie.getVague().get() < partie.getGestionnaireVagues().getNbTotalVagues()) {
            //Timers pour l'affichage du décompte juste avant la vague
            this.pauseInterVague = new Timeline(
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
                        this.modeRapideDeclenche = false;       //rechanche le boolean pour la prochaine vague

                        partie.passerAVagueSuivante();      //change la vague de la classe Jeu
                        SpawnVagues();      //rappel la méthode pour initialiser une TimeLine avec la taille du nombre de groupes de la vague suivante
                    })
            );
            pauseInterVague.setCycleCount(1);   //programme le timer de pause entre vagues pour se lancer qu'une seule fois
            pauseInterVague.play();         //lance le timer
        }
    }

    private void skipTimerVagues() {
        if (partie.getGestionnaireVagues().vagueTerminee() && partie.getListeEnnemis().isEmpty()) {
            if (!modeRapideDeclenche && partie.getVague().get() < partie.getGestionnaireVagues().getNbTotalVagues()) {
                System.out.println("Tous les ennemis sont morts ! Passage en mode rapide.");
                modeRapideDeclenche = true;

                // Stoppe la pause longue si elle tourne
                if (pauseInterVague != null) {
                    pauseInterVague.stop();
                }
                declencherPauseInterVague(1, 2, 3, 3.5, 4);
            }
        }
    }
}
