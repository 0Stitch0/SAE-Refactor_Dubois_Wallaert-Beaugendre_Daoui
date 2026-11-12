package universite_paris8.iut.mdubois.killer_princess.vue;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;

import java.net.URL;
import java.util.HashMap;

public class VueEnnemis {
    private HashMap<String, Image> ennemieRun;
    private HashMap<String, Image> ennemieAttaque;
    @FXML private Pane paneEntitee;
    private HashMap<Ennemi, ImageView> imagesEnnemies = new HashMap<>();

    public VueEnnemis(Pane paneEnnemie){
        ennemieRun = new HashMap<>();
        ennemieAttaque = new HashMap<>();

        this.paneEntitee = paneEnnemie;
        initializeEnnemis();
    }

    //sencé recup le chemin
    public Image creerImage(String chemin){
        URL urlIm= getClass().getResource(chemin);
        if(urlIm == null){
            System.out.println("Image non trouvée: "+chemin);
            return null;
        }
        return new Image(urlIm.toString());
    }

    public void ennemieRun(String id, String chemin){
        Image image = creerImage(chemin);
        if(image != null){
            ennemieRun.put(id, image);
        }
    }
    public void ennemieAttaque(String id, String chemin){
        Image image = creerImage(chemin);
        if(image != null){
            ennemieAttaque.put(id, image);
        }
    }

    //tile qui sont entre 10 et 19 compris = sol (10=fer, 11=sable, 12=herbe, ...)
    //tile entre 20 et 29 compris = obstacle (20=arbe, 21=buisson, 22=eau?...)
    //tile entre 30 et 39 compris = piege (30= trappe, 31=magma, ...)
    //id 40 a 49(a augmenter si besoin) compris = images des ennemis

    //va chercher les images dans ressources
    public void initializeEnnemis(){
        ennemieRun("CHEVALIER","/universite_paris8/iut/mdubois/killer_princess/Ennemie/Warrior_Blue_Run.gif");
        ennemieRun("GOBELIN","/universite_paris8/iut/mdubois/killer_princess/Ennemie/Gobelin_Run.gif");
        ennemieRun("SHREK","/universite_paris8/iut/mdubois/killer_princess/Ennemie/Shrek_Run.gif");
        ennemieRun("PRINCE","/universite_paris8/iut/mdubois/killer_princess/Ennemie/Prince_Run.gif");
        ennemieRun("INVINCIBLE","/universite_paris8/iut/mdubois/killer_princess/Ennemie/Invincible_Run.gif");

        ennemieAttaque("CHEVALIER","/universite_paris8/iut/mdubois/killer_princess/Ennemie/Warrior_Blue_Attack.gif");
        ennemieAttaque("GOBELIN","/universite_paris8/iut/mdubois/killer_princess/Ennemie/Gobelin_Attack.gif");
        ennemieAttaque("SHREK","/universite_paris8/iut/mdubois/killer_princess/Ennemie/Shrek_Attack.gif");
        ennemieAttaque("PRINCE","/universite_paris8/iut/mdubois/killer_princess/Ennemie/Prince_Attack.gif");
    }

    // Change le gifs de l'ennemie en attaque
    public void imageEnnemieEnAttaque(Ennemi ennemi){
        ImageView image = imagesEnnemies.get(ennemi);
        Image gif = ennemieAttaque.get(ennemi.getNom());
        image.setImage(gif);
    }

    // Change le gifs de l'ennemie en run
    public void imageEnnemieEnRun(Ennemi ennemi){
        ImageView image = imagesEnnemies.get(ennemi);
        Image gif = ennemieRun.get(ennemi.getNom());
        image.setImage(gif);
    }

    public void creerVuePourEnnemi(Ennemi monstre) {
        VBox conteneurMonstre = new VBox(2);        //Fixe l'écart vertical à 2 pixels

        //Initialise la barre de vie
        ProgressBar barreDeVie = new ProgressBar();
        barreDeVie.setPrefWidth(32);
        barreDeVie.setPrefHeight(10);

        // Ajustement de la barre de vie en y pour le petit gobelin
        if (monstre.getNom() == "GOBELIN"){
            barreDeVie.setTranslateX(0);
            barreDeVie.setTranslateY(10);
        }

        //réduit la barre de vie en fonction des pdv de l'ennemi avec un bind
        barreDeVie.progressProperty().bind(
                monstre.pdvProperty().divide((double) monstre.getVie())
        );

        barreDeVie.setStyle("-fx-accent: red;");

        ImageView imageViewsEn = new ImageView();
        imagesEnnemies.put(monstre, imageViewsEn);      //ajoute l'image et sa barre de vie au bagage de l'ennemi

        conteneurMonstre.getChildren().addAll(barreDeVie, imageViewsEn);
        monstre.getProperties().put("conteneur_monstre", conteneurMonstre);
        System.out.println(monstre.getNom());

        // Changer selon run ou attaque
        Image texture = ennemieRun.get(monstre.getNom());
        if (texture != null) {
            imageViewsEn.setImage(texture);
        }

        //fait avancer l'image de l'ennemi et sa barre en fonction de sa position dans le jeu
        conteneurMonstre.layoutXProperty().bind(monstre.getXProp());
        conteneurMonstre.layoutYProperty().bind(monstre.getYProp());

        // Car image ennemie trop bas
        conteneurMonstre.setTranslateY(-15);

        this.paneEntitee.getChildren().add(conteneurMonstre);
    }

    public void supprimerEnnemiDeLaMap(Ennemi e, Pane pan) {
        if (e != null && pan != null) {
            VBox v = (VBox) e.getProperties().get("conteneur_monstre");    //récupère l'image du monstre et sa barre de vie

            if (v != null) {
                pan.getChildren().remove(v);        //les retire de l'affichage du jeu
                e.getProperties().remove("conteneur_monstre");
            }
            imagesEnnemies.remove(e);
            System.out.println("Ennemi nettoyé de la carte.");
        }
    }
}
