package Models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RGBImageReader{

    public static int[][][] convertImageTo3DArray(String imagePath) {
        try {
            File file = new File(imagePath);
            BufferedImage bufferedImage = ImageIO.read(file);

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            int[][][] rgbValues = new int[width][height][3];

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = bufferedImage.getRGB(x, y);

                    rgbValues[x][y][0] = (rgb >> 16) & 0xFF; // Red channel
                    rgbValues[x][y][1] = (rgb >> 8) & 0xFF;  // Green channel
                    rgbValues[x][y][2] = rgb & 0xFF;         // Blue channel
                }
            }

            return rgbValues;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}

