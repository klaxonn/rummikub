package Rummikub.core.jeu.commands;

import java.util.List;
import java.util.ArrayList;

/**
 * Représentation d'un historique.
 *
 * L'historique contient toutes les actions qu'a effectuées un joueur pendant un
 * tour.
 */
public class Historique {

    List<Command> historique;

    /**
     * Crée un historique.
     */
    public Historique() {
        historique = new ArrayList<>();
    }

    /**
     * Ajoute une action.
     *
     * @param commande la commande à ajouter
     */
    public void ajouterCommande(Command commande) {
        historique.add(commande);
    }

    /**
     * Annule la dernière action et la retire de l'historique.
     */
    public void annulerDerniereCommande() {
        if (historique.size() >= 1) {
            Command derniereCommande = historique.get(historique.size() - 1);
            derniereCommande.undoCommand();
            historique.remove(derniereCommande);
        }
    }

    /**
     * Vide l'historique.
     */
    public void reinitialiserHistorique() {
        historique.clear();
    }

    /**
     * Détermine si l'historique est vide.
     *
     * @return <code>true</code> si il est vide
     */
    public boolean isVide() {
        return historique.isEmpty();
    }
}
