package Logic;
import Models.Vector;
import java.io.File;
import java.util.HashMap;

public class VectorQuantization {
    private File originalImage;
    private File compressedImage;
    private HashMap<Integer, Vector> codebook;

    public static File compress(File image) {
        // TODO: Implement compression algorithm.
        return null;
    }

    private void decompress() {
        // TODO: Implement decompression algorithm.
    }

    public static void saveCompressedImage(File compressedImage) {
        // TODO: Implement saving compressed image.
    }

    private File getOriginalImage() {
        return originalImage;
    }

    private File getCompressedImage() {
        return compressedImage;
    }
}
