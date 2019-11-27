package Rummikub.core.jeu.commands;

/**
 * Représentation d'une commande agissant sur le plateau.
 */
public interface Command {

    /**
     * Exécute la commande.
     *
     * @return <code>true</code> si la commande a réussi
     */
    public boolean doCommand();

    /**
     * Annule les effets de la commande.
     */
    public void undoCommand();
}
