package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class Dragon extends Piege {

    public Dragon(Jeu partie){
        super( 120, 220, partie, 10, 1000, "DRAGON", 0, 200);
    }

    public Ennemi EnnemisLePlusProche(ArrayList<Ennemi> ennemis) {
        Ennemi eProche = ennemis.get(0);

        for (int i=1; i<ennemis.size(); i++) {
            if(ennemis.get(i).getY()<eProche.getY()){
                eProche = ennemis.get(i);
            }
        }
        return eProche;
    }

    @Override
    public void attaqueE(ArrayList<Ennemi> ennemis) {
        Ennemi e = EnnemisLePlusProche(ennemis);
        e.enleverPdv(e.getVie()-this.getDegat());
    }

    public boolean enPlace(){
        return true;
    }
}