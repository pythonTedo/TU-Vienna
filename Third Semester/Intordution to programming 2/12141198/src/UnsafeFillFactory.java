import java.awt.*;
import java.util.Scanner;

// A factory that creates a 'UnsafeFillOperation' object.
//
public class UnsafeFillFactory implements UnsafeFactory {

    // TODO: define missing parts of this class.

    // TODO: add constructor specification.
    Color[] c;
    public UnsafeFillFactory(Color[] c) {

        // TODO: implement constructor.
        this.c = c;
    }

    // TODO: add method specification.
    public UnsafeFillOperation create(Scanner sc) {

        // TODO: implement method.
        if (sc != null) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            Color color = this.c[0];
            return new UnsafeFillOperation();
        }
        return null;
    }
}
