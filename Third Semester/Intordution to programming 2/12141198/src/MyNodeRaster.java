public class MyNodeRaster {
    RasterRGBA data;
    MyNodeRaster next;
    MyNodeRaster prev;

    public MyNodeRaster(RasterRGBA rasterRGBA) {
        this.data = rasterRGBA;
        this.next = null;
        this.prev = null;
    }
}
