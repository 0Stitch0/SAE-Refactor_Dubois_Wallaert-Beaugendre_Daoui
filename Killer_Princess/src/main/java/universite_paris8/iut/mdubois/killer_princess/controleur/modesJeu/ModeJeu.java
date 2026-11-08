package universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu;

public interface ModeJeu {
    void demarrer();
    void mettreAJour();   // appelé à chaque frame
    void arreter();
    void pause();
    void reprendre();
}