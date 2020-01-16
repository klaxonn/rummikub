package rummikub.core.api;

import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.Pioche;
import rummikub.core.jeu.commands.*;
import rummikub.core.plateau.Plateau;
import java.util.List;

/**
* Implémentation d'une partie.
*/
class PartieImpl implements Partie {

    private final Pioche pioche;
    private final Plateau plateau;
    private List<Joueur> listeJoueurs;
    private Joueur joueurEnCours;
    private int numJoueur;
    private final Historique historique;

    /**
     * Crée une nouvelle partie.
     *
     * @param listeNomsJoueurs la liste des noms des joueurs
     */
    public PartieImpl(List<Joueur> listeJoueurs, Pioche pioche, Plateau plateau, Historique historique) {
        this.pioche = pioche;
        this.plateau = plateau;
        this.historique = historique;
		this.listeJoueurs = listeJoueurs;
		//Pour que le joueur 0  soit le premier à commencer
        numJoueur = -1;
    }

    public MessagePartie commencerPartie() {
        initialiserJoueurs();
		return debutDuTour();
    }

    private void initialiserJoueurs() {
        listeJoueurs.forEach((joueur) -> {
            joueur.setPiocheInitiale(pioche.piocheInitiale());
        });
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
		int indexReelJoueur = indexJoueur - 1;
		if(correctIndex(indexReelJoueur)){
			return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.AFFICHER_PARTIE, "");
		}
		else {
			return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, "Joueur inexistant");
		}
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

    private boolean isJoueurCourant(int indexReelJoueur) {
		return indexReelJoueur == numJoueur;
	}

	private MessagePartie joueurIncorrect(int indexReelJoueur) {
		return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.ERREUR, "Ce n'est pas votre tour");
    }

    public MessagePartie annulerDerniereAction(int indexJoueur) {
		int indexReelJoueur = indexJoueur - 1;
		if(isJoueurCourant(indexReelJoueur)){
			historique.annulerDerniereCommande();
			return nouveauMessage(indexReelJoueur, MessagePartie.TypeMessage.RESULTAT_ACTION, "");
		}
		else {
			return joueurIncorrect(indexReelJoueur);
		}
    }

    public int getIndexJoueurCourant(){
		return numJoueur + 1;
	}

    public MessagePartie terminerTour(int indexJoueur) {
		int indexReelJoueur = indexJoueur - 1;
		if(isJoueurCourant(indexReelJoueur)){
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
		else {
			return joueurIncorrect(indexReelJoueur);
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

