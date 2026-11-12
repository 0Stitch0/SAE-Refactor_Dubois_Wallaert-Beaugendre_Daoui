package universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu;

import javafx.scene.image.ImageView;
import universite_paris8.iut.mdubois.killer_princess.controleur.chrono.ChronoPauseInterVague;
import universite_paris8.iut.mdubois.killer_princess.controleur.chrono.ChronoSpawn;
import universite_paris8.iut.mdubois.killer_princess.controleur.spawn.StrategieApparition;
import universite_paris8.iut.mdubois.killer_princess.modele.*;

public class ModeVagues implements ModeJeu {
    private final Jeu partie;
    private final StrategieApparition strategieApparition;
    private final ChronoSpawn chronoVagues = new ChronoSpawn();
    private final ChronoPauseInterVague pauseInterVague;
    private boolean modeRapideDeclenche = false;

    public ModeVagues(Jeu partie, StrategieApparition strategieApparition,
                      ImageView nouvelleVague, ImageView trois, ImageView deux, ImageView un) {
        this.partie = partie;
        this.strategieApparition = strategieApparition;
        this.pauseInterVague = new ChronoPauseInterVague(nouvelleVague, trois, deux, un);
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
        chronoVagues.arreter();
        pauseInterVague.arreter();
    }

    @Override
    public void pause() {
        chronoVagues.pause();
        pauseInterVague.pause();
    }

    @Override
    public void reprendre() {
        chronoVagues.reprendre();
        pauseInterVague.reprendre();
    }

    private boolean resteDesVagues() {
        return partie.getVague().get() < partie.getGestionnaireVagues().getNbTotalVagues();
    }

    //Lance le chronomètre de spawn de la vague actuelle
    public void SpawnVagues() {
        // Si une vague est déjà en train d'apparaître, on ne fait rien
        if (chronoVagues.enCours()) return;

        System.out.println("La vague commence !");

        //La cadence et le nombre de cycles viennent de la stratégie (selon la map)
        //À chaque tic de la cadence, on appelle gererApparitionEnnemi
        chronoVagues.lancer(strategieApparition.getCadence(),
                strategieApparition.getNombreCycles(),
                () -> gererApparitionEnnemi());
    }

    private void gererApparitionEnnemi() {
        //Le spawn dépend de la map, c'est la stratégie qui s'en occupe
        boolean vagueTermineeEtFileVide = strategieApparition.faireApparaitre();

        //Gestion unique de fin de vague pour les 2 maps (Pour les deux maps)
        if (!vagueTermineeEtFileVide) return;

        chronoVagues.arreter();

        //S'il reste des vagues et qu'il y a des ennemis en vie -> Pause de 30s
        if (resteDesVagues() && !partie.getListeEnnemis().isEmpty()) {
            pauseInterVague.arreter(); // évite les conflits
            declencherPauseInterVague(26, 27, 28, 29, 30);
        }
    }

    private void declencherPauseInterVague(double message, double three, double two, double one, double vague) {
        pauseInterVague.lancer(message, three, two, one, vague, () -> {
            this.modeRapideDeclenche = false;       //rechanche le boolean pour la prochaine vague

            partie.passerAVagueSuivante();      //change la vague de la classe Jeu
            SpawnVagues();      //rappel la méthode pour initialiser une TimeLine avec la taille du nombre de groupes de la vague suivante
        });
    }

    private void skipTimerVagues() {
        boolean toutEstMort = partie.getGestionnaireVagues().vagueTerminee() && partie.getListeEnnemis().isEmpty();
        if (toutEstMort && !modeRapideDeclenche && resteDesVagues()) {
            System.out.println("Tous les ennemis sont morts ! Passage en mode rapide.");
            modeRapideDeclenche = true;

            // Stope la pause longue si elle tourne
            pauseInterVague.arreter();
            declencherPauseInterVague(1, 2, 3, 3.5, 4);
        }
    }
}
