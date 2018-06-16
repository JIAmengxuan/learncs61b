package hw3.puzzle;

import edu.princeton.cs.algs4.Queue;


public class Board implements WorldState{
    /**
     Board(tiles): Constructs a board from an N-by-N array of tiles where
     tiles[i][j] = tile at row i, column j
     tileAt(i, j): Returns value of tile at row i, column j (or 0 if blank)
     size():       Returns the board size N
     neighbors():  Returns the neighbors of the current board
     hamming():    Hamming estimate described below
     manhattan():  Manhattan estimate described below
     estimatedDistanceToGoal(): Estimated distance to goal. This method should
     simply return the results of manhattan() when submitted to Gradescope.
     isGoal():    Returns true if is this board the goal board
     equals(y):    Returns true if this board's tile values are the same position as y's
     toString():   Returns the string representation of the board. This method is provided in the skeleton
     */

    private int N;
    private int size;
    private int[] values;
    private static int BLANK = 0;

    public Board(int[][] tiles) {
        N = tiles.length;
        size = N * N;
        values = new int[size];
        for(int i = 0; i < size; i++) {
            if (i < N) values[i] = tiles[0][i];
            else values[i] = tiles[i / N][i % N];
        }
    }

    public int tileAt(int i, int j) throws RuntimeException{
        if(i >= N || i < 0 || j >= N || j < 0)
            throw new java.lang.IndexOutOfBoundsException();
        return values[i * N + j];
    }

    public int size() {
        return N;
    }

    /**
     * Returns neighbors of this board.
     * SPOILERZ: This is the answer.
     */
    //@Josh hug
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int ham = 0;
        for(int i = 0; i < size - 1; i++) {
            if(values[i] != i + 1) ham++;
        }
        if(values[size - 1] != 0) ham++;
        return ham;
    }

    public int manhattan() {
        int man = 0;
        for(int i = 0; i < size; i++) {
            if(values[i] != i + 1) {
                int trueX = values[i] < N ? 0 : values[i] / N;
                int trueY = values[i] < N ? values[i] : values[i] % N;
                int curX = i < N ? 0 : i / N;
                int curY = i < N ? i : i % N;
                man = man + Math.abs(curY + curX - trueY - trueX);
            }
        }
        return man;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean isGoal() {
        return hamming() == 0;
    }

    @Override
    public boolean equals(Object y) {
        if(y == this) return true;
        if(y instanceof Board) {
            if (size != ((Board) y).size) return false;

            Boolean check = true;
            for (int i = 0; i < size; i++) {
                int curX = i < N ? 0 : i / N;
                int curY = i < N ? i : i % N;
                if (values[i] != ((Board) y).tileAt(curX, curY)) {
                    check = false;
                    break;
                }
            }
            return check;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int res = 0;
        for(int i : values) {
            res = res * 31 + i;
        }
        return res;
    }

    /** Returns the string representation of the board.
     * Uncomment this method. */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
