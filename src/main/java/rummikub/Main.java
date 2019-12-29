package rummikub;

import rummikub.core.Partie;
import rummikub.ihm.ControleurAbstrait;
import rummikub.ihm.ControleurTexte;

public class Main {

    public static void main(String[] args) {
        ControleurAbstrait controleur = new ControleurTexte();
        Partie partie = new Partie(controleur);

        partie.commencerPartie();

    }
}
