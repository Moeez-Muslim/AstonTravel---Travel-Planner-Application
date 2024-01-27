/**
 * 
 */
package astonTravel;

/**
 * A controller for the UK Travel Planner system required by AstonTravel.
 * This controller includes the 4 system features that the intended prototype
 * UK Travel Planner system for AstonTravel is expected to have.
 *
 * @author Hai Wang and Sylvia Wong
 * @version 30/11/2023
 */
public interface Controller 
{

	/**
	 * Lists information about either all destination cities or a specific city.
	 * 
	 * @param destination	"all" for listing information about all destination cities;
	 *                      specific city name, e.g. "London", for listing information about
	 *                      the specified city.
	 * @return a String representation of the information about either all 
	 *         destination cities or a specific city,
	 *         presented by the order of name.
	 */
	String listDestinations(String destination);

	/**
	 * List destinations within a distance specified by users, relative to a 
	 * location users provide via latitude and longitude coordinates. The results 
	 * are ranked by cost, with lower-cost cities appearing first. In the case of 
	 * cities with the same cost, the ranking is determined by their distance from the
	 * specified location
	 * 
	 * @param latitude	the latitude coordinate of the user-specified location
	 * @param longitude	the longitude coordinate of the user-specified location
	 * @param distance	the distance relative to the given location
	 * @return a String representation of the of the information of the retrieved cities.
	 */
	String listNearbyDestinations(double latitude, double longitude, int distance);

	/**
	 * Compute and store distances between any two tourist destinations. 
	 * The result will be print out and saved in a data file
	 * 
	 * @return a String representation of the distances between any two destinations.
	 */
	String listDistances();

	/**
	 * List the shortest travel path that covers all the desired cities.
	 * 
	 * @param cities2visit	an array storing the cities a traveller would like to visit
	 * @return a String representation the shortest travel path that covers all the 
	 * desired cities.
	 */
	String listShortestPath(String[] cities2visit);

}
