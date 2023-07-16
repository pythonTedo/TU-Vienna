import java.awt.*;
// Represents a raster consisting of at least two layers.
//
public class MultiLayerRasterRGBA implements Layered // implements Layered //TODO: activate clause.
{

    // TODO: define missing parts of this class.
    SingleLayer foreground;
    RasterizedRGB background;
    int size;

    // Initializes 'this' with top-layer 'foreground' and 'background'.
    // Performs dynamic type checking of 'background'. If 'background' is an instance of 'Layered'
    // this constructor initializes 'this' with top-layer 'foreground' and layers of the
    // 'background'.
    // If 'background' is not an instance of 'Layered', 'background' is copied to a new object of
    // 'SingleLayer' which is then used to initialize the background.
    // Width and height of this raster is determined by width and height of the 'foreground'
    // raster.
    // Pixels that are not defined in the 'background' are assumed to have color (0,0,0,0).
    public MultiLayerRasterRGBA(SingleLayer foreground, RasterizedRGB background) {

        // TODO: implement constructor.
        if(background instanceof Layered){
            this.foreground = foreground;
            this.background = background;

            this.size = 1 + ((Layered) background).numberOfLayers();
        }else{
            this.foreground = foreground;
            SingleLayer temp = new TreeSparseRasterRGBA(foreground.getWidth(), foreground.getHeight());
            this.background = temp;
            this.size = 2;
        }
    }

    @Override
    public Layered newLayer() {

        return new MultiLayerRasterRGBA(new TreeSparseRasterRGBA(this.foreground.getWidth(), this.foreground.getHeight()), this);
    }

    @Override
    public int numberOfLayers() {
        return this.size;
    }

    @Override
    public SingleLayer getForeground() {
        return this.foreground;
    }

    @Override
    public Layered getBackground() {
        return (Layered) this.background;
    }

    @Override
    public int getWidth() {
        return this.background.getWidth();
    }

    @Override
    public int getHeight() {
        return this.background.getHeight();
    }

    @Override
    public Color getPixelColor(int x, int y) {
        Color color = this.foreground.getPixelColor(x, y);
        if (foreground != null){
            return color = RasterRGBA.over(color, this.background.getPixelColor(x, y));
        }
        else {
            return color = this.background.getPixelColor(x, y);
        }
    }

    @Override
    public void setPixelColor(int x, int y, Color color) {
        this.foreground.setPixelColor(x, y, color);
    }

    @Override
    public void convolve(double[][] filterKernel) {
        this.foreground.convolve(filterKernel);

    }

    @Override
    public void crop(int width, int height) {

    }
    //The iterator should iterate over all levels of the grid, starting with the
    //top level. Each iteration returns the next level down, down to the bottom
    //Level. If there is no further level, the next iteration returns 'null'.
    @Override
    public RasterizedRGBIterator iterator() {

        return new MyIterator(this);
    }
}
