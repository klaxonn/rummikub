package rummikub.core.jeu.commands;

/**
 * Représentation d'une commande agissant sur le plateau.
 */
public interface Command {

    /**
     * Exécute la commande.
     */
<<<<<<< HEAD:src/main/java/rummikub/core/jeu/commands/Command.java
    boolean doCommand();
=======
    void doCommand();
>>>>>>> web:src/main/java/rummikub/core/jeu/commands/Command.java

    /**
     * Annule les effets de la commande.
     */
    void undoCommand();
}
