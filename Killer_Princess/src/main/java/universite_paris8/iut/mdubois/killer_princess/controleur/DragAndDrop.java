package universite_paris8.iut.mdubois.killer_princess.controleur;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.image.Image;
import universite_paris8.iut.mdubois.killer_princess.modele.FabriqueInstance;
import universite_paris8.iut.mdubois.killer_princess.modele.Jeu;
import universite_paris8.iut.mdubois.killer_princess.modele.pieges.TypePiege;

import static universite_paris8.iut.mdubois.killer_princess.modele.Carte.tailleTuile;

public class DragAndDrop {
    private TilePane panneauTuile;
    private FabriqueInstance fabriquePiege;
    private Pane pan;
    private Jeu partie;
    private Controleur controleur;
    private GestionnaireClic gestionnaireClic;

    public DragAndDrop(TilePane panneauTuile, FabriqueInstance fabriquePiege, Pane pan, Jeu partie, Controleur c) {
        this.panneauTuile = panneauTuile;
        this.fabriquePiege = fabriquePiege;
        this.pan = pan;
        this.partie = partie;
        this.controleur = c;

        //Crée le gestionnaire de clic qui gère amélioration/vente des pièges
        this.gestionnaireClic = new GestionnaireClic(partie, controleur, pan);

        //Ferme le menu flottant si on clique ailleurs que sur un piège (sur la map)
        this.panneauTuile.setOnMouseClicked(e -> {
            if (this.gestionnaireClic.getMenuFlottant() != null) {
                this.gestionnaireClic.getMenuFlottant().setVisible(false);
            }
        });
    }

    //Retourne le gestionnaire de clic pour que le Controleur puisse le brancher aux listeners
    public GestionnaireClic getGestionnaireClic() {
        return gestionnaireClic;
    }

    //Configure le drag depuis les images de l'inventaire de pièges et bloque si jeu en pause ou or insuffisant
    public void configurerSourceDrag(ImageView... boutonsBoutique) {
        for (ImageView bouton : boutonsBoutique) {
            bouton.setOnDragDetected(event -> {
                if (!controleur.isPaused()) {

                    ImageView boutonClique = (ImageView) event.getSource();     //trouve sur quel bouton on a "clique"
                    TypePiege typePiege = TypePiege.valueOf(boutonClique.getId()); //l'id FXML de l'image = type du piège (ex: "BARRICADE_BOIS")

                    if (partie.getOr().get() >= fabriquePiege.getCoutPour(typePiege)) {
                        Dragboard db = boutonClique.startDragAndDrop(TransferMode.ANY);     //méthode qui commence le drag and drop
                                                                                            //transporte un presse-papier avec

                    Image imageFantome = new Image(boutonClique.getImage().getUrl(), 64, 64, true, true);
                    db.setDragView(imageFantome);
                    db.setDragViewOffsetX(32);
                    db.setDragViewOffsetY(32);

                        //Transmet le type du piège via le presse-papier interne
                        ClipboardContent content = new ClipboardContent();
                        content.putString(typePiege.name());
                        db.setContent(content);

                        event.consume();        //arrête l'événement proprement
                    }
                }
            });
        }
    }

    public void configurerCibleDrag() {
        //Autorise ou refuse visuellement le drop selon la case survolée
        panneauTuile.setOnDragOver(event -> {
            if (event.getGestureSource() != panneauTuile && event.getDragboard().hasString()) {
                double x = event.getX();
                double y = event.getY();
                TypePiege type = TypePiege.valueOf(event.getDragboard().getString());

                //Vérifie que la case est de l'herbe (posable) et pas un obstacle
                if (partie.getCarte().peutPoserPiege(x, y, type)) {
                    event.acceptTransferModes(TransferMode.COPY);       //accepte le dépot de l'image
                } else {
                    event.acceptTransferModes(TransferMode.NONE);       //refuse le dépot de l'image
                }
            }
            event.consume();
        });

        //Effectue le placement quand on relâche le piège sur une case valide
        panneauTuile.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean succes = false;

            if (db.hasString() && !controleur.isPaused()) {
                TypePiege typePiegeEnCours = TypePiege.valueOf(db.getString());       //reconnait le type de piège (son id)
                int col = (int) (event.getX() / tailleTuile);
                int lig = (int) (event.getY() / tailleTuile);

                //Vérifie en déléguant au modèle, si la case libre + or suffisant -> crée le piège
                boolean captureReussie = partie.tenterPlacerPiege(typePiegeEnCours, col, lig, fabriquePiege);

                if (captureReussie) {
                    succes = true;
                    //L'affichage est géré par le ListChangeListener dans le Controleur
                } else {
                    System.out.println("Refus Modèle : Placement ou monnaie insuffisante.");    //pour l'affichage fxml
                }
            }
            event.setDropCompleted(succes); //signale que le drop est terminé et accompli
            event.consume();
        });
    }
}