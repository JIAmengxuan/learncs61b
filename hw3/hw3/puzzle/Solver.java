package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.*;


/**
 Solver(initial): Constructor which solves the puzzle, computing
 everything necessary for moves() and solution() to
 not have to solve the problem again. Solves the
 puzzle using the A* algorithm. Assumes a solution exists.
 moves():         Returns the minimum number of moves to solve the puzzle starting
 at the initial WorldState.
 solution():      Returns a sequence of WorldStates from the initial WorldState
 to the solution.
 */
public class Solver {
    private MinPQ<searchNode> minPQ;
    Map<WorldState, searchNode> openSet;
    searchNode goal;
    Map<WorldState, searchNode> closedSet;

    class searchNode {
        int moves;
        searchNode pre;
        WorldState value;
        int sPlusH;

        searchNode(WorldState init, searchNode father, int m) {
            value = init;
            pre = father;
            moves = m;
            sPlusH = moves + value.estimatedDistanceToGoal();
        }

        void setNode(WorldState v, searchNode father, int m) {
            value = v;
            pre = father;
            moves = m;
            sPlusH = moves + value.estimatedDistanceToGoal();
        }

    }

    public Solver(WorldState initial) {
        minPQ = new MinPQ<>(
                (sn1, sn2) -> (sn1.sPlusH - sn2.sPlusH)
        );//here we use PriorityQueue to help sort the nodes in openSet
        closedSet = new HashMap<>();//to mark nodes have already been processed.
        openSet = new HashMap<>();//memorise nodes have already been discovered

        searchNode source = new searchNode(initial, null, 0);
        source.pre = source;
        minPQ.insert(source);
        openSet.put(initial, source);

        while(!openSet.isEmpty()) {
            searchNode cur = minPQ.delMin();//find the node with the lowest (h + s).
            openSet.remove(cur.value);//remove the node from openSet.
            closedSet.put(cur.value, cur);//mark the nodes that have been processed.

            if(cur.value.isGoal()) {
                goal = cur;
                break;
            }
            for(WorldState ws : cur.value.neighbors()) {
                searchNode child = new searchNode(ws, cur, cur.moves + 1);//1 here should be the distance from cur to child.

                if(closedSet.containsKey(ws) && child.sPlusH >= closedSet.get(ws).sPlusH) {
                    /* we have processed this node before, so check it again.
                     * is this a better way to this node? No, we continue.
                     *                                    Yes, add this node again. code is merged below.
                     *
                     * If our A * algorithm has the property "consistence", we don't should do it, for the reason that
                     * the second time we get to this node will increase the actual cost s(), and the h() should be the same,
                     * which cause the sPlusH greater, so we don't need to check this node again.
                     *
                     * In summary, if "consistent", we could just do:
                     * if(closedSet.contains(ws)) continue.
                     */
                    continue;

                }
                if (openSet.containsKey(ws)) {
                    /* we have discovered this node before, so check it again.
                     * is this a better way to this node? No, we continue.
                     *                                    Yes, reset this node.
                     *
                     * This part is analogous to the "relax" operation in Dijkstra's algorithm.
                     */
                    if(child.sPlusH >= openSet.get(ws).sPlusH ) continue;
                    //reset
                    openSet.put(ws, child);
                }

                /* this node is not in the openSet(we discover a new node),
                 * or in the closedSet but with a lower sPlusH, so we should put it to the openSet.
                 */
                openSet.put(ws, child);
                minPQ.insert(child);
            }
        }
    }

    public int moves() {
        return goal.moves;
    }

    public Iterable<WorldState> solution() {
        LinkedList<WorldState> list = new LinkedList<>();
        while(!goal.pre.equals(goal)) {
            list.addFirst(goal.value);
            goal = goal.pre;
        }
        return list;
    }
}