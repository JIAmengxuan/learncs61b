import sun.awt.image.ImageWatched;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    GraphDB graph;
    private routerNode stNode, destNode, goal;
    private Queue<routerNode> minPQ;
    private Set<Long> closedSet;
    private Map<Long, routerNode> openSet;

    private class routerNode{
        private long ID;
        private double totalDistToGoal;
        private double distToStart;
        private routerNode preNode;

        private routerNode(long id, double distToStart, double totalDist, routerNode pre) {
            ID = id;
            this.distToStart = distToStart;
            totalDistToGoal = totalDist;
            preNode = pre;
        }
    }

    /**
     * Constructor of Router.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     */
    private Router(GraphDB g, double stlon, double stlat, double destlon, double destlat) {
        graph = g;
        openSet = new HashMap<>();
        closedSet = new HashSet<>();
        minPQ = new PriorityQueue<>(
                (node1, node2)->(Double.compare(node1.totalDistToGoal, node2.totalDistToGoal))
        );

        stNode = new routerNode(graph.closest(stlon, stlat), 0, Double.MAX_VALUE, null);
        stNode.preNode = stNode;
        destNode = new routerNode(graph.closest(destlon, destlat), Double.MAX_VALUE, 0, null);
        goal = null;
    }

    /**
     * Calculates the heuristic of current node.
     * @param curNodeId Current node in graph to be calculated.
     * @return Heuristic distance of this node to goal.
     */
    private double heuristic(Long curNodeId) {
        return graph.distance(curNodeId, destNode.ID);
    }

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        Router r = new Router(g, stlon, stlat, destlon, destlat);
        return r.helpFindSP();
    }

    /**
     * Implementation of A * Graph Search algorithm.
     * @return A list of node id's in the order visited on the shortest path.
     */
    private List<Long> helpFindSP() {
        minPQ.offer(stNode);
        openSet.put(stNode.ID, stNode);

        while(!openSet.isEmpty()) {
            routerNode curNode = minPQ.poll();
            openSet.remove(curNode.ID);
            closedSet.add(curNode.ID);

            if(curNode.ID == destNode.ID) {
                goal = curNode;
                break;
            }

            for(Long adjId : graph.adjacent(curNode.ID)) {
                if(closedSet.contains(adjId)) continue;

                double adjNodeDistToSt = graph.distance(adjId, curNode.ID) + curNode.distToStart;
                routerNode adjNode = new routerNode(adjId, adjNodeDistToSt, adjNodeDistToSt + heuristic(adjId), curNode);
                if(!openSet.containsKey(adjId)) {
                    openSet.put(adjId, adjNode);
                    minPQ.offer(adjNode);
                } else {
                    routerNode oldNode = openSet.get(adjId);
                    if(oldNode.distToStart > graph.distance(adjId, curNode.ID) + curNode.distToStart) {
                        openSet.put(adjId, adjNode);
                        minPQ.offer(adjNode);
                    }
                }
            }
        }

        LinkedList<Long> res = new LinkedList<>();
        for(routerNode rn = goal; rn.preNode.ID != rn.ID ; rn = rn.preNode) {
            res.addFirst(rn.ID);
        }
        res.addFirst(stNode.ID);
        return res;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        LinkedList<NavigationDirection> res = new LinkedList<>();

        double lengthBeforeChangeWay = 0;
        boolean start = true;
        for(int i = 0; i < route.size() -1 ; i++) {
            if(g.isOnTheSameWay(route.get(i), route.get(i + 1))) {
                lengthBeforeChangeWay += g.distance(route.get(i), route.get(i + 1));
            } else {
                lengthBeforeChangeWay += g.distance(route.get(i), route.get(i + 1));
                System.out.println(g.findbelongWay(route.get(i)));
                System.out.println(g.findbelongWay(route.get(i + 1)));

                if(start) {
                    res.add(NavigationDirection.fromString("Start on " + g.findbelongWay(route.get(i)) + " and continue for "
                            + Double.toString(lengthBeforeChangeWay) + " miles."));
                    start = false;
                    lengthBeforeChangeWay = 0;
                    continue;
                }

                double angle = g.bearing(route.get(i - 1), route.get(i));
                if(Math.abs(angle) < 15) {
                    res.add(NavigationDirection.fromString("Go straight on " + g.findbelongWay(route.get(i)) + " and continue for "
                            + Double.toString(lengthBeforeChangeWay) + " miles."));
                } else if(Math.abs(angle) >= 15 && Math.abs(angle) < 30) {
                    if(angle > 0) {
                        res.add(NavigationDirection.fromString("Slight right on " + g.findbelongWay(route.get(i)) + " and continue for "
                                + Double.toString(lengthBeforeChangeWay) + " miles."));
                    } else {
                        res.add(NavigationDirection.fromString("Slight left on " + g.findbelongWay(route.get(i)) + " and continue for "
                                + Double.toString(lengthBeforeChangeWay) + " miles."));
                    }
                } else if(Math.abs(angle) >= 30 && Math.abs(angle) < 100) {
                    if (angle > 0) {
                        res.add(NavigationDirection.fromString("Turn right on " + g.findbelongWay(route.get(i)) + " and continue for "
                                + Double.toString(lengthBeforeChangeWay) + " miles."));
                    } else {
                        res.add(NavigationDirection.fromString("Turn left on " + g.findbelongWay(route.get(i)) + " and continue for "
                                + Double.toString(lengthBeforeChangeWay) + " miles."));
                    }
                } else {
                    if (angle > 0) {
                        res.add(NavigationDirection.fromString("Sharp right on " + g.findbelongWay(route.get(i)) + " and continue for "
                                + Double.toString(lengthBeforeChangeWay) + " miles."));
                    } else {
                        res.add(NavigationDirection.fromString("Sharp left on " + g.findbelongWay(route.get(i)) + " and continue for "
                                + Double.toString(lengthBeforeChangeWay) + " miles."));
                    }
                }
                lengthBeforeChangeWay = 0;
            }

        }
        return res;
    }



    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }

}
