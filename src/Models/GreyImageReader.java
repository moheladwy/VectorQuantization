package Models;//package Models;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;



public class GreyImageReader {

    public static double[][] convertImageTo2DArray(String imagePath) {
        try {

            File file = new File(imagePath);
            BufferedImage bufferedImage = ImageIO.read(file);

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            double[][] pixelValues = new double[width][height];

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = bufferedImage.getRGB(x, y);
                    int grayValue = (rgb >> 16) & 0xFF; // Red channel
                    pixelValues[x][y] = grayValue;
                }
            }

            return pixelValues;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<Vector> convertImageToVectors(String imagePath,int x,int y) {
        try {

            File file = new File(imagePath);
            BufferedImage bufferedImage = ImageIO.read(file);

            int width = x;
            int height = y;

            List<Vector> vectors = new ArrayList<Vector>();
            ArrayList<ArrayList<Double>> pixels;
            int currentX=0;
            int currentY=0;

            while (currentX < bufferedImage.getWidth()){
                while (currentY < bufferedImage.getHeight() && currentY+height <= bufferedImage.getHeight()){
                    pixels = new ArrayList<ArrayList<Double>>();
                    for (int i = currentY; i < currentY+height; i++) {
                        ArrayList<Double> row = new ArrayList<>();
                        for (int j = currentX; j < currentX+width; j++) {
                            int rgb = bufferedImage.getRGB(j, i);
                            int grayValue = (rgb >> 16) & 0xFF; // Red channel
                            row.add((double)grayValue);
                        }
                        pixels.add(row);
                    }
                vectors.add(new Vector(pixels,new Dimension(width,height)));
                currentY+=height;
            }
            currentX+=width;
             currentY=0;
          }


          return vectors;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
//    public static List<Vector> convertImageToVectors(double[][] image,int x,int y) {
//        try {
//            int width = x;
//            int height = y;
//
//            List<Vector> vectors = new ArrayList<Vector>();
//            ArrayList<ArrayList<Double>> pixels;
//
//            int currentX = 0;
//            int currentY = 0;
//
//            while (currentX < image.length) {
//                while (currentY < image[currentX].length && currentY + height <= image[currentX].length) {
//                    pixels = new ArrayList<ArrayList<Double>>();
//                    for (int i = currentY; i < currentY + height; i++) {
//                        ArrayList<Double> row = new ArrayList<>();
//                        for (int j = currentX; j < currentX + width; j++) {
//                            row.add((double) image[i][j]);
//                        }
//                        pixels.add(row);
//                    }
//                    vectors.add(new Vector(pixels, new Dimension(width, height)));
//                    currentY += height;
//                }
//                currentX += width;
//                currentY=0;
//            }
//            return vectors;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }



    public static void main(String[] args) {
        String imagePath = "D:\\FCAI\\Information theory and data compression\\Assignment4\\image.jpg"; // Replace with the actual path to your grayscale image
        double[][] image = convertImageTo2DArray(imagePath);
        List<Vector> vectors = convertImageToVectors(imagePath,4,4);
        for (Vector vector : vectors) {
            System.out.println(vector.getPixels());
            break;
        }
//            int[][] image = convertImageTo2DArray(imagePath);
//            for(int i=0;i<image.length;i++){
//                for(int j=0;j<image[0].length;j++){
//                    System.out.print(image[i][j]+" ");
//                }
//                System.out.println();
//            }
    }

}
