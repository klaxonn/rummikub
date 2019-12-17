package Rummikub;

import Rummikub.core.Partie;
import Rummikub.ihm.ControleurAbstrait;
import Rummikub.ihm.ControleurTexte;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        /*ControleurAbstrait controleur = new ControleurTexte();
        Partie partie = new Partie(controleur);

        partie.commencerPartie();*/

    }
}
