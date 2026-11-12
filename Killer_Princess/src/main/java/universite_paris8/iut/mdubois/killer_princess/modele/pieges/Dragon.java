package universite_paris8.iut.mdubois.killer_princess.modele.pieges;

import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;

import java.util.ArrayList;

public class Dragon extends Piege {

    public Dragon(Jeu partie){
        super( 120, 220, partie, 10, 1000, "DRAGON", 0, 200, 60);
    }

    @Override
    public void attaqueE(ArrayList<Ennemi> ennemis) {
        Ennemi e = EnnemisLePlusProche(ennemis);
        e.enleverPdv(getDegats());
    }

    @Override
    protected boolean appliquerEffet(ArrayList<Ennemi> ennemiSurCase) {
        return false;
    }
}