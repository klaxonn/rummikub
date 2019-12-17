package rummikub.ihm;

import rummikub.core.jeu.Joueur;
import rummikub.core.Actions;
import java.util.List;

/**
 * Représentation d'un controleur.
 *
 * Il fait le lien entre l'IHM et le moteur du jeu
 */
public interface ControleurAbstrait {

    /**
     * Affiche une introduction avant le lancement d'une partie.
     */
    void afficherIntroduction();

    /**
     * Retourne la liste des joueurs commençant la partie.
     * 
     * @return liste des joueurs
     */
    List<Joueur> listeDesJoueurs();

    /**
     * Affiche un message indicant le gagnant.
     *
     * @param joueur le gagnant
     */
    void afficherVictoireDe(Joueur joueur);

    /**
     * Affiche l'aide.
     *
     * Notamment les différentes actions possibles.
     */
    void afficherAide();

    /**
     * Change le joueur dont c'est le tour de jouer.
     * 
     * @param joueur le joueur courant
     */
    void changerJoueurCourant(Joueur joueur);

    /**
     * Affiche la partie en cours, dont le plateau et le joueur.
     *
     * @param plateau le plateau dans son état courant
     */
    void afficherPartie(String plateau);

    /**
     * Renvoie l'action choisie par le joueur.
     * @return l'action choisie par le joueur
     */
    Actions obtenirAction();

    /**
     * Affiche un message au joueur en cours.
     *
     * @param message le message à envoyer
     */
    void afficherMessage(String message);

    /**
     * Renvoie la liste des index des jetons choisis pour créer une nouvelle
     * séquence.
     * @return la liste des index des jetons
     */
    List<Integer> obtenirListeJetons();

    /**
     * Renvoie la liste des index répondant aux messages.
     *
     * @param messages les questions pour accomplir les actions
     * @return la liste des réponses aux questions
     */
    List<Integer> obtenirIndexes(List<String> messages);

    /**
     * Stoppe la partie quand un joueur abandonne.
     *
     * @param joueur le joueur qui a abandonné
     */
    void aQuittePartie(Joueur joueur);
}
