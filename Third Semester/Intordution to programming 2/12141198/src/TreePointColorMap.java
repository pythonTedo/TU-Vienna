import java.awt.*;

// A map that associates a position (objects of type 'Point') with a color (objects of type 'Color'). The number of
// key-value pairs is not limited.
// The map is implemented as a binary tree. The keys are ordered based on the 'compareTo' method of 'Point'.
// The map does not contain any keys being 'null'.
//
// TODO: define further classes and methods for the implementation of the binary search tree,
//  if needed.
//
public class TreePointColorMap {

    //TODO: declare variables.
    MyTreeNode root;
    int size;
    PointLinkedList list = new PointLinkedList();

    // Initializes 'this' as an empty map.
    public TreePointColorMap() {
    }


    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise, 'null' is returned.
    // Precondition: key != null.
    public Color put(Point key, Color value) {
        MyTreeNode node = new MyTreeNode(key, value);
        if (key != null) {
            if (this.root == null) {
                this.root = node;
                list.addLast(key);
                this.size++;
                return null;
            } else {
                MyTreeNode focusNode = this.root;
                MyTreeNode parent;
                while (true) {
                    parent = focusNode;
                    if (key.compareTo(focusNode.data) == 0) {
                        Color oldColor = focusNode.color;
                        focusNode.color = value;
                        return oldColor;
                    }
                    if (key.compareTo(focusNode.data) < 0) {
                        focusNode = focusNode.left;
                        if (focusNode == null) {
                            parent.left = node;
                            list.addLast(key);
                            this.size++;
                            return null;
                        }
                    } else {
                        focusNode = focusNode.right;
                        if (focusNode == null) {
                            parent.right = node;
                            list.addFirst(key);
                            this.size++;
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    // Returns the value associated with the specified key, i.e. the method returns the color
    // associated with coordinates specified by key (the key must have the same coordinates as the
    // specified 'key'). Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Color get(Point key) {

        //TODO: implement method.
        if (this.root == null) {
            return null;
        } else {
            MyTreeNode n = this.root;
            while (n != null) {
                if (n.data.compareTo(key) == 0) {
                    return n.color;
                } else if (n.data.compareTo(key) > 0) {
                    n = n.left;
                } else {
                    n = n.right;
                }
            }
        }
        return null;
    }

    // Returns 'true' if this map contains a mapping for the specified key, this means
    // for a point with the same coordinates as the specified 'key'.
    // Precondition: key != null.
    public boolean containsKey(Point key) {

        //TODO: implement method.
        if (this.root == null) {
            return false;
        } else {
            MyTreeNode n = this.root;
            while (n != null) {
                if (n.data.compareTo(key) == 0) {
                    return true;
                } else if (n.data.compareTo(key) > 0) {
                    n = n.left;
                } else {
                    n = n.right;
                }
            }
        }
        return false;
    }

    // Returns a list with all keys of this map ordered ascending according to the
    // key order relation.
    public PointLinkedList keys() {

        //TODO: implement method.
        list.sortNodes();
        //list.print();
        return list;
    }

    // Returns a new raster representing a region with the specified size from this
    // map. The upper left corner of the region is (0,0) and the lower right corner is (width-1, height-1).
    // All pixels outside the specified region are cropped (not included).
    // Preconditions: width > 0 && height > 0
    public SimpleRasterRGB asRasterRGB(int width, int height) {

        //TODO: implement method.
        SimpleRasterRGB raster = new SimpleRasterRGB(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Point p = new Point(i, j);
                if (this.containsKey(p)) {
                    raster.setPixelColor(p.getX(), p.getY(), this.get(p));
                }
            }

        }
        return raster;
    }

    public int numberOfFullNodes() {
         return root.numberOfFullNodes();
    }
}

// TODO: define further classes, if needed (either here or in a separate file).

