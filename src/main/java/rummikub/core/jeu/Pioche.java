package rummikub.core.jeu;

import rummikub.core.pieces.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Représentation d'une pioche.
 *
 * Une pioche contient tous les jetons au début de la partie.
 */
public class Pioche {

    private final List<Jeton> listeJetons;
    /**
     * Valeur maximale d'un jeton.
     */
    public static final int VALEUR_MAX = 13;

    /**
     * Nombre de jokers.
     */
    public static final int NB_JOKERS = 2;

    /**
     * Nombre de jetons par joueur.
     */
    public static final int NB_INITIAL_JETONS = 14;

    /**
     * Nombre d'exemplaires d'un même jeton.
     */
    public static final int NB_JEUX_JETONS = 2;

    /**
     * Crée une nouvelle pioche et l'initialise.
     */
    public Pioche() {
        listeJetons = new ArrayList<>();
        initialiser();
    }

    private void initialiser() {
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
        Collections.shuffle(listeJetons);

    }

    /**
     * Retire un jeton à la pioche.
     *
     * @return un jeton aléatoire contenu dans la pioche
     *
     * @throws UnsupportedOperationException si la pioche est vide
     */
    public Jeton piocher1Jeton() {
		if(listeJetons.isEmpty()){
			throw new UnsupportedOperationException("Pioche vide");
		}
		else {
		    int index = ThreadLocalRandom.current().nextInt(listeJetons.size());
		    Jeton jeton = listeJetons.get(index);
		    listeJetons.remove(jeton);
		    return jeton;
		}
    }

    /**
     * Retire <code>NB_INITIAL_JETONS</code> jetons à la pioche.
     *
     * @return la liste des jetons aléatoires contenus dans la pioche
     *
     */
    public List<Jeton> piocheInitiale() {
        List<Jeton> liste = new ArrayList<>();
        for (int i = 0; i < NB_INITIAL_JETONS; i++) {
            Jeton jeton = piocher1Jeton();
            liste.add(jeton);
        }
        return liste;
    }

    /**
     * Détermine si la pioche est vide.
     *
     * @return <code>true</code> si elle est vide
     *
     */
    public boolean isVide() {
        return listeJetons.isEmpty();
    }

    /**
     * Retourne le nombre restant de jetons dans la pioche.
     *
     * @return le nonbre de jetons restants
     *
     */
    public int nombreJetonsRestants() {
        return listeJetons.size();
    }
}
