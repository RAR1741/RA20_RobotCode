package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class PowercellDetection {

    private final double SCREEN_AREA = 1; //TODO: Determine area camera view.

    private double nb;
    private double[] boxes;
    private double error;
    private double motorPower;

    private Drivetrain drive;

    NetworkTable detectionTable;

    /**
     * Constructor
     */
    public PowercellDetection (Drivetrain drive) {
        detectionTable = NetworkTableInstance.getDefault().getTable("ML");
        this.drive = drive;
    }

    public void update() {
        this.nb = detectionTable.getEntry("nb_objects").getDouble(0);
        this.boxes = detectionTable.getEntry("boxes").getDoubleArray(new double[]{0.0, 0.0, 0.0, 0.0});
        //TODO: Sort powercells based on area
    }

    /**
     * Gets number of powercells detected.
     * 
     * @return number of powercells detected
     */
    public double getNumber() {
        return nb;
    }

    /**
     * Gets X-coordinate of center of the first powercell.
     * 
     * @return X-coordinate of center of the first powercell
     */
    public double getCenterX() {
        return (boxes[0] + boxes[2])/2;
    }

    /**
     * Gets Area of the first powercell.
     * 
     * @return area of the first powercell
     */
    public double getArea() {
        return (boxes[2] - boxes[0]) * (boxes[1] - boxes[3]);
    }

    /**
     * Moves the robot to intercept powercells
     * 
     * @param x target X-coordinate
     */
    public void approachPC(double x) {
        //TODO: Determine division variable for percent of screen
        drive.driveLeft(x >= 0 ? 0.75 : 0.75 * ((SCREEN_AREA - getArea())/SCREEN_AREA)/4);
        drive.driveRight(x <= 0 ? 0.75 : 0.75 * ((SCREEN_AREA - getArea())/SCREEN_AREA)/4);
    }
}