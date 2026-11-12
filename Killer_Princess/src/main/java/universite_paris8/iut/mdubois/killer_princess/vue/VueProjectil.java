
package universite_paris8.iut.mdubois.killer_princess.vue;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;
import universite_paris8.iut.mdubois.killer_princess.modele.pieges.tours.Canon;
import universite_paris8.iut.mdubois.killer_princess.modele.pieges.tours.TourDarchers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VueProjectil {

    private static final double LARGEUR_PROJECTILE = 12;
    private static final double HAUTEUR_PROJECTILE = 12;
    private static final double DECALAGE_DEPART_X = 10;
    private static final double DECALAGE_DEPART_Y = 16;
    private static final double DECALAGE_CIBLE_X = 10;
    private static final double DECALAGE_CIBLE_Y = 16;
    private static final double DUREE_ANIMATION = 300;

    private final Map<String, Image> imagesProjectils;

    public VueProjectil() {
        imagesProjectils = new HashMap<>();
        initialiserImages();
    }

    public Image creerImage(String chemin) {
        URL urlIm = getClass().getResource(chemin);

        if (urlIm == null) {
            System.out.println("Image non trouvée : " + chemin);
            return null;
        }

        return new Image(urlIm.toString());
    }

    public void ajouterImageBoulet(String nom, String chemin) {
        Image image = creerImage(chemin);

        if (image != null) {
            imagesProjectils.put(nom, image);
        }
    }

    private void initialiserImages() {
        ajouterImageBoulet(
                "boulet",
                "/universite_paris8/iut/mdubois/killer_princess/Piege/CanonBalle.png"
        );

        ajouterImageBoulet(
                "fleche",
                "/universite_paris8/iut/mdubois/killer_princess/Piege/Fleche.png"
        );
    }

    public void lancerBoulet(Canon canon, Ennemi cible, Pane pan) {
        lancerProjectil("boulet", canon.getX(), canon.getY(), cible, pan);
    }

    public void lancerFleche(TourDarchers tour, Ennemi cible, Pane pan) {
        lancerProjectil("fleche", tour.getX(), tour.getY(), cible, pan);
    }

    private void lancerProjectil(
            String nomProject,
            double departX,
            double departY,
            Ennemi cible,
            Pane pan
    ) {
        ImageView projectile = creerImageProjectil(nomProject);

        if (projectile == null) {
            return;
        }

        positionnerProjectile(projectile, departX, departY);
        pan.getChildren().add(projectile);

        TranslateTransition transition =
                creerTransitionProjectile(
                        projectile, departX, departY, cible, pan
                );

        transition.play();
    }

    private void positionnerProjectile(
            ImageView projectile,
            double departX,
            double departY
    ) {
        projectile.setLayoutX(departX + DECALAGE_DEPART_X);
        projectile.setLayoutY(departY + DECALAGE_DEPART_Y);
    }

    private ImageView creerImageProjectil(String nom) {
        Image image = imagesProjectils.get(nom);

        if (image == null) {
            return null;
        }

        ImageView projectile = new ImageView(image);
        projectile.setFitWidth(LARGEUR_PROJECTILE);
        projectile.setFitHeight(HAUTEUR_PROJECTILE);

        return projectile;
    }

    private TranslateTransition creerTransitionProjectile(
            ImageView projectile,
            double departX,
            double departY,
            Ennemi cible,
            Pane pan
    ) {
        TranslateTransition transition =
                new TranslateTransition(
                        Duration.millis(DUREE_ANIMATION),
                        projectile
                );

        transition.setByX(
                (cible.getX() + DECALAGE_CIBLE_X)
                        - (departX + DECALAGE_DEPART_Y)
        );

        transition.setByY(
                (cible.getY() + DECALAGE_CIBLE_Y)
                        - (departY + DECALAGE_DEPART_Y)
        );

        transition.setOnFinished(e ->
                pan.getChildren().remove(projectile)
        );

        return transition;
    }
}
