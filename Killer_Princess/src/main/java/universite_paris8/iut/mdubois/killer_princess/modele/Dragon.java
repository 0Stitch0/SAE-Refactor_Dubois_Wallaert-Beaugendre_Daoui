package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class Dragon extends Piege {

    public Dragon(Jeu partie){
        super( 120, 220, partie, 10, 1000, "DRAGON", 0, 200);
    }

    @Override
    public void attaqueE(ArrayList<Ennemi> ennemis) {
        Ennemi e = EnnemisLePlusProche(ennemis);
        e.enleverPdv(getDegats());
    }

    public boolean enPlace(){
        return true;
    }
}