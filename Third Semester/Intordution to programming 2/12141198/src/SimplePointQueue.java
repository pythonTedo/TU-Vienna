public class SimplePointQueue {

    // TODO: declare variables.
    private Point[] queue; // internal array to store the points
    private int head;  // index of the head of the queue
    private int tail;  // index of the tail of the queue
    private int size;  // number of elements in the queue

    // Initializes this queue with an initial capacity (length of internal array).
    // Precondition: initialCapacity > 0.
    public SimplePointQueue(int initialCapacity) {

        //TODO: define constructor.
        if (initialCapacity > 0){
            this.queue = new Point[initialCapacity];
            this.head = 0;
            this.tail = 0;
            this.size = 0;
        }
    }

    // Adds the specified point 'p' to this queue.
    // Precondition: p != null.
    public void add(Point p) {

        // TODO: implement method.
        if (this.size == this.queue.length) {
            resize();
        }
        this.queue[this.tail] = p;
        this.tail = (this.tail + 1) % this.queue.length;
        this.size++;

    }

    // Retrieves and removes the head of this queue, or returns 'null'
    // if this queue is empty.
    public Point poll() {

        // TODO: implement method.
        if (this.size() == 0){
            return null;
        }
        Point selectedP = this.queue[this.head];
        this.queue[head] = null;
        this.head = (this.head + 1) % this.queue.length;
        this.size--;

        return selectedP;
    }

    // Retrieves, but does not remove the head of this queue, or returns 'null'
    // if this queue is empty.
    public Point peek() {

        // TODO: implement method.
        if (this.size() == 0){
            return null;
        }
        return this.queue[this.head];
    }

    // Returns the number of entries in this queue.
    public int size() {

        // TODO: implement method.
        return this.size;
    }

    private void resize(){
        Point[] newQueue = new Point[this.queue.length * 2];
        for (int i = 0; i < this.size(); i++) {
            newQueue[i] = this.queue[(this.head + i) % this.queue.length];
        }
        this.queue = newQueue;
        this.head = 0;
        this.tail = size;
    }
}
