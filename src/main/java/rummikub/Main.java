package rummikub;

/*import rummikub.core.Partie;
import rummikub.ihm.ControleurAbstrait;
import rummikub.ihm.ControleurTexte;*/
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
