package lab11.graphs;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private Queue<Integer> pq;
    private int[] estimateDist;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        pq = new PriorityQueue<>(
                //(i1, i2) -> (estimateDist[i1] - estimateDist[i2]) optimize:
                (i1, i2) -> (estimateDist[i1] == estimateDist[i1] ? h(i1) - h(i2) : estimateDist[i1] - estimateDist[i2])
        );
        estimateDist = new int[maze.V()];
        for(int i = 0; i < estimateDist.length; i++) {
            estimateDist[i] = Integer.MAX_VALUE;
        }
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(t) - maze.toX(v)) + Math.abs(maze.toY(t) - maze.toY(v));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        // TODO
        pq.offer(s);
        marked[s] = true;
        while(pq.peek() != t) {
            int curID = pq.poll();
            for(int child : maze.adj(curID)) {
                if(!marked[child]) {
                    pq.offer(child);
                    marked[child] = true;
                    distTo[child] = distTo[curID] + 1;
                    edgeTo[child] = curID;
                    estimateDist[child] = distTo[child] + h(child);
                    announce();

                }
            }
        }

    }

    @Override
    public void solve() {
        astar(s);
    }

}

