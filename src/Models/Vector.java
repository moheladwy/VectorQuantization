package Models;
import java.util.ArrayList;
import java.util.List;

public class Vector {
    private int[][][] pixels;
    private Dimension dimension;

    public Vector(int[][][] pixels, Dimension dimension) {
        setDimension(dimension);
        setPixels(pixels);
    }

    public boolean equals(Vector vector) {
        if (vector == null)
            throw new NullPointerException("Vector cannot be null!");
        if (vector.getHeight() != dimension.getHeight() || vector.getWidth() != dimension.getWidth())
            return false;
        for (int i = 0; i < dimension.getHeight(); i++)
            for (int j = 0; j < dimension.getWidth(); j++)
                for (int k = 0; k < 3; k++)
                    if (pixels[i][j][k] != vector.getPixel(i, j, k))
                        return false;
        return true;
    }

    public int calculateDistance(Vector vector) {
        if (vector == null)
            throw new NullPointerException("Vector cannot be null.");
        int sum = 0;
        for (int i = 0; i < dimension.getHeight(); i++)
            for (int j = 0; j < dimension.getWidth(); j++)
                for (int k = 0; k < 3; k++)
                    sum += ((getPixel(i, j, k) - vector.getPixel(i, j, k)) * (getPixel(i, j, k) - vector.getPixel(i, j, k)));
        return sum;
    }

    public static Vector average(List<Vector> list, Dimension dimension) {
        Vector result = new Vector(new int[dimension.getHeight()][dimension.getWidth()][3], dimension);
        if (list.isEmpty()) return result;
        for (Vector vector : list) {
            for (int i = 0; i < dimension.getHeight(); i++) {
                for (int j = 0; j < dimension.getWidth(); j++) {
                    for (int k = 0; k < 3; k++) {
                        result.setPixel(i, j, k, (result.getPixel(i, j, k) + vector.getPixel(i, j, k)));
                    }
                }
            }
        }
        for (int i = 0; i < dimension.getHeight(); i++) {
            for (int j = 0; j < dimension.getWidth(); j++) {
                for (int k = 0; k < 3; k++) {
                    result.setPixel(i, j, k, result.getPixel(i, j, k) / list.size());
                }
            }
        }
        return result;
    }

    public static List<List<Vector>> splitLBG(int level, List<Vector> vectors, Dimension dimension) {
        ArrayList<List<Vector>> result = new ArrayList<>();
        if (level == 0) {
            result.add(vectors);
            return result;
        }
        List<Vector> smaller = new ArrayList<>();
        List<Vector> larger = new ArrayList<>();

        Vector avgFirst = Vector.average(vectors, dimension);
        Vector avgSecond = Vector.average(vectors, dimension);

        for (int i = 0; i < dimension.getHeight(); i++)
            for (int j = 0; j < dimension.getWidth(); j++)
                for (int k = 0; k < 3; k++)
                    avgSecond.setPixel(i, j, k, avgSecond.getPixel(i, j, k) + 1);

        for (Vector vector : vectors) {
            if (vector.calculateDistance(avgFirst) > vector.calculateDistance(avgSecond))
                larger.add(vector);
            else smaller.add(vector);
        }

        result.addAll(splitLBG(level - 1, smaller, dimension));
        result.addAll(splitLBG(level - 1, larger, dimension));
        return result;
    }

    public int[][][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][][] pixels) {
        if (pixels == null)
            throw new NullPointerException("Pixels cannot be null!");
        if (pixels.length != dimension.getHeight() || pixels[0].length != dimension.getWidth())
            throw new IllegalArgumentException("The Dimension Height and Width must be equal to the pixel Height and Width!");
        this.pixels = pixels;
    }

    public void setDimension(Dimension dimension) {
        if (dimension == null)
            throw new NullPointerException("Dimension cannot be null");
        this.dimension = dimension;
    }

    public int getPixel(int i, int j, int k) {
        if (i < 0 || i >= dimension.getHeight() || j < 0 || j >= dimension.getWidth() || k < 0 || k > 2)
            throw new IllegalArgumentException("Index out of bounds.");
        return pixels[i][j][k];
    }

    public void setPixel(int i, int j, int k, int value) {
        if (i < 0 || i >= dimension.getHeight() || j < 0 || j >= dimension.getWidth() || k < 0 || k > 2)
            throw new IllegalArgumentException("Index out of bounds.");
        pixels[i][j][k] = value;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getWidth() {
        return dimension.getWidth();
    }

    public int getHeight() {
        return dimension.getHeight();
    }
}
