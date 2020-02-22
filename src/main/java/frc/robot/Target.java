package frc.robot;

public class Target {

    private final double SCREEN_AREA = 76800;

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

    /**
     * Gets perentage of the screen taken up by the target.

     * @return percentage of screen taken up by the target.
     */
    public double getAreaPercent() {
        return getArea()/SCREEN_AREA;
    }

    /**
     * Gets percentage of the screen not taken up by the target.
     * 
     * @return percentage of screen not taken up by the target.
     */
    public double getInvertedAreaPercent() {
        return (SCREEN_AREA - getArea())/SCREEN_AREA;
    }
}