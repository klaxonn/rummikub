package rummikub.core.api;

import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.Pioche;
import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.commands.Historique;
import java.util.Set;

/**
 * Fabrique de parties.
 */
final public class FabriquePartie {

    private FabriquePartie() {
    }

    /**
     * Créé une nouvelle partie
     *
     * @param listeNomsJoueurs la liste des noms des joueurs
     * @return une nouvelle partie
     */
    public static Partie creerNouvellePartie(Set<String> listeNomsJoueurs) {
    	Pioche pioche = new Pioche();
		Plateau plateau = new Plateau();
		Historique historique = new Historique();
		return new PartieImpl(listeNomsJoueurs, pioche, plateau, historique);
    }

}
