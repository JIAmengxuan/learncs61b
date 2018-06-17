package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    int[] parent;
    int beginID;
    boolean isCycle;
    int cycleHead;
    int cycleEnd;

    public MazeCycles(Maze m) {
        super(m);
        parent = new int[maze.V()];
        beginID = 0;
        parent[beginID] = Integer.MAX_VALUE;
        marked[beginID] = true;
        distTo[beginID] = 0;
        isCycle = false;
    }

    @Override
    public void solve() {
        detectCycle(beginID);
        for(int i = cycleHead; i != cycleEnd; i = parent[i]) {
            edgeTo[i] = parent[i];
        }
        edgeTo[cycleEnd] = cycleHead;
        announce();
    }

    // Helper methods go here
    private void detectCycle(int curID) {
        announce();
        for(int child : maze.adj(curID)) {
            if(child != parent[curID] && marked[child]) {
                isCycle = true;
            }
            if(isCycle) {
                cycleEnd = child;
                cycleHead = curID;
                announce();
                return;
            }
            if(!marked[child]) {
                distTo[child] = distTo[curID] + 1;
                marked[child] = true;
                parent[child] = curID;
                detectCycle(child);
            }
            if(isCycle) return;
        }
    }
}

