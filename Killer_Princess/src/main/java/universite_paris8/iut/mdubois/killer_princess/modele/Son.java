package universite_paris8.iut.mdubois.killer_princess.modele;

import javax.sound.sampled.*;
import java.net.URL;

public class Son {
    private static final String DOSSIER_MUSIQUE = "/universite_paris8/iut/mdubois/killer_princess/Musique/";
    private static Son instance;
    private Clip clipMusique;
    public boolean sonOn = true;
    private float volumeMusique;
    private float volumeBruitage;

    private Son() {
    }

    public static Son getInstance() {
        if(instance == null) {
            instance =  new Son();
        }
        return instance;
    }

    public boolean estSonOn() {
        return sonOn;
    }

    public void setSonOn(boolean sonOn) {
        this.sonOn = sonOn;
    }

    private void appliquerVolume(Clip clip, float volume) {
        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), volume)));
    }

    // Permet de changer le volume du son
    public void volumeMusique(float volume){
        volumeMusique = volume;
        if (clipMusique != null){
            appliquerVolume(clipMusique, volume);
        }
    }

    // Permet de choisir la valeur du volume son
    public void volumeBruitage(float nb){
        volumeBruitage = nb;
    }

    // Joue la musique de fond
    public void musiqueDeFond(URL chemin){
        if(!sonOn)
            return;
        try {
            couperSon();
            clipMusique = chargerClip(chemin);
            appliquerVolume(clipMusique, volumeMusique);
            clipMusique.start();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    // joue le son du bruitage, appelé par bruitage selection
    public void bruitage (URL chemin){
        if (!sonOn)
            return;
        try {
            Clip clip = chargerClip(chemin);
            appliquerVolume(clip, volumeBruitage);
            clip.addLineListener(event -> {
                if(event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    // Utile dans le cas d'ajout de bruitage
    public void bruitageSelection(){
        bruitage(Son.class.getResource(DOSSIER_MUSIQUE + "Choix.wav"));
    }

    public void couperSon(){
        if(clipMusique != null) {
            clipMusique.stop();
            clipMusique.close();
            clipMusique = null;
        }
    }

    private Clip chargerClip(URL chemin) throws Exception {
        AudioInputStream audio = AudioSystem.getAudioInputStream(chemin);
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        return clip;
    }



}
