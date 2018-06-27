import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    /**
     * The root upper left/lower right longitudes and latitudes represent the bounding box of
     * the root tile, as the images in the img/ folder are scraped.
     * Longitude == x-axis; latitude == y-axis.
     */
    private static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    /** Each tile is 256x256 pixels. */
    private static final int TILE_SIZE = 256;

    private double[] DthLonDDP;

    public Rasterer() {
        // YOUR CODE HERE
        DthLonDDP = new double[8];
        for(int D = 0; D < DthLonDDP.length; D++) {
            DthLonDDP[D] = getLonDDP(ROOT_LRLON, ROOT_ULLON, TILE_SIZE * Math.pow(2.0, D));
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        //System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in your browser.");
        double ullon = params.get("ullon");
        double lrlon = params.get("lrlon");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double w = params.get("w");

        if(ullon > lrlon || ullat < lrlat || ullon >= ROOT_LRLON || lrlon <= ROOT_ULLON || ullat <= ROOT_LRLAT || lrlat >= ROOT_ULLAT) {
            results.put("query_success", false);
            return results;
        }
        results.put("query_success", true);

        double queryLonDDP = getLonDDP(lrlon, ullon, w);
        int D = findD(queryLonDDP);
        results.put("depth", D);

        Map<String, Object> lonBound;
        lonBound = findlonBound(ROOT_ULLON, ROOT_LRLON, ullon, lrlon, D);
        Map<String, Object> latBound;
        latBound = findlatBound(ROOT_ULLAT, ROOT_LRLAT, ullat, lrlat, D);
        int[] lonBoundIndex = ((int[]) lonBound.get("lonBoundIndex"));
        int[] latBoundIndex = ((int[]) latBound.get("latBoundIndex"));

        String[][] grid = new String[latBoundIndex.length][lonBoundIndex.length];
        for(int i  = 0; i < latBoundIndex.length; i++) {
            for(int j = 0; j < lonBoundIndex.length; j++) {
                grid[i][j] = "d" + String.valueOf(D) + "_x" + String.valueOf(lonBoundIndex[j]) + "_y" + String.valueOf(latBoundIndex[i]) + ".png";
            }
        }
        results.put("render_grid", grid);

        double raster_ul_lon = ((double) lonBound.get("ullon"));
        double raster_lr_lon = ((double) lonBound.get("lrlon"));
        double raster_ul_lat = ((double) latBound.get("ullat"));
        double raster_lr_lat = ((double) latBound.get("lrlat"));

        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lat", raster_lr_lat);

        return results;
    }

    private double getLonDDP(double lrlon, double ullon, double weight) {
        return Math.abs(lrlon - ullon) / weight;
    }

    private int findD(double queryLonDPP) {
        int D;
        for(D = 0; D < 8; D++) {
            if(DthLonDDP[D] <= queryLonDPP) break;
        }
        return D == 8 ? 7 : D;
    }

    private Map<String, Object> findlonBound(double root_ullon, double root_lrlon, double ullon, double lrlon, int D ) {
        Map<String, Object> result = new HashMap<>();
        int boundsNum = ((int) Math.pow(2.0, D));
        double lonPerTile = Math.abs(root_lrlon - root_ullon) / boundsNum;

        int lonBeginNum;
        if(ullon <= root_ullon) {
            lonBeginNum = 0;
        } else {
            lonBeginNum = ((int) (Math.abs(ullon - root_ullon) / lonPerTile));
        }
        result.put("ullon", root_ullon + lonBeginNum * lonPerTile);

        int lonEndNum;
        int quotient;
        if(lrlon >= root_lrlon) {
            lonEndNum = boundsNum - 1;
            quotient = 0;
        } else {
            quotient = ((int) (Math.abs(lrlon - root_lrlon) / lonPerTile));
            lonEndNum = boundsNum - quotient - 1;
        }
        result.put("lrlon", root_lrlon - quotient * lonPerTile);

        int[] lonBound = new int[lonEndNum - lonBeginNum + 1];
        for(int i = 0; i < lonBound.length; i++) {
            lonBound[i] = lonBeginNum;
            lonBeginNum++;
        }
        result.put("lonBoundIndex", lonBound);
        return result;
    }

    private Map<String, Object> findlatBound(double root_ullat, double root_lrlat, double ullat, double lrlat, int D ) {
        Map<String, Object> result = new HashMap<>();
        int boundsNum = ((int) Math.pow(2.0, D));
        double latPerTile = Math.abs(root_lrlat - root_ullat) / boundsNum;

        int latBeginNum;
        if(ullat >= root_ullat) {
            latBeginNum = 0;
        } else {
            latBeginNum = ((int) (Math.abs(ullat - root_ullat) / latPerTile));
        }
        result.put("ullat", root_ullat - latBeginNum * latPerTile);

        int latEndNum;
        int quotient;
        if(lrlat <= root_lrlat) {
            latEndNum = boundsNum - 1;
            quotient = 0;
        } else {
            quotient = ((int) (Math.abs(lrlat - root_lrlat) / latPerTile));
            latEndNum = boundsNum - quotient - 1;
        }
        result.put("lrlat", root_lrlat + quotient * latPerTile);

        int[] latBound = new int[latEndNum - latBeginNum + 1];
        for(int i = 0; i < latBound.length; i++) {
            latBound[i] = latBeginNum;
            latBeginNum++;
        }
        result.put("latBoundIndex", latBound);
        return result;
    }


}
