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

    public static BufferedImage convertPixelsToBufferedImage(int[][][] pixels, Dimension dimension2D) {
        BufferedImage bufferedImage = new BufferedImage(dimension2D.width(), dimension2D.height(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < dimension2D.height(); i++) {
            for (int j = 0; j < dimension2D.width(); j++) {
                int rgb = pixels[i][j][0] << 16 | pixels[i][j][1] << 8 | pixels[i][j][2];
                bufferedImage.setRGB(j, i, rgb);
            }
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
        for (int i = 2; i < number; i++) {
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
        for (int i = 0; i < image.getHeight(); i += vectorSize.height()) {
            for (int j = 0; j < image.getWidth(); j += vectorSize.width()) {
                int[][][] pixels = new int[vectorSize.height()][vectorSize.width()][3];
                for (int height = i; height < i + vectorSize.height(); height++) {
                    for (int width = j; width < j + vectorSize.width(); width++) {
                        int rgb = image.getRGB(width, height);
                        int redValue = (rgb >> 16) & 0xff;
                        int greenValue = (rgb >> 8) & 0xff;
                        int blueValue = rgb & 0xff;
                        pixels[height - i][width - j] = new int[]{redValue, greenValue, blueValue};
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
