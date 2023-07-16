import codedraw.CodeDraw;
import java.awt.*;

public class PhotoApp2 {

    public static void main(String[] args) {

        // TODO: implement method according to 'Aufgabenblatt2.md'.
        SimpleSparseRasterRGB newimg = new SimpleSparseRasterRGB(40, 60);
        newimg.drawLine(0, 1, 35, 9, new Color(20, 25, 250));
        newimg.drawLine(30, 5, 0, 30, Color.ORANGE);
        newimg.drawLine(2, 0, 7, 40, Color.GREEN);
        newimg.floodFill(7, 7, Color.CYAN);
        //draw(newimg);

        SimpleSparseRasterRGB newimg2 = newimg.convolve(new double[][]{
                {0.077847, 0.123317, 0.077847},
                {0.123317, 0.195346, 0.123317},
                {0.077847, 0.123317, 0.077847}
        });
        //draw(newimg2);

        newimg2.floodFill(7, 7, Color.BLACK);
        //draw(newimg2);

        SimpleSparseRasterRGB newimg3 = new SimpleSparseRasterRGB(40, 60);

        SimplePointQueue queue = new SimplePointQueue(4);
        queue.add(new Point(10, 10));
        queue.add(new Point(20, 20));
        queue.add(new Point(10, 30));
        queue.add(new Point(5, 20));
        queue.add(new Point(10, 10));
        newimg3.drawConnectedLines(queue, Color.GREEN);
        draw(newimg3);
    }

    // Draws the image (specified by a sparse raster) with fixed pixel size in a new window.
    // Precondition: raster != null.
    public static void draw(SimpleSparseRasterRGB raster) {

        int cellSize = 10;
        CodeDraw cd = new CodeDraw(raster.getWidth() * cellSize, raster.getHeight() * cellSize);

        // draw a square of size 'cellSize' for each pixel
        for (int j = 0; j < raster.getHeight(); j++) {
            for (int i = 0; i < raster.getWidth(); i++) {
                int x = i * cellSize;
                int y = j * cellSize;
                cd.setColor(raster.getPixelColor(i, j));
                cd.fillSquare(x, y, cellSize);
            }
        }
        cd.show();
    }
}
