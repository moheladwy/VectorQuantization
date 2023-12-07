package Models;

public class Dimension {
    private final int width;
    private final int height;

    public Dimension(int width, int height) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Width and height must be positive integers only!");
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
