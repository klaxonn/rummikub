package Rummikub;

import Rummikub.core.Partie;
import Rummikub.ihm.ControleurAbstrait;
import Rummikub.ihm.ControleurTexte;

public class Main {

    public static void main(String[] args) {
        ControleurAbstrait controleur = new ControleurTexte();
        Partie partie = new Partie(controleur);

        partie.commencerPartie();

    }
}
