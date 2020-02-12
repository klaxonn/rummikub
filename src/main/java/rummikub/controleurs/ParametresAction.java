package rummikub.controleurs;

import lombok.Data;

/**
 * Paramètres d'une action.
 */
@Data
public class ParametresAction {

	private int indexSequenceDepart;
	private int indexJeton;
	private int indexSequenceArrivee;

	/**
	 * Constructeur par défaut.
	 */
	public ParametresAction() {
		this.indexSequenceDepart = -1;
		this.indexJeton = -1;
		this.indexSequenceArrivee = -1;
	}

	/**
	 * Paramètres pour déplacer un jeton de la séquence de départ à celle d'arrivée.
	 *
	 * @param indexSequenceDepart index de la séquence qui contient le jeton
	 * @param indexJeton index du jeton à déplacer
	 * @param indexSequenceArrivee index de la séquence destination
	 */
	public void paramDeplacer(int indexSequenceDepart, int indexJeton, int indexSequenceArrivee) {
		this.indexSequenceDepart = indexSequenceDepart;
		this.indexJeton = indexJeton;
		this.indexSequenceArrivee = indexSequenceArrivee;
	}

	/**
	 * Paramètres pour fusionner la séquence d'arrivée avec la séquence de départ.
	 *
	 * @param indexSequenceFusionnee index de la séquence à fusionner
	 * @param indexSequence index de la séquence qui sera supprimée après fusion
	 */
	public void paramFusionner(int indexSequenceFusionnee, int indexSequence) {
		this.indexSequenceDepart = indexSequenceFusionnee;
		this.indexSequenceArrivee = indexSequence;
	}

	/**
	 * Paramètres pour couper la séquence de départ au niveau du jeton.
	 *
	 * @param indexSequenceDepart index de la séquence à couper
	 * @param indexJeton index du jeton qui sera le premier dans la nouvelle séquence
	 */
	public void paramCouper(int indexSequenceDepart, int indexJeton) {
		this.indexSequenceDepart = indexSequenceDepart;
		this.indexJeton = indexJeton;
	}

	/**
	 * Paramètres pour remplacer un joker d'une séquence.
	 *
	 * @param indexJeton index du jeton à utiliser pour remplacer le joker
	 * @param indexSequenceArrivee index de la séquence contenant le joker
	 */
	public void paramRemplacerJoker(int indexJeton, int indexSequenceArrivee) {
		this.indexJeton = indexJeton;
		this.indexSequenceArrivee = indexSequenceArrivee;
	}

	/**
	 * Paramètres pour ajouter un jeton à une séquence.
	 *
	 * @param indexJeton index du jeton dans le jeu du joueur
	 * @param indexSequenceArrivee index de la séquence de destination
	 */
	public void paramAjouterJeton(int indexJeton, int indexSequenceArrivee) {
		this.indexJeton = indexJeton;
		this.indexSequenceArrivee = indexSequenceArrivee;
	}
}
