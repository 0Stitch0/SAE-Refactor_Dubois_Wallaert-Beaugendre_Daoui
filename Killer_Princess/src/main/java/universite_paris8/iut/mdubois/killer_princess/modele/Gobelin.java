package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class Gobelin extends Ennemi {

    public Gobelin(Jeu partie, int x, int y) {
        super(x,  y, partie,15, 4, "GOBELIN", 1.5, 2);
    }
}
