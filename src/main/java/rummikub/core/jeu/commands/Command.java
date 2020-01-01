package rummikub.core.jeu.commands;

/**
 * Représentation d'une commande agissant sur le plateau.
 */
public interface Command {

    /**
     * Exécute la commande.
     */
    void doCommand();

    /**
     * Annule les effets de la commande.
     */
    void undoCommand();
}
