import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    private Map<Long, vertex> vertexes = new HashMap<>();
    private Map<Long, way> ways = new HashMap<>();

    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private class vertex{
        private double lon, lat;
        private HashSet<Long> adj;
        private String name;
        private long wayId;

        private vertex(double longitude, double latitude) {
            name = null;
            wayId = Long.MAX_VALUE;
            lon = longitude;
            lat = latitude;
            adj = new HashSet<>();
        }
    }

    private class way {
        private List<Long> nodes;
        private String name;

        private way(List<Long> ns) {
            nodes = ns;
        }

    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        LinkedList<Long> vToDel = new LinkedList<>();
        for(long v : vertexes.keySet()) {
            if(vertexes.get(v).adj.isEmpty()) {
                vToDel.add(v);
            }
        }
        for(long v : vToDel) {
            vertexes.remove(v);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return vertexes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return vertexes.get(v).adj;
    }

    void setNodeName(long v, String name){
        vertexes.get(v).name = name;
    }

    void setWayName(long w, String name) {
        ways.get(w).name = name;
    }

    boolean isOnTheSameWay(long node1, long node2) {
        String way1 = findbelongWay(node1);
        String way2 = findbelongWay(node2);
        return !way1.equals("unknown road") && way1.equals(way2);
    }

    String findbelongWay(long v) {
        System.out.println(Long.toString(vertexes.get(v).wayId));
        return ways.get(vertexes.get(v).wayId).name == null ? "unknown road" : ways.get(vertexes.get(v).wayId).name;
    }


    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closest = 0;
        double minimumDist = Double.MAX_VALUE;
        for(long v : vertexes.keySet()) {
            double curDist = distance(lon(v), lat(v), lon, lat);
            if(curDist < minimumDist) {
                closest = v;
                minimumDist = curDist;
            }
        }
        return closest;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return vertexes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return vertexes.get(v).lat;
    }

    /**
     * Adds a node to GraphDB.
     * @param v The id of the node.
     * @param lon The longitude of the node.
     * @param lat The latitude of the node.
     */
    void addNode(long v, double lon, double lat) {
        vertexes.put(v, new vertex(lon, lat));
    }

    /**
     * Initializes a way and connects all the vertices of this way.
     * Assumes the number of vertices is greater than 2.
     * Assumes all nodes have been added to GraphDB.
     * @param vertices Vertexes in the way to be added.
     */
    void connectWay(long wayID, List<Long> vertices) {
        ways.put(wayID, new way(vertices));

        for(int i = 0; i < vertices.size(); i++) {
            long vID = vertices.get(i);
            if(!vertexes.containsKey(vID)) {
                new RuntimeException("node: " + vID + "is not in the GraphDB").printStackTrace();
            }

            vertexes.get(vID).wayId = wayID;

            HashSet<Long> adjacentNodes = vertexes.get(vID).adj;
            if(i != 0) {
                adjacentNodes.add(vertices.get(i - 1));
            }
            if(i != vertices.size() - 1) {
                adjacentNodes.add(vertices.get(i + 1));
            }
        }
    }
}
