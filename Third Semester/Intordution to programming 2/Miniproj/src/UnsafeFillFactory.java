import java.awt.*;
import java.util.Scanner;

// A factory that creates a 'UnsafeFillOperation' object.
//
public class UnsafeFillFactory implements UnsafeFactory {

    // TODO: define missing parts of this class.

    // TODO: add constructor specification.
    public UnsafeFillFactory(Color[] c) {

        // TODO: implement constructor.
        Color[] colors = new Color[c.length];
        for (int i = 0; i < c.length; i++) {
            colors[i] = c[i];
        }
    }

    // TODO: add method specification.
    public UnsafeFillOperation create(Scanner sc) {

        // TODO: implement method.
        if (sc != null) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int w = sc.nextInt();
            int h = sc.nextInt();
            int c = sc.nextInt();
            return new UnsafeFillOperation(x, y, w, h, c);
        }

        return null;
    }
}
