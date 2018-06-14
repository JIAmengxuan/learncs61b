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
    private MinPQ<searchNode> pq;

    class searchNode implements Iterable<WorldState> {
        int moves;
        searchNode pre;
        WorldState value;
        int priority;

        searchNode(WorldState init, searchNode father, int m) {
            value = init;
            pre = father;
            moves = m;
            priority = moves + init.estimatedDistanceToGoal();
        }

        @Override
        public Iterator<WorldState> iterator() {
            return new snIter();
        }

        private class snIter implements Iterator<WorldState> {
            private searchNode cur;

            snIter() {
                cur = pq.min();
            }

            @Override
            public boolean hasNext() {
                return cur.pre != null;
            }

            @Override
            public WorldState next() {
                WorldState res = cur.value;
                cur = cur.pre;
                return res;
            }
        }
    }

    public Solver(WorldState initial) {
        pq = new MinPQ<>(
                (sn1, sn2) -> (sn1.priority - sn2.priority)
        );

        pq.insert(new searchNode(initial, null, 0));
        while(!pq.min().value.isGoal()) {
            searchNode cur = pq.delMin();
            for(WorldState ws : cur.value.neighbors()) {
                if(cur.pre == null || !ws.equals(cur.pre.value)) {
                    searchNode child = new searchNode(ws, cur, cur.moves + 1);
                    pq.insert(child);
                }
            }
        }

    }
    public int moves() {
        return pq.min().moves;
    }

    public Iterable<WorldState> solution() {
        LinkedList<WorldState> list = new LinkedList<>();
        Iterator<WorldState> i;
        for(i = pq.min().iterator(); i.hasNext();) {
            list.addFirst(i.next());
        }
        list.addFirst(i.next());
        return list;
    }
}