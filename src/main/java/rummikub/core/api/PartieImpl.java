package rummikub.core.api;

import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.Pioche;
import rummikub.core.jeu.commands.*;
import rummikub.core.plateau.Plateau;
import java.util.List;
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

    /**
     * Crée une nouvelle partie.
     *
     * @param listeNomsJoueurs la liste des noms des joueurs
     * @throws UnsupportedOperationException si la liste contient trop de joueurs
     */
    public PartieImpl(List<Joueur> listeJoueurs, Pioche pioche, Plateau plateau, Historique historique) {
		if(listeJoueurs.size() <= NOMBRE_MAX_JOUEURS_PARTIE) {
			this.pioche = pioche;
			this.plateau = plateau;
			this.historique = historique;
			this.listeJoueurs = listeJoueurs;
			partieCommence = false;
			//Pour que le joueur 0  soit le premier à commencer
			numJoueur = -1;
		}
		else {
			throw new IllegalArgumentException ("Trop de joueurs");
		}
    }

	public void ajouterJoueur(Joueur joueur) {
		if(!partieCommence && listeJoueurs.size() < NOMBRE_MAX_JOUEURS_PARTIE) {
			listeJoueurs.add(joueur);
		}
		else {
			String messageErreur = isPartieCommence() ? "Partie déjà commencée" : "Partie Pleine";
			throw new UnsupportedOperationException (messageErreur);
		}
	}

	public String afficherJoueursPartie(){
		String resultat = "";
		for (Joueur joueur : listeJoueurs) {
			resultat += "\"" + joueur.getNom() + "\", ";
		}
		return resultat.length() == 0 ? resultat : resultat.substring(0,resultat.length() -2);
	}

    public MessagePartie commencerPartie() {
		if(!partieCommence && listeJoueurs.size() >= NOMBRE_MIN_JOUEURS_PARTIE) {
			initialiserJoueurs();
			partieCommence = true;
			return debutDuTour();
		}
		else {
			String messageErreur = isPartieCommence() ? "Partie déjà commencée" : "Nombre de joueurs insuffisant";
			return nouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, messageErreur);
		}
    }

    private void initialiserJoueurs() {
        listeJoueurs.forEach((joueur) -> {
            joueur.setPiocheInitiale(pioche.piocheInitiale());
        });
    }

    public boolean isPartieCommence() {
		return partieCommence;
	}

    private MessagePartie debutDuTour() {
		numJoueur = (numJoueur + 1) % listeJoueurs.size();
		joueurEnCours = listeJoueurs.get(numJoueur);
		joueurEnCours.initialiserNouveauTour();
        historique.reinitialiserHistorique();
		return nouveauMessage(numJoueur, MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR, "");
	}

	private MessagePartie nouveauMessage(int indexJoueur, MessagePartie.TypeMessage type, String messageErreur){
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(type);
		message.setPlateau(plateau.toString());
		message.setMessageErreur(messageErreur);
		if(correctIndex(indexJoueur)){
			Joueur joueur = listeJoueurs.get(indexJoueur);
			message.setNomJoueur(joueur.getNom());
			message.setJeuJoueur(joueur.afficheJetonsJoueur());
		}
		else{
			message.setNomJoueur("");
			message.setJeuJoueur("");
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
				return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.AFFICHER_PARTIE, "");
			}
			else {
				messageErreur = "Joueur inexistant";
			}
		}
		else {
			messageErreur = "Partie non commencée";
		}
		return nouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, messageErreur);
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
					return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.RESULTAT_ACTION, "");
				}
				catch(Exception e){
					return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, e.getMessage());
				}
			}
			else {
				return joueurIncorrect(indexReelJoueur);
			}
		}
		else {
			return nouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, "Partie non commencée");
		}
    }

    private boolean isJoueurCourant(int indexReelJoueur) {
		return indexReelJoueur == numJoueur;
	}

	private MessagePartie joueurIncorrect(int indexReelJoueur) {
		return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, "Ce n'est pas votre tour");
    }

    public MessagePartie annulerDerniereAction(int indexJoueur) {
		if(partieCommence) {
			int indexReelJoueur = indexJoueur - 1;
			if(isJoueurCourant(indexReelJoueur)){
				historique.annulerDerniereCommande();
				return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.RESULTAT_ACTION, "");
			}
			else {
				return joueurIncorrect(indexReelJoueur);
			}
		}
		else {
			return nouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, "Partie non commencée");
		}
    }

    public int getIndexJoueurCourant() {
		return numJoueur + 1;
	}

    public MessagePartie terminerTour(int indexJoueur) {
		if(partieCommence) {
			int indexReelJoueur = indexJoueur - 1;
			if(isJoueurCourant(indexReelJoueur)){
				return traitementFinDeTour(indexReelJoueur);
			}
			else {
				return joueurIncorrect(indexReelJoueur);
			}
		}
		else {
			return nouveauMessage(INDEX_JOUEUR_ERREUR, MessagePartie.TypeMessage.ERREUR, "Partie non commencée");
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
			return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, "plateau non valide");
		}
	}

    private MessagePartie joueurAJoue(int indexReelJoueur) {
        if (joueurEnCours.isAutoriseAterminerLeTour()) {
			if(joueurEnCours.aGagne()){
				return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.FIN_DE_PARTIE, "");
			}
			else {
				return debutDuTour();
			}
        }
        else {
			String messageErreur = "" + joueurEnCours.pointsRestantsNecessaires() + " points restants nécessaires";
			return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, messageErreur);
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

