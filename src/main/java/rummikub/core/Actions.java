package rummikub.core;

import java.util.Map;
import java.util.HashMap;

/**
 * Actions possibles.
 */
public enum Actions {
    AFFICHER_AIDE(0, "Afficher l'aide"),
    NOUVELLE_SEQUENCE(1, "Créer une nouvelle séquence"),
    AJOUTER_JETON(2, "Ajouter un jeton à une séquence"),
    FUSIONNER_SEQUENCES(3, "Fusionner deux séquences"),
    COUPER_SEQUENCE(4, "Couper deux séquences"),
    DEPLACER_JETON(5, "Déplacer un jeton"),
    REMPLACER_JOKER(6, "Remplacer un joker par un jeton"),
    ANNULER_DERNIERE_ACTION(7, "Annuler le coup précédent"),
    TERMINER_TOUR(8, "Terminer le tour"),
    ABANDONNER(9, "Abandonner");

    private final String action;
    private final int index;
    private final static Map<Integer, Actions> indexActions = new HashMap<>();

    //Remplit la map qui fait correspondre index et action
    static {
        for (Actions action : Actions.values()) {
            indexActions.put(action.getIndex(), action);
        }
    }

	/**
	 * Retourne le message correspondant à l'action.
	 *
	 * @return le message
 	 */
    public String getAction() {
        return action;
    }

	/**
	 * Retourne l'index de l'action dans la liste.
	 *
	 * @return l'index (entre 0 et le nombre d'actions - 1)
 	 */
    public int getIndex() {
        return index;
    }

	/**
	 * Traduit l'index de l'action en action.
	 *
	 * @param index index de l'action
	 * @return l'action à l'index index, <code>null</code> si l'index est incorrect.
 	 */
    public static Actions intToAction(int index) {
        return indexActions.get(index);
    }

    Actions(int index, String action) {
        this.index = index;
        this.action = action;
    }
}
