/**
 * File: Course.java
 * Description: Represents a single course node in the degree
 *              planning graph. Each course has a unique course code
 *              that identifies it within the degree structure.
 * Author: Ayesha Siddiqa
 * Student ID: a3006502
 * Email ID: a3006502@adelaide.edu.au
 * AI Tool Used:
 * This is my own work as defined by
 * the University's Academic Integrity Policy.
 **/

/**
 * Represents a single course within a degree program.
 * Used as a vertex in the prerequisite graph structure.
 *
 * <p>Design decision: Course stores only the course code as its
 * identifier. All prerequisite relationships are managed by the
 * DegreePlanner graph class rather than within Course itself.
 * This keeps Course simple and focused — matching the single
 * responsibility principle.</p>
 */

public class Course {

    // unique course code e.g. INFT1024
    private String courseCode;

    // === Constructor ===

    /**
    * Construct a Course with the given course code.
    * @param courseCode the unique code identifying this course
    */
    public Course(String courseCode) {
        // validate course code is not null or empty
        if (courseCode == null || courseCode.isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be null or empty");
        }
        this.courseCode = courseCode.trim(); // store trimmed code
    }

    // === Getters ===

    /**
    * Returns the course code for this course.
    * @return the course code string
    */
    public String getCourseCode() {
        return courseCode;  // return the course code
    }

    // === Standard Methods ===

    /**
    * Returns a string representation of this course.
    * @return the course code as the string representation
    */
    @Override
    public String toString() {
        return courseCode;  // course code is the natural display
    }

    /**
     * Checks equality based on course code only.
     * Two courses are equal if they have the same course code.
     * @param o the object to compare
     * @return true if course codes match
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
        // cast and compare course codes
        Course other = (Course) o;
        return courseCode.equals(other.courseCode); // compare codes
    }

    /**
     * Returns hash code based on course code.
     * Required by Java contract when equals() is overridden.
     * @return hash code integer
     */
    @Override
    public int hashCode() {
        // prime multiplier reduces collisions in hash calculations
        final int prime = 31;
        return prime * courseCode.hashCode(); // hash based on code
    }

    // === References ===
    //
    // Java String.trim() - used to remove whitespace from
    // course codes read from file:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#trim--
    //
    // Java Object equals() and hashCode() contract - equals() takes
    // Object o as parameter because it overrides Object.equals() which
    // accepts any object type. The cast to Course happens after the
    // type check:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-
    //
    // Java IllegalArgumentException - used to validate course
    // code is not null or empty on construction:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html
    //
    // Java Object.getClass() - built-in method inherited from Object
    // class, used in equals() to verify both objects are the same
    // type before comparing fields:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#getClass--
}
