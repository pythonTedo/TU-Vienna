public class MyIterator implements RasterizedRGBIterator{

    private int size;
    private int index;
    private MultiLayerRasterRGBA layeredRasterRGBA;
    private RasterizedRGB back;

    MyIterator(MultiLayerRasterRGBA layeredRasterRGBA){
        this.layeredRasterRGBA = layeredRasterRGBA;
        this.size = layeredRasterRGBA.numberOfLayers();
        this.index = 0;
        this.back = layeredRasterRGBA.getBackground();

    }
    @Override
    public RasterizedRGB next() {

        RasterizedRGB back = layeredRasterRGBA.getBackground();
        if(index == 0){
            this.index++;
            this.back = layeredRasterRGBA.getForeground();

            return this.back;
        }
        else{
            this.index++;
            this.back = ((Layered) back).getBackground();
            return this.back;
        }
    }

    @Override
    public boolean hasNext() {
        if(index < size){
            return true;
        }
        return false;
    }
}
