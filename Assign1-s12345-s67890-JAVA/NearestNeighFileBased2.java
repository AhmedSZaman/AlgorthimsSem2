
import nearestNeigh.Category;
import nearestNeigh.Point;
import nearestNeigh.KDTreeNN;
import java.io.*;
import java.util.*;
import nearestNeigh.NaiveNN;
import nearestNeigh.NearestNeigh;

/**
 * This is to be the main class when we run the program in file-based point.
 * It uses the data file to initialise the set of points.
 * It takes a command file as input and output into the output file.
 *
 * 
 */
public class NearestNeighFileBased2 {

    /**
     * Name of class, used in error messages.
     */
    protected static final String progName = "NearestNeighFileBased";

    /**
     * Print help/usage message.
     */
    public static void usage(String progName) {
        System.err.println(progName + ": <approach> [data fileName] [command fileName] [output fileName]");
        System.err.println("<approach> = <naive | kdtree>");
        System.exit(1);
    } // end of usage

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // read command line arguments
        if (args.length != 4) {
            System.err.println("Incorrect number of arguments.");
            usage(progName);
        }

        // initialise search agent
   
        NearestNeigh agent = null;
        switch (args[0]) {
            case "naive":
                agent = new NaiveNN();
                break;
            case "kdtree":
                agent = new KDTreeNN();
                break;
            default:
                System.err.println("Incorrect argument value.");
                usage(progName);
        }
        // read in data file of initial set of points
        String dataFileName = args[1];
        List<Point> points = new ArrayList<Point>();
        try {
            File dataFile = new File(dataFileName);
            Scanner scanner = new Scanner(dataFile);
            while (scanner.hasNext()) {
                String id = scanner.next();
                Category cat = Point.parseCat(scanner.next());
                Point point = new Point(id, cat, scanner.nextDouble(), scanner.nextDouble());
                points.add(point);
            }
            scanner.close();
            long startBuildTime = System.nanoTime();
            agent.buildIndex(points);
            long endBuildTime = System.nanoTime();
            long buildTimeDiff = endBuildTime - startBuildTime;
            System.out.println("build index:" +((double)(buildTimeDiff )) / Math.pow(10, 9) );
        } catch (FileNotFoundException e) {
            System.err.println("Data file doesn't exist.");
            usage(progName);
        }

        String commandFileName = args[2];
        String outputFileName = args[3];
        File commandFile = new File(commandFileName);
        File outputFile = new File(outputFileName);

        // parse the commands in commandFile
        try {
            Scanner scanner = new Scanner(commandFile);
            PrintWriter writer = new PrintWriter(outputFile);

            // operating commands
            while (scanner.hasNext()) {
                String command = scanner.next();
                String id;
                Category cat;
                // remember lat = latitude (approximately correspond to x-coordinate)
                // remember lon = longitude (approximately correspond to y-coordinate)
                double lat;
                double lon;
                int k;
                Point point;
                switch (command) {
                    // search
                    case "S":
                    	long startNNTime = System.nanoTime();
                        cat = Point.parseCat(scanner.next());
                        lat = scanner.nextDouble();
                        lon = scanner.nextDouble();
                        k = scanner.nextInt();
                        point = new Point("searchTerm", cat, lat, lon);
                        List<Point> searchResult = agent.search(point, k);
                        for (Point writePoint : searchResult) {
                            writer.println(writePoint.toString());
                        }
                        long endNNTime = System.nanoTime();
                        System.out.println("Nearest Point:" +((double)( endNNTime - startNNTime)) / Math.pow(10, 9));
                        break;
                    // add
                    case "A":
                    	long startATime = System.nanoTime();
                        id = scanner.next();
                        cat = Point.parseCat(scanner.next());
                        lat = scanner.nextDouble();
                        lon = scanner.nextDouble();
                        point = new Point(id, cat, lat, lon);
                        if (!agent.addPoint(point)) {
                            writer.println("Add point failed.");
                        }
                        long endATime = System.nanoTime();
                        System.out.println("Add Point:" +((double)( endATime - startATime)) / Math.pow(10, 9) );
                        break;
                    // delete
                    case "D":
                    	long startDTime = System.nanoTime();
                        id = scanner.next();
                        cat = Point.parseCat(scanner.next());
                        lat = scanner.nextDouble();
                        lon = scanner.nextDouble();
                        point = new Point(id, cat, lat, lon);
                        if (!agent.deletePoint(point)) {
                            writer.println("Delete point failed.");
                        }
                        long endDTime = System.nanoTime();
                        System.out.println("Delete Point:" +((double)( endDTime - startDTime)) / Math.pow(10, 9) );
                        break;
                    // check
                    case "C":
                    	long startCTime = System.nanoTime();
                        id = scanner.next();
                        cat = Point.parseCat(scanner.next());
                        lat = scanner.nextDouble();
                        lon = scanner.nextDouble();
                        point = new Point(id, cat, lat, lon);
                        writer.println(agent.isPointIn(point));
                        long endCTime = System.nanoTime();
                        System.out.println("Search Point:" +((double)( endCTime - startCTime)) / Math.pow(10, 9) );
                        break;
                    default:
                        System.err.println("Unknown command.");
                        System.err.println(command + " " + scanner.nextLine());
                }
            }
            scanner.close();
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Command file doesn't exist.");
            usage(progName);
        }
    }
}
