package astonTravel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for providing a method to calculate distance between two
 * locations as specified in longitude and latitude.
 *
 * @author Hai Wang
 * @version 30/11/2023
 */
public class Utility 
{

	/**
	 * Calculates the distance between two locations using the Haversine formula.
	 *
	 * @param lat1 Latitude of the first location.
	 * @param lon1 Longitude of the first location.
	 * @param lat2 Latitude of the second location.
	 * @param lon2 Longitude of the second location.
	 * @return The distance in kilometers between the two locations.
	 */
	public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) 
	{
		// Radius of the Earth in kilometers
		final double R = 6371.0;

		// Convert latitude and longitude from degrees to radians
		double lat1Rad = Math.toRadians(lat1);
		double lon1Rad = Math.toRadians(lon1);
		double lat2Rad = Math.toRadians(lat2);
		double lon2Rad = Math.toRadians(lon2);

		// Haversine formula for calculating distance
		double dlon = lon2Rad - lon1Rad;
		double dlat = lat2Rad - lat1Rad;
		double a = Math.pow(Math.sin(dlat / 2), 2)
				+ Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		return distance;
	}

	/**
	 * Write the specified data to a default text file named "distances.txt".
	 *
	 * @param data	Data to be written into the "distances.txt" file
	 * @throws IOException If there's an issue with file writing
	 */
	public static void saveFile(String data) throws IOException 
	{
		// Create a BufferedWriter to write to the "distances.txt" file
		BufferedWriter writer = new BufferedWriter(new FileWriter("distances.txt"));
		writer.write(data); // Write the provided data to the file

		writer.close(); // Close the BufferedWriter to release resources
	}
	
	/**
	 * Read data from a CSV file and convert it into a list of Destination objects.
	 *
	 * @param filePath The path to the CSV file to be read
	 * @return A list of Destination objects parsed from the CSV file
	 * @throws IOException If there's an issue with file reading
	 */
	public static List<Destination> readDestinationsFromCSV(String filePath) throws IOException 
	{
	    List<Destination> destinations = new ArrayList<>();

	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
	    {
	        String line;
	        br.readLine(); // Skip the header line
	        while ((line = br.readLine()) != null) 
	        {
	            System.out.println("Processing line: " + line); // Debug statement

	            List<String> parts = parseCSVLine(line);

	            // Extracting data from CSV parts
	            String name = parts.get(0).trim();
	            String description = parts.get(1).trim();
	            double latitude = Double.parseDouble(parts.get(2).trim());
	            double longitude = Double.parseDouble(parts.get(3).trim());
	            String cost = parts.get(4).trim();

	            // Create a Destination object and add it to the list
	            Destination destination = new Destination(name, description, latitude, longitude, cost);
	            destinations.add(destination);
	        }
	    }

	    return destinations; // Return the list of Destination objects
	}

	// Helper method to parse a CSV line considering quoted strings
	private static List<String> parseCSVLine(String line) 
	{
	    List<String> parts = new ArrayList<>();
	    StringBuilder currentPart = new StringBuilder();

	    boolean inQuotes = false;

	    for (char c : line.toCharArray()) 
	    {
	        if (c == '"') {
	            inQuotes = !inQuotes; // Toggle the inQuotes flag when encountering a quote
	        } else if (c == ',' && !inQuotes) 
	        {
	            parts.add(currentPart.toString()); // Add the current part to the list when a comma is encountered outside quotes
	            currentPart.setLength(0); // Reset the currentPart StringBuilder
	        } else 
	        {
	            currentPart.append(c); // Append the character to the currentPart StringBuilder
	        }
	    }

	    parts.add(currentPart.toString()); // Add the last part after the loop completes

	    return parts; // Return the list of CSV parts
	}

}
