// A set of rational numbers implemented as a binary search tree. There are no
// duplicate entries in this set (no two elements e1 and e2 for which e1.compareTo(e2) == 0). The
// elements are sorted according to their value (the ordering is given by the method 'compareTo'
// of class 'Rational').
//
// TODO: define further classes and methods for the implementation of the binary search tree,
//   if needed. Do NOT use the Java-Collection framework in your implementation.
//
public class TreeSetRational
{

    // TODO: define missing parts of the class.
    MyTreeNode myroot;
    int size;
    // Initialises 'this' as an empty set.
    public TreeSetRational() {

        // TODO: implement constructor.
    }

    // Adds the specified Rational object to the set.
    // Returns 'true' if the set did not already contain the specified element
    // and 'false' otherwise.
    // Precondition: r != null.
    public boolean add(Rational r) {
        if (r != null){
            if (this.myroot == null){
                MyTreeNode n = new MyTreeNode(r);
                this.myroot = n;
                return true;
            }
            return this.myroot.add(r);
        }
        return false;
    }

    // Returns a new 'TreeSetRational' object that is the union of this set and the 'other' set.
    // 'this' and 'other' are not changed by this method.
    // Precondition: other != null.
    public TreeSetRational union(TreeSetRational other) {

        // TODO: implement method.
        return null;
    }

    // Returns the number of rational numbers in the set that are within the range defined by
    // the lowest and highest rational number (inclusive). The method exploits the structure of
    // the binary search tree and traverses only relevant parts of the tree.
    // Precondition: lowest != null && highest != null && lowest.compareTo(highest) <= 0.
    public int countWithinRange(Rational lowest, Rational highest) {
        int count = 0;
        // TODO: implement method.
        if (lowest != null && highest != null && lowest.compareTo(highest) <= 0){
            while (true){
                if (this.myroot == null){
                    return count;
                }
                if (this.myroot.getLeft() != null){
                    if (this.myroot.getLeft().getData().compareTo(lowest) >= 0){
                        this.myroot = this.myroot.getLeft();
                    }
                }
            }
        }
        return -1;
    }

    // Removes the lowest rational number from this set. Returns the rational number that was
    // removed from this set or 'null' if this set is empty.
    // (The corresponding node is removed by replacing it with the subtree of the node that
    // contains entries greater than the minimum.)
    public Rational removeMinimum() {

        // TODO: implement method.
        return null;
    }

    // Returns a string representation of 'this' with all rational objects
    // ordered ascending. The format of the string uses
    // brackets and is as in the following example with a set of four elements:
    // "[-3/4, -2/3, -1/3, 1/2]"
    // Returns "[]" if this set is empty.
    public String toString() {

        // TODO: implement method.
        return "";
    }
}


class MyTreeNode {
    private Rational data;
    private MyTreeNode left;
    private MyTreeNode right;

    // We have key(Point) and value(Color) for each node
    public MyTreeNode(Rational point) {
        this.data = point;
        this.left = null;
        this.right = null;
    }

    public Rational getData() {
        return this.data;
    }

    public boolean add(Rational point) {
        if (this.data == null) {
            this.data = point;
            return true;
        }
        if (this.data.compareTo(point) == 0) {
            return false;
        }
        if (point.compareTo(this.data) < 0) {
            if (this.left == null) {
                MyTreeNode temp = new MyTreeNode(point);
                this.left = temp;
                return true;
            } else {
                return this.left.add(point);
            }
        }
        if (point.compareTo(this.data) > 0) {
            if (this.right == null) {
                MyTreeNode temp = new MyTreeNode(point);
                this.right = temp;
                return true;
            } else {
                return this.right.add(point);
            }
        }
        return false;
    }

    public MyTreeNode getLeft() {
        return this.left;
    }

    public MyTreeNode getRight() {
        return this.right;
    }
}


// TODO: define further classes, if needed (either here or in a separate file).
