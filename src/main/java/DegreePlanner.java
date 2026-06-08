/**
 * File: DegreePlanner.java
 * Description: Represents the degree prerequisite graph and implements
 *              the study period planning algorithm. Reads course data
 *              from a text file, builds a directed adjacency list graph,
 *              and groups courses into study periods using a BFS-based
 *              approach with in-degree tracking.
 * Author: Ayesha Siddiqa
 * Student ID: a3006502
 * Email ID: a3006502@adelaide.edu.au
 * AI Tool Used:
 * This is my own work as defined by
 * the University's Academic Integrity Policy.
 **/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Represents the degree prerequisite graph and generates
 * an optimised study plan grouped into study periods.
 *
 * <p>Design decision: Directed graph chosen because prerequisites
 * only go in one direction - completing COMP1043 unlocks INFT1032
 * but not the other way around. An undirected graph would incorrectly
 * imply both courses depend on each other.</p>
 *
 * <p>Design decision: Adjacency list chosen over adjacency matrix
 * because the degree graph is sparse. Both XBIT and XBDA degrees
 * have a density of approximately 4% which is well below the
 * 25% break-even point identified in Koffman and Wolfgang (2016,
 * p.504). XBIT has 21 courses giving V squared of 441 with only
 * 18 actual edges. XBDA has 23 courses giving V squared of 529
 * with only 23 actual edges. An adjacency list saves memory by
 * only storing edges that actually exist.</p>
 *
 * <p>Design decision: Unweighted graph chosen because all
 * prerequisite relationships are equal - one course simply
 * must come before another. There is no distance or cost
 * associated with any edge. A weighted graph would add
 * unnecessary complexity with no benefit for this problem.</p>
 *
 * <p>Algorithm: Breadth-first search (BFS-based) level-by-level
 * traversal using in-degree counting.
 * Courses with in-degree zero have no prerequisites and
 * can be taken immediately. When a course is completed its adjacent
 * courses have their in-degree reduced by one. When any course
 * reaches in-degree zero it is unlocked and added to the next
 * study period. This approach is based on the BFS algorithm
 * from Koffman and Wolfgang (2016) pages 506-510.</p>
 */
public class DegreePlanner {

    // stores all Course objects mapped by their course code
    // e.g. "INFT1024" -> Course object
    // HashMap chosen for O(1) lookup when building graph edges
    private HashMap<String, Course> courses;

    // adjacency list - stores what courses each course unlocks
    // e.g. "COMP1043" -> ["INFT1032", "INFS1029", "COMP1048"]
    // directed graph - edge goes FROM prerequisite TO course
    private HashMap<String, ArrayList<String>> adjacencyList;

    // tracks how many prerequisites each course still needs
    // e.g. "INFT1032" -> 2 (needs COMP1043 and INFT1024)
    // when this reaches 0 the course is ready to be taken
    private HashMap<String, Integer> inDegree;

    // === Constructor ===

    /**
     * Constructs a DegreePlanner by reading course data from a file.
     * Builds the prerequisite graph and initialises all data structures.
     * @param fileName the name of the degree data file to read
     */
    public DegreePlanner(String fileName) {
        // initialise all three data structures
        courses = new HashMap<>();          // course code -> Course object
        adjacencyList = new HashMap<>();    // course code -> unlocked courses
        inDegree = new HashMap<>();         // course code -> prerequisite count

        // read file and build the graph
        loadFromFile(fileName);
    }

    // === Getters ===

    /**
     * Returns the number of courses in the degree.
     * @return the total course count
     */
    public int getCourseCount() {
        return courses.size(); // return total courses loaded
    }

    /**
     * Returns the in-degree of a specific course.
     * Used for testing and verification.
     * @param courseCode the course code to check
     * @return the in-degree value, or -1 if course not found
     */
    public int getInDegree(String courseCode) {
        // check course exists before returning
        if (inDegree.containsKey(courseCode)) {
            return inDegree.get(courseCode); // return prerequisite count
        }
        return -1; // course not found
    }

    /**
     * Returns the adjacency list for a specific course.
     * Used for testing and verification.
     * @param courseCode the course code to check
     * @return list of courses this course unlocks, or empty list
     */
    public ArrayList<String> getAdjacentCourses(String courseCode) {
        // check course exists before returning
        if (adjacencyList.containsKey(courseCode)) {
            return adjacencyList.get(courseCode); // return unlocked courses
        }
        return new ArrayList<>(); // return empty list if not found
    }

    // === Standard Methods ===

    /**
     * Returns a string representation of this DegreePlanner
     * showing the file name and number of courses loaded.
     * @return formatted string summary
     */
    @Override
    public String toString() {
        // show number of courses and adjacency list size
        return "DegreePlanner[courses=" + courses.size()
                + ", edges=" + adjacencyList.size() + "]";
    }

    /**
     * Checks equality based on courses and adjacency list.
     * Two DegreePlanners are equal if they have the same
     * graph structure.
     * @param o the object to compare
     * @return true if graph structures match
     */
    @Override
    public boolean equals(Object o) {
        // if comparing to itself always equal
        if (this == o) {
            return true;
        }
        // if null or different class not equal
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        // cast and compare graph structures
        DegreePlanner other = (DegreePlanner) o;
        return courses.equals(other.courses)
                && adjacencyList.equals(other.adjacencyList)
                && inDegree.equals(other.inDegree);
    }

    /**
     * Returns hash code based on graph structure.
     * Required by Java contract when equals() is overridden.
     * @return hash code integer
     */
    @Override
    public int hashCode() {
        // prime multiplier reduces collisions in hash calculations
        final int prime = 31;
        int result = courses.hashCode();
        result = prime * result + adjacencyList.hashCode();
        result = prime * result + inDegree.hashCode();
        return result;
    }

    // === File Reading ===

    /**
     * Reads course and prerequisite data from the degree text file.
     * Builds the adjacency list graph and calculates in-degrees.
     * File format: line 1 lists all courses, subsequent lines list
     * each course followed by its prerequisites separated by commas.
     *
     * <p>File reading approach uses Scanner with File as taught in
     * Koffman and Wolfgang (2016) Appendix A.10, page 599.</p>
     *
     * @param fileName the name of the degree data file
     */
    private void loadFromFile(String fileName) {
        // create scanner connected to file
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException ex) {
            // print error and exit if file not found
            System.err.println("[ERROR] File not found: " + fileName);
            System.exit(1);
        }

        // === PART 1 - Read line 1 to get all course codes ===
        if (scanner.hasNextLine()) {
            String firstLine = scanner.nextLine(); // read first line

            // split line by comma to get individual course codes
            String[] courseCodes = firstLine.split(",");

            // create a Course object for each code in line 1
            for (int i = 0; i < courseCodes.length; i++) {
                String code = courseCodes[i].trim(); // trim whitespace

                // create Course object and store in HashMap
                courses.put(code, new Course(code));

                // initialise empty adjacency list for this course
                // this course has not unlocked anything yet
                adjacencyList.put(code, new ArrayList<>());

                // initialise in-degree to 0 - no prerequisites found yet
                inDegree.put(code, 0);
            }
            System.out.println("[INFO] Loaded " + courses.size()
                    + " courses from " + fileName);
        }

        // === PART 2 - Read remaining lines for prerequisites ===
        // each line: course code, prereq1, prereq2...
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine(); // read next line

            // skip empty lines
            if (line.trim().isEmpty()) {
                continue; // move to next line
            }

            // split line by comma to get course and prerequisites
            String[] parts = line.split(",");

            // first item is the course itself
            String courseCode = parts[0].trim();

            // remaining items are prerequisites (if any exist)
            // loop starts at index 1 to skip the course itself
            for (int i = 1; i < parts.length; i++) {
                String prereqCode = parts[i].trim(); // trim whitespace

                // add courseCode to prereq's adjacency list
                // meaning: completing prereq unlocks courseCode
                if (adjacencyList.containsKey(prereqCode)) {
                    adjacencyList.get(prereqCode).add(courseCode);
                }

                // increase courseCode's in-degree by 1
                // meaning: courseCode needs one more prerequisite
                if (inDegree.containsKey(courseCode)) {
                    inDegree.put(courseCode,
                            inDegree.get(courseCode) + 1);
                }
            }
        }

        // close scanner after reading
        scanner.close();
        System.out.println("[INFO] Graph built successfully");
    }
    // === Algorithm ===

    /**
     * Generates and prints an optimised study plan grouped into
     * study periods using a BFS-based level-by-level traversal
     * with in-degree counting.
     *
     * <p>Algorithm: Based on BFS from Koffman and Wolfgang (2016)
     * pages 506-510. Courses with in-degree zero are identified
     * first and placed in a Queue. Each study period takes up to
     * maxCoursesPerPeriod courses from the Queue. Completing a
     * course reduces the in-degree of courses it unlocks. When
     * any course reaches in-degree zero it is added to the Queue
     * for the next study period.</p>
     *
     * @param maxCoursesPerPeriod maximum courses per study period
     */
    public void generateStudyPlan(int maxCoursesPerPeriod) {
        // validate input - must be at least 1 course per period
        if (maxCoursesPerPeriod < 1) {
            throw new IllegalArgumentException(
                    "Must take at least 1 course per period");
        }

        // make working copy of inDegree so original graph is unchanged
        // allows generateStudyPlan to be called multiple times safely
        HashMap<String, Integer> remainingDegree =
                new HashMap<>(inDegree);

        // create Queue to store courses ready to be taken
        // Queue chosen because BFS visits nodes level by level
        // LinkedList implements Queue interface in Java
        Queue<String> readyQueue = new LinkedList<>();

        // === STEP 1 - add all courses with no prerequisites to Queue ===
        // these courses can be taken immediately in any order
        for (String code : remainingDegree.keySet()) {
            if (remainingDegree.get(code) == 0) {
                readyQueue.offer(code); // add to back of Queue
            }
        }

        // track which study period we are currently filling
        int periodNumber = 1;

        // === STEP 2 - BFS level by level until all courses scheduled ===
        while (!readyQueue.isEmpty()) {
            // list to hold courses for this study period
            ArrayList<String> currentPeriod = new ArrayList<>();

            // take up to maxCoursesPerPeriod courses from Queue
            // each course taken = one course in this study period
            int coursesThisPeriod = 0;
            while (!readyQueue.isEmpty() && coursesThisPeriod < maxCoursesPerPeriod) {
                // remove first course from Queue - FIFO order
                String course = readyQueue.poll();
                currentPeriod.add(course);  // add to current period
                coursesThisPeriod++;        // increment counter
            }

            // === STEP 3 - process completed courses ===
            // for each course completed this period
            // find what it unlocks and update inDegrees
            for (String completedCourse : currentPeriod) {
                // get list of courses this course unlocks
                ArrayList<String> unlocked = adjacencyList.get(completedCourse);

                // reduce inDegree of each unlocked course by 1
                for (String unlockedCourse : unlocked) {
                    int newDegree = remainingDegree.get(unlockedCourse) - 1;
                    remainingDegree.put(unlockedCourse, newDegree);

                    // if inDegree reaches 0 course is ready to take
                    if (newDegree == 0) {
                        readyQueue.offer(unlockedCourse); // add to Queue
                    }
                }
            }

            // === STEP 4 - print this study period ===
            System.out.print("Study Period " + periodNumber + ": ");
            for (int i = 0; i < currentPeriod.size(); i++) {
                System.out.print(currentPeriod.get(i));
                if (i < currentPeriod.size() - 1) {
                    System.out.print(", "); // comma between courses
                }
            }
            System.out.println(); // new line after each period

            periodNumber++; // move to next study period
        }

        System.out.println("[INFO] Study plan complete - "
                + (periodNumber - 1) + " study periods total");
    }


    // === References ===
    //
    // Java HashMap - chosen for O(1) lookup by course code key.
    // HashMap(Map m) copy constructor used to create working copy
    // of inDegree so original graph remains unchanged.
    // Covered in Week 6 lecture material COMP2026 2026:
    // https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
    //
    // Java ArrayList - used inside adjacency list to store
    // lists of unlocked courses for each vertex:
    // https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
    //
    // Directed graph and adjacency list design decisions discussed
    // in Week 9 lecture material COMP2026 2026.
    // Koffman, E. B. (2016). Data Structures: Abstraction and Design
    // using Java (3rd ed., Section 10.3). John Wiley and Sons.
    //
    // BFS algorithm - Koffman, E. B. (2016). Data Structures:
    // Abstraction and Design using Java (3rd ed., pp. 506-510).
    // John Wiley and Sons.
    //
    // Java Scanner with File - used to read degree data from text file.
    // Scanner(File) constructor, hasNextLine() and nextLine() methods:
    // Koffman, E. B. (2016). Data Structures: Abstraction and Design
    // using Java (3rd ed., Appendix A.10, pp. 596-601). John Wiley and Sons.
    // https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
    //
    // Java Queue interface and LinkedList implementation -
    // Queue chosen for BFS as confirmed by Koffman and Wolfgang
    // (2016, p. 507): "To ensure identified vertices are visited
    // in correct sequence, we store them in a queue":
    // https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html
    // https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html
    //
    // Java Object equals() and hashCode() contract - implemented
    // to satisfy Java contract when equals() is overridden:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-
}
