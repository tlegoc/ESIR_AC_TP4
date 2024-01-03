package Algo_Genetiques;

import Voyageur_De_Commerce.Individu_VDC;

// L'ajout de cloneable permet de pouvoir
// inserer plusieurs fois un individu ayant une bonne
// adaptation et de le muter sans avoir de probleme
// (ajout par reference)
public interface Individu extends Cloneable {

	public abstract Individu clone();

	/**
	 * renvoie l'adaptation de cet individu
	 */
	public double adaptation();
	
	/**
	 * renvoie un tableau de 2 individus constituant les
	 * enfants de la reproduction entre this et conjoint
	 * @param conjoint à accoupler avec l'objet courant
	 * @return tableau des 2 enfants
	 */
	public Individu[] croisement(Individu conjoint);
	
	/**
	 * applique l'opérateur de mutation
	 * associé à la probabilité prob
	 */
	public void mutation(double prob);
}
