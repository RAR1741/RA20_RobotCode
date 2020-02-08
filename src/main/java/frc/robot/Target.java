package frc.robot;

public class Target {
    private double topLeftX;
    private double topLeftY;
    private double bottomRightX;
    private double bottomRightY;

    /**
     * Constructor
     * 
     * @param topLeftX X-coordinate of the top left edge.
     * @param topLeftY Y-coordinate of the top left edge.
     * @param bottomRightX X-coordinate of the bottom right edge.
     * @param bottomRightY Y-coordinate of the bottom right edge.
     */
    public Target(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
    }

    /**
     * @return X-coordinate of the target.
     */
    public double getCenterX() {
        return (topLeftX + bottomRightX) / 2;
    }

    /**
     * @return the area of the target.
     */
    public double getArea() {
        return (bottomRightX -topLeftX) * (topLeftY - bottomRightY);
    }
}