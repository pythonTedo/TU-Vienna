// This class represents a convolution operation.
public class UnsafeConvolveOperation implements UnsafeOperation {

    //TODO: declare variables.

    // Initializes this convolution operation with the specified filter kernel.
    // Precondition:
    // filterKernel != null && filterKernel.length > 0 &&
    // filterKernel.length % 2 == 1 &&
    // filterKernel.length == filterKernel[i].length (for valid i).
    public UnsafeConvolveOperation(double[][] filterKernel) {

        // TODO: implement constructor.
    }

    // Returns the filter kernel of this convolution operation.
    public double[][] getKernel() {

        // TODO: implement method.
        return null;
    }

    @Override
    // Executes the convolution of 'raster' with the filter kernel getKernel().
    // The specified object is directly modified by this method call.
    // The returned raster is identical to the specified 'raster'.
    // Precondition:
    // filterKernel.length < raster.getWidth() &&
    // filterKernel.length < raster.getHeight().
    public RasterizedRGB execute(RasterizedRGB raster) {

        // TODO: implement method.
        return null;
    }
}
