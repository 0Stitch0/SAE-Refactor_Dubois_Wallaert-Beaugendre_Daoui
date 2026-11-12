package universite_paris8.iut.mdubois.killer_princess.controleur.spawn;

import javafx.animation.Timeline;
import javafx.util.Duration;
import universite_paris8.iut.mdubois.killer_princess.modele.*;
import universite_paris8.iut.mdubois.killer_princess.modele.ennemis.Ennemi;

import java.util.ArrayList;

public class ApparitionUnParUn implements StrategieApparition {
    private final Jeu partie;
    private final AppelVagues spawner;
    private ArrayList<Ennemi> fileAttenteSpawn = new ArrayList<>();

    public ApparitionUnParUn(Jeu partie, AppelVagues spawner) {
        this.partie = partie;
        this.spawner = spawner;
    }

    @Override
    public Duration getCadence() {
        //Map 2 : spawn ennemi par ennemi toutes les secondes
        return Duration.seconds(1);
    }

    @Override
    public int getNombreCycles() {
        //INDEFINITE car c'est la fileAttenteSpawn qui gère l'arrêt
        return Timeline.INDEFINITE;
    }

    @Override
    public boolean faireApparaitre() {
        //carte 2 : Spawn un ennemi à la fois
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
        return partie.getGestionnaireVagues().vagueTerminee() && fileAttenteSpawn.isEmpty();
    }
}