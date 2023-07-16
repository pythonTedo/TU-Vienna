import java.awt.*;


// A map that associates a position (objects of type 'Point') with a color (objects of type 'Color').
// The number of key-value pairs is not limited.
// The map is implemented as hash map. The map does not contain any keys or values being 'null'.
//
public class HashPointColorMap {

    //TODO: declare variables.
    private Point[] keys = new Point[10];
    private Color[] values = new Color[10];
    private int size = 0;

    private int find(Point key) {
        for (int i = 0; i < size; i++) {
            if (keys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }
    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise, 'null' is returned.
    // Precondition: key != null && value != null.
    public Color put(Point key, Color value) {

        //TODO: implement method.
        if (key != null || value != null) {
            int index = find(key);
            if (index != -1) {
                Color oldValue = values[index];
                values[index] = value;
                return oldValue;
            } else {
                if (size == keys.length) {
                    Point[] newKeys = new Point[keys.length * 2];
                    Color[] newValues = new Color[values.length * 2];
                    for (int i = 0; i < size; i++) {
                        newKeys[i] = keys[i];
                        newValues[i] = values[i];
                    }
                    keys = newKeys;
                    values = newValues;
                }
                keys[size] = key;
                values[size] = value;
                size++;
            }
        }
        return null;
    }

    // Returns the value associated with the specified key, i.e. the method returns the color
    // associated with the specified point.
    // More formally, if this map contains a mapping from a key k to a value v such that
    // key.equals(k) == true, then this method returns v.
    // (There can be at most one such mapping.)
    // Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Color get(Point key) {

        //TODO: implement method.
        if (key != null) {
            int index = find(key);
            if (index != -1) {
                return values[index];
            }
        }
        return null;
    }

    // Removes the mapping for a key from this map if it is present. More formally, if this map
    // contains a mapping from key k to value v such that key.equals(k) == true,
    // that mapping is removed. (The map can contain at most one such mapping.)
    // Returns the value to which this map previously associated the key, or 'null' if the map
    // contained no mapping for the key.
    // Precondition: key != null.
    public Color remove(Point key) {

        //TODO: implement method.
        if (key != null) {
            int index = find(key);
            if (index != -1) {
                Color oldValue = values[index];
                for (int i = index; i < size - 1; i++) {
                    keys[i] = keys[i + 1];
                    values[i] = values[i + 1];
                }
                size--;
                return oldValue;
            }
        }
        return null;
    }

    // Returns a queue with all keys of this map (ordering is not specified).
    public SimplePointQueue keys() {

        //TODO: implement method.
        SimplePointQueue gatheredKeys = new SimplePointQueue(keys.length);
        for (int i = 0; i < size; i++) {
            gatheredKeys.add(keys[i]);

        }
        return gatheredKeys;
    }

    // Returns 'true' if the specified key is contained in this map.
    // Returns 'false' otherwise.
    public boolean containsKey(Point key) {

        //TODO: implement method.
        if (key != null) {
            int index = find(key);
            return index != -1;
        }
        return false;
    }

    // Returns 'true' if the specified value is contained at least once in this map.
    // Returns 'false' otherwise.
    public boolean containsValue(Color value) {

        //TODO: implement method.
        if (value != null) {
            for (int i = 0; i < size; i++) {
                if (values[i].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Returns a string representation of this map with key-value pairs in parentheses, separated
    // by commas (order is not specified).
    // Example: {([9, 2], java.awt.Color[r=255,g=255,b=0]), ([7, 1], java.awt.Color[r=255,g=0,b=0])}
    public String toString() {

        //TODO: implement method.
        if (size > 0) {
            String result = "{";
            for (int i = 0; i < size - 1; i++) {
                result += "(" + keys[i].toString() + ", " + values[i].toString() + "), ";
            }
            result += "(" + keys[size - 1].toString() + ", " + values[size - 1].toString() + ")}";
            return result;
        }
        return "";
    }

    // Returns 'true' if 'this' and 'o' are equal, meaning 'o' is of class 'HashPointColorMap'
    // and 'this' and 'o' contain the same key-value pairs, i.e. the number of key-value pairs is
    // the same in both maps and every key-value pair in 'this' equals one key-value pair in 'o'.
    // Two key-value pairs are equal if the two keys are equal and the two values are equal.
    // Otherwise, 'false' is returned.
    public boolean equals(Object o) {

        //TODO: implement method.
        if (o instanceof HashPointColorMap other) {
            if (size == other.size) {
                for (int i = 0; i < size; i++) {
                    if (!keys[i].equals(other.keys[i]) || !values[i].equals(other.values[i])) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Returns the hash code of 'this'.
    public int hashCode() {

        //TODO: implement method.
        int result = 0;
        for (int i = 0; i < size; i++) {
            result += keys[i].hashCode() + values[i].hashCode();
        }
        return result;
    }
}

