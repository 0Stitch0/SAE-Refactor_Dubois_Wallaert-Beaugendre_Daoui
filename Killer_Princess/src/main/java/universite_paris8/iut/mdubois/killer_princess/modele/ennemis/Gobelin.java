package universite_paris8.iut.mdubois.killer_princess.modele.ennemis;

import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;

public class Gobelin extends Ennemi {

    public Gobelin(Jeu partie, int x, int y) {
        super(x,  y, partie,15, 4, "GOBELIN", 1.5, 2);
    }
}
