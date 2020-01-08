package rummikub.core.api;

import java.util.List;

/**
* Représentation d'une partie.
*/
public interface Partie {

    /**
     * Commence la partie.
	 * Le message envoyé est de type DEBUT_NOUVEAU_TOUR.
	 * Il contient le nom du premier joueur et son jeu.
	 * Il contient aussi le plateau.
     *
     * @return le message contenant les informations
     */
    MessagePartie commencerPartie();

    /**
     * Affiche la partie.
	 * Le message envoyé est de type AFFICHER_PARTIE.
	 * Il contient le nom du joueur donné en index et son jeu.
	 * Il contient aussi le plateau.
     *
     * @param indexJoueur index du joueur qui souhaite afficher la partie
     * @return le message contenant les informations
     */
    MessagePartie afficherPartie(int indexJoueur);

    /**
     * Créé une nouvelle séquence.
	 * Le message envoyé peut être de deux types :
	 * Le message envoyé est de type RESULTAT_ACTION.
	 * Il contient le nom du joueur courant et son jeu.
	 * Il contient aussi le plateau.
	 *
	 * Le message envoyé est de type ERREUR.
	 * Il contient le message d'erreur
	 * Il contient le nom du joueur et son jeu si c'est un joueur valide
	 * Il contient aussi le plateau.
	 *
	 * @param indexJoueur index du joueur qui souhaite effectuer l'action
	 * @param indexes liste contenant les indexes des jetons
	 * dans le jeu du joueur utilisés pour la séquence
     * @return le message contenant les informations
     */
	MessagePartie creerNouvelleSequence(int indexJoueur, List<Integer> indexes);

    /**
     * Ajoute un nouveau jeton.
	 * Le message envoyé peut être de deux types :
	 * Le message envoyé est de type RESULTAT_ACTION.
	 * Il contient le nom du joueur et son jeu si c'est un joueur valide
	 * Il contient aussi le plateau.
	 *
	 * Le message envoyé est de type ERREUR.
	 * Il contient le message d'erreur
	 * Il contient le nom du joueur courant et son jeu.
	 * Il contient aussi le plateau.
     *
     * @param indexJoueur index du joueur qui souhaite effectuer l'action
	 * @param indexes liste contenant l'index du jeton dans le jeu du joueur
	 * et l'index de la séquence
     * @return le message contenant les informations
     */
    MessagePartie ajouterJeton(int indexJoueur, List<Integer> indexes);

    /**
     * Fusionne deux séquences.
	 * Le message envoyé peut être de deux types :
	 * Le message envoyé est de type RESULTAT_ACTION.
	 * Il contient le nom du joueur courant et son jeu.
	 * Il contient aussi le plateau.
	 *
	 * Le message envoyé est de type ERREUR.
	 * Il contient le message d'erreur
	 * Il contient le nom du joueur et son jeu si c'est un joueur valide
	 * Il contient aussi le plateau.
     *
     * @param indexJoueur index du joueur qui souhaite effectuer l'action
	 * @param indexes liste contenant l'index des deux
	 * séquences à fusionner
     * @return le message contenant les informations
     */
    MessagePartie fusionnerSequence(int indexJoueur, List<Integer> indexes);

    /**
     * Coupe une séquence.
	 * Le message envoyé peut être de deux types :
	 * Le message envoyé est de type RESULTAT_ACTION.
	 * Il contient le nom du joueur courant et son jeu.
	 * Il contient aussi le plateau.
	 *
	 * Le message envoyé est de type ERREUR.
	 * Il contient le message d'erreur
	 * Il contient le nom du joueur et son jeu si c'est un joueur valide
	 * Il contient aussi le plateau.
     *
     * @param indexJoueur index du joueur qui souhaite effectuer l'action
	 * @param indexes liste contenant l'index de la séquence
	 * à couper et l'index du jeton qui débutera la nouvelle séquence
     * @return le message contenant les informations
     */
    MessagePartie couperSequence(int indexJoueur, List<Integer> indexes);

    /**
     * Déplace un jeton.
	 * Le message envoyé peut être de deux types :
	 * Le message envoyé est de type RESULTAT_ACTION.
	 * Il contient le nom du joueur courant et son jeu.
	 * Il contient aussi le plateau.
	 *
	 * Le message envoyé est de type ERREUR.
	 * Il contient le message d'erreur
	 * Il contient le nom du joueur et son jeu si c'est un joueur valide
	 * Il contient aussi le plateau.
     *
     * @param indexJoueur index du joueur qui souhaite effectuer l'action
	 * @param indexes liste contenant l'index de séquence de départ,
	 * l'index du jeton à déplacer et l'index de la séquence d'arrivée
     * @return le message contenant les informations
     */
    MessagePartie deplacerJeton(int indexJoueur, List<Integer> indexes);

    /**
     * Remplace un joker.
	 * Le message envoyé peut être de deux types :
	 * Le message envoyé est de type RESULTAT_ACTION.
	 * Il contient le nom du joueur et son jeu si c'est un joueur valide
	 * Il contient aussi le plateau.
	 *
	 * Le message envoyé est de type ERREUR.
	 * Il contient le message d'erreur
	 * Il contient le nom du joueur et son jeu si c'est un joueur valide
	 * Il contient aussi le plateau.
     *
     * @param indexJoueur index du joueur qui souhaite effectuer l'action
	 * @param indexes liste contenant l'index du jeton dans le jeu du joueur
	 * et l'index de la séquence contenant le joker
     * @return le message contenant les informations
     */
    MessagePartie remplacerJoker(int indexJoueur, List<Integer> indexes);

    /**
     * Annule l'action précédente.
	 * Le message envoyé est de type RESULTAT_ACTION.
	 * Il contient le nom du joueur et son jeu si c'est un joueur valide
	 * Il contient aussi le plateau.
	 *
	 * @param indexJoueur index du joueur qui souhaite effectuer l'action
     * @return le message contenant les informations
     */
    MessagePartie annulerDerniereAction(int indexJoueur);

    /**
     * Termine un tour.
	 * Le message envoyé peut être de trois types :
	 * Le message envoyé est de type DEBUT_NOUVEAU_TOUR.
	 * Il contient le nom du joueur qui suit et son jeu.
	 * Il contient aussi le plateau.
	 *
	 * Le message envoyé est de type ERREUR.
	 * Il contient le message d'erreur
	 * Il contient le nom du joueur et son jeu si c'est un joueur valide
	 * Il contient aussi le plateau.
     *
	 * Le message envoyé est de type FIN_DE_PARTIE.
	 * Il contient le nom du joueur courant et son jeu.
	 * Il contient aussi le plateau.
	 *
	 * @param indexJoueur index du joueur qui souhaite effectuer l'action
     * @return le message contenant les informations
     */
    MessagePartie terminerTour(int indexJoueur);

    /**
     * Retourne l'index du joueur courant.
	 *
     * @return l'index
     */
    int getIndexJoueurCourant();
}

