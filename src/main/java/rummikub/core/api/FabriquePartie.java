package rummikub.core.api;

import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.Pioche;
import rummikub.core.plateau.Plateau;
import rummikub.core.plateau.PlateauImpl;
import rummikub.core.plateau.FabriqueSequence;
import rummikub.core.jeu.commands.Historique;
import rummikub.core.pieces.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Fabrique de parties.
 */
final public class FabriquePartie {

	/**
     * Valeur maximale d'un jeton.
     */
    public static final int VALEUR_MAX = 13;

    /**
     * Nombre de jokers.
     */
    public static final int NB_JOKERS = 2;

	/**
     * Nombre d'exemplaires d'un même jeton.
     */
    public static final int NB_JEUX_JETONS = 2;

    private FabriquePartie() {
    }

    /**
     * Créé une nouvelle partie
     *
     * @return une nouvelle partie
     */
    public static Partie creerNouvellePartie() {
		Pioche pioche = new Pioche(creerJetons());
		FabriqueSequence fabrique = FabriqueSequence.obtenirFabrique();
		Plateau plateau = new PlateauImpl(fabrique);
		Historique historique = new Historique();
		return new PartieImpl(pioche, plateau, historique);
    }

    private static List<Jeton> creerJetons() {
		List<Jeton> listeJetons = new ArrayList<>();
		for (Couleur couleur : Couleur.values()) {
            for (int i = 1; i <= VALEUR_MAX; i++) {
                for (int j = 1; j <= NB_JEUX_JETONS; j++) {
                    Jeton jeton = new JetonNormal(i, couleur);
                    listeJetons.add(jeton);
                }
            }
        }
        for (int i = 1; i <= NB_JOKERS; i++) {
            Jeton joker = new Joker();
            listeJetons.add(joker);
        }
        return listeJetons;
	}
}
