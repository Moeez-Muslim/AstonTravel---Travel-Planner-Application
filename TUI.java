/**
 * 
 */
package astonTravel;

import java.util.Scanner;

/**
 * A simple text-based user interface for the AstonTravel system -- for testing purpose.
 * 
 * @author Sylvia Wong and Hai Wang
 * @version 30/11/2023
 */

public class TUI 
{

    private Controller controller;
    private Scanner stdIn;

    public TUI(Controller controller) 
    {
        this.controller = controller;
        
        // Creates a Scanner object for obtaining user input
        stdIn = new Scanner(System.in);
        displayMenu();
        getAndProcessUserOption();
    }

    /**
	 * Displays the header of this application and a summary of menu options.
	 */
    private void displayMenu() 
    {
        System.out.println(header());
        System.out.println(menu());
    }

    /**
	 * Obtains an user option and processes it.
	 */
    private void getAndProcessUserOption() {
        String command;

        do {
            command = stdIn.nextLine().trim();

            switch (command) {
            case "1":   // List information about either all destination cities or a specific city.
                System.out.println("List information about either all destination cities or a specific city...");

                String specificCity;
                do {
                    System.out.println("Enter a specific city or 'all':");
                    specificCity = stdIn.nextLine().trim();

                    if ("all".equalsIgnoreCase(specificCity)) {
                        // List information about all destination cities
                        System.out.println(controller.listDestinations("all"));
                        break;
                    } else {
                        // List information about the specific city
                        String cityInfo = controller.listDestinations(specificCity);
                        if (!cityInfo.equals("Destination not found")) {
                            System.out.println(cityInfo);
                            break;
                        } else {
                            System.out.println("Error: Destination not found. Please enter a valid city name.");
                        }
                    }
                } while (true);
                break;

                case "2":   // List nearby destination cities.
                    System.out.println("List nearby destination cities of a location....");

                    double latitude, longitude;
                    int distance;

                    do {
                        System.out.println("Enter current location (Latitude Longitude):");
                        String[] coordinates = stdIn.nextLine().split("\\s+");
                        
                        try {
                            if (coordinates.length == 2) {
                                latitude = Double.parseDouble(coordinates[0]);
                                longitude = Double.parseDouble(coordinates[1]);
                                break;  // exit the loop if input is valid
                            } else {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter valid numeric values for latitude and longitude.");
                        }
                    } while (true);


                    do {
                        System.out.println("Enter distance:");
                        try {
                            distance = Integer.parseInt(stdIn.nextLine());
                            break;  // exit the loop if input is valid
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid integer for distance.");
                        }
                    } while (true);

                    System.out.println(controller.listNearbyDestinations(latitude, longitude, distance));
                    break;
                case "3":   // List and store the distances between any two destinations
                    System.out.println("List and store the distances between any two destinations...");
                    System.out.println(controller.listDistances());
                    break;
                case "4":   // List the shortest travel path that covers all the desired cities.
                    System.out.println("List the shortest travel path that covers all the desired cities...");

                    String[] cities;

                    do {
                        System.out.println("Enter the cities to visit (comma-separated):");
                        cities = stdIn.nextLine().split(",");

                        boolean validInput = true;
                        for (String city : cities) 
                        {
                            if (!city.trim().matches("[a-zA-Z ]+")) 
                            {
                                validInput = false;
                                System.out.println("Invalid input. City names should contain only alphabets and spaces.");
                                break;
                            }
                        }

                        if (validInput && cities.length > 1) 
                        {
                            break;  // exit the loop if input is valid
                        } else if (cities.length <= 1) 
                        {
                            System.out.println("Invalid input. Please enter at least two cities.");
                        }
                    } while (true);

                    System.out.println(controller.listShortestPath(cities));
                    break;

                case "5":   // Exits the application
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:    // Not a known command option
                    System.out.println(unrecogniseCommandErrorMsg(command));
            }
        } while (true);
    }

    

    /*
	 * Returns a string representation of a brief title for this application as the header.
	 * @return	a header
	 */
    private static String header() 
    {
        return "\n AstonTravel System\n";
    }

    
    /*
	 * Returns a string representation of the user menu.
	 * @return	the user menu
	 */
    private static String menu() 
    {
        return "Enter the number associated with your chosen menu option.\n" +
                "1: Lists information about either all destination cities or a specific city.\n" +
                "2: List nearby destination cities of a location.\n" +
                "3: List and store the distances between any two destinations. \n" +
                "4: List the shortest travel path that covers all the desired cities.\n" +
                "5: Exit this application\n";
    }

    
    /*
     * Returns an error message for an unrecognised command.
     * 
     * @param error the unrecognised command
     * @return      an error message
     */
    private static String unrecogniseCommandErrorMsg(String error) 
    {
        return String.format("Cannot recognise the given command: %s.%n", error);
    }
}