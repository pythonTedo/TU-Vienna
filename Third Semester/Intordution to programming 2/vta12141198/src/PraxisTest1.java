// This class can be modified and is for testing your solution.
// Modifications will NOT be reviewed or assessed.
//
public class PraxisTest1 {

    public static void main(String[] args) {

        Rational r1 = new Rational(1,2);
        Rational r2 = new Rational(4,8);
        Rational r3 = new Rational(3,-4);
        Rational r4 = new Rational(-2,6);
        Rational r5 = new Rational(-4,6);
        Rational r6 = new Rational(2, 3);
        Rational r7 = new Rational(1, 3);
        Rational r8 = new Rational(5, 6);

        System.out.println("Test 'add' method:");
        TreeSetRational s1 = new TreeSetRational();
        testTrue(s1.add(r1)); // 1/2
        testTrue(!s1.add(r2)); // 4/8 == 1/2
        testTrue(s1.add(r3)); // -3/4
        testTrue(s1.add(r4)); // -1/3
        testTrue(s1.add(r5)); // -2/3

        TreeSetRational s2 = new TreeSetRational();
        s2.add(r6); // 2/3
        s2.add(r7); // 1/3
        s2.add(r2); // 1/2
        s2.add(r8); // 5/6

        System.out.println("Test 'countWithinRange' method:");
        testValue(s1.countWithinRange(new Rational(0,1), new Rational(1,1)), 1);
        testValue(s1.countWithinRange(r3, r1), 4);
        testValue(s1.countWithinRange(r5, r1), 3);
        testValue(s2.countWithinRange(r3, r8), 4);
/*
        System.out.println("Test 'union' method:");
        TreeSetRational s3 = s1.union(s2);
        testValue(s3.countWithinRange(new Rational(-10,1), new Rational(10, 1)), 7);
        testValue(s3.countWithinRange(r7, r1), 2);
        testValue(s1.countWithinRange(r3, r1), 4);
        testValue(s1.countWithinRange(r5, r1), 3);
        testValue(s2.countWithinRange(r3, r8), 4);

        System.out.println("Test 'removeMinimum' method:");
        TreeSetRational s4 = new TreeSetRational();
        s4.add(r6); // 2/3
        s4.add(r7); // 1/3
        s4.add(r2); // 1/2
        s4.add(r8); // 5/6

        testEquals(s4.removeMinimum(), r7);
        testEquals(s4.removeMinimum(), r2);
        testEquals(s4.removeMinimum(), r6);
        testEquals(s4.removeMinimum(), r8);
        testEquals(s4.removeMinimum(), null);

        System.out.println("Test 'toString' method:");
        testEquals(new TreeSetRational().toString(), "[]");
        testEquals(s1.toString(), "[-3/4, -2/3, -1/3, 1/2]");
        testEquals(s2.toString(), "[1/3, 1/2, 2/3, 5/6]");
        testEquals(s3.toString(), "[-3/4, -2/3, -1/3, 1/3, 1/2, 2/3, 5/6]");
*/
    }

    public static void testIdentical(Object given, Object expected) {
        if (given == expected) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected value: " + expected + " / Given value: " + given);
        }
    }

    public static void testNotIdentical(Object given, Object expected) {
        if (given != expected) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected value: " + expected + " / Given value: " + given);
        }
    }

    public static void testEquals(Object given, Object expected) {
        if (given == expected) {
            System.out.println("Successful test");
            return;
        } else {
            if (given == null) {
                System.out.println("Test NOT successful! Expected value: " +
                        expected +
                        " / Given value: null");
                return;
            }
            if (expected == null) {
                System.out.println("Test NOT successful! Expected value: null / " +
                        "Given value: " + given);
                return;
            }
        }
        if (given.equals(expected)) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected value: " + expected.toString() + " / Given " +
                    "value: " + given.toString());
        }
    }

    public static void testTrue(boolean expectedTrue) {
        if (expectedTrue) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expression should be 'true' but is 'false'.");
        }
    }

    public static void testEquals(Rational given, Rational expected) {
        if (given == expected) {
            System.out.println("Successful test");
            return;
        } else {
            if (given == null) {
                System.out.println("Test NOT successful! Expected value: " +
                        "(" + expected.toString() + ")" +
                        " / Given value: null");
                return;
            }
            if (expected == null) {
                System.out.println("Test NOT successful! Expected value: null / " +
                        "Given value: " + "(" + given.toString() + ")");
                return;
            }
        }

        if (given.compareTo(expected) == 0) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected value: (" + expected.toString() +
                    ") / " +
                    "Given value: (" + given.toString() + ")");
        }
    }

    public static void testValue(int given, int expected) {
        if (given == expected) {
            System.out.println("Successful test");
        } else {
            System.out.println("Test NOT successful! Expected value: " + expected + " / Given value: " + given);
        }
    }

}
