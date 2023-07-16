// Leaf node of a mobile. The actual decoration of a mobile.
// A 'Star' has a specified weight, that can not be changed after
// initialisation. 'Star' implements 'Decoration'.
//
public class Star implements Decoration// implements Decoration //TODO: activate clause.
{

    //TODO: define missing parts of the class.
    private int weight;

    public Star(int weight) {
        // TODO: implement constructor.
        this.weight = weight;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    // Returns a readable representation of 'this' with the
    // symbol '*' followed by the weight of this star.
    public String toString() {
        // TODO: implement method.
        return "*" + this.weight;
    }

    @Override
    public StarCollection getStarCollection() {
        return null;
    }

    @Override
    public StarIterator iterator() {
        return null;
    }
}
