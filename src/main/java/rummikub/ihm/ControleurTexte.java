package rummikub.ihm;

import rummikub.core.jeu.Joueur;
import rummikub.core.Actions;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

/** 
 * Implémentation textuelle de l'interface.
 *
 * Les communications se font par la console. Tous les joueurs partagent la même console.
 * Cette implémentation est surtout pour tester les fonctionnalités.
 */
public class ControleurTexte implements ControleurAbstrait {

    private final Scanner in;
    private final List<Joueur> listeJoueurs;
    private Joueur joueurCourant;

    public ControleurTexte() {
        in = new Scanner(System.in);
        listeJoueurs = new ArrayList<>();
    }

    @Override
    public void afficherIntroduction() {
        System.out.println("RUMMIKUB\n");
    }

    @Override
    public List<Joueur> listeDesJoueurs() {
        System.out.print("Combien de joueurs ? ");
        int nbJoueurs = poserQuestionReponseInt();

        for (int i = 1; i <= nbJoueurs; i++) {
            System.out.print("Nom du joueur " + i + ": ");
            String nomJoueur = in.next();
            listeJoueurs.add(new Joueur(nomJoueur));
        }
        return listeJoueurs;
    }

    @Override
    public void afficherVictoireDe(Joueur joueur) {
        System.out.println("\nFélicitation " + joueur.getNom() + " !");
    }

    @Override
    public void afficherAide() {
        String menu = "\nActions : \n";
        for (Actions action : Actions.values()) {
            menu += action.getIndex() + ") " + action.getAction() + "\n";
        }
        System.out.println(menu);
    }

    @Override
    public void changerJoueurCourant(Joueur joueur) {
        joueurCourant = joueur;
    }

    @Override
    public void afficherPartie(String plateau) {
        if (plateau.isEmpty()) {
            System.out.println("\nPlateau vide\n");
        } else {
            System.out.println("\n" + "Plateau : \n" + plateau + "\n");
        }
        System.out.println("Joueur : " + joueurCourant);
        System.out.println("Points restants nécessaires : " + joueurCourant.pointsRestantsNecessaires());
    }

    @Override
    public Actions obtenirAction() {
        Actions action;
        do {
            System.out.print("\nVotre choix (0 pour l'aide) :");
            int index = poserQuestionReponseInt();
            action = Actions.intToAction(index);
        } while (action == null);
        return action;
    }

    @Override
    public void afficherMessage(String message) {
        System.out.println(message + "\n");
    }

    @Override
    public List<Integer> obtenirListeJetons() {
        System.out.print("Numéro des jetons à utiliser : ");
        List<Integer> listeIndexJetons = new ArrayList<>();
        while (in.hasNextInt()) {
            int index = poserQuestionReponseInt();
            listeIndexJetons.add(index);
        }
        in.next();
        return listeIndexJetons;
    }

    @Override
    public List<Integer> obtenirIndexes(List<String> messages) {
        List<Integer> listeIndexes = new ArrayList<>();
        for (String message : messages) {
            System.out.print(message);
            listeIndexes.add(poserQuestionReponseInt());
        }
        return listeIndexes;
    }

    @Override
    public void aQuittePartie(Joueur joueur) {
        System.exit(0);
    }

    private int poserQuestionReponseInt() {
        for (;;) {
            try {
                return in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Veuillez taper un nombre");
                in.next();
            }
        }
    }
}
