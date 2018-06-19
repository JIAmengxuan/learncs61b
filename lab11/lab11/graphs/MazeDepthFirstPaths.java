package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeDepthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;


    public MazeDepthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /**
     * So let us clear up the codes above:
     * First, we could merge the codes above, especially for the End case3 and common cases.
     * we can also optimze this code by adding a line(line55) for saving-time purpose.
     * Code below is the final type.
     */
    private void dfs(int curNodeId) {
        marked[curNodeId] = true;
        announce();//draw
        //End case1: current node is the target.
        if(curNodeId == t) {
            targetFound = true;
            return;
        }

        //End case2: we already found the target(in this maze case the target node is node at the top-right),so we do not need to recursion anymore.
        if(targetFound) return;

        //Enc case3 and common case:
        for(int child : maze.adj(curNodeId)) {
            if(!marked[child]) {
                //do the common operations
                distTo[child] = distTo[curNodeId] + 1;
                edgeTo[child] = curNodeId;
                dfs(child);
                if(targetFound) return;
            }
        }
        //and after this loop, all the child nodes of current node is visited and then we should return.
        //return;
    }

    @Override
    public void solve() {
        dfs(s);
    }
}

