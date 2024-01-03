package Sac_A_Dos;

import java.io.InputStream;

import Algo_Genetiques.Individu;
import Util.Lecture;
import Algo_Genetiques.Population;

public class Client_Sac_A_Dos {


    /**
     * lit une liste de poids dans un fichier
     *
     * @param nomFichier nom du fichier texte contenant les poids
     * @param nbr_objets nombre d'objets possibles
     * @return tableau de poids
     */
    public static double[] charge_poids(String nomFichier, int nbr_objets) {
        double[] poids = new double[nbr_objets];
        InputStream IS = Lecture.ouvrir(nomFichier);
        if (IS == null) {
            System.err.println("Impossible d'ouvrir le fichier \"" + nomFichier + "\" (n'existe pas ? pas au bon endroit ?)");
        }
        int i = 0;
        int somme = 0;
        while (!Lecture.finFichier(IS) && i < nbr_objets) {
            poids[i] = Lecture.lireDouble(IS);
            somme += poids[i];
            i++;
        }
        System.out.println("charge_poids (" + nomFichier + ") : poids total des objets = " + somme);
        Lecture.fermer(IS);
        return poids;
    }


    public static void main(String[] args) {

        /* paramètres */
        int nbr_indiv = 100;
        double prob_mut = 0.01;
        int iterations_max = 100;

        /* On initialise les poids en lisant un fichier
         */

        int nbr_objets = 28;
        int capacite = 1581;

//		int nbr_objets=70;
//		int capacite=350;

        double[] poids = charge_poids("./data_sad/nbrobj" + nbr_objets + "_capacite" + capacite + ".txt", nbr_objets);

        /* on crée une population (aléatoire)
         * de nbr_indiv individus associés au problème
         * du sac à dos considéré
         */
        Individu_SAD[] individus = new Individu_SAD[nbr_indiv];
        for (int i = 0; i < nbr_indiv; i++) {
            individus[i] = new Individu_SAD(poids, capacite);
        }
        Population<Individu_SAD> p = new Population<Individu_SAD>(individus);


        /* on génére les générations successives
         * en faisant se reproduire la population
         * et on affiche l'adaptation moyenne et maximale de chaque génération
         * on s'arrête si on a atteint la capacité ou si on fait un nombre donné (paramètre) d'itérations
         * le résultat est alors donné par l'individu maximal de la dernière génération
         */
        double previous_adaptation = 0;
        for (int i = 0; i < iterations_max; i++) {
            p.reproduction(prob_mut);
            System.out.println("-- Gen " + i);
            System.out.println("\tAdaptation moyenne: " + p.adaptation_moyenne());
            System.out.println("\tAdaptation maximale: " + p.adaptation_maximale());
            System.out.println("\tAdaptation individu max: " + p.individu_maximal().adaptation());
            if (p.individu_maximal().adaptation() < previous_adaptation)
            {
                System.out.println("Erreur: l'adaptation max a baissee!!!");
                break;
            }

            previous_adaptation = p.individu_maximal().adaptation();

            Individu le_best = p.individu_maximal();
            if (le_best.adaptation() == capacite) {
                System.out.println("Chad trouvé");
                break;
            }
        }

        Individu_SAD le_best = (Individu_SAD) p.individu_maximal();

        System.out.print("Objets: [");
        boolean[] t = le_best.getResult();
        for (int i = 0; i < nbr_objets; i++) {
            if (t[i]) {
                System.out.print(poids[i] + " ");
            }
        }
        System.out.println("]");
    }
}

