import java.awt.*;

// A map that associates a position with a color. The number of key-value pairs is not limited.
// The map does not contain any keys or values being 'null'.
//
public class SimplePointColorMap {

    //TODO: declare variables.
    private int initialCapacity;
    private int size;
    private Point[] PointKeys;
    private Color[] ColorValues;

    // Initializes this map with an initial capacity (length of internal array).
    // Precondition: initialCapacity > 0.
    public SimplePointColorMap(int initialCapacity) {

        //TODO: define constructor.
        this.PointKeys = new Point[initialCapacity];
        this.ColorValues = new Color[initialCapacity];
        this.size = 0;
    }

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise, 'null' is returned.
    // Precondition: key != null && value != null.
    public Color put(Point key, Color value) {
        boolean seen = false;
        if (key != null || value != null) {
            //TODO: implement method.
            for (int i = 0; i < size; i++) {
                if (this.PointKeys[i].compareTo(key) == 0) {
                    Color oldValue = this.ColorValues[i];
                    this.ColorValues[i] = value;
                    return oldValue;
                }
            }
            if (this.PointKeys.length == size && this.ColorValues.length == size) {
                Point[] npts = new Point[this.PointKeys.length * 2];
                Color[] clrs = new Color[this.ColorValues.length * 2];
                for (int i = 0; i < size; i++) {
                    npts[i] = this.PointKeys[i];
                    clrs[i] = this.ColorValues[i];
                }
                this.PointKeys = npts;
                this.ColorValues = clrs;
            }
            this.PointKeys[size] = key;
            this.ColorValues[size] = value;
            size++;
        }
        return null;
    }

    // Returns the value associated with the specified key, i.e. the method returns the color
    // associated with the specified point.
    // More formally, if this map contains a mapping from a key k to a value v such that
    // key.compareTo(k) == 0, then this method returns v.
    // (There can be at most one such mapping.)
    // Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Color get(Point key) {
        //TODO: implement method.
        if (key != null) {
            for (int i = 0; i < size; i++) {
                if (this.PointKeys[i] == null){
                    continue;
                }
                if (this.PointKeys[i].compareTo(key) == 0) {
                    return this.ColorValues[i];
                }
            }
            return  null;
        }
        return null;
    }

    // Removes the mapping for a key from this map if it is present. More formally, if this map
    // contains a mapping from key k to value v such that key.compareTo(k) == 0,
    // that mapping is removed. (The map can contain at most one such mapping.)
    // Returns the value to which this map previously associated the key, or 'null' if the map
    // contained no mapping for the key.
    // Precondition: key != null.
    public Color remove(Point key) {
        //TODO: implement method.
        Color pickedColor;
        if (key != null) {
            for (int i = 0; i < size; i++) {
                if (this.PointKeys[i].compareTo(key) == 0) {
                    Point[] newPointKeys = new Point[this.PointKeys.length - 1];
                    Color[] newColorValues = new Color[this.ColorValues.length - 1];
                    for (int j = 0; j < size; j++) {
                        if(this.PointKeys[j].compareTo(key) != 0){
                            newPointKeys[j] = this.PointKeys[j];
                            newColorValues[j] = this.ColorValues[j];
                        }
                    }
                    pickedColor = this.ColorValues[i];
                    this.PointKeys = newPointKeys;
                    this.ColorValues = newColorValues;
                    this.size--;
                    return pickedColor;
                }
            }
        }
        return null;
    }

    // Returns a queue with all keys of this map (ordering is not specified).
    public SimplePointQueue keys() {

        //TODO: implement method.
        SimplePointQueue keysQueue = new SimplePointQueue(this.size);
        for (int i = 0; i < size; i++) {
            keysQueue.add(this.PointKeys[i]);
        }
        return keysQueue;
    }
}