package rummikub.core.api;

import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.Pioche;
import rummikub.core.jeu.commands.*;
import rummikub.core.plateau.Plateau;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class Partie {

    private final Pioche pioche;
    private final Plateau plateau;
    private List<Joueur> listeJoueurs;
    private Joueur joueurEnCours;
    private int numJoueur;
    private final Historique historique;

    public Partie(Set<String> listeNomsJoueurs) {
        pioche = new Pioche();
        plateau = new Plateau();
        historique = new Historique();
        creerJoueurs(listeNomsJoueurs);
        numJoueur = 0;
    }

    private void creerJoueurs(Set<String> listeNomsJoueurs) {
		listeJoueurs = new ArrayList<>();
        listeNomsJoueurs.forEach((nomJoueur) -> {
            listeJoueurs.add(new Joueur(nomJoueur));
        });
    }

    public MessagePartie commencerPartie() {
        initialiserJoueurs();
		return debutDuTour();
		//return messageNouvellePartie();
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
		message.setNomJoueur("[" + nomsDesJoueurs() + "]");
		message.setJeuJoueur("[" + jeuxDesJoueurs() + "]");
		message.setPlateau(plateau.toString());
		return message;		
	}

	private String nomsDesJoueurs(){
		return "";
	}

	private String jeuxDesJoueurs(){
		return "";
	}

	private MessagePartie nouveauMessage(MessagePartie.TypeMessage type, String messageErreur){
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(type);
		message.setNomJoueur(joueurEnCours.getNom());
		message.setJeuJoueur(joueurEnCours.afficheJetonsJoueur());
		message.setPlateau(plateau.toString());
		message.setMessage(messageErreur);
		return message;
	}	
	
	public MessagePartie creerNouvelleSequence(List<Integer> listeIndexJetons) {
        return executerAction(new CreerNouvelleSequence(plateau, joueurEnCours, listeIndexJetons));
    }
    
    public MessagePartie ajouterJeton(List<Integer> indexes) {
        return executerAction(new AjouterJeton(plateau, joueurEnCours, indexes));
    }
    
    public MessagePartie fusionnerSequence(List<Integer> indexes) {
		return executerAction(new FusionnerSequences(plateau, indexes));
    }

    public MessagePartie couperSequence(List<Integer> indexes) {
		return executerAction(new CouperSequence(plateau, indexes));
    }
    
    public MessagePartie deplacerJeton(List<Integer> indexes) {
		return executerAction(new DeplacerJeton(plateau, indexes));
    }
    
    public MessagePartie remplacerJoker(List<Integer> indexes) {
		return executerAction(new RemplacerJoker(plateau, joueurEnCours, indexes));
    }
    
    private MessagePartie executerAction(Command action) {
		try{
        	action.doCommand();
        	historique.ajouterCommande(action);
			return nouveauMessage(MessagePartie.TypeMessage.RESULTAT_ACTION, "");
		}
		catch(Exception e){
			return nouveauMessage(MessagePartie.TypeMessage.ERREUR, e.getMessage());
		}
    }

    public MessagePartie annulerDerniereAction() {
		historique.annulerDerniereCommande();
		return nouveauMessage(MessagePartie.TypeMessage.RESULTAT_ACTION, "");
    }
    
    public MessagePartie terminerTour() throws UnsupportedOperationException {
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
        joueurEnCours.ajouteJeton(pioche.piocher1Jeton());
        return debutDuTour();
    }
}

