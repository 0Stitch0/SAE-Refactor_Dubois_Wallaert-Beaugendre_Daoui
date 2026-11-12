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

public class VueProjectil{

    private HashMap<String, Image> imagesProjectils;

    public VueProjectil(){
        imagesProjectils = new HashMap<>();
        initialiserImages();
    }

    /** permet de s'assurer que l'image est bien trouvée via le print qui nous indique si elle n'est pas trouvée
    * prend en parametre le chemin pour recuperer l'image puis nous l'affiche ou nous met dans le terminal le print
     * */
    public Image creerImage(String chemin){
        URL urlIm = getClass().getResource(chemin);
        if(urlIm == null){
            System.out.println("Image non trouvée: " + chemin);
            return null;
        }
        return new Image(urlIm.toString());
    }

    /**
     * Viens ajouter le chemin et une id d'image pour pouvoir garder une seule vue pour Canon et Tour d'Archer
     */
    public void ajouterImageBoulet(String nom, String chemin){
        Image image = creerImage(chemin);
        if(image != null){
            imagesProjectils.put(nom, image);
        }
    }

    /**
     * donne un id au image pour faciliter leurs utilisation
     */
    private void initialiserImages(){
        ajouterImageBoulet("boulet", "/universite_paris8/iut/mdubois/killer_princess/Piege/CanonBalle.png");
        ajouterImageBoulet("fleche", "/universite_paris8/iut/mdubois/killer_princess/Piege/Fleche.png");
    }

    /**
     * 2 methodes pour eviter la repetition de code de lancerProjectil
     */
    public void lancerBoulet(Canon canon, Ennemi cible, Pane pan) {
        lancerProjectil("boulet", canon.getX(), canon.getY(), cible, pan);
    }

    public void lancerFleche(TourDarchers tour, Ennemi cible, Pane pan) {
        lancerProjectil("fleche", tour.getX(), tour.getY(), cible, pan);
    }

    /**
     * une methode pour le point de depart du projectile l'ajoute au pane
     * pour le voir et appel la methode pourson deplacement
     */
    private void lancerProjectil(String nomProject, double departX, double departY, Ennemi cible, Pane pan) {
        ImageView projectil = creerImageProjectil(nomProject);
        if (projectil == null) return;

        projectil.setLayoutX(departX + 10);
        projectil.setLayoutY(departY + 16);
        pan.getChildren().add(projectil);

        TranslateTransition transition = faitDeplacementTourEnnemi(projectil, departX, departY, cible, pan);
        transition.play();
    }

    /**
     * cree l'image des projectiles avec leurs tailles
     */
    private ImageView creerImageProjectil(String nom){
        Image image = imagesProjectils.get(nom);
        if(image == null) {
            return null;
        }
        ImageView boulet = new ImageView(image);
        boulet.setFitWidth(12);
        boulet.setFitHeight(12);
        return boulet;
    }

    /**
     * fais le deplacement du projectile entre son point de depart qui et l'image du canon ou de la tour d'archer
     * jusqu'a l'ennemmis le plus proche en ligne droite
     */
    private TranslateTransition faitDeplacementTourEnnemi(ImageView projectil, double departX, double departY, Ennemi cible, Pane pan) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), projectil);
        transition.setByX((cible.getX() + 10) - (departX + 16));
        transition.setByY((cible.getY() + 16) - (departY + 16));
        transition.setOnFinished(e -> pan.getChildren().remove(projectil));
        return transition;
    }
}