package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public abstract class Tour extends Destructible {
    private int portee;
    private Ennemi cibleActuelle = null;

    public Tour(double x, double y, Jeu partie, int vie, int degat, String nom, int prime, int cout, int portee, int intervalle){
        super( x, y, partie,  vie, degat, nom, prime, cout, intervalle);
        this.portee = portee;
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

    @Override
    public void attaqueE(ArrayList<Ennemi> ennemiSurCase) {
        cibleActuelle = null;
        super.attaqueE(ennemiSurCase);
    }

    protected boolean appliquerEffet(ArrayList<Ennemi> ennemiSurCase) {

        double rayonPixels = getPortee() * 32; // dépend par rapport a la portée de la tour

        for (Ennemi ennemi : ennemiSurCase) {
            double dx = ennemi.getX() - this.getX();
            double dy = ennemi.getY() - this.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= rayonPixels) {
                ennemi.enleverPdv(getDegats());
                cibleActuelle = ennemi; // mémorise la cible pour l'animation
                return true; // 1 seul ennemi à la fois
            }
        }
        return false;
    }
}
