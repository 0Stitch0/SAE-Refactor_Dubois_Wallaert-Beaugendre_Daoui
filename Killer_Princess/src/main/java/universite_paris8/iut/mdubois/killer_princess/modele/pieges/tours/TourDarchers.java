package universite_paris8.iut.mdubois.killer_princess.modele.pieges.tours;

import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;

public class TourDarchers extends Tour {
    public TourDarchers(double x, double y, Jeu partie){
        super(x, y,partie,10,2,"TOUR_ARCHERS", 20, 20, 4, 60);
    }
}