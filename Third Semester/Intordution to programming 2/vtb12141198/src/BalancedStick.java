// A 'BalancedStick' has a specified stick weight, that can not be changed after
// initialisation. On the left and right end of the stick mobiles
// are attached (recursive structure). 'BalancedStick' implements 'Mobile'.
// You can assume that no part of a mobile has the same identity as another part.
//
public class BalancedStick implements Mobile// implements Mobile // TODO: activate clause.
{

    //TODO: define missing parts of the class.
    private int stickWeight;
    private Mobile left;
    private Mobile right;

    // Initialises 'this'; throws an 'UnbalancedException' if the resulting mobile
    // would not be balanced, i.e. if left.getWeight() != right.getWeight(). The detail message
    // of the exception contains information about the difference between left and right weight,
    // for example "Stick unbalanced (left 7 - right 16)" (see example in 'PraxisTest2.java').
    // Preconditions: stickWeight > 0, left != null, right != null, left != right,
    // no part of a mobile has the same identity as another part.
    public BalancedStick(int stickWeight, Mobile left, Mobile right) throws UnbalancedException {

        // TODO: implement constructor.
        if(stickWeight > 0 && left != null && right != null && left != right){
            this.stickWeight = stickWeight;
            this.left = left;
            this.right = right;
            if (left.getWeight() != right.getWeight()) {
                throw new UnbalancedException("Stick unbalanced (left " + left.getWeight() + " - right " + right.getWeight() + ")");
            }
        }
    }

    // Replaces the mobile equal to 'toReplace' with a new mobile 'replaceWith' and
    // returns 'true' if such a mobile is contained as part of this mobile, i.e., attached to this
    // stick or below (recursive search). Otherwise, the call of this method has no effect and
    // 'false' is returned.
    // Throws an 'UnbalancedException' if the replacement would violate the
    // conditions that all sticks need to be balanced. The detail message
    // of the exception contains information about the difference between left and right weight.
    // Precondition: toReplace != null && replaceWith != null
    public boolean replace(Mobile toReplace, Mobile replaceWith) throws UnbalancedException {

        // TODO: implement method.
        return false;
    }

    @Override
    public int getWeight() {
        if (this.left != null && this.right != null) {
            return this.stickWeight + this.left.getWeight() + this.right.getWeight();
        }
        return this.stickWeight;
    }

    @Override
    // Two sticks are equal if
    // 1.) they have the same stick weight and
    // 2.) if the left part of 'this' equals the left part of 'o' and the right part of 'this'
    //       equals the right part of 'o'
    //     or
    //     if the right part of 'this' equals the left part of 'o' and the left part of 'this'
    //       equals the right part of 'o' (i.e., exchanging left and right part does not
    //       change the value returned by 'equals').
    //
    // For example, all three of the following mobiles are equal (provided that corresponding
    // objects of Star are equal):
    //
    //          |                      |                |
    //      +---2---+              +---2---+        +---2---+
    //      |       |              |       |        |       |
    //   +--2--+    *           +--2--+    *        *    +--2--+
    //   |     |    16          |     |    16       16   |     |
    //   *  +--1--+          +--1--+  *               +--1--+  *
    //   7  |     |          |     |  7               |     |  7
    //      *     *          *     *                  *     *
    //      3     3          3     3                  3     3
    //
    public boolean equals(Object o) {

        // TODO: implement method.
        if(!this.getClass().equals(o.getClass())){
            return false;
        }
        if (this.getWeight() != ((BalancedStick) o).getWeight()) {
            return false;
        }
        if (this.left.equals(((BalancedStick) o).left) && this.right.equals(((BalancedStick) o).right) ||
                (this.left.equals(((BalancedStick) o).right) && this.right.equals(((BalancedStick) o).left)))
        {
            return true;
        }
        return false;
    }

    @Override
    public StarCollection getStarCollection() {
        if (this.left != null && this.right != null) {
            StarCollection starCollection = new MyStarCollection();
            starCollection.add(this.left.getStarCollection());
            starCollection.add(this.right.getStarCollection());
            return starCollection;
        }
        return null;
    }

    @Override
    public StarIterator iterator() {
        return new MyItterator(this);
    }

    public String toString() {

        return "(" + this.left.toString() + ")" + "-" + this.stickWeight + "-" + "(" + this.right.toString() + ")";
    }
}

// TODO: define additional classes if needed (either here or in a separate file).
class MyItterator implements StarIterator{

    private Mobile mobile;
    private int index;

    public MyItterator(BalancedStick mobile){
        this.mobile = mobile;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        if (this.index < this.mobile.getStarCollection().size()) {
            return true;
        }
        return false;
    }

    @Override
    public Star next() {
        return null;
    }
}