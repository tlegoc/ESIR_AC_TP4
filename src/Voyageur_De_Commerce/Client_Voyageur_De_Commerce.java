package Voyageur_De_Commerce;

import java.io.*;

import Algo_Genetiques.Individu;
import Sac_A_Dos.Individu_SAD;
import Util.Lecture;
import Algo_Genetiques.Population;

public class Client_Voyageur_De_Commerce {


    /**
     * lit une liste de poids dans un fichier
     *
     * @param nomFichier nom du fichier texte contenant les coordonnées des villes
     * @param nbr_villes nombre de villes
     * @param coord_x    et coord_y : les 2 tableaux que la fonction remplit et qui vont contenir les coordonnées des villes
     */
    public static void charge_coords(String nomFichier, int nbr_villes, double[] coord_x, double[] coord_y) {
        assert (coord_x.length == coord_y.length) : "charge_coords : coord_x et coord_y n'ont pas la même taille ?";
        InputStream IS = Lecture.ouvrir(nomFichier);
        if (IS == null) {
            System.err.println("pb d'ouverture du fichier " + nomFichier);
        }
        int i = 0;
        while (!Lecture.finFichier(IS) && i < coord_x.length) {
            coord_x[i] = Lecture.lireDouble(IS);
            coord_y[i] = Lecture.lireDouble(IS);
            i++;
        }
        Lecture.fermer(IS);
    }

    public static void main(String[] args) throws InterruptedException {

        // PARAMS
        int nbr_villes = 200;
        int nbr_indiv = 1000;
        int iterations_max = 1000;
        double prob_mut = 0.01;
        boolean use_specific_file = true;
        String specific_file = "quadraturecercle_200.txt";


        /* on initialise les coordonnées des villes en les lisant ds un fichier
         */

        double[] coord_x = new double[nbr_villes];
        double[] coord_y = new double[nbr_villes];
        if (!use_specific_file)
            charge_coords("data_vdc/" + nbr_villes + "coords.txt", nbr_villes, coord_x, coord_y);
        else
            charge_coords("data_vdc/" + specific_file, nbr_villes, coord_x, coord_y);

        /* Exemple d'utilisation de Display_VDCC (il faut d'abord faire le constructeur pour ce test fonctionne, ainsi que compléter les accesseurs)
         */
        Individu_VDC ind1 = new Individu_VDC(coord_x, coord_y); //on crée un individu aléatoire
        Individu_VDC ind2 = new Individu_VDC(coord_x, coord_y); //on en crée un autre

        // TEST CROISEMENT
        Individu[] enfants = ind1.croisement(ind2);
        Individu_VDC enfants1 = (Individu_VDC) enfants[0];

        // Verifie que chaque ville est parcourue une seule fois
        boolean[] b = new boolean[nbr_villes];
        assert (b.length == enfants1.chemin.length) : "Les tableaux n'ont pas la même taille";
        for (int i = 0; i < b.length; i++) {
            b[i] = false;
        }

        for (int i = 0; i < enfants1.chemin.length; i++) {
            b[enfants1.chemin[i]] = true;
        }

        for (int i = 0; i < b.length; i++) {
            assert (b[i]) : "La ville " + i + " n'a pas été visitée";
        }

        // ALGO GENETIQUE
        Individu_VDC[] individus = new Individu_VDC[nbr_indiv];
        for (int i = 0; i < nbr_indiv; i++) {
            individus[i] = new Individu_VDC(coord_x, coord_y);
        }
        Population<Individu_VDC> p = new Population<>(individus);


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

            Individu_VDC le_best = (Individu_VDC) p.individu_maximal();
            System.out.println("\tDistance: " + 100.0 / le_best.adaptation());
        }

        Individu_VDC le_best = (Individu_VDC) p.individu_maximal();
        Display_VDC disp = new Display_VDC(le_best);


    }
}