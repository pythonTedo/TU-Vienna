// A list of objects of 'RasterRGBA' implemented as a doubly linked list.
// The number of elements of the list is not limited. Entries of 'null' are allowed.
//
// TODO: define further classes and methods for the implementation of the doubly linked list, if
//  needed.
//
public class LinkedListRasterRGBA {

    //TODO: declare variables.
    private MyNodeRaster head;
    private MyNodeRaster tail;
    private int size;

    // Initializes 'this' as an empty list.
    public LinkedListRasterRGBA() {

        //TODO: define constructor.
        this.head = null;
        this.tail = null;
        this.size = 0;

    }

    // Inserts the specified element 'raster' at the beginning of this list.
    public void addFirst(RasterRGBA raster) {

        //TODO: implement method.
        if (raster != null) {

            MyNodeRaster node = new MyNodeRaster(raster);

            // If the node is first, passing it to head and tail
            if (this.head == null) {
                this.head = node;
                this.tail = node;
            } else {
                // we overwrite the head with the new node and the next node is linked to the old head
                node.next = this.head;
                this.head.prev = node;
                this.head = node;
            }
            this.size++;
        }
    }

    // Appends the specified element 'raster' to the end of this list.
    public void addLast(RasterRGBA raster) {

        //TODO: implement method.
        if (raster != null) {

            MyNodeRaster node = new MyNodeRaster(raster);

            // If the node is first passing it to head and tail
            if (this.head == null) {
                this.head = node;
                this.tail = node;
            } else {
                MyNodeRaster n = this.head;
                while (n.next != null) {
                    n = n.next;
                }
                n.next = node;
                node.prev = n;
                this.tail = node;
            }
            this.size++;
        }
    }

    // Returns the last element in this list.
    // Returns 'null' if the list is empty.
    public RasterRGBA getLast() {

        //TODO: implement method.
        MyNodeRaster n = this.head;
        while (n.next != null) {
            n = n.next;
        }
        return n.data;
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public RasterRGBA getFirst() {

        //TODO: implement method.
        MyNodeRaster n = this.head;
        if (n == null) {
            return null;
        }
        return n.data;
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public RasterRGBA pollFirst() {

        //TODO: implement method.
        if (this.head != null) {
            MyNodeRaster n = this.head;
            this.head = this.head.next;
            if(this.head != null) {
                this.head.prev = null;
            }
            this.size--;
            return n.data;
        }
        return null;
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public RasterRGBA pollLast() {

        //TODO: implement method.
        if (this.tail != null) {
            MyNodeRaster n = this.tail;
            this.tail = this.tail.prev;
            this.tail.next = null;
            this.size--;
            return n.data;
        }
        return null;
    }

    // Inserts the specified element 'raster' at the specified position in this list.
    // More specifically, 'raster' is inserted as follows:
    // before insertion elements have indices from 0 to size()-1;
    // 'raster' is inserted immediately before the element with the given index 'i' (or as last
    // element if 'i == size()') such that 'raster' can be found at index 'i' after insertion.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, RasterRGBA raster) {

        //TODO: implement method.
        if (i >= 0 && i <= size && raster != null){
            MyNodeRaster toput = new MyNodeRaster(raster);
            MyNodeRaster n = this.head;

            if (i == 0) {
                addFirst(raster);
                this.size++;
            }
            else {
                int count = 0;
                while (count < i-1) {
                    n = n.next;
                    count++;
                }
                toput.next = n.next;
                n.next = toput;
                toput.prev = n;
                this.size++;
            }
        }
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public RasterRGBA get(int i) {

        //TODO: implement method.
        if (i >= 0 && i < this.size) {
            MyNodeRaster n = this.head;
            int count = 0;
            if (i == 0) {
                return n.data;
            }
            else {
                while (count < i) {
                    n = n.next;
                    count++;
                }
                return n.data;
            }
        }
        return null;
    }

    // Replaces the element at the specified position in this list with the specified element.
    // Returns the element that was replaced.
    // Precondition: i >= 0 && i < size().
    public RasterRGBA set(int i, RasterRGBA raster) {

        //TODO: implement method.
        if (i >= 0 && i < this.size && raster != null) {
            MyNodeRaster n = this.head;
            int count = 0;
            if (i == 0) {
                RasterRGBA old = n.data;
                n.data = raster;
                return old;
            }
            else {
                while (count < i) {
                    n = n.next;
                    count++;
                }
                RasterRGBA old = n.data;
                n.data = raster;
                return old;
            }
        }
        return null;
    }

    // Removes the element at the specified position in this list. Shifts any subsequent
    // elements to the left (subtracts one from their indices). Returns the element that was
    // removed from the list.
    // Precondition: i >= 0 && i < size().
    public RasterRGBA remove(int i) {

        //TODO: implement method.
        if (i >= 0 && i < this.size) {
            MyNodeRaster n = this.head;
            int count = 0;
            if (i == 0) {
                RasterRGBA old = n.data;
                this.head = this.head.next;
                this.size--;
                return old;
            }
            else {
                while (count < i-1) {
                    n = n.next;
                    count++;
                }
                RasterRGBA old = n.next.data;
                n.next = n.next.next;
                this.size--;
                return old;
            }
        }
        return null;
    }

    // Returns the index of the last occurrence of 'raster' in this list (the highest index with an
    // element equal to 'raster'), or -1 if this list does not contain the element.
    // Equality of elements is determined by object identity (== operator).
    public int lastIndexOf(RasterRGBA raster) {

        //TODO: implement method.
        if (raster != null) {
            MyNodeRaster n = this.head;
            int count = 0;
            int index = -1;
            while (n != null) {
                if (n.data == raster) {
                    index = count;
                }
                n = n.next;
                count++;
            }
            return index;
        }
        return -1;
    }

    // Returns the number of elements in this list.
    public int size() {

        //TODO: implement method.
        return this.size;
    }

    //TODO (optional): add more operations (e.g., floodfill).
}

// TODO: define further classes, if needed (either here or in a separate file).
