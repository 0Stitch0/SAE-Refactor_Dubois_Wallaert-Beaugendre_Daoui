package universite_paris8.iut.mdubois.killer_princess.modele.pieges;

public enum TypePiege {
    BARRICADE_BOIS,
    TRAPPE,
    MAGMA,
    TOUR_ARCHERS,
    CANON;

    public boolean estTour() {
        return this == TOUR_ARCHERS || this == CANON;
    }
}
