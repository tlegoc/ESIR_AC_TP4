package Algo_Genetiques;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Population<Indiv extends Individu> {

    // Liste contenant les différents individus d'une génération
    private List<Indiv> population;


    /**
     * construit une population à partir d'un tableau d'individu
     */
    public Population(Indiv[] popu) {
        population = Arrays.asList(popu);
    }

    /**
     * sélectionne un individu (sélection par roulette par exemple, cf TD)
     *
     * @param adapt_totale somme des adaptations de tous les individus (pour ne pas avoir à la recalculer)
     * @return indice de l'individu sélectionné
     */
    public int selection(double adapt_totale) {
        double r = new Random().nextDouble();

        int i = 0;
        double tmp = 0;
        do {
            tmp += population.get(i).adaptation();
            i++;
        } while (r < tmp / adapt_totale && i < population.size());
        return i-1;
    }

    /**
     * remplace la génération par la suivante
     * (croisement + mutation)
     *
     * @param prob_mut probabilité de mutation
     */
    @SuppressWarnings("unchecked")
    public void reproduction(double prob_mut) {

        /***** on construit la nouvelle génération ****/
        List<Indiv> new_generation = new ArrayList<>();

        /* élitisme */
        double adapt_totale = adaptation_totale();
        Indiv indiv_max = individu_maximal();

        new_generation.add((Indiv) indiv_max.clone());
        new_generation.add((Indiv) indiv_max.clone());
        new_generation.add((Indiv) indiv_max.clone());
        new_generation.add((Indiv) indiv_max.clone());

        // tant qu'on n'a pas le bon nombre
        while (new_generation.size() < population.size()) {
            // on sélectionne les parents
            Indiv i1 = population.get(selection(adapt_totale));
            Indiv i2 = population.get(selection(adapt_totale));

            // ils se reproduisent
            Indiv[] enfants = (Indiv[]) i1.croisement(i2);

            // on les ajoute à la nouvelle génération
            if (new_generation.size() < population.size()) new_generation.add(enfants[0]);

            if (new_generation.size() < population.size()) new_generation.add(enfants[1]);
        }

        // on applique une éventuelle mutation à toute la nouvelle génération
        for (int i = 1; i < new_generation.size(); i++) {
            new_generation.get(i).mutation(prob_mut);
        }

        //on remplace l'ancienne par la nouvelle
        population = new_generation;
    }

    /**
     * renvoie l'individu de la population ayant l'adaptation maximale
     */
    public Indiv individu_maximal() {
        int indice = 0;
        double adapt_max = population.getFirst().adaptation();
        for (int i = 1; i < population.size(); i++) {
            if (adapt_max < population.get(i).adaptation()) {
                indice = i;
                adapt_max = population.get(i).adaptation();
            }
        }

        return population.get(indice);
    }

    public double adaptation_totale()
    {
        double adapt_totale = 0;
        for (Indiv i : population)
        {
            adapt_totale += i.adaptation();
        }

        return adapt_totale;
    }

    /**
     * renvoie l'adaptation moyenne de la population
     */
    public double adaptation_moyenne() {
        return adaptation_totale() / population.size();
    }

    /**
     * renvoie l'adaptation maximale de la population
     */
    public double adaptation_maximale() {
        return individu_maximal().adaptation();
    }
}
