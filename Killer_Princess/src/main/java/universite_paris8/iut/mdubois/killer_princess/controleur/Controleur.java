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
import universite_paris8.iut.mdubois.killer_princess.controleur.chrono.GestionnairePause;
import universite_paris8.iut.mdubois.killer_princess.controleur.listeners.ListenerJeu;
import universite_paris8.iut.mdubois.killer_princess.controleur.modesJeu.*;
import universite_paris8.iut.mdubois.killer_princess.controleur.spawn.*;
import universite_paris8.iut.mdubois.killer_princess.modele.*;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;
import universite_paris8.iut.mdubois.killer_princess.modele.pieges.Piege;
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
    private GestionnairePause gestionnairePause;

    @FXML BorderPane root;
    @FXML private Pane paneEntitee, menuJeu;
    @FXML private StackPane inventaire, ecranFinPartie;
    @FXML private HBox top;
    @FXML private VBox cartePiege2, cartePiege3, cartePiege4, cartePiege5, cartePiege6;
    @FXML private TilePane panneauTuile;
    @FXML private Label goldDisplay, waveDisplay, chrono;
    @FXML private ImageView BARRICADE_BOIS, TRAPPE, MAGMA, TOUR_ARCHERS, CANON, iconPause, iconMenu, restartGame, backToMenu, returnMenu, nouvelleVague, trois, deux, un, victoire, perte, imagePrincesse, imageDragon;
    @FXML private BoxBlur blur;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        initialiseModele();
        initialiseVues();
        initiliseInterface();
        initialiseListener();

        configurerDragAndDrop();
        modeJeu.demarrer();
        changerImagesCarte2();
        appliquerHovers();
        configurerAffichage();

        initAnimation();
        gameLoop.play();

        chrono.textProperty().bind(chronometre.asString());
    }

// fais les initialisations lors de l'appel de la classe
    public void initialiseModele(){
        partie = new Jeu(mapSelectionnee);
        carte = new Carte(mapSelectionnee);
        spawner = new AppelVagues(partie);
        fabriquePiege = new FabriqueInstance(partie);
        fabriqueInstance = fabriquePiege;
        strategieApparition = choisirStrategieApparition();
        modeJeu = choisirModeDeJeu();
        gestionnairePause = new GestionnairePause(gameLoop, modeJeu);
    }

//choix en fonction de la carte pour l'apparition (spwan) des ennemis
    private StrategieApparition choisirStrategieApparition(){
        if (mapSelectionnee == 1){
            return new ApparitionParGroupe(partie, spawner);
        }
        return new ApparitionUnParUn(partie, spawner);
    }

//choix du mode de jeu (survie ou vagues)
    private ModeJeu choisirModeDeJeu(){
        if (ControleurMenu.survie){
            return new ModeSurvie(partie, fabriqueInstance);
        }
        return new ModeVagues(partie, strategieApparition, nouvelleVague, trois, deux, un);
    }

// initialise les vues
    public void initialiseVues(){
        vueCarte = new VueCarte(panneauTuile, carte);
        vueEnnemis = new VueEnnemis(paneEntitee);
        spawner = new AppelVagues(partie);
        vuePiege = new VuePiege();
        vueProjectil = new VueProjectil();
    }

//initialise l'inventaire/ affichage de base quand on joue
    public void initiliseInterface(){
        blur = new BoxBlur();
        inventaire.setTranslateY(-100); //déplace l'inventaire de 20 pixels vers le haut
        gnd = new DragAndDrop(panneauTuile, fabriquePiege, paneEntitee, partie, this);
    }

//inutilisée les listeneur (ennemis, pièges)
    public void initialiseListener(){
        new ListenerJeu(partie, vuePiege, vueEnnemis, paneEntitee, gnd).activer();
    }

//configure le drag and drop avec les éléments qui peuvent être drag and drop
    private void configurerDragAndDrop(){
        gnd.configurerSourceDrag(BARRICADE_BOIS, TRAPPE, MAGMA, TOUR_ARCHERS, CANON);
        gnd.configurerCibleDrag();
    }

//changement des images du château pour la carte 2 (là où il est + taille)
    private void changerImagesCarte2(){
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

//met un effet de grossissement quand la souris passe sur les cartes de l'inventaire ou des boutons pour les paramétrer
    private void appliquerHovers(){
        // Hover sur les cartes
        VBox[] cartePiege = { cartePiege2, cartePiege3, cartePiege4, cartePiege5, cartePiege6};
        for(VBox carte : cartePiege){
            Style.styleCarteHover(carte);
        }
        // Hover menu Bouton
        ImageView[] menuBouton = {iconMenu, iconPause, backToMenu, restartGame, returnMenu};
        for(ImageView bouton : menuBouton){
            Style.styleMenuBouton(bouton);
        }
    }

//mets en place l'inventaire en bas et la barre en haut dans la fenêtre de jeu
    private void configurerAffichage(){
        inventaire.setTranslateY(-100); // Déplace l'inventaire de 20 pixels vers le haut

        panneauTuile.setPickOnBounds(true);
        panneauTuile.setMinSize(960, 740); // 30 colonnes x 21 lignes en 32px

        if(paneEntitee != null){
            paneEntitee.setPickOnBounds(false);
        }
        goldDisplay.textProperty().bind( partie.getOr().asString("Or : %d") );
        int nbTotalVagues = partie.getGestionnaireVagues().getNbTotalVagues();
        waveDisplay.textProperty().bind( partie.getVague().asString("Vague : %d / " + nbTotalVagues) );
    }

//pour l'attaque du dragon
    @FXML
    public void Dragon(){
        if (!gestionnairePause.isPaused()){
            partie.declencherBouleDeFeu();
        }
    }

//menu du Jeu
    @FXML
    public void menuJeu(){
        Son.getInstance().bruitageSelection();
        afficherMenuPause(true);
        if(!gestionnairePause.isPaused())
            gestionnairePause.basculer();
    }

//ramène au menu principal (menu d'arrivée)
    @FXML
    public void backToMenu() throws Exception{
        Son.getInstance().bruitageSelection();
        chargerScene("viewMenu.fxml");

//        toujours inversion bizarre switchSon
//        if(Menu.switchSon.isSelected()){
//            Son.musiqueDeFond(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Musique/NoceEscape.wav"));
//        }
    }

//relance la partie
    @FXML
    public void restartGame() throws Exception{
        Son.getInstance().bruitageSelection();
        chargerScene("view.fxml");
    }

//reprends la partie là où on en était
    @FXML
    public void returnToGame(){
        Son.getInstance().bruitageSelection();
        afficherMenuPause(false);
        gestionnairePause.basculer();
    }

//pour l'affichage du menu pause, qui floute la carte et affiche les options si on veut quitter ou revenir à notre partie
    private void afficherMenuPause(boolean afficher){
        if (afficher){
            blur.setIterations(1);
            panneauTuile.setEffect(blur);
            paneEntitee.setEffect(blur);
            inventaire.setEffect(blur);
            top.setEffect(blur);
        } else{
            blur.setIterations(0);
        }
        menuJeu.setOpacity(afficher ? 1 : 0);
        menuJeu.setMouseTransparent(!afficher);
        top.setMouseTransparent(afficher);
        inventaire.setMouseTransparent(afficher);
    }

//charge la scène dont on donne le nom du fichier fxml
    private void chargerScene(String fichier) throws Exception{
        FXMLLoader vueFx = new FXMLLoader(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/" + fichier));
        Pane rootJeu = vueFx.load();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(rootJeu));
        stage.show();
    }

    private void initAnimation(){
        gameLoop= new Timeline();
        temps= 0;
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
            //définit le FPS (nbre de frames par seconde)
                Duration.seconds(0.017),
            //un eventHandler, d'où le lambda, on définit ce qui se passe à chaque frame
                (ev -> mettreAJourFrame())
        );
        gameLoop.getKeyFrames().add(kf);
    }

//ce qui se passe à chaque frame
    private void mettreAJourFrame(){
    //pas da if pour mouvement lisse, pas sacadé
        if (gestionnairePause.isPaused()){
            return;
        }
        if (!partie.estPartiePerdue() && !partie.estPartieGagnee()){
            partie.mettreAJour();
            temps++;
            mettreAJourProjectils();

            modeJeu.mettreAJour();
        } else{
            terminerPartie();
        }
    }

//affiche l'écran de fin (victoire ou défaite) et arrête le jeu
    private void terminerPartie(){
        ecranFinPartie.setOpacity(1.0);
        ecranFinPartie.setMouseTransparent(false);

        if (partie.estPartiePerdue()){
            perte.setOpacity(1);
            affichageFinPartie(perte);
        } else{
            victoire.setOpacity(1);
            affichageFinPartie(victoire);
        }
        finPartie();
    }

    public void affichageFinPartie(ImageView i){
        menuJeu();
        menuJeu.getChildren().add(i);
    }

// permet d'avoir les boulets ou flèches d'afficher quand le canon et la tour d'archers attaque un ennemi
    private void mettreAJourProjectils(){
        for (Piege p : new ArrayList<>(partie.getListePieges())){
            Ennemi cible = p.getCibleActuelle();
            if (cible != null){
                vueProjectil.lancerProjectil(p, cible, paneEntitee);
            }
        }
    }

//mets en pause le jeu et mets le son pour la sélection
    @FXML
    public void basculerPause(){
        Son.getInstance().bruitageSelection();
        gestionnairePause.basculer();
    }

//arrête le jeu
    public void finPartie(){
        gestionnairePause.arreter();
    }

//mets la partie en pause
    public boolean isPaused(){
        return gestionnairePause.isPaused();
    }
}