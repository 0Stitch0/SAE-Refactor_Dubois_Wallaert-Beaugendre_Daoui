package universite_paris8.iut.mdubois.killer_princess.modele.ennemis;

import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;

public class Invincible extends Ennemi {

    public Invincible(Jeu partie, int x, int y) {
        super(x,  y, partie,15, 4, "INVINCIBLE", 1, 8);
    }
}
