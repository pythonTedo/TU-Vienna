// A list of 2D-points (objects of type 'Point') implemented as a linked list.
// The number of elements of the list is not limited.
//
// TODO: define further classes and methods for the implementation of the linked list,
//  if needed.
//
public class PointLinkedList {

    //TODO: declare variables.
    MyNode head;
    MyNode tail;
    int size;

    // Initializes 'this' as an empty list.
    public PointLinkedList() {

        //TODO: define constructor.
        this.head = null;
        this.tail = null;
        this.size = 0;

    }

    // Inserts the specified element 'point' at the beginning of this list.
    // Precondition: point != null.
    public void addFirst(Point point) {
        //TODO: implement method.
        if (point != null) {

            MyNode node = new MyNode(point);

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

    // Appends the specified element 'point' to the end of this list.
    // Precondition: point != null.
    public void addLast(Point point) {

        //TODO: implement method.
        if (point != null) {

            MyNode node = new MyNode(point);

            // If the node is first passing it to head and tail
            if (this.head == null) {
                this.head = node;
                this.tail = node;
            } else {
                MyNode n = this.head;
                // we are jumping to the last node in the list since it has NULL as next
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
    public Point getLast() {

        //TODO: implement method.
        MyNode n = this.head;
        if (n == null) {
            return null;
        }
        while (n.next != null) {
            n = n.next;
        }
        return n.data;
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public Point getFirst() {

        //TODO: implement method.
        MyNode n = this.head;
        if (n == null) {
            return null;
        }
        return n.data;
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Point pollFirst() {

        //TODO: implement method.
        if(this.head != null){
            MyNode n = this.head;
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
    public Point pollLast() {

        //TODO: implement method.
        if (this.tail != null) {
            MyNode n = this.tail;
            this.tail = this.tail.prev;
            this.tail.next = null;
            this.size--;
            return n.data;
        }
        return null;
    }

    // Inserts the specified element 'point' at the specified position in this list.
    // Precondition: i >= 0 && i <= size() && point != null.
    public void add(int i, Point point) {

        //TODO: implement method.
        if (i >= 0 && i <= size && point != null){
            MyNode toput = new MyNode(point);
            MyNode n = this.head;

            if (i == 0) {
                addFirst(point);
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
    public Point get(int i) {

        //TODO: implement method.
        if (i >= 0 && i < this.size) {
            MyNode n = this.head;
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

    // Returns the index of the first occurrence (element with equal coordinates to 'point') of the
    // specified element in this list, or -1 if this list does not contain the element.
    // Precondition: point != null.
    public int indexOf(Point point) {

        //TODO: implement method.
        if (point != null) {
            MyNode n = this.head;
            int count = 0;
            while (n != null) {
                if (n.data.getX() == point.getX() && n.data.getY() == point.getY()) {
                    return count;
                }
                n = n.next;
                count++;
            }
        }
        return -1;
    }
    // Returns the number of elements in this list.
    public int size() {

        //TODO: implement method.
        return this.size;
    }
    public void print() {
        MyNode n = this.head;
        while (n != null) {
            System.out.println(n.data.getX() + " " + n.data.getY());
            n = n.next;
        }
    }
    public void sortNodes() {
        MyNode n = this.head;
        MyNode temp = null;
        while (n != null) {
            temp = n.next;
            while (temp != null) {
                if (n.data.getX() > temp.data.getX()) {
                    Point p = n.data;
                    n.data = temp.data;
                    temp.data = p;
                }
                temp = temp.next;
            }
            n = n.next;
        }
    }
}