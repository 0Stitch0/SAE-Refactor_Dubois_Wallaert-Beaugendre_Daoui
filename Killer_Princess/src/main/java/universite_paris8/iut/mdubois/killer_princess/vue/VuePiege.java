package universite_paris8.iut.mdubois.killer_princess.vue;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mdubois.killer_princess.controleur.GestionnaireClic;
import universite_paris8.iut.mdubois.killer_princess.modele.Destructible;
import universite_paris8.iut.mdubois.killer_princess.modele.Piege;
import java.util.HashMap;

import static universite_paris8.iut.mdubois.killer_princess.modele.Carte.tailleTuile;

public class VuePiege {
    private HashMap<String, Image> imagesPieges;

    public VuePiege() {
        this.imagesPieges = new HashMap<>();
        initialiserImages();
    }


    //Associe les types à leur images (en fonction de leur niveau d'amélioration également)
    private void initialiserImages() {
        imagesPieges.put("BARRICADE_BOIS", new Image(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Piege/Barricade.png").toString()));
        imagesPieges.put("TOUR_ARCHERS", new Image(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Piege/Tour_Archer.png").toString()));
        imagesPieges.put("CANON", new Image(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Piege/Canon.png").toString()));
        imagesPieges.put("MAGMA", new Image(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Piege/Magma.png").toString()));
        imagesPieges.put("TRAPPE", new Image(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Piege/Trappe.png").toString()));
    }

    public Image getImagePour(String libelle) {
        return imagesPieges.get(libelle);
    }

    //créer l'image du piège en fonction de son type et lui attribue la postion du piège
    public ImageView creerImagePiege(Piege p, String type) {
        ImageView imageSurMap = new ImageView(this.getImagePour(type));
        imageSurMap.setFitWidth(tailleTuile);
        imageSurMap.setFitHeight(tailleTuile);
        imageSurMap.setLayoutX(p.getX());
        imageSurMap.setLayoutY(p.getY());
        imageSurMap.getProperties().put("modele", p);
        p.getProperties().put("node_image", imageSurMap); //Ajout d'attributs à l'image et au piège pour les associer
        return imageSurMap;
    }

    //créer une barre de vie
    public ProgressBar creerBarreDeVie() {
        //création de la barre de vie
        ProgressBar barreDeVie = new ProgressBar();
        barreDeVie.setPrefWidth(tailleTuile);    //sa longueur
        barreDeVie.setPrefHeight(10);   //sa largeur
        barreDeVie.setStyle("-fx-accent: #2ecc71;");    //sa couleur (verte)
        return barreDeVie;

    }

    public void diminuerBarreDeVie(Piege p) {
        Destructible piegeDestructible = (Destructible) p;
        ProgressBar barreDeVie = new ProgressBar();
        //La barre diminue correctement au contact de l'ennemi
        barreDeVie.progressProperty().bind(
                piegeDestructible.getPdvProperty().divide((double) piegeDestructible.getVie())
        );
    }

    public void afficherBarreDeVie(Piege p) {
        Destructible piegeDestructible = (Destructible) p;
        ProgressBar barreDeVie = new ProgressBar();
        //La barre s'affiche uniquement si les PV actuels sont inférieurs aux PV de base (contact avec l'ennemi)
        barreDeVie.visibleProperty().bind(
                piegeDestructible.getPdvProperty().lessThan((double) piegeDestructible.getVie())
        );
    }

    public void afficherPiegeSurMap(Piege p, String type, Pane pan, GestionnaireClic gestionnaireClic) {
        ImageView imageSurMap = creerImagePiege(p, type);    //création de l'image du piège
        gestionnaireClic.configurerClic(imageSurMap, (int) p.getY(), (int) p.getX(), type, p);
        //Création de la barre de vie seulement pour les pièges destructibles (avec pdv)
        if (p instanceof Destructible) {
            Destructible piegeDestructible = (Destructible) p;
            ProgressBar barreDeVie = creerBarreDeVie();

            //on place la barre de vie sur la même colonne que le piège mais un peu au dessus (de 8px)
            barreDeVie.setLayoutX(p.getX() * tailleTuile);
            barreDeVie.setLayoutY((p.getY() * tailleTuile) - 8);

            diminuerBarreDeVie(p); // diminution de la barre de vie
            afficherBarreDeVie(p); // affichage de la barre de vie

            p.getProperties().put("node_barre", barreDeVie); //Stockage de la barre de vie dans le bagage du piège

            //ajout de sa barre de vie au pan
            if (pan != null) {
                pan.getChildren().add(barreDeVie);
                barreDeVie.toBack();
            }
        }
        //ajout de l'image du piège au pan
        if (pan != null) {
            pan.getChildren().add(imageSurMap);
            imageSurMap.toBack();//pour avoir les images des piege en dessous des ennemsi utile dasn visuel trappe
        }
    }

    public void supprimerPiegeDeLaMap(Piege p, Pane pan) {
        if (p != null && pan != null) {
            //récupération de la barre de vie du piège ET de son image de son "bagage" personnel
            ImageView imageSurMap = (ImageView) p.getProperties().get("node_image");
            ProgressBar barreDeVie = (ProgressBar) p.getProperties().get("node_barre");

            //surrpime l'image du piège si elle est non nulle
            if (imageSurMap != null) {
                pan.getChildren().remove(imageSurMap);
            }
            //surrpime l'image de la barre de vie du piège si il en possède une
            if (barreDeVie != null) {
                pan.getChildren().remove(barreDeVie);
            }
        }
    }
}