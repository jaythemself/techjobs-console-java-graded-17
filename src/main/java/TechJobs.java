import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by LaunchCode
 */
public class TechJobs {

    static Scanner in = new Scanner(System.in);

    public static void main (String[] args) {

        // Initialize our field map with key/name pairs
        HashMap<String, String> columnChoices = new HashMap<>();
        columnChoices.put("core competency", "Skill");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("position type", "Position Type");
        columnChoices.put("all", "All");

        // Top-level menu options
        HashMap<String, String> actionChoices = new HashMap<>();
        actionChoices.put("search", "Search");
        actionChoices.put("list", "List");

        System.out.println("Welcome to LaunchCode's TechJobs App!");

        // Allow the user to search until they manually quit
        while (true) {

            String actionChoice = getUserSelection("View jobs by (type 'x' to quit):", actionChoices);

            if (actionChoice == null) {
                break;
            } else if (actionChoice.equals("list")) {

                String columnChoice = getUserSelection("List", columnChoices);

                if (columnChoice.equals("all")) {
                    printJobs(JobData.findAll());
                } else {

                    ArrayList<String> results = JobData.findAll(columnChoice);

                    System.out.println("\n*** All " + columnChoices.get(columnChoice) + " Values ***");

                    // Print list of skills, employers, etc
                    for (String item : results) {
                        System.out.println(item);
                    }
                }

            } else { // choice is "search"

                // How does the user want to search (e.g. by skill or employer)
                String searchField = getUserSelection("Search by:", columnChoices);

                // What is their search term? Gets user input
                System.out.println("\nSearch term:");

                // Initially used IgnoreCase instead of toLowerCase, became inapplicable when covering all potential search terms in later tasks
                // (conflicted when changing findByColumnAndValue/findByValue in JobData/with partial searches)
                // Takes user input and alters it to compare nicely to JobData
                String searchTerm = in.nextLine().toLowerCase();

                // Searches every column for either partial or full occurrence of search term
                if (searchField.equals("all")) {
                    printJobs(JobData.findByValue(searchTerm));
                // Searches specified column for either partial or full occurrence of search term
                } else {
                    printJobs(JobData.findByColumnAndValue(searchField, searchTerm));
                }
            }
        }
    }

    //Returns the key of the selected item from the choices Dictionary
    private static String getUserSelection(String menuHeader, HashMap<String, String> choices) {

        int choiceIdx = -1;
        Boolean validChoice = false;
        String[] choiceKeys = new String[choices.size()];

        // Put the choices in an ordered structure so that we can associate an integer with each one
        int i = 0;
        for (String choiceKey : choices.keySet()) {
            choiceKeys[i] = choiceKey;
            i++;
        }

        do {

            System.out.println("\n" + menuHeader);

            // Print available choices
            for (int j = 0; j < choiceKeys.length; j++) {
                System.out.println("" + j + " - " + choices.get(choiceKeys[j]));
            }

            if (in.hasNextInt()) {
                choiceIdx = in.nextInt();
                in.nextLine();
            } else {
                String line = in.nextLine();
                boolean shouldQuit = line.equals("x");
                if (shouldQuit) {
                    return null;
                }
            }

            // Validate user's input
            if (choiceIdx < 0 || choiceIdx >= choiceKeys.length) {
                System.out.println("Invalid choice. Try again.");
            } else {
                validChoice = true;
            }

        } while(!validChoice);

        return choiceKeys[choiceIdx];
    }

    // Print a list of jobs
    private static void printJobs(ArrayList<HashMap<String, String>> pertinentJobs) {

        // (Original pertinentJobs was someJobs, changed to be more specific)
        // Checks to see if any jobs match user search, and if there are none prints "No Results"
        if (pertinentJobs.isEmpty()) {
            System.out.print("No Results");
        // If there ARE jobs that match what the user is looking for, prints them in requested format
            // Failed tests bc put \n at end of 142 println instead of beginning of 136 println
        } else {
            // Iterate through JobData. Nested to first look through array, then hashmaps
            for (HashMap<String, String> job : pertinentJobs) {
                System.out.println("\n*****");
                    // Hashmaps nested for looping
                    for (String key : job.keySet()) {
                        String value = job.get(key);
                        System.out.println(key + ": " + value);
                    }
                System.out.println("*****");
            }
        }
    }
}