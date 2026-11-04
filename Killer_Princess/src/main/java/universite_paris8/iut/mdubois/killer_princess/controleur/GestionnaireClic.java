package universite_paris8.iut.mdubois.killer_princess.controleur;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;
import universite_paris8.iut.mdubois.killer_princess.modele.Piege;
import universite_paris8.iut.mdubois.killer_princess.vue.VuePiege;

public class GestionnaireClic {
    private Jeu partie;
    private Controleur controleur;
    private Pane paneEntitee;
    private HBox menuFlottant; //menu contextuel affiché au clic sur un piège (améliorer/vendre)
    private static String decoGenerale = "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;";

    public GestionnaireClic(Jeu partie, Controleur c, Pane paneEntitee) {
        this.partie = partie;
        this.controleur = c;
        this.paneEntitee = paneEntitee;

        //Initialisation du menu flottant qui contiendra les boutons améliorer/vendre
        this.menuFlottant = new HBox(5);
        this.menuFlottant.setStyle("-fx-padding: 2; -fx-background-radius: 5;");
        this.menuFlottant.setVisible(false);
        this.paneEntitee.getChildren().add(menuFlottant);       //Ajout au pane pour qu'il soit toujours présent, juste caché
    }

    //Attache un handler de clic sur l'ImageView d'un piège posé sur la map
    //caseLigne et caseColonne sont en pixels pour positionner le menu flottant
    public void configurerClic(ImageView imageSurMap, int caseLigne, int caseColonne, String type, Piege p) {
        imageSurMap.setOnMouseClicked(clickEvent -> {
            if (controleur.isPaused()) {
                clickEvent.consume();   //bloque le clic si le jeu est en pause
            }
            else {
                //Vérifie si le menu est déjà ouvert sur CE piège précis en regardant la position (x) du piège et de la bulle
                boolean bulleEstSurCePiege = this.menuFlottant.isVisible() && this.menuFlottant.getLayoutX() == (caseColonne - 60);

                if (!bulleEstSurCePiege) {
                    partie.selectionnerPiege(p); //selectionne ce piège dans la classe Jeu

                    //Positionne le menu juste au-dessus du piège cliqué
                    this.menuFlottant.setLayoutX(caseColonne - 60);
                    this.menuFlottant.setLayoutY(caseLigne - 35);
                    this.menuFlottant.getChildren().clear();    //vide les anciens boutons

                    //Crée les 2 différents boutons
                    Button btnAmeliorer = creerBoutonAmeliorer(p);
                    Button btnVendre = creerBoutonVendre(p);

                    this.menuFlottant.getChildren().addAll(btnAmeliorer, btnVendre);
                    this.menuFlottant.setVisible(true);
                    this.menuFlottant.toFront(); //s'assure que le menu passe devant les ennemis/pièges
                } else {
                    //Deuxième clic sur le même piège = ferme le menu
                    this.menuFlottant.setVisible(false);
                }
                clickEvent.consume(); //empêche le clic de remonter au panneauTuile et fermer le menu
            }
        });
    }

    //Crée le bouton d'amélioration avec 3 états possibles selon la situation
    private Button creerBoutonAmeliorer(Piege p) {
        Button btn = new Button();
        String texte;
        String couleur;

        if (p.getNiveau() >= p.getNivMax()) {
            //Piège déjà au niveau maximum : bouton désactivé visuellement
            texte = "Niveau Max !";
            couleur = "-fx-background-color: green;";
        }
        else if (p.getCout() > this.partie.getOr().get()) {
            //Or insuffisant : bouton désactivé visuellement
            texte = "Plus d'or !";
            couleur = "-fx-background-color: darkgrey;";
        }
        else {
            //Amélioration possible : bouton actif avec le coût affiché
            texte = "Update : " + p.getCout();
            couleur = "-fx-background-color: #2980b9;";

            btn.setOnAction(e -> {
                if (controleur.isPaused()) {
                    e.consume();            //bloque le clic si le jeu est en pause
                }
                else {
                    partie.selectionnerPiege(p);
                    partie.AmeliorerPiege();        //améliore le piège dans le modèle

                    this.menuFlottant.setVisible(false);
                }
            });
        }
        //applique le visuel au bouton
        btn.setText(texte);
        btn.setStyle(couleur + decoGenerale);
        btn.setVisible(true);
        return btn;
    }

    //Crée le bouton de vente du piège
    //La suppression visuelle est gérée par le ListChangeListener dans Controleur
    private Button creerBoutonVendre(Piege p) {
        Button btn = new Button("Sell : " + p.prixVente());
        btn.setStyle("-fx-background-color: #c0392b;" + decoGenerale);

        btn.setOnAction(e -> {
            e.consume();    //évite le bug du double-clic
            if (!controleur.isPaused()) {
                this.partie.VendrePiege();      //retire le piège du modèle et déclenche le ListChangeListener
                this.menuFlottant.setVisible(false);
            }
        });
        return btn;
    }

    public HBox getMenuFlottant() {
        return menuFlottant;
    }
}