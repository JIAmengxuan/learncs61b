package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    /*Throw a java.lang.IndexOutOfBoundsException if any argument to open(), isOpen(), or isFull() is outside its prescribed range.
     The constructor should throw a java.lang.IllegalArgumentException if N ≤ 0.
      */
    private boolean[][] rocks;
    private int sizeOfOpened;
    private boolean isPercolation;
    private int sizeN, bottom, top;
    private WeightedQuickUnionUF unionUF;
    private WeightedQuickUnionUF uf;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) throws RuntimeException {
        if(N <= 0)
            throw new java.lang.IllegalArgumentException("N: " + Integer.toString(N) + " should greater than zero");

        rocks = new boolean[N][N];
        sizeN = N;
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                rocks[i][j] = false;
            }
        }

        unionUF = new WeightedQuickUnionUF(N*N + 2);
        uf = new WeightedQuickUnionUF(N*N + 2);
        bottom = N*N + 1;
        top = 0;
        for(int i = 0; i < N; i ++) {
            unionUF.union(xyToInt(0, i), top);
            uf.union(xyToInt(0, i), top);
            unionUF.union(xyToInt(N-1, i), bottom);
        }

        sizeOfOpened = 0;
        isPercolation = false;

    }

    private int xyToInt (int row, int col) {
        return (row*sizeN + col + 1);
    }

    private void unionWithNeighbor (int row, int col) {
        if(isOpen(row, col)) {
            if(row != 0 && isOpen(row-1, col)) {
                unionUF.union(xyToInt(row, col), xyToInt(row-1, col));
                uf.union(xyToInt(row, col), xyToInt(row-1, col));
            }
            if(row != sizeN-1 && isOpen(row+1, col)) {
                unionUF.union(xyToInt(row, col), xyToInt(row+1, col));
                uf.union(xyToInt(row, col), xyToInt(row+1, col));
            }
            if(col != 0 && isOpen(row, col-1)) {
                unionUF.union(xyToInt(row, col), xyToInt(row, col-1));
                uf.union(xyToInt(row, col), xyToInt(row, col-1));
            }
            if(col != sizeN-1 && isOpen(row, col+1)) {
                unionUF.union(xyToInt(row, col), xyToInt(row, col+1));
                uf.union(xyToInt(row, col), xyToInt(row, col+1));
            }
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) throws RuntimeException {
        if(row >= sizeN || col >= sizeN || row < 0 || col < 0)
            throw new java.lang.IndexOutOfBoundsException("(" +Integer.toString(row)+ ", " +Integer.toString(col)+") is outside its prescribed range.");

        if(!isOpen(row, col)) {
            rocks[row][col] = true;
            sizeOfOpened ++;
            unionWithNeighbor(row, col);

            if(unionUF.connected(top, bottom))
                isPercolation = true;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) throws RuntimeException {
        if(row >= sizeN || col >= sizeN || row < 0 || col < 0)
            throw new java.lang.IndexOutOfBoundsException("(" +Integer.toString(row)+ ", " +Integer.toString(col)+") is outside its prescribed range.");

        return rocks[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) throws RuntimeException {
        if(row >= sizeN || col >= sizeN || row < 0 || col < 0)
            throw new java.lang.IndexOutOfBoundsException("(" +Integer.toString(row)+ ", " +Integer.toString(col)+") is outside its prescribed range.");

        return isOpen(row, col) && uf.connected(xyToInt(row, col), top);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return sizeOfOpened;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolation;
    }

    // unit testing (not required)
    public static void main(String[] args) {

    }
}                       
