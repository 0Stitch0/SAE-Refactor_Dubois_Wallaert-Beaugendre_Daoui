package universite_paris8.iut.mdubois.killer_princess.modele.pieges.tours;


import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;

public class Canon extends Tour {
    public Canon(double x, double y, Jeu partie) {
        super(x, y, partie, 10, 3, "CANON", 75, 40, 3, 60);
    }
}