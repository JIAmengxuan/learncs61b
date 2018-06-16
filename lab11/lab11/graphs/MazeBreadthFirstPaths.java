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
    private Queue<Integer> pq;
    private int sID;
    private int tID;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        // Add more variables here!
        sID = maze.xyTo1D(sourceX, sourceY);
        tID = maze.xyTo1D(targetX, targetY);
        distTo[sID] = 0;
        edgeTo[sID] = sID;
        pq = new PriorityQueue<>(
                (i1, i2) -> (distTo[i1] - distTo[i2]));
        pq.offer(sID);
        marked[sID] = true;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        if(!pq.isEmpty()) {
            int curID = pq.poll();
            //curID is node that we currently at, and it means that we just arrived at this node so we marked it after poll it from PQ.
            marked[curID] = true;
            announce();

            if(curID == tID) return;


            for(int child : maze.adj(curID)) {
                //child is the node will be visited next, so only offer it to the PQ(without marked).
                if(!marked[child]) {
                    edgeTo[child] = curID;
                    distTo[child] = distTo[curID] + 1;
                    pq.offer(child);
                }
            }
        }
        bfs();
    }


    @Override
    public void solve() {
        bfs();
    }
}

