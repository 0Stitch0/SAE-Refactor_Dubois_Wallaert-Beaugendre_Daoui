package universite_paris8.iut.mdubois.killer_princess.controleur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
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
    private Timeline chronoVagues,  actionSpawn,  pauseInterVague;
    private DragAndDrop gnd;
    private int temps, numeroMapActuelle = 1;
    private boolean isPaused = false;
    private boolean defaite = false;
    private boolean modeRapideDeclenche = false;
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
        initiliseTimeline();
        initiliseInterface();
        //creationEnnemi = new FabriqueInstance(partie);

        inventaire.setTranslateY(-100); // Déplace l'inventaire de 20 pixels vers le haut

        gnd.configurerSourceDrag(BARRICADE_BOIS, TRAPPE, MAGMA, TOUR_ARCHERS, CANON);
        gnd.configurerCibleDrag();

        panneauTuile.setPickOnBounds(true);
        panneauTuile.setMinSize(960, 740); // 30 colonnes x 21 lignes en 32px

        if (paneEntitee != null) {
            paneEntitee.setPickOnBounds(false);
        }
        goldDisplay.textProperty().bind(
                partie.getOr().asString("Or : %d")
        );
        waveDisplay.textProperty().bind(
                partie.getVague().asString("Vague : %d /4")
        );
        //Listener des pièges
        partie.getListePieges().addListener((ListChangeListener<Piege>) change -> {
            while (change.next()) {
                //Un piège est ajouté via drag and drop
                if (change.wasAdded()) {
                    for (Piege nouveauPiege : change.getAddedSubList()) {
                        vuePiege.afficherPiegeSurMap(nouveauPiege, nouveauPiege.getNom(), paneEntitee, gnd.getGestionnaireClic());
                    }
                }
                //Un piège est vendu ou détruit pas ennemis
                if (change.wasRemoved()) {
                    for (Piege piegeSupprime : change.getRemoved()) {
                        vuePiege.supprimerPiegeDeLaMap(piegeSupprime, paneEntitee);
                    }
                }
            }
        });
        //Listener des Ennemis Vie
        partie.getListeEnnemis().addListener((ListChangeListener<Ennemi>) change -> {
            while (change.next()) {
                //Un ennemi apparait pendant les vagues
                if (change.wasAdded()) {
                    for (Ennemi nouvelEnnemi : change.getAddedSubList()) {
                        vueEnnemis.creerVuePourEnnemi(nouvelEnnemi);
                        // Change image si attaque ou run
                        nouvelEnnemi.attaqueProperty().addListener((obs, oldVal, newVal) -> {
                            if(newVal){
                                vueEnnemis.imageEnnemieEnAttaque(nouvelEnnemi);
                            }
                            else{
                                vueEnnemis.imageEnnemieEnRun(nouvelEnnemi);
                            }
                        });
                    }
                }
                //Un ennemi meurt
                if (change.wasRemoved()) {
                    for (Ennemi ennemiMort : change.getRemoved()) {
                        vueEnnemis.supprimerEnnemiDeLaMap(ennemiMort, paneEntitee);
                    }
                }
            }
        });

        // mode normal avec des vagues
        if (!ControleurMenu.survie){
            this.spawner = new AppelVagues(partie);
            if(partie.getGestionnaireVagues().getVagueActuelle().getNum()<partie.getGestionnaireVagues().getNbTotalVagues()) {
                SpawnVagues();
            }
        }
        // survie mode
        else{
            vagueInfini();
        }

        //changement des images du chateau pour la map2
        if (mapSelectionnee == 2) {
            imagePrincesse.setTranslateX(400);
            imagePrincesse.setTranslateY(-45);
            imagePrincesse.setFitWidth(66.66);
            imagePrincesse.setFitHeight(110);
            imageDragon.setTranslateX(-50);
            imageDragon.setTranslateY(-25);
            imageDragon.setFitWidth(48);
            imageDragon.setFitHeight(66);
        }

        // Hover sur les cartes
        VBox[] cartePiege = {cartePiege1, cartePiege2, cartePiege3, cartePiege4, cartePiege5, cartePiege6};
        for (VBox carte : cartePiege)
            Style.styleCarteHover(carte);

        // Hover menu Bouton
        ImageView[] menuBouton = {iconMenu, iconPause, backToMenu, restartGame, returnMenu};
        for (ImageView bouton : menuBouton){
            Style.styleMenuBouton(bouton);
        }

        initAnimation();
        gameLoop.play();

        chrono.textProperty().bind(chronometre.asString());
    }

    public void initialiseModele(){
        partie = new Jeu(mapSelectionnee);
        carte = new Carte(mapSelectionnee);
        spawner = new AppelVagues(partie);
        fabriquePiege = new FabriqueInstance(partie);
    }

    public void initialiseVues(){
        vueCarte = new VueCarte(panneauTuile, carte);
        vueEnnemis = new VueEnnemis(paneEntitee);
        vuePiege = new VuePiege();
        vueProjectil = new VueProjectil();
    }

    public void initiliseTimeline(){
        actionSpawn = new Timeline();
        pauseInterVague = new Timeline();
    }

    public void initiliseInterface(){
        blur = new BoxBlur();
        gnd = new DragAndDrop(panneauTuile, fabriquePiege, paneEntitee, partie, this);
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
        blur.setIterations(1);
        panneauTuile.setEffect(blur);
        paneEntitee.setEffect(blur);
        inventaire.setEffect(blur);
        top.setEffect(blur);
        menuJeu.setOpacity(1);
        menuJeu.setMouseTransparent(false);
        top.setMouseTransparent(true);
        inventaire.setMouseTransparent(true);
        if(!isPaused)
            switchPause();
    }


    @FXML
    public void backToMenu() throws Exception{
        Son.bruitageSelection();
        FXMLLoader vueFx = new FXMLLoader(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/viewMenu.fxml"));
        Pane rootJeu = vueFx.load();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(rootJeu));
        stage.show();

        //toujours inversion bizarre switchSon
//        if(Menu.switchSon.isSelected()){
//            Son.musiqueDeFond(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/Musique/NoceEscape.wav"));
//        }
    }

    @FXML
    public void restartGame() throws Exception{
        Son.bruitageSelection();
        FXMLLoader vueFx = new FXMLLoader(getClass().getResource("/universite_paris8/iut/mdubois/killer_princess/view.fxml"));
        Pane rootJeu = vueFx.load();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(rootJeu));
        stage.show();
    }

    @FXML
    public void returnToGame(){
        Son.bruitageSelection();
        blur.setIterations(0);
        menuJeu.setOpacity(0);
        menuJeu.setMouseTransparent(true);
        top.setMouseTransparent(false);
        inventaire.setMouseTransparent(false);
        switchPause();
    }

    private void initAnimation() {
        gameLoop= new Timeline();
        temps= 0;
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                /// on définit le FPS (nbre de frame par seconde)
                Duration.seconds(0.017),
                /// c'est un eventHandler d'ou le lambda on définit ce qui se passe à chaque frame
                (ev -> {///pas de if pour mouvement lisse pas sacadé
                    if (this.isPaused) {
                        return;
                    }
                    if (!partie.estPartiePerdue() && !partie.estPartieGagnee()) {
                        partie.mettreAJour();
                        temps++;
                        mettreAJourProjectils();

                        if(!ControleurMenu.survie) {
                            skipTimerVagues();
                        }
                    } else {
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
                })
        );
        gameLoop.getKeyFrames().add(kf);
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
        chronoVagues.stop();
        actionSpawn.stop();
        pauseInterVague.stop();
    }

    public void switchPause(){
        this.isPaused = !this.isPaused;
        if(this.isPaused){
            gameLoop.pause();
            if (chronoVagues != null) chronoVagues.pause();
            if (actionSpawn != null) actionSpawn.pause();
            if (pauseInterVague != null) pauseInterVague.pause();
        } else {                //sinon (booléen faux) on remet en marche toutes les TimeLine initialisée dans le Controleur
            gameLoop.play();
            if (chronoVagues != null && chronoVagues.getStatus() == Timeline.Status.PAUSED) {
                chronoVagues.play();
            }
            if (actionSpawn != null && actionSpawn.getStatus() == Timeline.Status.PAUSED) {
                actionSpawn.play();
            }
            if (pauseInterVague != null && pauseInterVague.getStatus() == Timeline.Status.PAUSED) {
                pauseInterVague.play();
            }
        }
    }


    public boolean isPaused(){
        return this.isPaused;
    }

    // Mode survie Vague infini
    public void vagueInfini(){
        System.out.println("La vague infini commence");
        chronoVagues = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            if(!defaite){
                int aleatoireEnnemi;
                int aleatoirePositionY;
                aleatoireEnnemi = (int) (Math.random() * 100) + 1;
                aleatoirePositionY = (int) (Math.random() * 19) + 1;
                String ennemiNom = fabriqueInstance.retournerStringEnnemi(aleatoireEnnemi);
                // 31 est mieux que 32 car ennemie a moitié sur la map
                Ennemi ennemi = fabriqueInstance.creerInstanceEnnemi(ennemiNom, 29 * 31, aleatoirePositionY * 31);
                partie.ajouterEnnemi(ennemi);
            }
        }));
        chronoVagues.setCycleCount(Timeline.INDEFINITE);
        chronoVagues.play();
    }

    //Lance le chronomètre de spawn de la vague actuelle
    public void SpawnVagues() {
        if (chronoVagues == null || chronoVagues.getStatus() != Timeline.Status.RUNNING) {
            System.out.println("La vague commence !");

            Duration cadence;
            int cycles;

            if (ControleurMenu.mapSelectionnee == 1) {
                //Map 1 : spawn par GROUPES toutes les 3 secondes
                cadence = Duration.seconds(3);
                cycles = partie.getGestionnaireVagues().getVagueActuelle().getNombreDeGroupes(); //nombre de groupes dans la vague actuelle
            } else {
                //Map 2 : spawn ennemi par ennemi toutes les secondes
                cadence = Duration.seconds(1);
                cycles = Timeline.INDEFINITE;       //INDEFINITE car c'est la fileAttenteSpawn qui gère l'arrêt
            }

            //À chaque tic de la cadence, on appelle gererApparitionEnnemi
            KeyFrame actionSpawn = new KeyFrame(cadence, event -> gererApparitionEnnemi());

            //Initialisation de chronovagues
            chronoVagues = new Timeline(actionSpawn);
            chronoVagues.setCycleCount(cycles);
            chronoVagues.play();
        }
    }

    private void gererApparitionEnnemi() {
        boolean vagueTermineeEtFileVide;

        if (ControleurMenu.mapSelectionnee == 1) {      //MAP 1 : Spawn du groupe entier d'un coup
            this.spawner.spawnUnGroupe();
            vagueTermineeEtFileVide = partie.getGestionnaireVagues().vagueTerminee();
        }
        else {                  //MAP 2 : Spawn un ennemi à la fois
            //Si la file contient déjà des ennemis, on en sort un seul pour ce tic
            if (!fileAttenteSpawn.isEmpty()) {
                partie.ajouterEnnemi(fileAttenteSpawn.remove(0));
            }
            //Si la file est vide mais qu'il reste des groupes, on la remplit
            else if (!partie.getGestionnaireVagues().vagueTerminee()) {
                int ennemisAvant = partie.getListeEnnemis().size();
                this.spawner.spawnUnGroupe();

                //On transfère le groupe de la liste du jeu vers notre file d'attente
                while (partie.getListeEnnemis().size() > ennemisAvant) {
                    Ennemi ennemi = partie.getListeEnnemis().remove(partie.getListeEnnemis().size() - 1);
                    fileAttenteSpawn.add(0, ennemi);
                }

                //On fait spawn le tout premier de la file immédiatement pour ne pas sauter ce tick
                if (!fileAttenteSpawn.isEmpty()) {
                    partie.ajouterEnnemi(fileAttenteSpawn.remove(0));
                }
            }
            //On met à jour l'état de fin de vague pour la Map 2 (vague terminée ET file d'attente vide)
            vagueTermineeEtFileVide = partie.getGestionnaireVagues().vagueTerminee() && fileAttenteSpawn.isEmpty();
        }

        //Gestion unique de fin de vague pour les 2 maps (Pour les deux maps)
        if (vagueTermineeEtFileVide) {
            if (chronoVagues != null) {
                chronoVagues.stop();
            }

            //S'il reste des vagues et qu'il y a des ennemis en vie -> Pause de 30s
            if (partie.getVague().get() < partie.getGestionnaireVagues().getNbTotalVagues() && !partie.getListeEnnemis().isEmpty()) {
                if (pauseInterVague != null) pauseInterVague.stop(); // évite les conflits
                declencherPauseInterVague(26, 27, 28, 29, 30);
            }
        }
    }
    private void declencherPauseInterVague(double message, double three, double two, double one, double vague) {
        if (partie.getGestionnaireVagues().vagueTerminee() && partie.getVague().get() < partie.getGestionnaireVagues().getNbTotalVagues()) {
            //Timers pour l'affichage du décompte juste avant la vague
            this.pauseInterVague = new Timeline(
                    new KeyFrame(Duration.seconds(message), event -> {
                        nouvelleVague.setOpacity(1);
                    }),
                    new KeyFrame(Duration.seconds(three), event -> {
                        nouvelleVague.setOpacity(0);
                        trois.setOpacity(1);
                    }),
                    new KeyFrame(Duration.seconds(two), event -> {
                        trois.setOpacity(0);
                        deux.setOpacity(1);
                    }),
                    new KeyFrame(Duration.seconds(one), event -> {
                        deux.setOpacity(0);
                        un.setOpacity(1);
                    }),
                    new KeyFrame(Duration.seconds(vague), event -> {
                        un.setOpacity(0);
                        this.modeRapideDeclenche = false;       //rechanche le boolean pour la prochaine vague

                        partie.passerAVagueSuivante();      //change la vague de la classe Jeu
                        SpawnVagues();      //rappel la méthode pour initialiser une TimeLine avec la taille du nombre de groupes de la vague suivante
                    })
            );
            pauseInterVague.setCycleCount(1);   //programme le timer de pause entre vagues pour se lancer qu'une seule fois
            pauseInterVague.play();         //lance le timer
        }
    }

    private void skipTimerVagues() {
        if (partie.getGestionnaireVagues().vagueTerminee() && partie.getListeEnnemis().isEmpty()) {
            if (!modeRapideDeclenche && partie.getVague().get() < partie.getGestionnaireVagues().getNbTotalVagues()) {
                System.out.println("Tous les ennemis sont morts ! Passage en mode rapide.");
                modeRapideDeclenche = true;

                // Stoppe la pause longue si elle tourne
                if (pauseInterVague != null) {
                    pauseInterVague.stop();
                }
                declencherPauseInterVague(1, 2, 3, 3.5, 4);
            }
        }
    }
}