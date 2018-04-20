package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    /*private int[] percThreshold;
    private int T;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        percThreshold = new int[T];
        this.T = T;
        for(int i = 0; i < T; i ++) {
            Percolation perc = new Percolation(N);
            int[] rows = StdRandom.permutation(N);
            int[] cols = StdRandom.permutation(N);
            for(int j = 0;!perc.percolates(); j++) {
                perc.open(rows[j], cols[j]);
            }
            percThreshold[i] = perc.numberOfOpenSites() / (N*N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percThreshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percThreshold);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96*stddev()/Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96*stddev()/Math.sqrt(T);
    }

    // unit testing (not required)
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        System.out.println(T + " independent experiments on an "+ N + "-by-" + N + " grid." );
        System.out.println("Please Wait......");
        PercolationStats percStats = new PercolationStats(N, T);
        System.out.println("——————————————result of experiments——————————————");
        System.out.println("mean of percolation threshold: " + percStats.mean());
        System.out.println("standard deviation of percolation threshold: " + percStats.stddev());
        System.out.println("95% confidence interval: [" + percStats.confidenceLow() + ", " + percStats.confidenceHigh() + "].");
    }*/
}                       
