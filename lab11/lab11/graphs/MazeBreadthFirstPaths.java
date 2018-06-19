package lab11.graphs;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /*Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Queue<Integer> fringe;
    private int sID;
    private int tID;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        // Add more variables here!
        sID = maze.xyTo1D(sourceX, sourceY);
        tID = maze.xyTo1D(targetX, targetY);
        fringe = new PriorityQueue<>(
                (i1, i2) -> (distTo[i1] - distTo[i2])
        );
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int sourceNode) {
        fringe.offer(sourceNode);
        marked[sourceNode] = true;
        distTo[sourceNode] = 0;
        edgeTo[sourceNode] = 0;
        announce();

        while(!fringe.isEmpty()) {
            int curID = fringe.poll();

            if(curID == tID) return;

            for(int child : maze.adj(curID)) {
                if(!marked[child]) {
                    edgeTo[child] = curID;
                    distTo[child] = distTo[curID] + 1;
                    fringe.offer(child);
                    marked[child] = true;
                    announce();
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(sID);
    }
}

