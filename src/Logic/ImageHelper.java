package Logic;
import Models.Dimension;
import Models.Vector;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageHelper {
    public static BufferedImage convertImageToBufferedImage(File image) {
        try {
            return ImageIO.read(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage convertPixelsToBufferedImage(int[][] pixels, Dimension dimension) {
        BufferedImage bufferedImage = new BufferedImage(dimension.getWidth(), dimension.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < dimension.getHeight(); i++) {
            for (int j = 0; j < dimension.getWidth(); j++)
                bufferedImage.setRGB(j, i, pixels[i][j] << 16 | pixels[i][j] << 8 | pixels[i][j]);
        }
        return bufferedImage;
    }

    public static void saveImage(BufferedImage bufferedImage, File output) {
        try {
            String extension = output.getName().substring(output.getName().lastIndexOf(".") + 1);
            ImageIO.write(bufferedImage, extension, output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isPrimeNumber(int number) {
        if (number <= 1)
            return false;
        for (int i = 2; i <= number / 2; ++i)
            if (number % i == 0)
                return false;
        return true;
    }

    private static int optimalVectorSize(int number) {
        for (int i = 4; i < number; i++) {
            if (number % i != 0)
                continue;
            return i;
        }
        return 0;
    }

    public static Dimension calculateOptimalVectorSize(BufferedImage image) {
        boolean isHeightPrime = isPrimeNumber(image.getHeight());
        boolean isWidthPrime = isPrimeNumber(image.getWidth());
        int originalHeight = isHeightPrime ? image.getHeight() - 1 : image.getHeight();
        int originalWidth = isWidthPrime ? image.getWidth() - 1 : image.getWidth();
        int vectorHeight = optimalVectorSize(originalHeight);
        int vectorWidth = optimalVectorSize(originalWidth);
        return new Dimension(vectorWidth, vectorHeight);
    }

    public static ArrayList<Vector> initializeVectors(BufferedImage image, Dimension vectorSize) {
        ArrayList<Vector> vectors = new ArrayList<>();
        for (int i = 0; i < image.getHeight(); i += vectorSize.getHeight()) {
            for (int j = 0; j < image.getWidth(); j += vectorSize.getWidth()) {
                int[][] pixels = new int[vectorSize.getHeight()][vectorSize.getWidth()];
                for (int height = i; height < i + vectorSize.getHeight(); height++) {
                    for (int width = j; width < j + vectorSize.getWidth(); width++) {
                        int rgb = image.getRGB(width, height);
                        int greyValue = (rgb >> 16) & 0xff;
                        pixels[height - i][width - j] = greyValue;
                    }
                }
                vectors.add(new Vector(pixels, vectorSize));
            }
        }
        return vectors;
    }

    public static String getCompressedImageName(File image) {
        String name = image.getName().substring(0, image.getName().lastIndexOf(".")) + ".dat";
        return "src/CompressedImages/" + name;
    }

    public static String getDecompressedImageName(File image) {
        String extension = image.getName().substring(image.getName().lastIndexOf(".") + 1);
        String name = image.getName().substring(0, image.getName().lastIndexOf(".")) + "_decompressed." + extension;
        return "src/DecompressedImages/" + name;
    }
}
