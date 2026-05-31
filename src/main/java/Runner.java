/**
 * File: Runner.java
 * Description: Entry point for the OptiTime degree planning tool.
 *              Reads a degree file name and maximum courses per period
 *              from the user, then outputs an optimised study plan
 *              grouped by study period.
 * Author: Ayesha Siddiqa
 * Student ID: a3006502
 * Email ID: a3006502@adelaide.edu.au
 * AI Tool Used:
 * This is my own work as defined by
 *    the University's Academic Integrity Policy.
 **/
import java.util.Scanner;

/**
 * Entry point for the OptiTime degree planning tool.
 * Reads user input and generates an optimised study plan.
 *
 * <p>Design decision: User input collected via Scanner reading
 * from System.in - same approach used throughout this course
 * for console input. Input validation ensures sensible values
 * before passing to DegreePlanner.</p>
 */

public class Runner {

    /**
     * Main entry point for the OptiTime tool.
     * Asks user for degree file and courses per period,
     * then generates and prints the study plan.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Welcome to OptiTime Degree Planner ===");
        System.out.println();

        // create scanner for user input
        Scanner scanner = new Scanner(System.in);

        // === Step 1 - ask for degree file name ===
        System.out.print("Enter degree file name (e.g. XBIT.txt): ");
        String fileName = scanner.next(); // read file name

        // === Step 2 - ask for max courses per period ===
        System.out.print("Enter maximum courses per study period: ");

        // validate input is a positive number
        int maxCourses = 0;
        while (maxCourses < 1) {
            try {
                maxCourses = scanner.nextInt(); // read number
                // check number is at least 1
                if (maxCourses < 1) {
                    System.out.println("[ERROR] Please enter a number greater than 0");
                    System.out.print("Enter maximum courses per study period: ");
                }
            } catch (Exception e) {
                // handle non-numeric input
                System.out.println("[ERROR] Please enter a valid number");
                System.out.print("Enter maximum courses per study period: ");
                scanner.next(); // clear invalid input
            }
        }

        System.out.println();

        // === Step 3 - build graph and generate study plan ===
        System.out.println("[INFO] Loading degree data from "
                + fileName + "...");
        DegreePlanner planner = new DegreePlanner(fileName);

        // print study plan header
        System.out.println();
        System.out.println("=== Study Plan for " + fileName
                + " (max " + maxCourses
                + " courses per period) ===");
        System.out.println();

        // generate and print the study plan
        planner.generateStudyPlan(maxCourses);

        // close scanner when done
        scanner.close(); // release scanner resource
    }

    // === References ===
    //
    // Java Scanner class - used for reading user input from console.
    // Scanner(InputStream) constructor connects to System.in for
    // keyboard input as described in Koffman and Wolfgang (2016)
    // Appendix A.10, p.596:
    // https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
    //
    // Java Scanner.nextInt() - reads integer input from user:
    // https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html#nextInt--
    //
    // Java try/catch - handles non-numeric Scanner input.
    // try-catch sequence described in Koffman and Wolfgang (2016)
    // Appendix A.10, p.599:
    // https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html
    //
    // Input validation loop - while loop used to re-prompt user
    // until a valid positive integer is entered.
}
