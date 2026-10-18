package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class Trappe extends Piege{

    public Trappe(double x, double y, Jeu partie){
        super(x, y, partie, 20, 0, "TRAPPE", 10, 20);
    }

    @Override
    public void attaqueE(ArrayList<Ennemi> ennemiSurCase) {
        double caseFaceX = this.getX() + 10;
        double caseFaceY = this.getY();
        for(Ennemi ennemi : ennemiSurCase){
            if(Math.abs(ennemi.getX()-caseFaceX)<1 && Math.abs(ennemi.getY()-caseFaceY)<=0){
                ennemi.enleverPdv(ennemi.getVie());
            }
        }
    }
    public boolean enPlace(){
        return true;
    }
}