import java.awt.*;


// This class represents a 2D raster of RGB color entries. The class uses
// the class 'SimpleDataBufferInt' to store the entries.
public class SimpleRasterRGB {

    // TODO: make these two variables 'private' and remove 'static'.
    private int width = 40;
    private int height = 60;
    private SimpleDataBufferInt buffer;

    // TODO: add more object variables, if needed.
    // Initialises this raster of the specified size with
    // all pixels being black, i.e. (R,G,B) = (0,0,0).
    // Preconditions: height > 0, width > 0
    public SimpleRasterRGB(int width, int height) {

        // TODO: implement constructor.
        if (width > 0 && height > 0) {
            this.height = height;
            this.width = width;
            this.buffer = new SimpleDataBufferInt(6, this.height*this.width);

        }
    }

    // Returns the color of the specified pixel.
    // Precondition: (x,y) is a valid coordinate of the raster
    public Color getPixelColor(int x, int y) {

        // TODO: modify method to become an object method (not static).
        return new Color(buffer.getElem(0, y * this.getWidth() + x),
                buffer.getElem(1, y * this.getWidth() + x),
                buffer.getElem(2, y * this.getWidth() + x));
    }

    // Sets the color of the specified pixel.
    // Precondition: (x,y) is a valid coordinate of the raster, color != null
    public void setPixelColor(int x, int y, Color color) {

        // TODO: modify method to become an object method (not static).
        buffer.setElem(0, y * this.getWidth() + x, color.getRed());
        buffer.setElem(1, y * this.getWidth() + x, color.getGreen());
        buffer.setElem(2, y * this.getWidth() + x, color.getBlue());
    }

    // Returns the result of convolution of 'this' with the specified filter kernel. 'this' is not
    // changed by the method call.
    // Precondition (needs to be checked):
    // filterKernel != 0 && filterKernel.length > 0 &&
    // filterKernel.length % 2 == 1 &&
    // filterKernel.length == filterKernel[0].length &&
    // filterKernel.length < this.getWidth() &&
    // filterKernel.length < this.getHeight().
    public SimpleRasterRGB convolve(double[][] filterKernel) {

        SimpleRasterRGB img = new SimpleRasterRGB(this.getWidth(), this.getHeight());

        double[] temp_result;
        int filterSideLength = filterKernel.length / 2;
        for (int x = filterSideLength; x < this.width - filterSideLength; x++) {
            for (int y = filterSideLength; y < this.height - filterSideLength; y++) {
                //at every array position, compute filter result
                temp_result = new double[3];
                for (int xx = -filterSideLength; xx <= filterSideLength; xx++) {
                    for (int yy = -filterSideLength; yy <= filterSideLength; yy++) {
                        temp_result[0] += this.getPixelColor(x - xx, y - yy).getRed() * filterKernel[xx + filterSideLength][yy + filterSideLength];
                        temp_result[1] += this.getPixelColor(x - xx, y - yy).getGreen() * filterKernel[xx + filterSideLength][yy + filterSideLength];
                        temp_result[2] += this.getPixelColor(x - xx, y - yy).getBlue() * filterKernel[xx + filterSideLength][yy + filterSideLength];
                    }
                }
                img.buffer.setElem(3, y * this.width + x, (int) temp_result[0]);
                img.buffer.setElem(4, y * this.width + x, (int) temp_result[1]);
                img.buffer.setElem(5, y * this.width + x, (int) temp_result[2]);
            }
        }

        img.buffer.getData()[0] = img.buffer.getData()[3];
        img.buffer.getData()[1] = img.buffer.getData()[4];
        img.buffer.getData()[2] = img.buffer.getData()[5];

        return img;
    }


    // Returns the result of convolution of the specified raster 'toBeFiltered' with the specified
    // filter kernel 'filterKernel'.
    // Precondition (needs not be checked):
    // filterKernel != null && filterKernel.length > 0 &&
    // filterKernel.length % 2 == 1 &&
    // filterKernel.length == filterKernel[0].length &&
    // filterKernel.length < this.getWidth() &&
    // filterKernel.length < this.getHeight().
    public static SimpleRasterRGB convolve(SimpleRasterRGB toBeFiltered, double[][] filterKernel) {

        SimpleRasterRGB img = toBeFiltered;

        SimpleRasterRGB temp = img.convolve(filterKernel);

        return temp;

    }

    /*
    // TODO: remove following method.

    // Returns the result of convolution of the RGB raster with pixel data stored in the first
    // three rows of SimpleDataBufferInt.data with the specified filter kernel (see
    // https://de.wikipedia.org/wiki/Faltungsmatrix).
    // The method assumes that the pixel (x,y) of the raster is stored at position
    // [component][y*SimpleRasterRGB.width+x] in the data buffer array, where
    // the data bank index 'component' is 0,1 and 2 for the red, green and blue component
    // respectively.
    // The method uses 3 additional data banks with indices 3 to 5 for calculations.
    // Precondition (needs not be checked):
    // SimpleDataBufferInt.data.length >= 6 &&
    // SimpleDataBufferInt.data[i].length >= SimpleRasterRGB.width * SimpleRasterRGB.height for
    // valid i.
    // filterKernel != null && filterKernel.length > 0 &&
    // filterKernel.length % 2 == 1 &&
    // filterKernel.length == filterKernel[0].length &&
    // filterKernel.length < SimpleRasterRGB.width &&
    // filterKernel.length < SimpleRasterRGB.height.
    public static void convolve(double[][] filterKernel) {

        double[] temp_result;

        int filterSideLength = filterKernel.length / 2;
        for (int x = filterSideLength; x < SimpleRasterRGB.width - filterSideLength; x++) {
            for (int y = filterSideLength; y < SimpleRasterRGB.height - filterSideLength; y++) {
                //at every array position, compute filter result
                temp_result = new double[3];
                for (int xx = -filterSideLength; xx <= filterSideLength; xx++) {
                    for (int yy = -filterSideLength; yy <= filterSideLength; yy++) {
                        temp_result[0] += SimpleRasterRGB.getPixelColor(x - xx, y - yy).getRed() * filterKernel[xx + filterSideLength][yy + filterSideLength];
                        temp_result[1] += SimpleRasterRGB.getPixelColor(x - xx, y - yy).getGreen() * filterKernel[xx + filterSideLength][yy + filterSideLength];
                        temp_result[2] += SimpleRasterRGB.getPixelColor(x - xx, y - yy).getBlue() * filterKernel[xx + filterSideLength][yy + filterSideLength];
                    }
                }

                SimpleDataBufferInt.setElem(3, y * SimpleRasterRGB.width + x, (int) temp_result[0]);
                SimpleDataBufferInt.setElem(4, y * SimpleRasterRGB.width + x, (int) temp_result[1]);
                SimpleDataBufferInt.setElem(5, y * SimpleRasterRGB.width + x, (int) temp_result[2]);
            }
        }
        SimpleDataBufferInt.data[0] = SimpleDataBufferInt.data[3];
        SimpleDataBufferInt.data[1] = SimpleDataBufferInt.data[4];
        SimpleDataBufferInt.data[2] = SimpleDataBufferInt.data[5];
    }
*/
    // Returns the width of this raster.
    public int getWidth() {

        // TODO: implement method.
        return this.width;
    }

    // Returns the height of this raster.
    public int getHeight() {

        // TODO: implement method.
        return this.height;
    }

    // Draws a line from (x1,y1) to (x2,y2) in the raster using the Bresenham algorithm.
    //Preconditions: (x1,y1) and (x2,y2) are valid coordinates of the raster, color != null
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {

        // TODO: modify method to become an object method (not static) operating on 'this'.
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (x1 != x2 || y1 != y2) {
            this.setPixelColor(x1, y1, color);

            int err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (err2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    public SimpleRasterRGB enlarge3x(){
        SimpleRasterRGB img = new SimpleRasterRGB(this.getWidth() * 3, this.getHeight() * 3);

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        img.setPixelColor(3* i+k, 3 * j+l, this.getPixelColor(i,j));
                    }

                }
            }
        }

        return img;
    }

    // Returns a mapping from all width*height pixel positions (Point) to corresponding colors
    // (Color) of the pixels. Values of color (0,0,0) are also included in the mapping.
    public TreePointColorMap asMap() {

        // TODO: implement method.
        TreePointColorMap map = new TreePointColorMap();
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                map.put(new Point(i,j), this.getPixelColor(i,j));
            }
        }
        return map;
    }

    public SimpleRasterRGB crop(int i, int i1, int width, int height) {
        SimpleRasterRGB img = new SimpleRasterRGB(width, height);
        for (int j = 0; j < width; j++) {
            for (int k = 0; k < height; k++) {
                img.setPixelColor(j,k, this.getPixelColor(j+i,k+i1));
            }
        }
        return img;
    }
}
