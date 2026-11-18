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

    public ModeVagues(Jeu partie, StrategieApparition strategieApparition, ImageView nouvelleVague, ImageView trois, ImageView deux, ImageView un){
        this.partie = partie;
        this.strategieApparition = strategieApparition;
        this.pauseInterVague = new ChronoPauseInterVague(nouvelleVague, trois, deux, un);
    }

//Lance la partie en commençant l'apparition de la vague
    @Override
    public void demarrer(){
        if(partie.getGestionnaireVagues().getVagueActuelle().getNum()<partie.getGestionnaireVagues().getNbTotalVagues()) {
            SpawnVagues();
        }
    }

    @Override
    public void mettreAJour(){
        skipTimerVagues();
    }

    @Override
    public void arreter(){
        chronoVagues.arreter();
        pauseInterVague.arreter();
    }

    @Override
    public void pause(){
        chronoVagues.pause();
        pauseInterVague.pause();
    }

    @Override
    public void reprendre(){
        chronoVagues.reprendre();
        pauseInterVague.reprendre();
    }

//permet de voir s'il reste des vagues à faire apparaître
    private boolean resteDesVagues(){
        return partie.getVague().get() < partie.getGestionnaireVagues().getNbTotalVagues();
    }

//lance le chrono de l'apparition (spawn) de la vague actuelle
    public void SpawnVagues(){
    //si une vague est déjà en train d'apparaître, on ne fait rien
        if (chronoVagues.enCours()) return;
        System.out.println("La vague commence !");

    //la cadence et le nombre de cycles viennent de la stratégie (en fonction de la carte)
    //a chaque tic de la cadence, on appelle gererApparitionEnnemi
        chronoVagues.lancer(strategieApparition.getCadence(), strategieApparition.getNombreCycles(), () -> gererApparitionEnnemi());
    }

    private void gererApparitionEnnemi(){
    //l'appariton dépend de la carte, c'est la stratégie qui s'en occupe
        boolean vagueTermineeEtFileVide = strategieApparition.faireApparaitre();

    //gestion de la fin de vague pour les 2 cartes
        if (!vagueTermineeEtFileVide) return;
        chronoVagues.arreter();

    //si il reste des vagues et qu'il y a des ennemis en vie il y a une pause de 40sec
        if (resteDesVagues() && !partie.getListeEnnemis().isEmpty()){
            pauseInterVague.arreter(); // évite les conflits
            declencherPauseInterVague(26, 27, 28, 29, 40);
        }
    }

//gere la pause entre 2 vagues
    private void declencherPauseInterVague(double message, double three, double two, double one, double vague){
        pauseInterVague.lancer(message, three, two, one, vague, () -> {
            this.modeRapideDeclenche = false;   //remet la boolean a false pour la prochaine vague

            partie.passerAVagueSuivante();  //change la vague de la classe Jeu (passe a la suivante)
            SpawnVagues();   //recrée la Timeline avec le nombre de groupes de la nouvelle vague
        });
    }

//permet, lorsqu'il n'y a plus d'ennemi sur la carte, de ne pas avoir à attendre le reste du temps
    private void skipTimerVagues(){
    //vérifie qu'il n'y a plus d'ennemis = qu'ils sont tous morts
        boolean toutEstMort = partie.getGestionnaireVagues().vagueTerminee() && partie.getListeEnnemis().isEmpty();
        if (toutEstMort && !modeRapideDeclenche && resteDesVagues()){
            System.out.println("Tous les ennemis sont morts ! Passage en mode rapide.");
            modeRapideDeclenche = true;

            //stoppe la pause longue si elle tourne
            pauseInterVague.arreter();
            declencherPauseInterVague(1, 2, 3, 3.5, 4);
        }
    }
}
