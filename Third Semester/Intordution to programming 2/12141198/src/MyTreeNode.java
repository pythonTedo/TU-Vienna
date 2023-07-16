import java.awt.*;

public class MyTreeNode {
    Point data;
    Color color;
    MyTreeNode left;
    MyTreeNode right;

    // We have key(Point) and value(Color) for each node
    public MyTreeNode(Point point, Color color) {
        this.data = point;
        this.color = color;
        this.left = null;
        this.right = null;
    }
    public int numberOfFullNodes() {
        int counter = 0;

        if (left != null && right != null) {
            return 1 + left.numberOfFullNodes() + right.numberOfFullNodes();
        }
        if (left != null) {
            return left.numberOfFullNodes();
        }
        if (right != null) {
            return right.numberOfFullNodes();
        }
        return 0;
    }
}
