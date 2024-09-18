package Models;

public record Dimension(int width, int height) {
    public Dimension {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Width and height must be positive integers only!");
    }
}
