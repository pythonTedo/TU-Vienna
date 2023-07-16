import codedraw.CodeDraw;
import java.awt.*;

public class PhotoApp1 {

    public static void main(String[] args) {

        // TODO: change this method according to 'Aufgabenblatt1.md'.
        SimpleRasterRGB newimg = new SimpleRasterRGB(40, 60);
        newimg.drawLine(0, 1, 35, 9, new Color(20, 25, 250));
        newimg.drawLine(30, 5, 0, 30, Color.ORANGE);
        newimg.drawLine(2, 0, 7, 40, Color.GREEN);
        draw(newimg);

        SimpleRasterRGB newimg2 = newimg.convolve(new double[][]{
                {0.077847, 0.123317, 0.077847},
                {0.123317, 0.195346, 0.123317},
                {0.077847, 0.123317, 0.077847}
        });
        draw(newimg2);

        draw(newimg.enlarge3x());
    }

    // Draws the image with fixed pixel size in a new window.
    public static void draw(SimpleRasterRGB newimg) {

        // TODO: change this method according to 'Aufgabenblatt1.md'.
        int cellSize = 10;
        CodeDraw cd = new CodeDraw(newimg.getWidth() * cellSize, newimg.getHeight() * cellSize);

        // draw a square of size 'cellSize' for each pixel
        for (int j = 0; j < newimg.getHeight(); j++) {
            for (int i = 0; i < newimg.getWidth(); i++) {
                int x = i * cellSize;
                int y = j * cellSize;
                cd.setColor(newimg.getPixelColor(i, j));
                cd.fillSquare(x, y, cellSize);
            }
        }
        cd.show();

    }
}
