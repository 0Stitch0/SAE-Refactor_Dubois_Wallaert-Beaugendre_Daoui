package universite_paris8.iut.mdubois.killer_princess.modele;

public class FabriqueInstance {
    private Jeu partie;

    public FabriqueInstance(Jeu partie) {
        this.partie = partie;
    }

    //crée le piège
    public Piege creerInstancePiege(String type, double x, double y) {
        switch (type) {
            case "BARRICADE_BOIS":
                return new BarricadeBois(x, y, partie);
            case "TOUR_ARCHERS":
                return new TourDarchers(x, y, partie);
            case "CANON":
                return new Canon(x, y, partie);
            case "MAGMA":
                return new Magma(x, y, partie);
            case "TRAPPE":
                return new Trappe(x, y, partie);
                //cree squelette voir apres les attaque des autres pieges
//            case "SQUELETTE":
//                return new Squelette(x, y, partie);
            default:
                return null;
        }
    }

    // Pour le mode Survie car choisit aléatoire appelé par le controleur
    public String retournerStringEnnemi (int tirage){
        if (tirage <= 5)
            return "INVINCIBLE";
        if(tirage <= 15)
            return "PRINCE";
        if(tirage <= 35)
            return "SHREK";
        if(tirage <= 65)
            return "CHEVALIER";
        if (tirage <= 100)
            return "GOBELIN";
        return null;
    }

    public Ennemi creerInstanceEnnemi(String type, int x, int y) {
        switch (type) {
            case "GOBELIN":
                return new Gobelin(partie, x, y);
            case "CHEVALIER":
                return new Chevalier(partie, x, y);
            case "SHREK":
                return new Shrek(partie, x, y);
            case "PRINCE":
                return new Prince(partie, x, y);
            case "INVINCIBLE":
                return new Invincible(partie, x, y);
            default:
                return null;
        }
    }

    public int getCoutPour(String type) {
        Piege piegeTemporaire = this.creerInstancePiege(type, 0, 0);
        if (piegeTemporaire != null) {
            return piegeTemporaire.getCout();
        }
        return 0;
    }
}