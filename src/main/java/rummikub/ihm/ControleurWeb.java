package rummikub.ihm;

import rummikub.core.jeu.Joueur;
import rummikub.core.Actions;
import java.util.ArrayList;
import java.util.List;

/** 
 * Implémentation web de l'interface.
 *
 */
public class ControleurWeb implements ControleurAbstrait {

    //private final List<Joueur> listeJoueurs;

    public ControleurWeb(Joueur[] arrayJoueurs) {
		//listeJoueurs = new ArrayList<>(arrayJoueurs);
    }

    @Override
    public void afficherIntroduction() {
    }

    @Override
    public List<Joueur> listeDesJoueurs() {
        return null;
    }

    @Override
    public void afficherVictoireDe(Joueur joueur) {
    }

    @Override
    public void afficherAide() {
    }

    @Override
    public void changerJoueurCourant(Joueur joueur) {
    }

    @Override
    public void afficherPartie(String plateau) {
    }

    @Override
    public Actions obtenirAction() {
        return null;
    }

    @Override
    public void afficherMessage(String message) {
    }

    @Override
    public List<Integer> obtenirListeJetons() {
        return null;
    }

    @Override
    public List<Integer> obtenirIndexes(List<String> messages) {
        return null;
    }

    @Override
    public void aQuittePartie(Joueur joueur) {
    }
}
