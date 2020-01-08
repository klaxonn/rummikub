package rummikub.core.api;

import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.Pioche;
import rummikub.core.jeu.commands.*;
import rummikub.core.plateau.Plateau;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;
 
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
    public PartieImpl(Set<String> listeNomsJoueurs, Pioche pioche, Plateau plateau, Historique historique) {
        this.pioche = pioche;
        this.plateau = plateau;
        this.historique = historique;
        creerJoueurs(listeNomsJoueurs);
		//Pour que le joueur 0  soit le premier à commencer
        numJoueur = -1;
    }

    private void creerJoueurs(Set<String> listeNomsJoueurs) {
		listeJoueurs = new ArrayList<>();
        listeNomsJoueurs.forEach((nomJoueur) -> {
            listeJoueurs.add(new Joueur(nomJoueur));
        });
    }

    public MessagePartie commencerPartie() {
        initialiserJoueurs();
		debutDuTour();
		return messageNouvellePartie();
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
		return nouveauMessage(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR, "");
	}

	private MessagePartie messageNouvellePartie() {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(MessagePartie.TypeMessage.DEBUT_PARTIE);
		message.setNomJoueur(nomsDesJoueurs());
		message.setJeuJoueur(jeuxDesJoueurs());
		message.setPlateau(plateau.toString());
		message.setMessageErreur("");
		return message;		
	}

	private String nomsDesJoueurs(){
        return listeJoueurs.stream().map((joueur) -> joueur.getNom())
									.collect(Collectors.joining(","));
	}

	private String jeuxDesJoueurs(){
        return listeJoueurs.stream().map((joueur) -> joueur.afficheJetonsJoueur())
									.collect(Collectors.joining(","));
	}

	private MessagePartie nouveauMessage(MessagePartie.TypeMessage type, String messageErreur){
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(type);
		message.setNomJoueur(joueurEnCours.getNom());
		message.setJeuJoueur(joueurEnCours.afficheJetonsJoueur());
		message.setPlateau(plateau.toString());
		message.setMessageErreur(messageErreur);
		return message;
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
		if(isJoueurCourant(indexJoueur)){
			try{
				action.doCommand();
				historique.ajouterCommande(action);
				return nouveauMessage(MessagePartie.TypeMessage.RESULTAT_ACTION, "");
			}
			catch(Exception e){
				return nouveauMessage(MessagePartie.TypeMessage.ERREUR, e.getMessage());
			}
		}
		else {
			return joueurIncorrect();
		}
    }

    private boolean isJoueurCourant(int indexJoueur){
		return indexJoueur == numJoueur + 1;
	}

	public MessagePartie joueurIncorrect() {
		return nouveauMessage(MessagePartie.TypeMessage.ERREUR, "Ce n'est pas votre tour");
    }

    public MessagePartie annulerDerniereAction(int indexJoueur) {
		if(isJoueurCourant(indexJoueur)){
			historique.annulerDerniereCommande();
			return nouveauMessage(MessagePartie.TypeMessage.RESULTAT_ACTION, "");
		}
		else {
			return joueurIncorrect();
		}
    }
    
    public int getIndexJoueurCourant(){
		return numJoueur + 1;
	}

    public MessagePartie terminerTour(int indexJoueur) {
		if(isJoueurCourant(indexJoueur)){
			if (plateau.isValide()) {
				if (joueurEnCours.aJoueAuMoins1Jeton()) {
					return joueurAJoue();
				} else {
					return piocher();
				}
			} else {
				return nouveauMessage(MessagePartie.TypeMessage.ERREUR, "plateau non valide");
			}
		}
		else {
			return joueurIncorrect();
		}
    }
    
    private MessagePartie joueurAJoue() {
        if (joueurEnCours.isAutoriseAterminerLeTour()) {
			if(joueurEnCours.aGagne()){
				return nouveauMessage(MessagePartie.TypeMessage.FIN_DE_PARTIE, "");
			}
			else {
				return debutDuTour();
			}
        }  
        else {
			String messageErreur = "" + joueurEnCours.pointsRestantsNecessaires() + " points restants nécessaires";
			return nouveauMessage(MessagePartie.TypeMessage.ERREUR, messageErreur);
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

