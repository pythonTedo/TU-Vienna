import java.awt.*;
import java.util.Scanner;

// A factory that creates an 'UnsafeLineOperation' object.
//
public class UnsafeLineFactory implements UnsafeFactory {

    //TODO: declare variables.

    // Initializes 'this' with an array 'color'.
    // 'color' contains the default color as an array entry (color[0]). An array is
    // used because it enables the default color to be changed by other classes after 'this'
    // has been initialized. Other entries in the specified array (except color[0]) are ignored.
    // Precondition: color != null && color.length > 0.
    private Color defcol;

    public UnsafeLineFactory(Color[] color) {

        //TODO: implement constructor.
        if (color != null && color.length > 0) {
            this.defcol = color[0];
        }
    }

    // Returns a new 'UnsafeLineOperation' object. The coordinates for the starting point and end
    // point are provided by the scanner object 'sc'.
    @Override
    public UnsafeOperation create(Scanner sc) {

        // TODO: implement method.
        if (sc != null) {
            int x1 = sc.nextInt();
            int y1 = sc.nextInt();
            int x2 = sc.nextInt();
            int y2 = sc.nextInt();
            Color color = this.defcol;
            if (sc.hasNext()) {
                color = Color.decode(sc.next());
            }
            return new UnsafeLineOperation(x1, y1, x2, y2, color);
        }
        return null;
    }
}
