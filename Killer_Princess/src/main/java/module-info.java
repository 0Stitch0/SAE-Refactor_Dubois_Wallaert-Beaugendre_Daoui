module universite_paris8.iut.mdubois.killer_princess {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires javafx.base;
    opens universite_paris8.iut.mdubois.killer_princess to javafx.graphics;
    opens universite_paris8.iut.mdubois.killer_princess.controleur to javafx.fxml;
    opens universite_paris8.iut.mdubois.killer_princess.vue to javafx.fxml;
    opens universite_paris8.iut.mdubois.killer_princess.modele to javafx.fxml;
}