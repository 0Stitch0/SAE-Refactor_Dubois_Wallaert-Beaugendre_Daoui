package universite_paris8.iut.mdubois.killer_princess.controleur.spawn;

import javafx.util.Duration;
import universite_paris8.iut.mdubois.killer_princess.controleur.*;
import universite_paris8.iut.mdubois.killer_princess.modele.*;
import universite_paris8.iut.mdubois.killer_princess.vue.*;

public class ApparitionParGroupe implements StrategieApparition {
    private final Jeu partie;
    private final AppelVagues spawner;

    public ApparitionParGroupe(Jeu partie, AppelVagues spawner) {
        this.partie = partie;
        this.spawner = spawner;
    }

    @Override
    public Duration getCadence() {
        //carte 1 : spawn par groupe toutes les 3 secondes
        return Duration.seconds(3);
    }

    @Override
    public int getNombreCycles() {
        //nombre de groupes dans la vague actuelle
        return partie.getGestionnaireVagues().getVagueActuelle().getNombreDeGroupes();
    }

    @Override
    public boolean faireApparaitre() {
        //carte 1 : Spawn du groupe entier d'un coup
        this.spawner.spawnUnGroupe();
        return partie.getGestionnaireVagues().vagueTerminee();
    }
}