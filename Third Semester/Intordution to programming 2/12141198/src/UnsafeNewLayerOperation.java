// This class represents an operation creating a new top-most layer.
//
public class UnsafeNewLayerOperation implements UnsafeOperation {

    // TODO: specification of the method.
    @Override
    public RasterizedRGB execute(RasterizedRGB raster) {

        // TODO: implement method.
        if (raster instanceof Layered) {
            return ((Layered) raster).newLayer();
        }
        else {
            TreeSparseRasterRGBA r = new TreeSparseRasterRGBA(raster.getWidth(), raster.getHeight());
            raster.copyTo(r);
            return r.newLayer();
        }
    }
}
