import java.awt.*;
import java.util.Scanner;
import codedraw.CodeDraw;

public class demo {
    public static void main(String[] args) {
        System.out.println("Test 'HashPointColorMap':");
        HashPointColorMap map = new HashPointColorMap();
        map.put(new Point(5, 4), Color.BLUE);
        map.put(new Point(7, 1), Color.RED);
        map.put(new Point(6, 4), Color.BLUE);
        map.put(new Point(9, 2), Color.RED);
        map.put(new Point(9, 2), Color.YELLOW);

        RasterRGBA r1 = new RasterRGBA(40, 60);
        r1.clear(Color.BLACK);
        r1.drawLine(0, 1, 35, 9, new Color(20, 25, 250));
        r1.drawLine(30, 5, 0, 30, Color.ORANGE);
        r1.drawLine(2, 0, 7, 40, Color.GREEN);

        HashSparseRasterRGB copy = new HashSparseRasterRGB(40, 60);

        for (int i = 0; i < copy.getWidth(); i++) {
            for (int j = 0; j < copy.getHeight(); j++) {
                copy.setPixelColor(i, j, r1.getPixelColor(i, j));
            }
        }

        /*
        copy.convolve(new double[][]{
                {0.077847, 0.123317, 0.077847},
                {0.123317, 0.195346, 0.123317},
                {0.077847, 0.123317, 0.077847}
        });
        System.out.println(copy.getPixelColor(25, 8));

         */
    }
}
