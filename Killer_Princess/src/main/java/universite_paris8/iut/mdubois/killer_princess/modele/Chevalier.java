package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class Chevalier extends Ennemi {

    public Chevalier(Jeu partie, int x, int y) {
        super(x,  y, partie,15, 4, "CHEVALIER", 1, 1);
    }
}
