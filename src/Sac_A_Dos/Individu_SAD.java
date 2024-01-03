package Sac_A_Dos;

import Algo_Genetiques.Individu;
import Voyageur_De_Commerce.Individu_VDC;

import java.util.Random;

public class Individu_SAD implements Individu {

    private double[] poids;
    private double poids_max;
    private boolean[] t;

    public boolean[] getResult() {
        return t;
    }

    public Individu_SAD(double[] poids, double pm) {
        this.poids = poids;
        this.poids_max = pm;
        this.t = new boolean[poids.length];

        Random r = new Random();
        for (int i = 0; i < t.length; i++)
            t[i] = r.nextBoolean();
    }

    @Override
    public double adaptation() {
        double sum = 0;
        for (int i = 0; i < t.length; i++) {
            if (t[i]) sum += poids[i];
        }

        if (sum > poids_max) return 0;

        return sum;
    }

    @Override
    public Individu[] croisement(Individu conjoint) {
        Random r = new Random();

        int coupure = r.nextInt(t.length);
        Individu_SAD i1 = new Individu_SAD(poids, poids_max);
        Individu_SAD i2 = new Individu_SAD(poids, poids_max);

        for (int i = 0; i < t.length; i++) {
            if (i < coupure) {
                i1.t[i] = this.t[i];
                i2.t[i] = ((Individu_SAD) conjoint).t[i];
            } else {
                i2.t[i] = this.t[i];
                i1.t[i] = ((Individu_SAD) conjoint).t[i];
            }
        }

        return new Individu[]{i1, i2};
    }

    @Override
    public void mutation(double prob) {
        Random r = new Random();

        for (int i = 0; i < t.length; i++) {
            double c = r.nextDouble(1);

            if (c < prob) {
                t[i] = !t[i];
            }
        }
    }

    @Override
    public Individu_SAD clone()
    {
        Individu_SAD individuSad = new Individu_SAD(this.poids, this.poids_max);
        individuSad.t = this.t.clone();

        return individuSad;
    }
}
