package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public abstract class Tour extends Destructible {
    private int portee;
    private int cooldown = 0;
    private Ennemi cibleActuelle = null;
    private int intervalle;

    public Tour(double x, double y, Jeu partie, int vie, int degat, String nom, int prime, int cout, int portee, int intervalle){
        super( x, y, partie,  vie, degat, nom, prime, cout);
        this.portee = portee;
        this.intervalle = intervalle;
    }

    public void amelioration(){
        super.amelioration();
        this.setPortee(this.portee+2);
    }

    public Ennemi getCibleActuelle() {
        return cibleActuelle;
    }

    public void setPortee(int portee) {
        this.portee = portee;
    }

    public int getPortee() {
        return portee;
    }

    public boolean enPlace(){
        return true;
    }

    public abstract int degatTir();

    public int getIntervalle() {
        return this.intervalle;
    }

    @Override
    public void attaqueE(ArrayList<Ennemi> ennemiSurCase) {
        cibleActuelle = null;

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        double rayonPixels = 3 * 32; // 3 cases en pixels

        for (Ennemi ennemi : ennemiSurCase) {
            double dx = ennemi.getX() - this.getX();
            double dy = ennemi.getY() - this.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= rayonPixels) {
                ennemi.enleverPdv(degatTir());
                cibleActuelle = ennemi; // mémorise la cible pour l'animation
                cooldown = this.getIntervalle();
                return; // 1 seul ennemi à la fois
            }
        }
    }

}
