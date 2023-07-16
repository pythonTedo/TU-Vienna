public class MyLayerdrasterIterator implements RasterizedRGBIterator {
    private int activeLayer;
    private LayeredRasterRGBA iter;

    MyLayerdrasterIterator(LayeredRasterRGBA iter) {
        this.iter = iter;
        this.activeLayer = 0;
    }

    @Override
    public RasterizedRGB next() {
        LayeredRasterRGBA temp = new LayeredRasterRGBA(this.iter.getWidth(), this.iter.getHeight());

        RasterRGBA tempras;
        if (this.iter.numberOfLayers() >= this.activeLayer) {
            tempras = this.iter.getLayer(this.activeLayer);
        }
        else {
            tempras = null;
        }
        this.activeLayer++;

        return tempras;
    }

    @Override
    public boolean hasNext() {
        if (this.iter.numberOfLayers() > this.activeLayer) {
            return true;
        }
        return false;
    }
}
