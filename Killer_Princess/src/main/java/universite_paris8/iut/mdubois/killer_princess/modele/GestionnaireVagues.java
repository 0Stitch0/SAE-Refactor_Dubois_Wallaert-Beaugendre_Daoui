
package universite_paris8.iut.mdubois.killer_princess.modele;

import java.util.ArrayList;

public class GestionnaireVagues {
    private ArrayList<Vague> vagues;
    private int vagueActuelle = 0;

    public GestionnaireVagues() {
        this.vagues = new ArrayList<>();
        genererVagues();
    }

    public ArrayList<Vague> getVagues() {
        return this.vagues;
    }

    // Génère les vagues dans l'ordre prévu
    public void genererVagues() {
        ajouterVague1();
        ajouterVague2();
        ajouterVague3();
        ajouterVague4();
        ajouterVague5();
        ajouterVague6();
    }

    private void ajouterVague1() {
        Vague vague = new Vague(1);

        GroupeParVague groupe1 = new GroupeParVague(11);
        ajouterEnnemi(groupe1, "GOBELIN", 2);

        GroupeParVague groupe2 = new GroupeParVague(9);
        ajouterEnnemi(groupe2, "GOBELIN", 1);
        ajouterEnnemi(groupe2, "CHEVALIER", 1);

        vague.ajouterGroupe(groupe1);
        vague.ajouterGroupe(groupe2);
        vagues.add(vague);
    }

    private void ajouterVague2() {
        Vague vague = new Vague(2);

        GroupeParVague groupe1 = new GroupeParVague(9);
        ajouterEnnemi(groupe1, "GOBELIN", 2);
        ajouterEnnemi(groupe1, "CHEVALIER", 1);

        GroupeParVague groupe2 = new GroupeParVague(8);
        ajouterEnnemi(groupe2, "CHEVALIER", 2);
        ajouterEnnemi(groupe2, "SHREK", 1);

        vague.ajouterGroupe(groupe1);
        vague.ajouterGroupe(groupe2);
        vagues.add(vague);
    }

    private void ajouterVague3() {
        Vague vague = new Vague(3);

        GroupeParVague groupe1 = new GroupeParVague(9);
        ajouterEnnemi(groupe1, "GOBELIN", 2);
        ajouterEnnemi(groupe1, "CHEVALIER", 1);

        GroupeParVague groupe2 = new GroupeParVague(8);
        ajouterEnnemi(groupe2, "SHREK", 1);
        ajouterEnnemi(groupe2, "PRINCE", 1);

        GroupeParVague groupe3 = new GroupeParVague(9);
        ajouterEnnemi(groupe3, "CHEVALIER", 1);
        ajouterEnnemi(groupe3, "GOBELIN", 2);

        vague.ajouterGroupe(groupe1);
        vague.ajouterGroupe(groupe2);
        vague.ajouterGroupe(groupe3);
        vagues.add(vague);
    }

    private void ajouterVague4() {
        Vague vague = new Vague(4);

        GroupeParVague groupe1 = new GroupeParVague(8);
        ajouterEnnemi(groupe1, "GOBELIN", 2);
        ajouterEnnemi(groupe1, "CHEVALIER", 2);

        GroupeParVague groupe2 = new GroupeParVague(8);
        ajouterEnnemi(groupe2, "SHREK", 1);
        ajouterEnnemi(groupe2, "PRINCE", 1);

        GroupeParVague groupe3 = new GroupeParVague(7);
        ajouterEnnemi(groupe3, "CHEVALIER", 2);
        ajouterEnnemi(groupe3, "GOBELIN", 2);

        vague.ajouterGroupe(groupe1);
        vague.ajouterGroupe(groupe2);
        vague.ajouterGroupe(groupe3);
        vagues.add(vague);
    }

    private void ajouterVague5() {
        Vague vague = new Vague(5);

        GroupeParVague groupe1 = new GroupeParVague(8);
        ajouterEnnemi(groupe1, "GOBELIN", 2);
        ajouterEnnemi(groupe1, "CHEVALIER", 2);

        GroupeParVague groupe2 = new GroupeParVague(7);
        ajouterEnnemi(groupe2, "SHREK", 1);
        ajouterEnnemi(groupe2, "PRINCE", 1);

        GroupeParVague groupe3 = new GroupeParVague(7);
        ajouterEnnemi(groupe3, "CHEVALIER", 2);
        ajouterEnnemi(groupe3, "PRINCE", 1);

        GroupeParVague groupe4 = new GroupeParVague(6);

        vague.ajouterGroupe(groupe1);
        vague.ajouterGroupe(groupe2);
        vague.ajouterGroupe(groupe3);
        vague.ajouterGroupe(groupe4);
        vagues.add(vague);
    }

    private void ajouterVague6() {
        // Le numéro 5 est conservé comme dans le code d'origine
        Vague vague = new Vague(5);

        vague.ajouterGroupe(new GroupeParVague(9));
        vague.ajouterGroupe(new GroupeParVague(8));
        vague.ajouterGroupe(new GroupeParVague(7));
        vague.ajouterGroupe(new GroupeParVague(6));

        vagues.add(vague);
    }

    // Ajoute un type d'ennemi et sa quantité à un groupe
    private void ajouterEnnemi(
            GroupeParVague groupe, String type, int nombre) {
        groupe.ajouterEnnemi(type, nombre);
    }

    public Vague getVagueActuelle() {
        if (this.vagueActuelle >= this.vagues.size()) {
            return this.vagues.get(this.vagues.size() - 1);
        }
        return this.vagues.get(this.vagueActuelle);
    }

    public boolean vagueTerminee() {
        Vague v = getVagueActuelle();
        return v.vagueTerminee();
    }

    public void vagueSuivante() {
        if (vagueTerminee()) {
            if (this.vagueActuelle < this.vagues.size() - 1) {
                this.vagueActuelle++;
            }
        }
    }

    public int getNbTotalVagues() {
        return this.vagues.size();
    }
}
