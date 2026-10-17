
package universite_paris8.iut.mdubois.killer_princess.vue;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import universite_paris8.iut.mdubois.killer_princess.modele.*;

import java.net.URL;
import java.util.HashMap;

public class VueProjectil {

    private final HashMap<String, Image> imagesProjectils;

    public VueProjectil() {
        imagesProjectils = new HashMap<>();
        initialiserImages();
    }

    /**
     * Charge une image à partir de son chemin.
     * Affiche un message dans le terminal si l'image est introuvable.
     */
    public Image creerImage(String chemin) {
        URL urlIm = getClass().getResource(chemin);

        if (urlIm == null) {
            System.out.println("Image non trouvée : " + chemin);
            return null;
        }

        return new Image(urlIm.toString());
    }

    /**
     * Charge une image et l'associe à un nom pour pouvoir la réutiliser.
     */
    public void ajouterImageBoulet(String nom, String chemin) {
        Image image = creerImage(chemin);

        if (image != null) {
            imagesProjectils.put(nom, image);
        }
    }

    /**
     * Initialise les images utilisées par les projectiles.
     */
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

    /**
     * Lance un boulet depuis un canon vers un ennemi.
     */
    public void lancerBoulet(Canon canon, Ennemi cible, Pane pan) {
        lancerProjectil("boulet", canon.getX(), canon.getY(), cible, pan);
    }

    /**
     * Lance une flèche depuis une tour d'archers vers un ennemi.
     */
    public void lancerFleche(TourDarchers tour, Ennemi cible, Pane pan) {
        lancerProjectil("fleche", tour.getX(), tour.getY(), cible, pan);
    }

    /**
     * Crée le projectile, le positionne dans le panneau
     * puis démarre son animation.
     */
    private void lancerProjectil(
            String nomProject,
            double departX,
            double departY,
            Ennemi cible,
            Pane pan
    ) {
        ImageView projectil = creerImageProjectil(nomProject);

        if (projectil == null) {
            return;
        }

        positionnerProjectile(projectil, departX, departY);
        pan.getChildren().add(projectil);

        TranslateTransition transition =
                creerTransitionProjectile(projectil, departX, departY, cible, pan);

        transition.play();
    }

    /**
     * Positionne le projectile à son point de départ.
     */
    private void positionnerProjectile(
            ImageView projectil,
            double departX,
            double departY
    ) {
        projectil.setLayoutX(departX + 10);
        projectil.setLayoutY(departY + 16);
    }

    /**
     * Crée l'image du projectile et définit ses dimensions.
     */
    private ImageView creerImageProjectil(String nom) {
        Image image = imagesProjectils.get(nom);

        if (image == null) {
            return null;
        }

        ImageView projectil = new ImageView(image);
        projectil.setFitWidth(12);
        projectil.setFitHeight(12);

        return projectil;
    }

    /**
     * Crée l'animation qui déplace le projectile vers sa cible.
     * Retire le projectile du panneau lorsque l'animation se termine.
     */
    private TranslateTransition creerTransitionProjectile(
            ImageView projectil,
            double departX,
            double departY,
            Ennemi cible,
            Pane pan
    ) {
        TranslateTransition transition =
                new TranslateTransition(Duration.millis(300), projectil);

        transition.setByX((cible.getX() + 10) - (departX + 16));
        transition.setByY((cible.getY() + 16) - (departY + 16));

        transition.setOnFinished(e -> pan.getChildren().remove(projectil));

        return transition;
    }
}
