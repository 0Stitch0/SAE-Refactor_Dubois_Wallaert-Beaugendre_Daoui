package universite_paris8.iut.mdubois.killer_princess.controleur;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import universite_paris8.iut.mdubois.killer_princess.modele.Son;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControleurMenu implements Initializable {
    @FXML private Pane root;
    @FXML private Pane menu;
    @FXML private Pane choixMapMenu;
    @FXML private Pane settingsMenu;
    @FXML private ImageView start;
    @FXML private ImageView survieMode;
    @FXML private ImageView settings;
    @FXML private ImageView quit;
    @FXML private ImageView returnOption;
    @FXML private ToggleSwitch switchSon;
    @FXML private Slider sliderMusique;
    @FXML private Slider sliderBruitage;
    @FXML private ImageView miniatureMap1;
    @FXML private ImageView miniatureMap2;
    public static boolean survie = false;
    public static int mapSelectionnee = 1;


    public void initialize(URL url, ResourceBundle resourceBundle){

        // Régler le volume de la musique de fond
        sliderMusique.valueProperty().addListener((obs, oldVal, newVal) -> {
            Son.getInstance().volumeMusique(newVal.floatValue());
        });

        // Régler le volume des bruitages
        sliderBruitage.valueProperty().addListener((obs, oldVal, newVal) -> {
            Son.getInstance().volumeBruitage(newVal.floatValue());
        });

        sliderMusique.setValue(-30);
        sliderBruitage.setValue(-25);
        Son.getInstance().musiqueDeFond(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Musique/NoceEscape.wav"));

        // Hover sur les cartes  + bruitage
        ImageView[] menuBouton = {start, survieMode, settings, quit, returnOption};
        for (ImageView bouton : menuBouton)
            Style.styleMenuBouton(bouton);
        Style.styleMenuBouton(miniatureMap1);
        Style.styleMenuBouton(miniatureMap2);

        // Mettre On ou Off le son
        switchSon.setSelected(true);
        // Inversion bizarre de newVal et de oldVal
        switchSon.selectedProperty().addListener((obs,oldVal, newVal) -> {
            if(newVal){
                Son.getInstance().setSonOn(true);
                Son.getInstance().musiqueDeFond(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Musique/NoceEscape.wav"));
            }
            else{
                Son.getInstance().setSonOn(false);
                Son.getInstance().couperSon();
            }
        });

    }

    // Fonction Start
    @FXML
    public void startGame() {
        Son.getInstance().bruitageSelection();

        //On masque le menu principal
        menu.setOpacity(0);
        menu.setMouseTransparent(true);

        //On affiche le panneau de sélection déjà créé par la Vue FXML
        choixMapMenu.setOpacity(1);
        choixMapMenu.setMouseTransparent(false);
    }

    @FXML
    public void clicMap1() throws IOException {
        mapSelectionnee = 1; // Mise à jour du Modèle
        lancerLeJeu();       // Changement de Vue
    }

    @FXML
    public void clicMap2() throws IOException {
        mapSelectionnee = 2; // Mise à jour du Modèle
        lancerLeJeu();       // Changement de Vue
    }

    private void lancerLeJeu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/view.fxml"));
        Pane rootJeu = loader.load();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(rootJeu));
        stage.show();

        // toujours inversion bizarre switchSon
        if (switchSon.isSelected()) {
            Son.getInstance().musiqueDeFond(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Musique/BrumeDeLoup.wav"));
        }
    }

    // Autre mode de jeu proposer : vague infini
    @FXML
    public void survieMode() throws IOException {
        Son.getInstance().bruitageSelection();
        survie = true;
        FXMLLoader vueFx = new FXMLLoader(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/view.fxml"));
        Pane rootJeu = vueFx.load();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(rootJeu));
        stage.show();

        // toujours inversion bizarre switchSon
        if(switchSon.isSelected()){
            Son.getInstance().musiqueDeFond(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Musique/BrumeDeLoup.wav"));
        }
    }

    // Parametres
    @FXML
    public void settings(){
        Son.getInstance().bruitageSelection();
        menu.setMouseTransparent(true);
        settingsMenu.setOpacity(1);
        settingsMenu.setMouseTransparent(false);
    }

    @FXML
    public void returnOption(){
        settingsMenu.setOpacity(0);
        settingsMenu.setMouseTransparent(true);
        menu.setMouseTransparent(false);
    }

    // Arrêt du jeu
    @FXML
    public void quitJeu(){
        Son.getInstance().bruitageSelection();
        Platform.exit();
    }

}