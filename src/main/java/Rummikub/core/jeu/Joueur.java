package Rummikub.core.jeu;

import Rummikub.core.pieces.Jeton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représentation d'un joueur.
 *
 */
public class Joueur {

    /**
     * Nombre minimum de points qu'un joueur doit marquer pour commencer la
     * partie.
     */
    public static final int SCORE_MINIMUM_POUR_COMMENCER = 30;
    private String nomJoueur;
    private List<Jeton> listeJetons;
    private int totalPointsJoues;
    private boolean peutJouer;

    /**
     * Crée un nouveau joueur.
     *
     * @param nom nom du joueur
     */
    public Joueur(String nom) {
        nomJoueur = nom;
        peutJouer = false;
        totalPointsJoues = 0;
    }

    public Joueur() {
        peutJouer = false;
        totalPointsJoues = 0;
    }

    /**
     * Initialise les jetons du joueur.
     *
     * @param listeJeton les jetons qui constituent le jeu du joueur
     */
    public void setPiocheInitiale(List<Jeton> listeJeton) {
        this.listeJetons = new ArrayList<>(listeJeton);
    }

    /**
     * Initialise le joueur pour un nouveau tour.
     *
     */
    public void initialiserNouveauTour() {
        peutJouer = isAutoriseAterminerLeTour();
        totalPointsJoues = 0;
    }

    /**
     * Détermine si le joueur a posé au moins un jeton.
     *
     * @return <code>true</code> s'il a posé au moins un jeton (sauf joker)
     *
     */
    public boolean aJoueAuMoins1Jeton() {
        return totalPointsJoues != 0;
    }

    /**
     * Renvoie le nom du joueur.
     *
     * @return le nom
     *
     */
    public String getNom() {
        return nomJoueur;
    }

    public void setNom(String nom) {
        nomJoueur = nom;
    }

    /**
     * Renvoie la représentation textuelle des jetons du joueur.
     *
     * @return la représentation des jetons
     *
     */
    public String afficheJetonsJoueur() {
        return listeJetons.stream().map((jeton) -> jeton.toString() + " ")
                .reduce("", String::concat)
                .strip();
    }

    /**
     * Détermine si le joueur a gagné.
     *
     * @return <code>true</code> s'il a gagné
     *
     */
    public boolean aGagne() {
        return listeJetons.isEmpty();
    }

    /**
     * Utilise le jeton.
     *
     * @param indexJeton l'index du jeton
     * @return le jeton
     * @throws IndexOutOfBoundsException si l'index n'est pas compris entre 1 et
     * le nombre total de jetons
     */
    public Jeton utiliseJeton(int indexJeton) {
        if (isIndexCorrect(indexJeton)) {
            Jeton jeton = listeJetons.get(indexJeton - 1);
            listeJetons.remove(jeton);
            totalPointsJoues += jeton.getValeur();
            return jeton;
        }
        throw new IndexOutOfBoundsException("Index hors limite");
    }

    /**
     * Utilise les jetons.
     *
     * @param indexJetons les index des jetons
     * @return la liste des jetons
     * @throws IndexOutOfBoundsException si les index ne sont pas compris entre
     * 1 et le nombre total de jetons
     */
    public List<Jeton> utiliseJetons(List<Integer> indexJetons) {
        List<Jeton> listeJetonsAUtiliser = new ArrayList<>();
        Collections.sort(indexJetons, Collections.reverseOrder());
        indexJetons.forEach((indexJeton) -> {
            if (isIndexCorrect(indexJeton)) {
                Jeton jeton = listeJetons.get(indexJeton - 1);
                listeJetonsAUtiliser.add(jeton);
                listeJetons.remove(indexJeton - 1);
            } else {
                listeJetons.addAll(listeJetonsAUtiliser);
                throw new IndexOutOfBoundsException("Index hors limite");
            }
        });
        totalPointsJoues += calculerSommeValeurs(listeJetonsAUtiliser);
        return listeJetonsAUtiliser;
    }

    private boolean isIndexCorrect(int index) {
        return index >= 1 && index <= listeJetons.size();
    }

    private int calculerSommeValeurs(List<Jeton> jetons) {
        return jetons.stream().map((jeton) -> jeton.getValeur()).reduce(0, Integer::sum);
    }

    /**
     * Ajoute le jeton dans le jeu du joueur.
     *
     * @param jeton le jeton
     */
    public void ajouteJeton(Jeton jeton) {
        listeJetons.add(jeton);
        totalPointsJoues -= jeton.getValeur();
    }

    /**
     * Ajoute les jetons dans le jeu du joueur.
     *
     * @param jetons la liste des jetons à ajouter
     */
    public void ajouteJetons(List<Jeton> jetons) {
        listeJetons.addAll(jetons);
        totalPointsJoues -= calculerSommeValeurs(jetons);
    }

    /**
     * Renvoie la forme textuelle d'un joueur.
     *
     * Le format contient le nom du joueur et ses jetons.
     */
    @Override
    public String toString() {
        return getNom() + "\n" + afficheJetonsJoueur();
    }

    /**
     * Renvoie le score du joueur.
     *
     * @return le score
     *
     */
    public int getScore() {
        return calculerSommeValeurs(listeJetons);
    }

    /**
     * Détermine si le joueur est autorisé à terminer le tour.
     *
     * Il le peut si le total minimum des points est dépassé ou s'il l'a déjà
     * dépassé dans un tour précédent.
     *
     * @return <code>true</code> si c'est possible
     *
     */
    public boolean isAutoriseAterminerLeTour() {
        boolean limiteDepassee = totalPointsJoues >= SCORE_MINIMUM_POUR_COMMENCER;
        //plus de limite si on l'a atteint une fois
        return peutJouer || limiteDepassee;
    }

    /**
     * Renvoie les points restants à marquer pour être autorisé à terminer le
     * tour.
     *
     * @return le nombre de points
     *
     */
    public int pointsRestantsNecessaires() {
        int score = SCORE_MINIMUM_POUR_COMMENCER - totalPointsJoues;
        return score > 0 && !peutJouer ? score : 0;
    }

    /**
     * Renvoie le nombre de jetons restants.
     *
     * @return le nombre de jetons restants
     *
     */
    public int nombreJetonsRestants() {
        return listeJetons.size();
    }
}
