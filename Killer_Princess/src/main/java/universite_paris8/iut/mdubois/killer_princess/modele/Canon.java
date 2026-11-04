package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class Canon extends Tour {
    public Canon(double x, double y, Jeu partie) {
        super(x, y, partie, 10, 20, "CANON", 75, 2, 3);
    }

    @Override
    public int degatTir() {
        return 3;
    }
}