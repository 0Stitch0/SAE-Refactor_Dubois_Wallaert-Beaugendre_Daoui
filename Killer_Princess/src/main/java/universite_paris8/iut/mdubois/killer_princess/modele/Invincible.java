package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class Invincible extends Ennemi {

    public Invincible(Jeu partie, int x, int y) {
        super(x,  y, partie,15, 4, "INVINCIBLE", 1, 1);
    }
}
