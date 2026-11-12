package universite_paris8.iut.mdubois.killer_princess.controleur.spawn;

import javafx.util.Duration;

public interface StrategieApparition {
    Duration getCadence();
    int getNombreCycles();

    // fait apparaître les ennemis, renvoie true si la vague est terminée (et la file vide)
    boolean faireApparaitre();
}