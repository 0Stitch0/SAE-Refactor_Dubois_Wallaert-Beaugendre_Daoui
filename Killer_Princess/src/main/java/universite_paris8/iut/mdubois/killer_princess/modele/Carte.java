
package universite_paris8.iut.mdubois.killer_princess.modele;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;
import java.util.*;


public class Carte {
    public static int tailleTuile = 32;
    private ArrayList<Rectangle2D> listHitBox;
    private ArrayList<Rectangle2D> listPiegeHitBox;
    private Map<Case, Set<Case>> listeAdj;
    private ObservableList<Case> Obstacles;
    private int[][] carte;
    private int modeJeu;


    public Carte(int modeJeu){
        listHitBox = new ArrayList<>();
        listPiegeHitBox = new ArrayList<>();
        listeAdj= new HashMap<>();
        Obstacles= FXCollections.observableArrayList();
        this.modeJeu= modeJeu;

        if (this.modeJeu == 1) {
            this.carte = new int[][] {
                    {10, 10, 10,  12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12},
                    {10, 10, 10,  13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13},
                    {10, 10, 10,  12, 13, 12, 13, 12, 13, 20, 13, 12, 13, 12, 13, 12, 13, 21, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 20, 12},
                    {10, 10, 10,  13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 21, 21, 20, 13, 12, 13, 12, 13, 12, 13, 12, 13},
                    {10, 10, 10,  12, 13, 12, 13, 12, 13, 12, 20, 12, 13, 12, 13, 20, 13, 21, 20, 21, 13, 12, 13, 12, 20, 12, 13, 21, 13, 21},
                    {10, 10, 10,  13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 21, 13, 12, 13, 12, 13, 21, 13, 20, 13, 21, 13},
                    {10, 10, 10,  12, 13, 12, 13, 20, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 21, 20, 12, 13, 20, 13, 20, 13, 12, 13, 12},
                    {10, 10, 10,  13, 12, 13, 12, 20, 12, 13, 12, 13, 12, 13, 12, 13, 12, 21, 14, 13, 20, 13, 12, 13, 12, 13, 20, 13, 12, 13},
                    {10, 10, 10,  12, 13, 12, 13, 20, 13, 21, 13, 12, 13, 12, 13, 20, 13, 20, 13, 21, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12},
                    {10, 10, 10,  13, 12, 13, 12, 13, 12, 13, 20, 20, 21, 13, 20, 20, 12, 21, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13},
                    {10, 10, 100, 12, 13, 12, 13, 12, 13, 12, 20, 12, 20, 12, 20, 20, 13, 12, 20, 12, 13, 12, 20, 12, 13, 12, 13, 12, 13, 12},//100=tuile qui determine le point cible du bfs avant de prendre toute la ligne
                    {10, 10, 10,  13, 12, 13, 14, 13, 20, 13, 12, 13, 12, 13, 12, 20, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 20, 12, 21},
                    {10, 10, 10,  12, 13, 14, 13, 21, 20, 21, 13, 12, 13, 12, 13, 20, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 20, 12},
                    {10, 10, 10,  13, 12, 13, 14, 20, 20, 21, 12, 13, 12, 13, 21, 13, 20, 13, 21, 13, 12, 13, 12, 13, 12, 13, 12, 21, 12, 13},
                    {10, 10, 10,  12, 13, 21, 13, 14, 20, 21, 13, 12, 13, 12, 21, 12, 13, 20, 20, 21, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12},
                    {10, 10, 10,  13, 12, 13, 12, 13, 20, 13, 12, 13, 12, 20, 12, 13, 12, 13, 20, 13, 12, 13, 20, 21, 12, 20, 12, 13, 12, 13},
                    {10, 10, 10,  12, 13, 12, 20, 12, 13, 12, 13, 20, 21, 12, 13, 12, 13, 12, 21, 12, 13, 12, 20, 12, 21, 21, 13, 20, 13, 12},
                    {10, 10, 10,  13, 12, 13, 12, 21, 12, 13, 12, 21, 21, 13, 12, 13, 12, 13, 12, 13, 21, 20, 12, 13, 21, 20, 12, 13, 21, 13},
                    {10, 10, 10,  12, 13, 12, 13, 20, 13, 12, 13, 12, 13, 20, 13, 12, 13, 12, 13, 12, 13, 12, 21, 12, 13,20, 13, 12, 13, 12},
                    {10, 10, 10,  13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 21, 13, 12, 13, 12, 13, 12, 13, 12, 13},
                    {10, 10, 10,  12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 20, 13, 12, 13, 12, 13, 12, 13, 12},
                    {10, 10, 10,  13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 12, 13, 21, 13, 12, 13, 12, 13, 12, 13, 12, 13},
            };
        }
        else {
            this.carte = new int[][] {
                    {21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {8, 12, 12, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 12, 12, 12, 12, 21, 21, 21, 21, 21, 21, 21},
                    {21, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 12, 12, 12, 12, 12, 21, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {21, 21, 21, 12, 12, 21, 12, 12, 12, 12, 12, 12, 12, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 12, 12, 12, 12, 12, 21, 21},
                    {21, 21, 21, 21, 12, 12, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 12, 12, 12, 12, 21, 21, 12, 21, 21, 21, 12, 21, 21},
                    {11, 12, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 12, 12, 12, 12, 12, 21, 21, 12, 12, 12, 12, 12, 21, 21, 21, 12, 21, 21},
                    {21, 12, 12, 12, 12, 21, 21, 21, 21, 21, 12, 12, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21},
                    {21, 21, 21, 21, 12, 12, 21, 21, 21, 12, 12, 21, 21, 10, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 12, 21, 21},
                    {21, 21, 21, 21, 21, 12, 12, 12, 12, 12, 21, 21, 10, 10, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 10, 100, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {21, 21, 12, 12, 12, 12, 12, 12, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {9, 12, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 21, 21, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 21, 21, 21, 21, 21},
                    {21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 12, 12, 12, 12, 12, 7},
            };
        }
        construitGraphe();
    }

    //tile qui sont entre 10 et 19 compris = sol (10=fer, 11=sable, 12=herbe, ...)
    //tile entre 20 et 29 compris = obstacle (20=arbe, 21=buisson, 22=eau?...)
    //tile entre 30 et 39 compris = piege (30= trappe, 31=magma, ...)

    //dit quel tuiles est un obstacle pour la colision
    public boolean estObstacle(double entiteX, double entiteY, int largeur, int hauteur) {
        // Les coins de la hiybox de l'entite
        int[] cointX = {(int) entiteX, (int)(entiteX + largeur - 1)};
        int[] cointY = {(int) entiteY, (int)(entiteY + hauteur - 1)};

        for (int cx : cointX){
            for (int cy : cointY) {
                int col= cx/tailleTuile; // x = colonne (j)
                int lig= cy/tailleTuile; // y = ligne (i)
                if (lig>=0 && lig<carte.length && col>=0 && col<carte[0].length) {
                    int id= carte[lig][col];
                    if (id>=20 && id<30) {
                        return true; //dit que y a colision
                    }
                }
            }
        }
        return false;
    }

    // Retourne la Case qui contient l'id cherché (ex: 100)
    public Case trouverTuile(int idRecherche){
        for (int i=0; i<carte.length; i++){
            for (int j=0; j<carte[i].length; j++){
                if (carte[i][j]==idRecherche){
                    //return new Case(i, j, carte[i][j]);
                    return getCaseAlgo(j, i);
                }
            }
        }
        return null; //tuile introuvable
    }

    private void construitGraphe() {
        //crée toutes les cases/sommets
        for (int i=0; i<carte.length; i++) {
            for (int j=0; j<carte[i].length; j++) {
                Case c= new Case(i, j, carte[i][j]); // x=colonne y=ligne
                listeAdj.put(c, new HashSet<>());
            }
        }

        // lies chaque case à ses voisins (haut/bas/gauche/droite)
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        for (Case c : listeAdj.keySet()) {
            for (int[] d : directions) {
                int nx = c.getColonne() + d[0];
                int ny = c.getLigne() + d[1];
                if (dansGrille(nx, ny)) {
                    Case voisin = getCaseAlgo(nx, ny);
                    if (voisin != null) listeAdj.get(c).add(voisin);
                }
            }
        }
        // evite de mettre les obstacles (tuiles 20-29) dans la liste
        for (Case c : listeAdj.keySet()) {
            int id = carte[c.getLigne()][c.getColonne()];
            if(id >= 20 && id < 30){
                Obstacles.add(c);
                deconnecte(c);
            }
        }
    }

    /**
     * Reconnecte une case au graphe (ex : on retire un obstacle).
     */
    public void reconnecte(Case c) {
        if (!listeAdj.containsKey(c)) return;


        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        for (int[] d : directions) {
            int nx= c.getColonne() + d[0];
            int ny= c.getLigne() + d[1];
            if(dansGrille(nx, ny)){
                Case voisin = getCaseAlgo(nx, ny);
                if(voisin != null){
                    listeAdj.get(c).add(voisin);
                    listeAdj.get(voisin).add(c);
                }
            }
        }
        Obstacles.remove(c);
    }

    /**
     * Déconnecte une case du graphe (ex : on pose un obstacle).
     */
    public void deconnecte(Case c) {
        if (!listeAdj.containsKey(c)) return;


        for(Case voisin : listeAdj.get(c)){
            listeAdj.get(voisin).remove(c);
        }
        listeAdj.get(c).clear();
    }

    /**
     * Retourne true si la case n'a aucun voisin (= obstacle / déconnectée).
     */
    public boolean estDeconnecte(Case c){
        Set<Case> voisins = listeAdj.get(c);
        return voisins==null || voisins.isEmpty();
    }

    /**
     * Retourne les voisins accessibles d'une case.
     */
    public Set<Case> adjacents(Case c){
        return listeAdj.getOrDefault(c, Collections.emptySet());
    }
    /**
     * Pose des obstacles aléatoires sur un pourcentage de cases libres.
     */
    public void poseObstacles(int pourcent){
        List<Case> libres= new ArrayList<>();
        for(Case c : listeAdj.keySet()){
            if(!estDeconnecte(c)) libres.add(c);
        }
        int nb= (int)(libres.size()*pourcent/100.0);
        Collections.shuffle(libres);
        for(int i=0; i<nb; i++) {
            Obstacles.add(libres.get(i));
            deconnecte(libres.get(i));
        }
    }

    private boolean dansGrille(int x, int y) {
        return x >= 0 && x < carte[0].length && y >= 0 && y < carte.length;
    }

    public Set<Case> getSommets(){
        return listeAdj.keySet();
    }

    /**
     * Retourne la Case à la position (x=colonne, y=ligne) dans le graphe.
     */
    public Case getCaseAlgo(int x, int y) {
        for (Case c : listeAdj.keySet()) {
            if (c.getColonne()==x && c.getLigne()==y){
                return c;
            }
        }
        return null;
    }


    /**
     * Retourne l'id de tuile brut à la position (x=ligne, y=colonne).
     * Attention : convention d'origine de ta Carte (x=ligne, y=colonne).
     */
    public int getCaseBrute(int x, int y) {
        if(x>=0 && x<carte.length && y>=0 && y<carte[0].length){
            return carte[x][y];
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Carte ").append(getColonne()).append("x").append(getLigne()).append("\n");
        for (int i = 0; i < carte.length; i++) {
            for (int j = 0; j < carte[i].length; j++) {
                Case c = getCaseAlgo(j, i);
                sb.append(c != null && estDeconnecte(c) ? "[X]" : "[ ]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int[][] getCarte() {
        return carte;
    }

    public int getTailleTuile(){
        return tailleTuile;
    }

    public  int getColonne(){
        return carte[0].length;
    }

    public int getLigne(){
        return carte.length;
    }

    public int getCase(int x, int y){
        if (x >= 0 && x < carte.length && y >= 0 && y < carte[0].length){
            return carte[x][y];
        }
        return 0;
    }

    public boolean peutPoserPiege(double pixelX, double pixelY, String typePiege) {
        // Convertir les pixels en coordonnées de la carte
        int col = (int) (pixelX / tailleTuile);
        int lig = (int) (pixelY / tailleTuile);

        // Vérifier que la position est dans la carte
        if (lig < 0 || lig >= carte.length
                || col < 0 || col >= carte[0].length) {
            return false;
        }

        int idTuile = carte[lig][col];

        return estTuileAutorisee(idTuile, typePiege);
    }

    private boolean estTuileAutorisee(int idTuile, String typePiege) {
        // Sur la carte 2, les tours et les canons se posent sur la tuile 21
        if (modeJeu == 2 && estTourOuCanon(typePiege)) {
            return idTuile == 21;
        }

        // Les autres pièges se posent sur les tuiles d'herbe
        return idTuile >= 12 && idTuile < 20;
    }

    private boolean estTourOuCanon(String typePiege) {
        return "TOUR_ARCHERS".equals(typePiege)
                || "CANON".equals(typePiege);
    }
}
