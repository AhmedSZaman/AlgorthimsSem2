package generation;
import java.util.Random;
import java.util.Date;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class DataGenerator {

	public static void main(String[] args) {
		Date currentDate = new Date();
		Random generator = new Random(currentDate.getTime());
		int datasetSize = 10000;
		final double latMin = -39.0;
		final double latMax = -36.0;
		final double lonMin = 141.0;
		final double lonMax = 149.0;
		
		try (PrintWriter writer = new PrintWriter(new File("largeDataset.txt"))) {
			for (int i = 0; i < datasetSize; i ++) {
				double newLat = latMin + (latMax - latMin) * generator.nextDouble();
				double newLon = lonMin + (lonMax - lonMin) * generator.nextDouble();
				String category;
				double newDouble = generator.nextDouble();
				if (newDouble <= 1.0/3.0) {
					category = "restaurant";
				} else if (newDouble <= 2.0/3.0) {
					category = "hospital";
				} else {
					category = "education";
				}
				writer.println(String.format("id%d %s %.10f %.10f", i, category, newLat, newLon));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
