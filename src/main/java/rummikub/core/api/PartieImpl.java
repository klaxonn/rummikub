package rummikub.core.api;

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

	public MessagePartie ajouterJoueur(Joueur joueur) {
		if(!partieCommence && listeJoueurs.size() < NOMBRE_MAX_JOUEURS_PARTIE) {
			listeJoueurs.add(joueur);
            joueur.setPiocheInitiale(pioche.piocheInitiale());
			return creerNouveauMessage(listeJoueurs.indexOf(joueur), MessagePartie.TypeMessage.AJOUTER_JOUEUR, "");
		}
		else {
			String messageErreur = partieCommence ? "Partie déjà commencée" : "Partie Pleine";
			return creerNouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, messageErreur);
		}
	}

	public List<String> listeJoueursPrets(){
		if(!partieCommence) {
			return listeJoueurs.stream().map((joueur) -> joueur.getNom())
										.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

    public MessagePartie commencerPartie() {
		if(!partieCommence && listeJoueurs.size() >= NOMBRE_MIN_JOUEURS_PARTIE) {
			partieCommence = true;
			return debutDuTour();
		}
		else {
			String messageErreur = partieCommence ? "Partie déjà commencée" : "Nombre de joueurs insuffisant";
			return creerNouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, messageErreur);
		}
    }

    private MessagePartie debutDuTour() {
		MessagePartie message = creerNouveauMessage(numJoueur, MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR, "");
		numJoueur = (numJoueur + 1) % listeJoueurs.size();
		message.setIdJoueurCourant(numJoueur + 1);
		joueurEnCours = listeJoueurs.get(numJoueur);
		joueurEnCours.initialiserNouveauTour();
        historique.reinitialiserHistorique();
		return message;
	}

	private MessagePartie creerNouveauMessage(int indexJoueur, MessagePartie.TypeMessage type, String messageErreur){
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(type);
		message.setPlateau(plateau.toString());
		message.setMessageErreur(messageErreur);
		if(correctIndex(indexJoueur)){
			Joueur joueur = listeJoueurs.get(indexJoueur);
			message.setNomJoueur(joueur.getNom());
			message.setIdJoueur(indexJoueur + 1);
			message.setJeuJoueur(joueur.afficheJetonsJoueur());
		}
		if(partieCommence) {
			message.setIdJoueurCourant(numJoueur + 1);
		}
		return message;
	}

	private boolean correctIndex(int indexJoueur){
		return indexJoueur >= 0 && indexJoueur < listeJoueurs.size();
	}

	public MessagePartie afficherPartie(int indexJoueur){
		String messageErreur = "";
		if(partieCommence) {
			int indexReelJoueur = indexJoueur - 1;
			if(correctIndex(indexReelJoueur)){
				return creerNouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.AFFICHER_PARTIE, "");
			}
			else {
				messageErreur = "Joueur inexistant";
			}
		}
		else {
			messageErreur = "Partie non commencée";
		}
		return creerNouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, messageErreur);
	}

	public MessagePartie creerNouvelleSequence(int indexJoueur, List<Integer> indexes) {
        return executerAction(indexJoueur, new CreerNouvelleSequence(plateau, joueurEnCours, indexes));
    }

    public MessagePartie ajouterJeton(int indexJoueur, List<Integer> indexes) {
        return executerAction(indexJoueur, new AjouterJeton(plateau, joueurEnCours, indexes));
    }

    public MessagePartie fusionnerSequence(int indexJoueur, List<Integer> indexes) {
		return executerAction(indexJoueur, new FusionnerSequences(plateau, indexes));
    }

    public MessagePartie couperSequence(int indexJoueur, List<Integer> indexes) {
		return executerAction(indexJoueur, new CouperSequence(plateau, indexes));
    }

    public MessagePartie deplacerJeton(int indexJoueur, List<Integer> indexes) {
		return executerAction(indexJoueur, new DeplacerJeton(plateau, indexes));
    }

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
					return creerNouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.RESULTAT_ACTION, "");
				}
				catch(Exception e){
					return creerNouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, e.getMessage());
				}
			}
			else {
				return creerNouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, "Ce n'est pas votre tour");
			}
		}
		else {
			return creerNouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, "Partie non commencée");
		}
    }

    private boolean isJoueurCourant(int indexReelJoueur) {
		return indexReelJoueur == numJoueur;
	}

    public MessagePartie annulerDerniereAction(int indexJoueur) {
		return executerActionNonCommande(indexJoueur, TypeAction.ANNULER_COMMANDE);
    }

    public MessagePartie terminerTour(int indexJoueur) {
		return executerActionNonCommande(indexJoueur, TypeAction.TERMINER_TOUR);
    }

    private MessagePartie executerActionNonCommande(int indexJoueur, TypeAction type) {
		if(partieCommence) {
			int indexReelJoueur = indexJoueur - 1;
			if(isJoueurCourant(indexReelJoueur)){
				switch(type) {
					case ANNULER_COMMANDE:
						historique.annulerDerniereCommande();
						return creerNouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.RESULTAT_ACTION, "");
					case TERMINER_TOUR:
						return traitementFinDeTour(indexReelJoueur);
					default:
						return null;
				}
			}
			else {
				return creerNouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, "Ce n'est pas votre tour");
			}
		}
		else {
			return creerNouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, "Partie non commencée");
		}
    }

    private MessagePartie traitementFinDeTour(int indexReelJoueur) {
		if (plateau.isValide()) {
			if (joueurEnCours.aJoueAuMoins1Jeton()) {
				return joueurAJoue(indexReelJoueur);
			} else {
				return piocher();
			}
		} else {
			return creerNouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, "plateau non valide");
		}
	}

    private MessagePartie joueurAJoue(int indexReelJoueur) {
        if (joueurEnCours.isAutoriseAterminerLeTour()) {
			if(joueurEnCours.aGagne()){
				return creerNouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.FIN_DE_PARTIE, "");
			}
			else {
				return debutDuTour();
			}
        }
        else {
			String messageErreur = "" + joueurEnCours.pointsRestantsNecessaires() + " points restants nécessaires";
			return creerNouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, messageErreur);
        }
    }

    private MessagePartie piocher() {
		try{
        	joueurEnCours.ajouteJeton(pioche.piocher1Jeton());
			return debutDuTour();
		}
		catch(Exception e){
			return debutDuTour();
		}
    }
}

