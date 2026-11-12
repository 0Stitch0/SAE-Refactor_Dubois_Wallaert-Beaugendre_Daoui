package universite_paris8.iut.mdubois.killer_princess.modele.ennemis;

import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;

public class Shrek extends Ennemi {

    public Shrek (Jeu partie, int x, int y) {
        super(x,  y, partie,15, 4, "SHREK", 0.5, 15);
    }
}
