package Voyageur_De_Commerce;

import Algo_Genetiques.Individu;

import java.util.*;

import static java.lang.Math.*;

public class Individu_VDC implements Individu {

    private double[] coord_x;
    private double[] coord_y;
    int[] chemin;

    public Individu_VDC(double[] coord_x, double[] coord_y) {
        this.coord_x = coord_x;
        this.coord_y = coord_y;
        this.chemin = new int[coord_x.length];

        List<Integer> indicesShuffled = new ArrayList<>();
        for (int i = 0; i < chemin.length; i++)
            indicesShuffled.add(i);

        Collections.shuffle(indicesShuffled);

        for (int i = 0; i < chemin.length; i++)
            chemin[i] = indicesShuffled.get(i);
    }

    /* Classes de l'interface Individu
     */
    @Override
    public double adaptation() {
        double dist = 0;
        for (int i = 1; i < chemin.length; i++) {
            double dist_x = abs(coord_x[chemin[i]] - coord_x[chemin[i - 1]]);
            double dist_y = abs(coord_y[chemin[i]] - coord_y[chemin[i - 1]]);
            dist += sqrt(dist_x * dist_x + dist_y * dist_y);
        }

        return 100.0 / dist;
    }

    @Override
    public Individu[] croisement(Individu conjoint) {

        Individu_VDC[] enfants = new Individu_VDC[2];
        enfants[0] = new Individu_VDC(coord_x, coord_y);;
        enfants[1] = new Individu_VDC(coord_x, coord_y);
        Individu_VDC conjoint_vdc = (Individu_VDC) conjoint;

        boolean[] b1 = new boolean[chemin.length];
        boolean[] b2 = new boolean[chemin.length];
        for (int i = 0; i < chemin.length; i++) {
            b1[i] = false;
            b2[i] = false;
        }
        Random r = new Random();
        int ind = r.nextInt(chemin.length);

        // on regarde les villes qu'on rencontre dans la premiere passe
        for (int i = 0; i < ind; i++) {
            enfants[0].chemin[i] = this.chemin[i];
            b1[this.chemin[i]] = true;

            enfants[1].chemin[i] = conjoint_vdc.chemin[i];
            b2[conjoint_vdc.chemin[i]] = true;
        }

        //deuxieme passe : si la ville n'a pas été visitée dans la premiere passe, on prend
        int current_1 = 0;
        int current_2 = 0;
        for (int i = ind; i < chemin.length; i++) {
            if (!b1[conjoint_vdc.chemin[i]]) {
                enfants[0].chemin[ind + current_1++] = conjoint_vdc.chemin[i];
                b1[conjoint_vdc.chemin[i]] = true;
            }

            if (!b2[this.chemin[i]]) {
                enfants[1].chemin[ind + current_2++] = this.chemin[i];
                b2[this.chemin[i]] = true;
            }
        }

        //fin : on complète avec les villes non rencontrées
        // Construction de la liste des villes non rencontrées
        int count_1 = 0;
        int count_2 = 0;
        for (int i = 0; i < b1.length; i++) {
            count_1 += b1[i] ? 0 : 1;
            count_2 += b2[i] ? 0 : 1;
        }

        int[] non_renc1 = new int[count_1];
        int[] non_renc2 = new int[count_2];

        int current = 0;
        for (int i = 0; i < b1.length; i++) {
            if (!b1[i])
                non_renc1[current++] = i;
        }
        current = 0;
        for (int i = 0; i < b2.length; i++) {
            if (!b2[i])
                non_renc2[current++] = i;
        }

        for (int i = 0; i < non_renc1.length; i++)
        {
            enfants[0].chemin[ind + current_1 + i] = non_renc1[i];
        }
        for (int i = 0; i < non_renc2.length; i++)
        {
            enfants[1].chemin[ind + current_2 + i] = non_renc2[i];
        }

        return enfants;
    }

    @Override
    public void mutation(double prob) {
        Random r = new Random();
        for (int i = 0; i < chemin.length; i++) {
            if (r.nextDouble() < prob) {
                int ind = r.nextInt(chemin.length);
                int tmp = chemin[i];
                chemin[i] = chemin[ind];
                chemin[ind] = tmp;
            }
        }
    }

    /* Accesseurs (pour Display_VDC)
     */
    public int[] get_parcours() {
        return chemin;
    }

    public double[] get_coord_x() {
        return coord_x;
    }

    public double[] get_coord_y() {
        return coord_y;
    }

    // Pour empecher d'avoir des erreurs lorsque l'on insere
    // plusieurs fois un meme individu dans la liste
    @Override
    public Individu_VDC clone()
    {
        Individu_VDC individuVdc = new Individu_VDC(this.coord_x, this.coord_y);
        individuVdc.chemin = this.chemin.clone();

        return individuVdc;
    }
}
