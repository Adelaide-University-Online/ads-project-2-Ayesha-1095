/**
 * File: CourseTest.java
 * Description: Unit tests for the Course class covering constructor,
 *              getter, toString, equals and hashCode methods including
 *              edge cases and exception handling.
 * Author: Ayesha Siddiqa
 * Student ID: a3006502
 * Email ID: a3006502@adelaide.edu.au
 * AI Tool Used:
 * This is my own work as defined by
 *    the University's Academic Integrity Policy.
 **/
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Comprehensive unit tests for the Course class.
 * Covers construction, getter access, standard method behaviour,
 * edge cases and exception handling.
 */
public class CourseTest {

    // === Test 1: Constructor sets course code correctly ===

    /**
     * Tests that the constructor correctly stores the course code
     * and that getCourseCode() returns the expected value.
     */
    @Test
    public void testConstructorSetsCourseCode() {
        // create a new course with a standard code
        Course course = new Course("INFT1024");

        // verify course code was stored correctly
        assertEquals("INFT1024", course.getCourseCode()); // code should match
    }

    // === Test 2: Constructor trims whitespace ===

    /**
     * Tests that the constructor trims leading and trailing
     * whitespace from the course code when storing it.
     * This handles codes read from file with extra spaces.
     */
    @Test
    public void testConstructorTrimsCourseCode() {
        // create course with whitespace around code
        Course course = new Course("  COMP1043  ");

        // whitespace should be removed
        assertEquals("COMP1043", course.getCourseCode()); // trimmed code
    }

    // === Test 3: getCourseCode returns correct code ===

    /**
     * Tests that getCourseCode() returns the exact course code
     * that was passed to the constructor.
     */
    @Test
    public void testGetCourseCode() {
        // create a course with a known code
        Course course = new Course("INFS1023");

        // getter should return same code
        assertEquals("INFS1023", course.getCourseCode()); // code returned
    }

    // === Test 4: toString returns course code ===

    /**
     * Tests that toString() returns the course code as the
     * string representation of the course.
     */
    @Test
    public void testToString() {
        // create a course
        Course course = new Course("CURR3021");

        // toString should return the course code directly
        assertEquals("CURR3021", course.toString()); // code as string
    }

    // === Test 5: equals - same course codes are equal ===

    /**
     * Tests that two courses with identical course codes
     * are considered equal.
     */
    @Test
    public void testEqualsTrue() {
        // create two courses with same code
        Course course1 = new Course("INFT1024");
        Course course2 = new Course("INFT1024");

        // courses with same code should be equal
        assertTrue(course1.equals(course2)); // same code equals true
    }

    // === Test 6: equals - different course codes not equal ===

    /**
     * Tests that two courses with different course codes
     * are not considered equal.
     */
    @Test
    public void testEqualsFalse() {
        // create two courses with different codes
        Course course1 = new Course("INFT1024");
        Course course2 = new Course("COMP1043");

        // courses with different codes should not be equal
        assertFalse(course1.equals(course2)); // different codes not equal
    }

    // === Test 7: equals - same object reference ===

    /**
     * Tests the reflexive property of equals.
     * A course must always equal itself.
     */
    @Test
    public void testEqualsSameReference() {
        // create one course
        Course course = new Course("INFT1024");

        // course must equal itself - reflexive property
        assertTrue(course.equals(course)); // same reference always equal
    }

    // === Test 8: equals - null returns false ===

    /**
     * Tests that equals returns false when compared to null.
     * Prevents NullPointerException during graph operations.
     */
    @Test
    public void testEqualsNull() {
        // create a course
        Course course = new Course("INFT1024");

        // comparing to null should return false not crash
        assertFalse(course.equals(null)); // null check returns false
    }

    // === Test 9: equals - different object type returns false ===

    /**
     * Tests that equals returns false when compared to a
     * non-Course object. Ensures type safety in equals.
     */
    @Test
    public void testEqualsDifferentType() {
        // create a course and a string with same value
        Course course = new Course("INFT1024");
        String notACourse = "INFT1024";

        // comparing to different type should return false
        assertFalse(course.equals(notACourse)); // different type not equal
    }

    // === Test 10: hashCode - equal courses same hash ===

    /**
     * Tests that two equal courses produce the same hashCode.
     * Required by Java equals/hashCode contract.
     */
    @Test
    public void testHashCodeEqualCourses() {
        // create two identical courses
        Course course1 = new Course("INFT1024");
        Course course2 = new Course("INFT1024");

        // equal courses must have same hashCode by Java contract
        assertEquals(course1.hashCode(), course2.hashCode()); // same hash
    }

    // === Test 11: hashCode - different courses different hash ===

    /**
     * Tests that two different courses produce different hashCodes.
     * Not guaranteed by contract but expected for good hash function.
     */
    @Test
    public void testHashCodeDifferentCourses() {
        // create two different courses
        Course course1 = new Course("INFT1024");
        Course course2 = new Course("COMP1043");

        // different courses should ideally have different hashCodes
        assertNotEquals(course1.hashCode(), course2.hashCode()); // different hash
    }

    // === Test 12: Edge case - single character code ===

    /**
     * Tests that a single character course code is accepted
     * and stored correctly.
     */
    @Test
    public void testSingleCharacterCode() {
        // create course with minimal valid code
        Course course = new Course("A");

        // single character should be accepted
        assertEquals("A", course.getCourseCode()); // single char stored
    }

    // === Test 13: Edge case - equals after trim ===

    /**
     * Tests that a course created with whitespace equals a course
     * created without whitespace after trimming occurs.
     */
    @Test
    public void testEqualsAfterTrim() {
        // create one course with whitespace and one without
        Course course1 = new Course("  INFT1024  ");
        Course course2 = new Course("INFT1024");

        // both should be equal after trim
        assertTrue(course1.equals(course2)); // trim makes them equal
    }

    // === Test 14: Edge case - course code with numbers ===

    /**
     * Tests that course codes containing numbers are
     * stored and returned correctly.
     */
    @Test
    public void testCourseCodeWithNumbers() {
        // create course with alphanumeric code as used in degree files
        Course course = new Course("COMP2026");

        // alphanumeric code should be stored correctly
        assertEquals("COMP2026", course.getCourseCode()); // numbers in code ok
    }

    // === Test 15: Exception - null course code ===

    /**
     * Tests that IllegalArgumentException is thrown when
     * null is passed as the course code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullCourseCode() {
        // passing null should throw IllegalArgumentException
        new Course(null); // null code not allowed
    }

    // === Test 16: Exception - empty course code ===

    /**
     * Tests that IllegalArgumentException is thrown when
     * an empty string is passed as the course code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCourseCode() {
        // passing empty string should throw IllegalArgumentException
        new Course(""); // empty code not allowed
    }

    // === Test 17: Exception - whitespace only course code ===

    /**
     * Tests that IllegalArgumentException is thrown when
     * a whitespace-only string is passed as the course code.
     * After trimming, the code would be empty which is invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWhitespaceOnlyCourseCode() {
        // whitespace only becomes empty after trim - should throw
        new Course("   "); // whitespace only not allowed
    }

    // === References ===
    //
    // JUnit 4 @Test annotation and assertion methods:
    // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
    //
    // JUnit 4 assertNotEquals - used to verify different courses
    // produce different hash codes:
    // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html#assertNotEquals(long,%20long)
    //
    // JUnit 4 expected exception testing using @Test(expected = ...):
    // https://github.com/junit-team/junit4/wiki/Exception-testing
    //
    // Java equals() and hashCode() contract - equal objects must
    // have equal hash codes, tested in Test 10:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#hashCode--
}
