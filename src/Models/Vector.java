package Models;
import java.util.ArrayList;
import java.util.List;

public class Vector {
    private int[][] pixels;
    private final Dimension dimension;

    public Vector(int[][] pixels, Dimension dimension) {
        if (dimension == null)
            throw new NullPointerException("Dimension cannot be null.");
        this.dimension = dimension;
        setPixels(pixels);
    }

    public void setPixels(int[][] pixels) {
        if (pixels == null)
            throw new NullPointerException("Pixels cannot be null.");
        if (pixels.length != dimension.getHeight() || pixels[0].length != dimension.getWidth())
            throw new IllegalArgumentException("Pixels must be of size " + dimension.getHeight() + "x" + dimension.getWidth() + "!");
        this.pixels = pixels;
    }

    public void setPixel(int i, int j, int value) {
        if (i < 0 || i >= dimension.getHeight() || j < 0 || j >= dimension.getWidth())
            throw new IndexOutOfBoundsException("Index out of bounds.");
        pixels[i][j] = value;
    }

    public int getPixel(int i, int j) {
        if (i < 0 || i >= dimension.getHeight() || j < 0 || j >= dimension.getWidth())
            throw new IndexOutOfBoundsException("Index out of bounds.");
        return pixels[i][j];
    }

    public int[][] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return dimension.getWidth();
    }

    public int getHeight() {
        return dimension.getHeight();
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int calculateDistance(Vector vector) {
        if (vector == null)
            throw new NullPointerException("Vector cannot be null.");
        int sum = 0;
        for (int i = 0; i < dimension.getHeight(); i++)
            for (int j = 0; j < dimension.getWidth(); j++)
                sum += (int) ((getPixel(i, j) - vector.getPixel(i, j)) * (getPixel(i, j) - vector.getPixel(i, j)));
        return sum;
    }

    public static Vector average(List<Vector> list, Dimension dimension) {
        Vector result = new Vector(new int[dimension.getHeight()][dimension.getWidth()], dimension);
        if (list.isEmpty()) return result;
        for (Vector vector : list) {
            for (int i = 0; i < dimension.getHeight(); i++) {
                for (int j = 0; j < dimension.getWidth(); j++) {
                    result.setPixel(i, j, (result.getPixel(i, j) + vector.getPixel(i, j)));
                }
            }
        }
        for (int i = 0; i < dimension.getHeight(); i++) {
            for (int j = 0; j < dimension.getWidth(); j++) {
                result.setPixel(i, j, result.getPixel(i, j) / list.size());
            }
        }
        return result;
    }

    public static List<Vector> split(Vector vector) {
        if (vector == null)
            throw new NullPointerException("Vector cannot be null.");
        ArrayList<Vector> result = new ArrayList<>();
        for (int i = 0; i < vector.getHeight(); i++) {
            for (int j = 0; j < vector.getWidth(); j++) {
                Vector newVector = new Vector(new int[vector.getHeight()][vector.getWidth()], vector.getDimension());
                for (int x = 0; i < vector.getHeight(); i++) {
                    for (int y = 0; j < vector.getWidth(); j++)
                        newVector.setPixel(x, y, vector.getPixel(x + i, y + j));
                }
                result.add(newVector);
            }
        }
        return result;
    }

    public boolean equals(Vector vector) {
        if (vector == null)
            throw new NullPointerException("Vector cannot be null.");
        if (vector.getHeight() != getHeight() || vector.getWidth() != getWidth())
            return false;
        for (int i = 0; i < dimension.getHeight(); i++)
            for (int j = 0; j < dimension.getWidth(); j++)
                if (getPixel(i, j) != vector.getPixel(i, j))
                    return false;
        return true;
    }
    
    public static List<List<Vector>> splitLBG(int level, List<Vector> vectors, Dimension dimension) {
        ArrayList<List<Vector>> result = new ArrayList<>();
        if (level == 0) {
            result.add(vectors);
            return result;
        }
        ArrayList<Vector> smaller = new ArrayList<>();
        ArrayList<Vector> larger = new ArrayList<>();

        Vector avgFirst = Vector.average(vectors, dimension);
        Vector avgSecond = Vector.average(vectors, dimension);

        for (int i = 0; i < avgSecond.getHeight(); i++) {
            for (int j = 0; j < avgSecond.getWidth(); j++) {
                avgSecond.setPixel(i, j, avgSecond.getPixel(i, j) + 1);
            }
        }

        for (Vector vector : vectors) {
            if (vector.calculateDistance(avgFirst) > vector.calculateDistance(avgSecond))
                larger.add(vector);
            else
                smaller.add(vector);
        }

        result.addAll(splitLBG(level - 1, smaller, dimension));
        result.addAll(splitLBG(level - 1, larger, dimension));

        return result;
    }
}
