package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class BarricadeBois extends Destructible {

    public BarricadeBois(double x, double y, Jeu partie) {
        super(x, y, partie, 20,2, "BARRICADE_BOIS", 10, 20, 60);
    }

    @Override
    protected boolean appliquerEffet(ArrayList<Ennemi> ennemiSurCase) {
        double caseFaceX = this.getX() + 25;
        double caseFaceY = this.getY();
        boolean aAttaque = false;

        for(Ennemi ennemi : ennemiSurCase) {
            if(Math.abs(ennemi.getX()-caseFaceX) < 1 && Math.abs(ennemi.getY() - caseFaceY) <= 0) {
                ennemi.enleverPdv(getDegats());
                aAttaque = true;
            }
        }
        return aAttaque;
    }
}