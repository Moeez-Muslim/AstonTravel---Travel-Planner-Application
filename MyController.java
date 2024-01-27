package astonTravel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.*;

public class MyController implements Controller 
{

    private List<Destination> destinations;
    private double[][] distanceMatrix;

    // Constructor for MyController class
    public MyController() 
    {
        // Replace "your_file_path/destinations.csv" with the actual path to your CSV file
        String filePath = "C:\\Programming\\JAVA\\TravelPlanner\\src\\astonTravel\\destinations.csv";

        try 
        {
            // Read destinations from CSV and populate the list
            destinations = Utility.readDestinationsFromCSV(filePath);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    public String listDestinations(String destination) 
    {
        if ("all".equalsIgnoreCase(destination)) 
        {
            // List information about all destination cities
            return formatDestinations(destinations);
        } 
        else 
        {
            // List information about the specific city
            List<Destination> filteredDestinations = destinations.stream()
                    .filter(d -> d.getName().equalsIgnoreCase(destination))
                    .collect(Collectors.toList());

            if (!filteredDestinations.isEmpty()) 
            {
                return formatDestinations(filteredDestinations);
            }
            else 
            {
                return "Destination not found";
            }
        }
    }

    // Helper method to format a list of destinations for display
    private String formatDestinations(List<Destination> destinationList) 
    {
        StringBuilder result = new StringBuilder("Name\t\tDescription\t\t\tLatitude\tLongitude\tCost\n");

        for (Destination destination : destinationList) 
        {
            result.append(String.format("%s\t%s\t%.4f\t%.4f\t%s%n",
                    destination.getName(),
                    destination.getDescription(),
                    destination.getLatitude(),
                    destination.getLongitude(),
                    destination.getCost()));
        }

        return result.toString();
    }

    @Override
    public String listNearbyDestinations(double latitude, double longitude, int distance) {
        // Calculate distances and filter destinations within the specified distance
        List<Destination> nearbyDestinations = destinations.stream()
                .filter(d -> Utility.calculateDistance(latitude, longitude, d.getLatitude(), d.getLongitude()) <= distance)
                .collect(Collectors.toList());

        // Sort the filtered destinations by cost and then by distance
        Comparator<Destination> comparator = Comparator.comparing(Destination::getCost)
                .thenComparing(d -> Utility.calculateDistance(latitude, longitude, d.getLatitude(), d.getLongitude()));

        nearbyDestinations.sort(comparator);

        // Format the result
        StringBuilder result = new StringBuilder("Name\t\tCost\t\tDistance to current location (KM)\n");

        for (Destination destination : nearbyDestinations) {
            double distanceToLocation = Utility.calculateDistance(latitude, longitude, destination.getLatitude(), destination.getLongitude());

            result.append(String.format("%-16s%-16s%.2f%n",
                    destination.getName(),
                    destination.getCost(),
                    distanceToLocation));
        }

        return result.toString();
    }


    @Override
    public String listDistances() 
    {
        // Compute distances and store in the distanceMatrix
        computeDistances();

        // Print distances
        StringBuilder result = new StringBuilder("Distances between tourist destinations:\n");

        for (int i = 0; i < destinations.size(); i++) 
        {
            for (int j = 0; j < destinations.size(); j++) 
            {
                if (i != j) 
                {
                    result.append(String.format("%s to %s: %.2f km%n",
                            destinations.get(i).getName(),
                            destinations.get(j).getName(),
                            distanceMatrix[i][j]));
                }
            }
        }

        // Save distances in a data file
        try 
        {
            saveDistancesToFile(result.toString());
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        return result.toString();
    }

    // Helper method to compute distances between destinations and save them to a file
    private void computeDistances() 
    {
        int size = destinations.size();
        distanceMatrix = new double[size][size];

        for (int i = 0; i < size; i++) 
        {
            for (int j = 0; j < size; j++) 
            {
                if (i != j) 
                {
                    double distance = Utility.calculateDistance(
                            destinations.get(i).getLatitude(),
                            destinations.get(i).getLongitude(),
                            destinations.get(j).getLatitude(),
                            destinations.get(j).getLongitude()
                    );

                    distanceMatrix[i][j] = distance;
                }
            }
        }
    }

    // Helper method to save distances to a file
    private void saveDistancesToFile(String data) throws IOException 
    {
        Utility.saveFile(data);
    }

    @Override
    public String listShortestPath(String[] cities2visit) 
    {
        // Compute distances and store in the distanceMatrix
        computeDistances();

        // Find the shortest path using Dijkstra's algorithm
        List<String> shortestPath = findShortestPath(cities2visit);

        // Format the result
        StringBuilder result = new StringBuilder("Travel Path (total XX miles):\n");

        double totalDistance = 0.0;
        for (int i = 0; i < shortestPath.size() - 1; i++) 
        {
            String currentCity = shortestPath.get(i);
            String nextCity = shortestPath.get(i + 1);

            double distanceToNextStop = distanceMatrix[destinations.indexOf(getDestinationByName(currentCity))]
                    [destinations.indexOf(getDestinationByName(nextCity))];

            totalDistance += distanceToNextStop;

            result.append(String.format("%s –(%.2f miles)-> %s%n", currentCity, distanceToNextStop, nextCity));
        }

        result.insert(result.indexOf("XX"), String.format("%.2f", totalDistance));

        return result.toString();
    }

    // Helper method to find the shortest path using Dijkstra's algorithm
    private List<String> findShortestPath(String[] cities2visit) 
    {
        List<String> cities = Arrays.asList(cities2visit);

        // Validate city names
        for (String city : cities) 
        {
            if (getDestinationByName(city) == null) 
            {
                System.out.println("Invalid city name: " + city);
                return Collections.emptyList(); // Return an empty list to indicate an error
            }
        }

        int start = destinations.indexOf(getDestinationByName(cities.get(0)));
        int end = destinations.indexOf(getDestinationByName(cities.get(cities.size() - 1)));

        // Dijkstra's algorithm
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, Integer> predecessors = new HashMap<>();
        Set<Integer> unvisited = new HashSet<>();

        for (int i = 0; i < destinations.size(); i++) {
            distances.put(i, Double.MAX_VALUE);
            predecessors.put(i, null);
            unvisited.add(i);
        }

        distances.put(start, 0.0);

        while (!unvisited.isEmpty()) {
            int current = getClosestUnvisited(distances, unvisited);
            unvisited.remove(current);

            for (int neighbor : unvisited) {
                double newDistance = distances.get(current) + distanceMatrix[current][neighbor];

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, current);
                }
            }
        }

        // Reconstruct the path
        List<String> path = new ArrayList<>();
        Integer current = end;
        while (current != null) {
            path.add(destinations.get(current).getName());
            current = predecessors.get(current);
        }
        Collections.reverse(path);

        return path;
    }


    // Helper method to get the closest unvisited vertex
    private int getClosestUnvisited(Map<Integer, Double> distances, Set<Integer> unvisited) 
    {
        double minDistance = Double.MAX_VALUE;
        int closest = -1;

        for (int vertex : unvisited) 
        {
            if (distances.get(vertex) < minDistance) 
            {
                minDistance = distances.get(vertex);
                closest = vertex;
            }
        }

        return closest;
    }

    // Helper method to get a destination by name
    private Destination getDestinationByName(String name) 
    {
        for (Destination destination : destinations) 
        {
            if (destination.getName().equalsIgnoreCase(name)) 
            {
                return destination;
            }
        }
        return null; // Handle not found case
    }
}
