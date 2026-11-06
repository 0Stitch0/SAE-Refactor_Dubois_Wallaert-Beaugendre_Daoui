package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class Prince extends Ennemi {

    public Prince(Jeu partie, int x, int y) {
        super(x,  y, partie,15, 4, "PRINCE", 1, 1);
    }
}
