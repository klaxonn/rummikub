package rummikub.core.jeu;

import rummikub.core.pieces.Jeton;
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
     * Nombre de jetons par joueur.
     */
    public static final int NB_INITIAL_JETONS = 14;

    /**
     * Crée une nouvelle pioche.
     *
     * @param listeJetons la liste de tous les jetons de la partie
     */
    public Pioche(List<Jeton> listeJetons) {
        this.listeJetons = listeJetons;
        Collections.shuffle(listeJetons);
	}

    /**
     * Retire un jeton à la pioche.
     *
     * @return un jeton de la pioche choisi aléatoirement
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
     * @return la liste des jetons choisis aléatoirement
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
     * Remet les jetons dans la pioche.
     *
     * @param jetons la liste des jetons à remettre dans la pioche
     */
    public void remettreJetons(List<Jeton> jetons) {
		listeJetons.addAll(jetons);
    }

    /**
     * Détermine si la pioche est vide.
     *
     * @return <code>true</code> si elle est vide
     */
    public boolean isVide() {
        return listeJetons.isEmpty();
    }

    /**
     * Retourne le nombre restant de jetons dans la pioche.
     *
     * @return le nonbre de jetons restants
     */
    public int nombreJetonsRestants() {
        return listeJetons.size();
    }
}
