package universite_paris8.iut.mdubois.killer_princess.modele.ennemis;
import universite_paris8.iut.mdubois.killer_princess.modele.EnMouvement;
import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;

public abstract class Ennemi extends EnMouvement {

    public Ennemi(double x, double y, Jeu partie, int vie, int degats, String nom, double vitesse, int prime) {
        super(x, y, partie, vie, degats, nom, vitesse, prime);
    }

    public void mouvementAutoVersObjctif(){
        //faire un if si incincible pour son deplacment et un if pas attaque ou juste il a un degat de fou et nique tout
        super.mouvementBFS();
    }

    public void enleverPdv(int degats){
        if(degats>0){
            enleverPointDeVie(degats);
        }else{
            System.out.println("valeur invalide");
        }
    }

    public boolean estMort(){
        return getPdv()<=0;
    }
}
