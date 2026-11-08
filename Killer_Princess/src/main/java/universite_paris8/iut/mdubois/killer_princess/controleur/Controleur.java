package universite_paris8.iut.mdubois.killer_princess.controleur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import universite_paris8.iut.mdubois.killer_princess.controleur.listeners.ListenerJeu;
import universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu.ModeJeu;
import universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu.ModeSurvie;
import universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu.ModeVagues;
import universite_paris8.iut.mdubois.killer_princess.controleur.spawn.ApparitionParGroupe;
import universite_paris8.iut.mdubois.killer_princess.controleur.spawn.ApparitionUnParUn;
import universite_paris8.iut.mdubois.killer_princess.controleur.spawn.StrategieApparition;
import universite_paris8.iut.mdubois.killer_princess.modele.*;
import universite_paris8.iut.mdubois.killer_princess.vue.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static universite_paris8.iut.mdubois.killer_princess.controleur.ControleurMenu.mapSelectionnee;

public class Controleur implements Initializable {
    private Carte carte;
    private VueCarte vueCarte;
    private VueEnnemis vueEnnemis;
    private VuePiege vuePiege;
    private VueProjectil vueProjectil;
    private IntegerProperty chronometre = new SimpleIntegerProperty();
    private Timeline gameLoop;
    private FabriqueInstance fabriqueInstance, fabriquePiege;
    private Jeu partie;
    private AppelVagues spawner;
    private StrategieApparition strategieApparition;
    private ModeJeu modeJeu;
    private DragAndDrop gnd;
    private int temps, numeroMapActuelle = 1;
    private boolean isPaused = false;
    private ArrayList<Ennemi> fileAttenteSpawn = new ArrayList<>();

    @FXML BorderPane root;
    @FXML private Pane paneEntitee, menuJeu;
    @FXML private StackPane inventaire, ecranFinPartie;
    @FXML private HBox top;
    @FXML private VBox cartePiege1, cartePiege2, cartePiege3, cartePiege4, cartePiege5, cartePiege6;
    @FXML private TilePane panneauTuile;
    @FXML private Label goldDisplay, waveDisplay, chrono;
    @FXML private ImageView BARRICADE_BOIS, TRAPPE, MAGMA, TOUR_ARCHERS, CANON, iconPause, iconMenu, restartGame, backToMenu, returnMenu, nouvelleVague, trois, deux, un, victoire, perte, imagePrincesse, imageDragon;
    @FXML private BoxBlur blur;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initialiseModele();
        initialiseVues();
        initiliseInterface();
        initialiseListener();
        //creationEnnemi = new FabriqueInstance(partie);

        configurerDragAndDrop();
        modeJeu.demarrer();
        changerImagesMap2();
        appliquerHovers();
        configurerAffichage();

        initAnimation();
        gameLoop.play();

        chrono.textProperty().bind(chronometre.asString());
    }


    public void initialiseModele(){
        partie = new Jeu(mapSelectionnee);
        carte = new Carte(mapSelectionnee);
        spawner = new AppelVagues(partie);
        fabriquePiege = new FabriqueInstance(partie);
        fabriqueInstance = fabriquePiege;
        strategieApparition = choisirStrategieApparition();
        modeDeJeu = choisirModeDeJeu();
    }

    // Choix de la façon dont les ennemis apparaissent selon la map (fait une seule fois)
    private StrategieApparition choisirStrategieApparition() {
        if (mapSelectionnee == 1) {
            return new ApparitionParGroupe(partie, spawner);
        }
        return new ApparitionUnParUn(partie, spawner);
    }

    // Choix du mode de jeu (survie ou vagues), fait une seule fois
    private ModeJeu choisirModeDeJeu() {
        if (ControleurMenu.survie) {
            return new ModeSurvie(partie, fabriqueInstance);
        }
        return new ModeVagues(partie, strategieApparition, nouvelleVague, trois, deux, un);
    }

    public void initialiseVues(){
        vueCarte = new VueCarte(panneauTuile, carte);
        vueEnnemis = new VueEnnemis(paneEntitee);
        vuePiege = new VuePiege();
        vueProjectil = new VueProjectil();
    }

    public void initiliseInterface(){
        blur = new BoxBlur();
        gnd = new DragAndDrop(panneauTuile, fabriquePiege, paneEntitee, partie, this);
    }

    public void initialiseListener(){
        new ListenerJeu(partie, vuePiege, vueEnnemis, paneEntitee, gnd).activer();
    }

    private void configurerDragAndDrop() {
        gnd.configurerSourceDrag(BARRICADE_BOIS, TRAPPE, MAGMA, TOUR_ARCHERS, CANON);
        gnd.configurerCibleDrag();
    }

    //changement des images du chateau pour la map2
    private void changerImagesMap2() {
        if(mapSelectionnee == 2){
            imagePrincesse.setTranslateX(400);
            imagePrincesse.setTranslateY(-45);
            imagePrincesse.setFitWidth(66.66);
            imagePrincesse.setFitHeight(110);
            imageDragon.setTranslateX(-50);
            imageDragon.setTranslateY(-25);
            imageDragon.setFitWidth(48);
            imageDragon.setFitHeight(66);
        }
    }

    private void appliquerHovers(){
        // Hover sur les cartes
        VBox[] cartePiege = {cartePiege1, cartePiege2, cartePiege3, cartePiege4, cartePiege5, cartePiege6};
        for(VBox carte : cartePiege) {
            Style.styleCarteHover(carte);
        }
        // Hover menu Bouton
        ImageView[] menuBouton = {iconMenu, iconPause, backToMenu, restartGame, returnMenu};
        for(ImageView bouton : menuBouton){
            Style.styleMenuBouton(bouton);
        }
    }


    private void configurerAffichage(){
        inventaire.setTranslateY(-100); // Déplace l'inventaire de 20 pixels vers le haut

        panneauTuile.setPickOnBounds(true);
        panneauTuile.setMinSize(960, 740); // 30 colonnes x 21 lignes en 32px

        if(paneEntitee != null){
            paneEntitee.setPickOnBounds(false);
        }
        goldDisplay.textProperty().bind( partie.getOr().asString("Or : %d") );
        waveDisplay.textProperty().bind( partie.getVague().asString("Vague : %d /4") );
    }


    @FXML
    public void Dragon() {
        if (!isPaused) {
            partie.declencherBouleDeFeu();
        }
    }

    // Menu du Jeu
    @FXML
    public void menuJeu(){
        Son.bruitageSelection();
        afficherMenuPause(true);
        if(!isPaused)
            switchPause();
    }

    @FXML
    public void returnToGame(){
        Son.bruitageSelection();
        afficherMenuPause(false);
        switchPause();
    }

    private void afficherMenuPause(boolean afficher) {
        if (afficher) {
            blur.setIterations(1);
            panneauTuile.setEffect(blur);
            paneEntitee.setEffect(blur);
            inventaire.setEffect(blur);
            top.setEffect(blur);
        } else {
            blur.setIterations(0);
        }
        menuJeu.setOpacity(afficher ? 1 : 0);
        menuJeu.setMouseTransparent(!afficher);
        top.setMouseTransparent(afficher);
        inventaire.setMouseTransparent(afficher);
    }

    @FXML
    public void backToMenu() throws Exception{
        Son.bruitageSelection();
        chargerScene("viewMenu.fxml");

//        toujours inversion bizarre switchSon
//        if(Menu.switchSon.isSelected()){
//            Son.musiqueDeFond(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Musique/NoceEscape.wav"));
//        }
    }

    @FXML
    public void restartGame() throws Exception{
        Son.bruitageSelection();
        chargerScene("view.fxml");
    }

    // Charge la scène dont on donne le nom du fichier fxml
    private void chargerScene(String fichier) throws Exception {
        FXMLLoader vueFx = new FXMLLoader(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/" + fichier));
        Pane rootJeu = vueFx.load();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(rootJeu));
        stage.show();
    }

    private void initAnimation() {
        gameLoop= new Timeline();
        temps= 0;
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                /// on définit le FPS (nbre de frame par seconde)
                Duration.seconds(0.017),
                /// c'est un eventHandler d'ou le lambda on définit ce qui se passe à chaque frame
                (ev -> mettreAJourFrame())
        );
        gameLoop.getKeyFrames().add(kf);
    }

    // Ce qui se passe à chaque frame
    private void mettreAJourFrame() {
        ///pas de if pour mouvement lisse pas sacadé
        if (this.isPaused) {
            return;
        }
        if (!partie.estPartiePerdue() && !partie.estPartieGagnee()) {
            partie.mettreAJour();
            temps++;
            mettreAJourProjectils();

            modeJeu.mettreAJour();
        } else {
            terminerPartie();
        }
    }

    // Affiche l'écran de fin (victoire ou défaite) et arrête le jeu
    private void terminerPartie() {
        ecranFinPartie.setOpacity(1.0);
        ecranFinPartie.setMouseTransparent(false);

        if (partie.estPartiePerdue()) {
            perte.setOpacity(1);
            affichageFinPartie(perte);
        } else {
            victoire.setOpacity(1);
            affichageFinPartie(victoire);
        }
        finPartie();
    }

    public void affichageFinPartie(ImageView i) {
        menuJeu();
        menuJeu.getChildren().add(i);
    }
    /**
     * ça permet d'avoir les boulet ou fleche d'afficher quand
     * canon et la tour d'achers son mise et attaque
     */
    private void mettreAJourProjectils() {
        for (Piege p : new ArrayList<>(partie.getListePieges())) {
            if (p instanceof Canon canon) {
                Ennemi cible = canon.getCibleActuelle();
                if (cible != null) {
                    vueProjectil.lancerBoulet(canon, cible, paneEntitee);
                }
            }
            if (p instanceof TourDarchers tourArcher) {
                Ennemi cible = tourArcher.getCibleActuelle();
                if (cible != null) {
                    vueProjectil.lancerFleche(tourArcher, cible, paneEntitee);
                }
            }
        }
    }

    @FXML
    public void basculerPause(){
        Son.bruitageSelection();
        switchPause();
    }
    public void finPartie() {
        gameLoop.stop();
        modeJeu.arreter();
    }

    public void switchPause(){
        this.isPaused = !this.isPaused;
        if(this.isPaused){
            gameLoop.pause();
            modeJeu.pause();
        } else {                //sinon (booléen faux) on remet en marche la boucle et le mode de jeu
            gameLoop.play();
            modeJeu.reprendre();
        }
    }

    public boolean isPaused(){
        return this.isPaused;
    }
}