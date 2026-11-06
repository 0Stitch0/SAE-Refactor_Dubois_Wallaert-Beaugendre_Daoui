package universite_paris8.iut.mdubois.killer_princess.controleur;

import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import universite_paris8.iut.mdubois.killer_princess.modele.Son;

import java.net.URL;


public class Style {

    // Grossit légèrement les éléments d'une Vbox lors d'un hover
    public static void styleCarteHover(VBox cartePiege){
        //changement nom carte piège

        cartePiege.setOnMouseEntered(e-> {
            cartePiege.setScaleX(1.15);
            cartePiege.setScaleY(1.15);
        });

        cartePiege.setOnMouseExited(e-> {
            cartePiege.setScaleX(1);
            cartePiege.setScaleY(1);
        });
    }

    // Grossit légèrement une image lors d'un hover
    public static void styleMenuBouton(ImageView imageMenu){
        URL son = Style.class.getResource("/universite_paris8/iut/mdubois/killer_princess/Musique/Selection.wav");
        imageMenu.setOnMouseEntered(e-> {
            imageMenu.setScaleX(1.15);
            imageMenu.setScaleY(1.15);
            Son.getInstance().bruitage(son);;

        });

        imageMenu.setOnMouseExited( e-> {
            imageMenu.setScaleX(1);
            imageMenu.setScaleY(1);
        });
    }
}
