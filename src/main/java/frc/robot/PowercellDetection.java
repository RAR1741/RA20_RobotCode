package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class PowercellDetection {

    private final double SCREEN_AREA = 1; //TODO: Determine area camera view.

    private double nb;
    private double[] boxes;
    private double[][] targets;
    private double[] tempTarget;
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
        this.sortTargets(this.boxes);
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
        return (targets[0][0] + targets[0][2]) / 2;
    }

    /**
     * Gets Area of the first powercell.
     * 
     * @return area of the first powercell
     */
    public double getArea() {
        return (targets[0][2] - targets[0][0]) * (targets[0][1] - targets[0][3]);
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

    /**
     * Sorts an array of box coordinates into a 2-D array sorted by the area of those boxes.
     * 
     * @param boxes array of box coordinates.
     */
    private void sortTargets(double[] boxes) {
        targets = new double[boxes.length / 4][4];
        for (int i = 0; i < boxes.length / 4; i++){
            for (int j = 0; j < 4; j++)
                targets[i][0] = boxes[i * 4 + j];
        }

        tempTarget = new double[4];
        for (int i = 1; i < targets.length; i++) {
            for (int j = i; j > 0; j--) {
                if ((targets[j][2] - targets[j][0]) * (targets[j][1] - targets[j][3]) > 
                (targets[j-1][2] - targets[j-1][0]) * (targets[j-1][1] - targets[j-1][3])) {
                    for (int k = 0; k < targets[j].length; k++)
                        tempTarget[k] = targets[j][k];
                    for (int k = 0; k < targets[j].length; k++)
                        targets[j][k] = targets[j-1][k];
                    for (int k = 0; k < targets[j].length; k++)
                        targets[j-1][k] = tempTarget[k];
                }
            }
        }
    }
}