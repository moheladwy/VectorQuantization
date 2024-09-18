package Logic;

import Models.Dimension;
import Models.Vector;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VectorQuantization {
    private final File originalImage;
    private File compressedImage;
    private final BufferedImage originalBufferedImage;
    private BufferedImage compressedBufferedImage;
    private ArrayList<Vector> codebook;
    private int codebookSize;
    private int codebookSizeBits;
    private final Dimension optimalVectorSize;
    private ArrayList<Vector> vectors;
    private ArrayList<Integer> compressedVectors;

    public VectorQuantization(File originalImage) {
        this.originalImage = originalImage;
        this.originalBufferedImage = ImageHelper.convertImageToBufferedImage(originalImage);
        this.optimalVectorSize = ImageHelper.calculateOptimalVectorSize(originalBufferedImage);
        setVectors(ImageHelper.initializeVectors(originalBufferedImage, optimalVectorSize));
        setCodebookSize();
        setCodebook();
        setCompressedVectors();
        setCompressedImage();
    }

    private void setCompressedVectors() {
        compressedVectors = new ArrayList<>();
        for (Vector vector : vectors) {
            int averageIndex = getBestCodebookIndex(vector, codebook);
            compressedVectors.add(averageIndex);
        }
    }

    private void setVectors(ArrayList<Vector> vectors) {
        if (vectors == null || vectors.isEmpty())
            throw new NullPointerException("Vectors cannot be null or empty.");
        this.vectors = vectors;
    }

    public static BufferedImage compress(File image) {
        if (image == null)
            throw new NullPointerException("Image cannot be null.");
        VectorQuantization vectorQuantization = new VectorQuantization(image);
        vectorQuantization.compress();
        vectorQuantization.compressedBufferedImage = decompress(vectorQuantization.originalImage);
        return vectorQuantization.compressedBufferedImage;
    }

    private void compress() {
            try {
            FileOutputStream fileOutputStream = new FileOutputStream(ImageHelper.getCompressedImageName(originalImage));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(originalBufferedImage.getHeight());
            objectOutputStream.writeObject(originalBufferedImage.getWidth());
            objectOutputStream.writeObject(optimalVectorSize.height());
            objectOutputStream.writeObject(optimalVectorSize.width());
            objectOutputStream.writeObject(codebookSize);
            for (Vector vector : codebook) {
                objectOutputStream.writeObject(vector.getPixels());
            }
            objectOutputStream.writeObject(compressedVectors);
            objectOutputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } }

    public static BufferedImage decompress(File originalImage) {
        try {
            InputStream file = new FileInputStream(ImageHelper.getCompressedImageName(originalImage));
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            int height = (int) input.readObject();
            int width = (int) input.readObject();
            Dimension originalDimension2D = new Dimension(width, height);
            int vectorHeight = (int) input.readObject();
            int vectorWidth = (int) input.readObject();
            Dimension vectorDimension2D = new Dimension(vectorWidth, vectorHeight);
            int codebookSize = (int) input.readObject();
            ArrayList<Vector> codebook = new ArrayList<>();
            for (int i = 0; i < codebookSize; i++)
                codebook.add(new Vector((int[][][]) input.readObject(), vectorDimension2D));
            ArrayList<Integer> compressedVectors = (ArrayList<Integer>) input.readObject();

            return reconstruct(originalDimension2D, vectorDimension2D, codebook, compressedVectors);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedImage reconstruct(Dimension originalDimension, Dimension vectorDimension,
                                             ArrayList<Vector> codebook, ArrayList<Integer> compressedVectors) {
        ArrayList<Vector> vectors = new ArrayList<>();
        for (int index : compressedVectors)
            vectors.add(codebook.get(index));

        int[][][] pixels = new int[originalDimension.height()][originalDimension.width()][3];
        for (int i = 0; i < originalDimension.height(); i++) {
            int[][] row = new int[originalDimension.width()][3];
            for (int j = 0; j < originalDimension.width(); j++) {
                int vectorIndex = (i / vectorDimension.height()) * (originalDimension.width() / vectorDimension.width()) + (j / vectorDimension.width());
                Vector vector = vectors.get(vectorIndex);
                int vectorHeightIndex = i % vectorDimension.height();
                int vectorWidthIndex = j % vectorDimension.width();
                for (int k = 0; k < 3; k++) {
                    row[j][k] = vector.getPixel(vectorHeightIndex, vectorWidthIndex, k);
                }
            }
            pixels[i] = row;
        }
        return ImageHelper.convertPixelsToBufferedImage(pixels, originalDimension);
    }

    private void setCodebookSize() {
        this.codebookSizeBits = 5;
        this.codebookSize = (int) Math.pow(2, codebookSizeBits);
    }

    private void setCodebook() {
        ArrayList<Vector> averages = new ArrayList<>();
        List<List<Vector>> LBGs = Vector.splitLBG(codebookSizeBits, vectors, optimalVectorSize);

        for (List<Vector> list : LBGs)
            averages.add(Vector.average(list, optimalVectorSize));

        ArrayList<Vector>[] newLBG;
        while (true) {
            newLBG = new ArrayList[averages.size()];
            for (int i = 0; i < averages.size(); i++)
                newLBG[i] = new ArrayList<>();
            for (Vector vector : vectors) {
                int averageIndex = getBestCodebookIndex(vector, averages);
                newLBG[averageIndex].add(vector);
            }
            boolean isSame = true;
            for (int i = 0; i < averages.size(); i++) {
                Vector currentAverage = averages.get(i);
                Vector newAverage = Vector.average(newLBG[i], optimalVectorSize);
                if (!currentAverage.equals(newAverage)) isSame = false;
                averages.set(i, newAverage);
            }
            if (isSame) break;
        }
        codebook = averages;
    }

    private static int getBestCodebookIndex(Vector vector, ArrayList<Vector> books) {
        int averageIndex = -1;
        int distance = Integer.MAX_VALUE;
        for (int i = 0; i < books.size(); i++) {
            double currentDistance = books.get(i).calculateDistance(vector);
            if (currentDistance < distance) {
                distance = (int) currentDistance;
                averageIndex = i;
            }
        }
        return averageIndex;
    }

    private void setCompressedImage() {
        String path = originalImage.getAbsolutePath();
        String extension = path.substring(path.lastIndexOf(".") + 1);
        String name = path.substring(0, path.lastIndexOf(".")) + "_compressed." + extension;
        compressedImage = new File(name);
    }
}
