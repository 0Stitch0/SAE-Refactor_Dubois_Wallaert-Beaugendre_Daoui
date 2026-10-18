package universite_paris8.iut.mdubois.killer_princess.modele;

import javax.sound.sampled.*;
import java.net.URL;

public class Son {
    private static Clip clipMusique;
    public static boolean sonOn = true;
    private static float volumeMusique;
    private static float volumeBruitage;

    // Permet de changer le volume du son
    public static void volumeMusique(float nb){
        volumeMusique = nb;
        if (clipMusique != null){
            FloatControl gain = (FloatControl) clipMusique.getControl(FloatControl.Type.MASTER_GAIN);
            gain.setValue(nb);
        }
    }

    // Permet de choisir la valeur du volume son
    public static void volumeBruitage(float nb){
        volumeBruitage = nb;
    }

    // Joue la musique de fond
    public static void musiqueDeFond(URL chemin){
        if (!sonOn)
            return;
        try {
            if (clipMusique != null)
                clipMusique.stop();
            AudioInputStream audio = AudioSystem.getAudioInputStream(chemin);
            clipMusique = AudioSystem.getClip();
            clipMusique.open(audio);

            // Bouton volume
            FloatControl gain = (FloatControl) clipMusique.getControl(FloatControl.Type.MASTER_GAIN);
            gain.setValue(volumeMusique);
            clipMusique.start();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    // joue le son du bruitage, appelé par bruitage selection
    public static void bruitage (URL chemin){
        if (!sonOn)
            return;
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(chemin);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gain.setValue(volumeBruitage);

            clip.start();

            // pour fermer un clip après le son du bruitage finit
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    // Utile dans le cas d'ajout de bruitage
    public static void bruitageSelection(){
        if (!sonOn)
            return;
        Son.bruitage(Son.class.getResource("/universite_paris8/iut/mdubois/killer_princess/Musique/Choix.wav"));
    }


    public static void couperSon(){
        clipMusique.stop();
    }

}
