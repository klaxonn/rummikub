package rummikub.core.api;

import static rummikub.core.api.MessagePartie.TypeMessage.*;
import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.Pioche;
import rummikub.core.jeu.commands.*;
import rummikub.core.plateau.Plateau;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
* Implémentation d'une partie.
*/
class PartieImpl implements Partie {

	private static final int INDEX_JOUEUR_ERREUR = -1;
    private final Pioche pioche;
    private final Plateau plateau;
    private List<Joueur> listeJoueurs;
    private Joueur joueurEnCours;
    private int numJoueur;
    private final Historique historique;
    private boolean partieCommence;

	private enum TypeAction {
        ANNULER_COMMANDE, TERMINER_TOUR
    }

    /**
     * Crée une nouvelle partie.
     *
     * @param pioche la pioche
     * @param plateau le plateau
     * @param historique l'historique
     */
    public PartieImpl(Pioche pioche, Plateau plateau, Historique historique) {
		this.pioche = pioche;
		this.plateau = plateau;
		this.historique = historique;
		listeJoueurs = new ArrayList<>();
		partieCommence = false;
		//Pour que le joueur 0  soit le premier à commencer
		numJoueur = -1;
    }

	@Override
	public MessagePartie ajouterJoueur(Joueur joueur) {
		if(!partieCommence && nombreJoueurs() < NOMBRE_MAX_JOUEURS_PARTIE) {
			listeJoueurs.add(joueur);
            joueur.setPiocheInitiale(pioche.piocheInitiale());
			return creerNouveauMessage(listeJoueurs.indexOf(joueur), AJOUTER_JOUEUR, "");
		}
		else {
			String messageErreur = partieCommence ? "Partie déjà commencée" : "Partie Pleine";
			return creerNouveauMessage(INDEX_JOUEUR_ERREUR, ERREUR, messageErreur);
		}
	}

	@Override
	public int nombreJoueurs() {
		return (int) listeJoueurs.stream().filter(joueur -> joueur != null)
										  .count();
	}

	@Override
	public List<String> listeJoueursPrets(){
		if(!partieCommence) {
			return listeJoueurs.stream().filter(joueur -> joueur != null)
										.map((joueur) -> joueur.getNom())
										.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Override
    public MessagePartie commencerPartie(int indexJoueur) {
		String messageErreur;
		int indexReelJoueur = indexJoueur - 1;
		if(!correctIndex(indexReelJoueur)) {
			messageErreur = "Joueur inexistant";
		}
		else if(nombreJoueurs() < NOMBRE_MIN_JOUEURS_PARTIE) {
			messageErreur = "Nombre de joueurs insuffisant";
		}
		else if(partieCommence) {
			return creerNouveauMessage(indexReelJoueur, DEBUT_NOUVEAU_TOUR, "");
		}
		else {
			partieCommence = true;
			return debutDuTour(indexReelJoueur);
		}
		return creerNouveauMessage(INDEX_JOUEUR_ERREUR, ERREUR, messageErreur);
    }

	@Override
    public MessagePartie quitterPartie(int indexJoueur) {
		int indexReelJoueur = indexJoueur - 1;
		if(correctIndex(indexReelJoueur) && nombreJoueurs() > NOMBRE_MIN_JOUEURS_PARTIE) {
			pioche.remettreJetons(listeJoueurs.get(indexReelJoueur).retireTouslesJetons());
			listeJoueurs.set(indexReelJoueur, null);
			return creerNouveauMessage(INDEX_JOUEUR_ERREUR, FIN_DE_PARTIE, "");
		}
		else {
			String messageErreur = nombreJoueurs() <= NOMBRE_MIN_JOUEURS_PARTIE ? "Minimum joueurs atteint" : "Joueur inexistant";
			return creerNouveauMessage(INDEX_JOUEUR_ERREUR, ERREUR, messageErreur);
		}
	}

    private MessagePartie debutDuTour(int indexReelJoueur) {
		MessagePartie message = creerNouveauMessage(indexReelJoueur, DEBUT_NOUVEAU_TOUR, "");
		do {
			numJoueur = (numJoueur + 1) % listeJoueurs.size();
			joueurEnCours = listeJoueurs.get(numJoueur);
		}
		while(joueurEnCours == null);
		message.setIdJoueurCourant(numJoueur + 1);
		joueurEnCours.initialiserNouveauTour();
        historique.reinitialiserHistorique();
		return message;
	}

	private MessagePartie creerNouveauMessage(int indexJoueur, MessagePartie.TypeMessage type, String messageErreur){
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(type);
		message.setMessageErreur(messageErreur);
		if(correctIndex(indexJoueur)){
			Joueur joueur = listeJoueurs.get(indexJoueur);
			message.setPlateau(plateau.toString());
			message.setNomJoueur(joueur.getNom());
			message.setIdJoueur(indexJoueur + 1);
			message.setJeuJoueur(joueur.afficheJetonsJoueur());
		}
		if(partieCommence && correctIndex(indexJoueur)) {
			message.setIdJoueurCourant(numJoueur + 1);
		}
		return message;
	}

	private boolean correctIndex(int indexJoueur){
		return indexJoueur >= 0 && indexJoueur < listeJoueurs.size()
		   && listeJoueurs.get(indexJoueur) != null;
	}

	@Override
	public MessagePartie afficherPartie(int indexJoueur){
		String messageErreur = "";
		if(partieCommence) {
			int indexReelJoueur = indexJoueur - 1;
			if(correctIndex(indexReelJoueur)){
				return creerNouveauMessage(indexReelJoueur, AFFICHER_PARTIE, "");
			}
			else {
				messageErreur = "Joueur inexistant";
			}
		}
		else {
			messageErreur = "Partie non commencée";
		}
		return creerNouveauMessage(INDEX_JOUEUR_ERREUR, ERREUR, messageErreur);
	}

	@Override
	public MessagePartie creerNouvelleSequence(int indexJoueur, List<Integer> indexes) {
        return executerAction(indexJoueur, new CreerNouvelleSequence(plateau, joueurEnCours, indexes));
    }

	@Override
    public MessagePartie ajouterJeton(int indexJoueur, List<Integer> indexes) {
        return executerAction(indexJoueur, new AjouterJeton(plateau, joueurEnCours, indexes));
    }

	@Override
    public MessagePartie fusionnerSequence(int indexJoueur, List<Integer> indexes) {
		return executerAction(indexJoueur, new FusionnerSequences(plateau, indexes));
    }

	@Override
    public MessagePartie couperSequence(int indexJoueur, List<Integer> indexes) {
		return executerAction(indexJoueur, new CouperSequence(plateau, indexes));
    }

	@Override
    public MessagePartie deplacerJeton(int indexJoueur, List<Integer> indexes) {
		return executerAction(indexJoueur, new DeplacerJeton(plateau, indexes));
    }

	@Override
    public MessagePartie remplacerJoker(int indexJoueur, List<Integer> indexes) {
		return executerAction(indexJoueur, new RemplacerJoker(plateau, joueurEnCours, indexes));
    }

    private MessagePartie executerAction(int indexJoueur, Command action) {
		if(partieCommence) {
			int indexReelJoueur = indexJoueur - 1;
			if(isJoueurCourant(indexReelJoueur)){
				try{
					action.doCommand();
					historique.ajouterCommande(action);
					return creerNouveauMessage(indexReelJoueur, RESULTAT_ACTION, "");
				}
				catch(Exception e){
					return creerNouveauMessage(indexReelJoueur, ERREUR, e.getMessage());
				}
			}
			else {
				return creerNouveauMessage(indexReelJoueur, ERREUR, "Ce n'est pas votre tour");
			}
		}
		else {
			return creerNouveauMessage(INDEX_JOUEUR_ERREUR, ERREUR, "Partie non commencée");
		}
    }

    private boolean isJoueurCourant(int indexReelJoueur) {
		return indexReelJoueur == numJoueur;
	}

	@Override
    public MessagePartie annulerDerniereAction(int indexJoueur) {
		return executerActionNonCommande(indexJoueur, TypeAction.ANNULER_COMMANDE);
    }

	@Override
    public MessagePartie terminerTour(int indexJoueur) {
		return executerActionNonCommande(indexJoueur, TypeAction.TERMINER_TOUR);
    }

    private MessagePartie executerActionNonCommande(int indexJoueur, TypeAction type) {
		if(partieCommence) {
			int indexReelJoueur = indexJoueur - 1;
			if(isJoueurCourant(indexReelJoueur)){
				if(type.equals(TypeAction.ANNULER_COMMANDE)) {
					historique.annulerDerniereCommande();
					return creerNouveauMessage(indexReelJoueur, RESULTAT_ACTION, "");
				}
				else {
					//type = TERMINER_TOUR
					return traitementFinDeTour(indexReelJoueur);
				}
			}
			else {
				return creerNouveauMessage(indexReelJoueur, ERREUR, "Ce n'est pas votre tour");
			}
		}
		else {
			return creerNouveauMessage(INDEX_JOUEUR_ERREUR, ERREUR, "Partie non commencée");
		}
    }

    private MessagePartie traitementFinDeTour(int indexReelJoueur) {
		if (plateau.isValide()) {
			if (joueurEnCours.aJoueAuMoins1Jeton()) {
				return joueurAJoue(indexReelJoueur);
			} else {
				return piocher(indexReelJoueur);
			}
		} else {
			return creerNouveauMessage(indexReelJoueur, ERREUR, "plateau non valide");
		}
	}

    private MessagePartie joueurAJoue(int indexReelJoueur) {
        if (joueurEnCours.isAutoriseAterminerLeTour()) {
			if(joueurEnCours.aGagne()){
				return creerNouveauMessage(indexReelJoueur, FIN_DE_PARTIE, "");
			}
			else {
				return debutDuTour(indexReelJoueur);
			}
        }
        else {
			String messageErreur = "" + joueurEnCours.pointsRestantsNecessaires() + " points restants nécessaires";
			return creerNouveauMessage(indexReelJoueur, ERREUR, messageErreur);
        }
    }

    private MessagePartie piocher(int indexReelJoueur) {
		try{
        	joueurEnCours.ajouteJeton(pioche.piocher1Jeton());
			return debutDuTour(indexReelJoueur);
		}
		catch(Exception e){
			return debutDuTour(indexReelJoueur);
		}
    }
}
