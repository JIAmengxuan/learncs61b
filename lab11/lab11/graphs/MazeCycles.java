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

    private void detectCycle(int curID) {
        //We are READY to operate this node.
        marked[curID] = true;
        announce();

        //End case1: This node is a cycle-neighbor node. Cycle-neighbor here means one of the child node of this node is the cycle intersection.
        for(int child : maze.adj(curID)) {
            if(child != parent[curID] && marked[child]) {
                isCycle = true;
                cycleEnd = child;
                cycleHead = curID;
                return;
            }
        }

        //End case2: We have already found a cycle.
        if(isCycle) {
            return;
        }

        //Common case and end case3:
        for(int child : maze.adj(curID)) {
            if(!marked[child]) {
                distTo[child] = distTo[curID] + 1;
                parent[child] = curID;
                detectCycle(child);
            }
            if(isCycle) return;//Optmize
        }
        //return;
    }
}

