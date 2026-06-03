/**
 * File: DegreePlannerTest.java
 * Description: Unit tests for the DegreePlanner class covering
 *              constructor, file loading, graph building, BFS
 *              algorithm, standard methods and exception handling.
 * Author: Ayesha Siddiqa
 * Student ID: a3006502
 * Email ID: a3006502@adelaide.edu.au
 * AI Tool Used:
 * This is my own work as defined by
 *    the University's Academic Integrity Policy.
 **/
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Comprehensive unit tests for the DegreePlanner class.
 * Covers file loading, graph construction, BFS algorithm,
 * standard methods, edge cases and exception handling.
 */
public class DegreePlannerTest {

    // === Test 1: Constructor loads correct course count from XBIT ===

    /**
     * Tests that loading XBIT.txt creates exactly 21 courses.
     */
    @Test
    public void testConstructorLoadsCourseCountXBIT() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // XBIT.txt has exactly 21 courses in line 1
        assertEquals(21, planner.getCourseCount()); // should be 21
    }

    // === Test 2: Constructor loads correct course count from XBDA ===

    /**
     * Tests that loading XBDA.txt creates exactly 23 courses.
     * Verifies tool works correctly with both degree files.
     */
    @Test
    public void testConstructorLoadsCourseCountXBDA() {
        // create planner from XBDA degree file
        DegreePlanner planner = new DegreePlanner("XBDA.txt");

        // XBDA.txt has exactly 23 courses in line 1
        assertEquals(23, planner.getCourseCount()); // should be 23
    }

    // === Test 3: Course with no prerequisites has inDegree 0 ===

    /**
     * Tests that INFT1024 has inDegree 0 after loading XBIT.txt.
     * INFT1024 has no prerequisites so it is ready immediately.
     */
    @Test
    public void testInDegreeZeroForNoPrerequisites() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // INFT1024 has no prerequisites - inDegree should be 0
        assertEquals(0, planner.getInDegree("INFT1024")); // no prerequisites
    }

    // === Test 4: Course with 2 prerequisites has inDegree 2 ===

    /**
     * Tests that INFT1032 has inDegree 2 after loading XBIT.txt.
     * INFT1032 needs both COMP1043 and INFT1024.
     */
    @Test
    public void testInDegreeTwoForTwoPrerequisites() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // INFT1032 needs COMP1043 and INFT1024 - inDegree should be 2
        assertEquals(2, planner.getInDegree("INFT1032")); // two prerequisites
    }

    // === Test 5: Course with 1 prerequisite has inDegree 1 ===

    /**
     * Tests that COMP1048 has inDegree 1 after loading XBIT.txt.
     * COMP1048 needs only COMP1043.
     */
    @Test
    public void testInDegreeOneForOnePrerequisite() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // COMP1048 needs only COMP1043 - inDegree should be 1
        assertEquals(1, planner.getInDegree("COMP1048")); // one prerequisite
    }

    // === Test 6: XBDA inDegree check ===

    /**
     * Tests that MATH2032 has inDegree 1 in XBDA degree.
     * Verifies graph is built correctly for XBDA file too.
     */
    @Test
    public void testXBDAInDegreeCorrect() {
        // create planner from XBDA degree file
        DegreePlanner planner = new DegreePlanner("XBDA.txt");

        // MATH2032 needs only COMP1043 - inDegree should be 1
        assertEquals(1, planner.getInDegree("MATH2032")); // one prerequisite
    }

    // === Test 7: Adjacency list built correctly for XBIT ===

    /**
     * Tests that COMP1043 unlocks the correct courses in XBIT.
     * COMP1043 should unlock INFT1032, INFS1029 and COMP1048.
     */
    @Test
    public void testAdjacencyListBuiltCorrectlyXBIT() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // get courses unlocked by COMP1043
        ArrayList<String> unlocked = planner.getAdjacentCourses("COMP1043");

        // COMP1043 should unlock these three courses
        assertTrue(unlocked.contains("INFT1032")); // INFT1032 unlocked
        assertTrue(unlocked.contains("INFS1029")); // INFS1029 unlocked
        assertTrue(unlocked.contains("COMP1048")); // COMP1048 unlocked
    }

    // === Test 8: Adjacency list built correctly for XBDA ===

    /**
     * Tests that COMP1043 unlocks correct courses in XBDA degree.
     * Verifies adjacency list works correctly for both files.
     */
    @Test
    public void testAdjacencyListBuiltCorrectlyXBDA() {
        // create planner from XBDA degree file
        DegreePlanner planner = new DegreePlanner("XBDA.txt");

        // get courses unlocked by COMP1043 in XBDA
        ArrayList<String> unlocked = planner.getAdjacentCourses("COMP1043");

        // COMP1043 should unlock these courses in XBDA
        assertTrue(unlocked.contains("MATH2032")); // MATH2032 unlocked
        assertTrue(unlocked.contains("INFT1032")); // INFT1032 unlocked
        assertTrue(unlocked.contains("COMP1048")); // COMP1048 unlocked
    }

    // === Test 9: Course with no unlocks has empty adjacency list ===

    /**
     * Tests that a course that does not unlock anything
     * has an empty adjacency list.
     */
    @Test
    public void testEmptyAdjacencyListForNoUnlocks() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // BUSS2082 does not unlock any courses
        ArrayList<String> unlocked = planner.getAdjacentCourses("BUSS2082");

        // adjacency list should be empty
        assertTrue(unlocked.isEmpty()); // nothing unlocked
    }

    // === Test 10: Unknown course returns inDegree -1 ===

    /**
     * Tests that getInDegree returns -1 for a course
     * that does not exist in the degree.
     */
    @Test
    public void testUnknownCourseInDegreeReturnsMinusOne() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // course that does not exist should return -1
        assertEquals(-1, planner.getInDegree("FAKE999")); // not found
    }

    // === Test 11: Unknown course returns empty adjacency list ===

    /**
     * Tests that getAdjacentCourses returns empty list
     * for a course that does not exist in the degree.
     */
    @Test
    public void testUnknownCourseReturnsEmptyAdjacentList() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // course that does not exist should return empty list
        ArrayList<String> result =
                planner.getAdjacentCourses("FAKE999");

        // should return empty list not crash
        assertTrue(result.isEmpty()); // empty list returned
    }

    // === Test 12: getCourseCount returns correct count ===

    /**
     * Tests that getCourseCount returns the correct number
     * after loading the degree file.
     */
    @Test
    public void testGetCourseCountCorrect() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // course count should match line 1 of XBIT.txt
        assertEquals(21, planner.getCourseCount()); // 21 courses
    }

    // === Test 13: generateStudyPlan runs without crashing ===
    /**
     * Tests that generateStudyPlan completes normally
     * with a standard maxCoursesPerPeriod value.
     */
    @Test
    public void testGenerateStudyPlanDoesNotCrash() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // generateStudyPlan should run without throwing exception
        planner.generateStudyPlan(2); // should complete normally
    }

    // === Test 14: generateStudyPlan works with 1 course per period ===
    /**
     * Tests that generateStudyPlan works correctly with
     * only 1 course per study period - the slowest possible plan.
     */
    @Test
    public void testGenerateStudyPlanWithOnePerPeriod() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // should work with 1 course per period
        planner.generateStudyPlan(1); // should complete normally
    }

    // === Test 15: generateStudyPlan works with large max ===
    /**
     * Tests that generateStudyPlan works correctly when
     * maxCoursesPerPeriod is larger than total course count.
     */
    @Test
    public void testGenerateStudyPlanWithLargeMax() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // should work even if max is larger than total course count
        planner.generateStudyPlan(100); // should complete normally
    }

    // === Test 16: generateStudyPlan works with XBDA file ===
    /**
     * Tests that generateStudyPlan works correctly with
     * the XBDA degree file as well as XBIT.
     */
    @Test
    public void testGenerateStudyPlanWithXBDA() {
        // create planner from XBDA degree file
        DegreePlanner planner = new DegreePlanner("XBDA.txt");

        // should work with XBDA file as well as XBIT
        planner.generateStudyPlan(2); // should complete normally
    }

    // === Test 17: toString contains expected information ===
    /**
     * Tests that toString returns the expected format
     * showing course count and class name.
     */
    @Test
    public void testToString() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // toString should contain course count and class name
        assertTrue(planner.toString().contains("21")); // 21 courses
        assertTrue(planner.toString().contains(
                "DegreePlanner")); // class name present
    }

    // === Test 18: equals - same file produces equal planners ===
    /**
     * Tests that two DegreePlanners loaded from the same file
     * are considered equal.
     */
    @Test
    public void testEqualsTrue() {
        // create two planners from same file
        DegreePlanner planner1 = new DegreePlanner("XBIT.txt");
        DegreePlanner planner2 = new DegreePlanner("XBIT.txt");

        // same file should produce equal planners
        assertTrue(planner1.equals(planner2)); // same structure
    }

    // === Test 19: equals - different files not equal ===
    /**
     * Tests that two DegreePlanners loaded from different files
     * are not considered equal.
     */
    @Test
    public void testEqualsFalse() {
        // create planners from different files
        DegreePlanner planner1 = new DegreePlanner("XBIT.txt");
        DegreePlanner planner2 = new DegreePlanner("XBDA.txt");

        // different files should produce different planners
        assertFalse(planner1.equals(planner2)); // different structure
    }

    // === Test 20: equals - same reference ===
    /**
     * Tests the reflexive property of equals.
     * A planner must always equal itself.
     */
    @Test
    public void testEqualsSameReference() {
        // create one planner
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // planner must equal itself - reflexive property
        assertTrue(planner.equals(planner)); // same reference
    }

    // === Test 21: equals - null returns false ===
    /**
     * Tests that equals returns false when compared to null.
     */
    @Test
    public void testEqualsNull() {
        // create a planner
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // comparing to null should return false
        assertFalse(planner.equals(null)); // null check
    }

    // === Test 22: hashCode - equal planners same hash ===
    /**
     * Tests that two equal planners produce the same hashCode.
     * Required by Java equals/hashCode contract.
     */
    @Test
    public void testHashCodeEqualPlanners() {
        // create two identical planners
        DegreePlanner planner1 = new DegreePlanner("XBIT.txt");
        DegreePlanner planner2 = new DegreePlanner("XBIT.txt");

        // equal planners must have same hashCode
        assertEquals(planner1.hashCode(),
                planner2.hashCode()); // same hash
    }

    // === Test 23: Exception - zero courses per period ===
    /**
     * Tests that generateStudyPlan throws IllegalArgumentException
     * when maxCoursesPerPeriod is zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroCoursesPerPeriodThrowsException() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // zero courses per period should throw exception
        planner.generateStudyPlan(0); // should throw
    }

    // === Test 24: Exception - negative courses per period ===
    /**
     * Tests that generateStudyPlan throws IllegalArgumentException
     * when maxCoursesPerPeriod is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCoursesPerPeriodThrowsException() {
        // create planner from XBIT degree file
        DegreePlanner planner = new DegreePlanner("XBIT.txt");

        // negative courses per period should throw exception
        planner.generateStudyPlan(-1); // should throw
    }

    // === Test 25: equals - different type returns false ===
    /**
     * Tests that equals returns false when compared to
     * a non-DegreePlanner object.
     */
    @Test
    public void testEqualsDifferentType() {
        // create a planner
        DegreePlanner planner = new DegreePlanner("XBIT.txt");
        String notAPlanner = "XBIT.txt";

        // comparing to different type should return false
        assertFalse(planner.equals(notAPlanner)); // different type
    }

    // === References ===
    //
    // JUnit 4 @Test annotation and assertion methods including
    // assertEquals, assertTrue and assertFalse:
    // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
    //
    // JUnit 4 assertTrue - used to verify adjacency list contents
    // and toString format in Tests 7, 8, 9, 11, 17, 18, 20:
    // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html#assertTrue(boolean)
    //
    // JUnit 4 assertFalse - used to verify equals returns false
    // for null, different type and different file in Tests 19, 21, 25:
    // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html#assertFalse(boolean)
    //
    // JUnit 4 expected exception testing using @Test(expected = ...):
    // https://github.com/junit-team/junit4/wiki/Exception-testing
    //
    // Java ArrayList.contains() - used to verify adjacency list
    // contains expected course codes in Tests 7 and 8:
    // https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html#contains-java.lang.Object-
    //
    // Java equals() and hashCode() contract - equal objects must
    // have equal hash codes, tested in Test 22:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#hashCode--
}