package universite_paris8.iut.mdubois.killer_princess.modele;

import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.TypeEnnemi;

import java.util.ArrayList;

public class GroupeParVague {
    private ArrayList<TypeEnnemi> typesEnnemis;
    private int idEntree;

    public GroupeParVague(int idEntree) {
        this.typesEnnemis = new ArrayList<>();
        this.idEntree = idEntree;
    }

    public void ajouterEnnemi(TypeEnnemi type, int nb) {
        for (int i = 0; i < nb; i++) {
            this.typesEnnemis.add(type);
        }
    }

    public ArrayList<TypeEnnemi> getTypesEnnemis() {
        return this.typesEnnemis;
    }

    public int getIdEntree() {
        return this.idEntree;
    }
}