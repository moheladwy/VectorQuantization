package Models;
import java.util.ArrayList;
import java.util.List;

public class Vector {
    private ArrayList<ArrayList<Double>> pixels;
    private final Dimension dimension;

    public Vector(ArrayList<ArrayList<Double>> pixels, Dimension dimension) {
        if (dimension == null)
            throw new NullPointerException("Dimension cannot be null.");
        this.dimension = dimension;
        setPixels(pixels);
    }

    public void setPixels(ArrayList<ArrayList<Double>> pixels) {
        if (pixels == null || pixels.isEmpty() || pixels.get(0).isEmpty())
            throw new NullPointerException("Pixels cannot be null or empty.");
        if (pixels.size() != dimension.getHeight() || pixels.get(0).size() != dimension.getWidth())
            throw new IllegalArgumentException("Pixels must be of size " + dimension.getHeight() + "x" + dimension.getWidth() + "!");
        this.pixels = pixels;
    }

    public ArrayList<ArrayList<Double>> getPixels() {
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

    public boolean hasSameDimension(Vector anotherVector) {
        if (anotherVector == null)
            throw new NullPointerException("Vector cannot be null.");
        return dimension.equals(anotherVector.getDimension());
    }

    public Vector add(Vector anotherVector) {
        if (anotherVector == null)
            throw new NullPointerException("Vector cannot be null.");
        if (hasSameDimension(anotherVector))
            throw new IllegalArgumentException("Vector must be of size " + dimension.getWidth() + "x" + dimension.getHeight() + "!");
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        for (int i = 0; i < dimension.getHeight(); i++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < dimension.getWidth(); j++)
                row.add(pixels.get(i).get(j) + anotherVector.getPixels().get(i).get(j));
            result.add(row);
        }
        return new Vector(result, dimension);
    }

    public static Vector average(List<Vector> list) {
        if (list == null || list.isEmpty())
            throw new NullPointerException("List cannot be null or empty.");
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        Dimension dimension = list.get(0).getDimension();
        for (int i = 0; i < dimension.getHeight(); i++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < dimension.getWidth(); j++) {
                double sum = 0;
                for (Vector vector : list)
                    sum += vector.getPixels().get(i).get(j);
                row.add(sum / list.size());
            }
            result.add(row);
        }
        return new Vector(result, dimension);
    }
}
