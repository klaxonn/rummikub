package rummikub.controleurs;

import rummikub.core.jeu.Joueur;
import rummikub.core.api.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Implémentation textuelle de l'interface.
 *
 * Les communications se font par la console. Tous les joueurs partagent la même console.
 * Cette implémentation est surtout pour tester les fonctionnalités.
 */
public class InterfaceConsole {

    private final Scanner in;
	private Partie partie;
	private boolean finTour=false;
	private boolean finPartie=false;
	private int indexJoueurCourant = 1;

    /**
     * Crée une nouvelle partie console.
     */
    public InterfaceConsole() {
        in = new Scanner(System.in);
    }

    /**
     * Démarre une nouvelle partie console.
     */
	public void demarrerPartie() {
		afficherIntroduction();
		partie = FabriquePartie.creerNouvellePartie();
		ajouterJoueurs();
		traitementMessages(partie.commencerPartie());
		do {
			finTour=false;
			jouerTour();
		}
		while(!finPartie);
		afficherFindePartie();
	}

	private void afficherIntroduction() {
        System.out.println("RUMMIKUB\n");
    }

	private void afficherFindePartie() {
		System.out.println("Félicitation vous avez gagné !");
    }

    private void ajouterJoueurs() {
        System.out.print("Combien de joueurs ? ");
        int nbJoueurs = poserQuestionReponseInt();

        for (int i = 1; i <= nbJoueurs; i++) {
            System.out.print("Nom du joueur " + i + ": ");
            String nomJoueur = in.next();
            Joueur joueur = new Joueur(nomJoueur);
            partie.ajouterJoueur(joueur);
        }
    }

    private void traitementMessages(MessagePartie message) {
		switch(message.getTypeMessage()){
			case DEBUT_NOUVEAU_TOUR:
				finTour = true;
				indexJoueurCourant = message.getIdJoueurCourant();
				afficherPartie();
				break;
			case FIN_DE_PARTIE:
				finTour = true;
				finPartie = true;
				break;
			case ERREUR:
				System.out.println(message.getMessageErreur());
				afficherPartie();
				break;
			case RESULTAT_ACTION:
				afficherPartie();
				break;
			default:
				System.out.println("message non reconnu");
				break;
		}
    }

	private void afficherPartie(){
		MessagePartie message = partie.afficherPartie(indexJoueurCourant);
		String plateau = message.getPlateau();
		if (plateau.isEmpty()) {
            System.out.println("\nPlateau vide\n");
        } else {
            System.out.println("\n" + "Plateau : \n" + plateau + "\n");
        }

        System.out.println("Joueur : " + message.getNomJoueur());
		System.out.println(message.getJeuJoueur());
	}

    private void jouerTour() {
        do {
            System.out.print("\nVotre choix (0 pour l'aide) :");
            int index = poserQuestionReponseInt();
            executerAction(index);
        } while (!finTour);
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

   	private void executerAction(int index) {
		switch(index){
			case 0:
				afficherAide();
				break;
			case 1:
				creerNouvelleSequence();
				break;
			case 2:
				ajouterJeton();
				break;
			case 3:
				fusionnerSequence();
				break;
			case 4:
				couperSequence();
				break;
			case 5:
				deplacerJeton();
				break;
			case 6:
				remplacerJoker();
				break;
			case 7:
				traitementMessages(partie.annulerDerniereAction(indexJoueurCourant));
				break;
			case 8:
				traitementMessages(partie.terminerTour(indexJoueurCourant));
				break;
			case 9:
				System.exit(0);
				break;
			default:
				System.out.println("Action non reconnue");
				break;
		}
    }

	private void creerNouvelleSequence() {
		List<Integer> listeIndexJetons = obtenirListeJetons();
		traitementMessages(partie.creerNouvelleSequence(indexJoueurCourant,listeIndexJetons));
	}

	private void ajouterJeton() {
		List<Integer> indexes = obtenirIndexes(Arrays.asList("Numéro du jeton à ajouter : ",
                "Numéro de la séquence d'arrivée : "));
		traitementMessages(partie.ajouterJeton(indexJoueurCourant, indexes));
	}

	private void fusionnerSequence() {
		List<Integer> indexes = obtenirIndexes(Arrays.asList("Numéro de la séquence de départ : ",
                "Numéro de la séquence d'arrivée : "));
		traitementMessages(partie.fusionnerSequence(indexJoueurCourant, indexes));
	}

	private void couperSequence() {
		List<Integer> indexes = obtenirIndexes(Arrays.asList("Numéro de la séquence à couper : ",
                "Numéro du jeton où couper : "));
		traitementMessages(partie.couperSequence(indexJoueurCourant, indexes));
	}

	private void deplacerJeton() {
		List<Integer> indexes = obtenirIndexes(Arrays.asList("Numéro de la séquence qui contient le jeton : ",
                "Numéro du jeton à déplacer : ",
                "Numéro de la séquence d'arrivée : "));
		traitementMessages(partie.deplacerJeton(indexJoueurCourant, indexes));
	}

	private void remplacerJoker() {
		List<Integer> indexes = obtenirIndexes(Arrays.asList("Numéro du jeton à uiliser : ",
                "Numéro de la séquence d'arrivée : "));
		traitementMessages(partie.remplacerJoker(indexJoueurCourant, indexes));
	}

    private void afficherAide() {
        String menu = "\nActions : \n";
		menu += "0) Afficher l'aide\n";
		menu += "1) Créer une nouvelle séquence\n";
		menu += "2) Ajouter un jeton à une séquence\n";
		menu += "3) Fusionner deux séquences\n";
		menu += "4) Couper deux séquences\n";
		menu += "5) Déplacer un jeton\n";
		menu += "6) Remplacer un joker par un jeton\n";
		menu += "7) Annuler le coup précédent\n";
  		menu += "8) Terminer le tour\n";
		menu += "9) Abandonner\n";
        System.out.println(menu);
    }

    private List<Integer> obtenirListeJetons() {
        System.out.print("Numéro des jetons à utiliser : ");
        List<Integer> listeIndexJetons = new ArrayList<>();
        while (in.hasNextInt()) {
            int index = poserQuestionReponseInt();
            listeIndexJetons.add(index);
        }
        in.next();
        return listeIndexJetons;
    }

    private List<Integer> obtenirIndexes(List<String> messages) {
        List<Integer> listeIndexes = new ArrayList<>();
        for (String message : messages) {
            System.out.print(message);
            listeIndexes.add(poserQuestionReponseInt());
        }
        return listeIndexes;
    }
}