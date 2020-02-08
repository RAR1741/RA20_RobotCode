package frc.robot;

public class Target {
    private double topLeftX;
    private double topLeftY;
    private double bottomRightX;
    private double bottomRightY;

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