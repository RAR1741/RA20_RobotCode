package frc.robot;

import java.util.Arrays;
import java.util.Comparator;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class PowercellDetection {
    private final double SCREEN_AREA = 1; // TODO: Determine area camera view.

    private int nb;
    private double[] boxes;
    private Target[] targets;

    NetworkTable detectionTable;

    /**
     * Constructor
     */
    public PowercellDetection() {
        detectionTable = NetworkTableInstance.getDefault().getTable("ML");
    }

    /**
     * Updates variables.
     */
    public void update() {
        this.nb = (int) detectionTable.getEntry("nb_objects").getDouble(0);
        this.boxes = detectionTable.getEntry("boxes").getDoubleArray(new double[] { 0.0, 0.0, 0.0, 0.0 });
        this.sortTargets(this.boxes);
    }

    /**
     * Gets number of powercells detected.
     * 
     * @return number of powercells detected
     */
    public int getNumber() {
        return nb;
    }

    /**
     * Sorts an array of box coordinates into a 2-D array sorted by decending area
     * of those boxes.
     * 
     * @param boxes array of box coordinates.
     */
    private void sortTargets(double[] boxes) {
        targets = new Target[nb];
        for (int i = 0; i < nb; i++) {
            targets[i] = new Target(boxes[i * 4 + 0], boxes[i * 4 + 1], boxes[i * 4 + 2], boxes[i * 4 + 3]);
        }

        Arrays.sort(targets, new sortByArea());
    }

    /**
     * Gets the target at the given index.
     * 
     * @param index index of the target.
     * @return target at the given index.
     */
    public Target getTarget(int index) {
        return (index < getNumber()) ? targets[index] : new Target(0, 0, 0, 0);
    }

    /**
     * Gets perentage of the screen taken up by given target.
     * 
     * @param target target powercell.
     * @return percentage of screen taken up by the target.
     */
    public double getAreaPercent(Target target) {
        return target.getArea() / SCREEN_AREA;
    }

    /**
     * Gets percentage of the screen not taken up by given target.
     * 
     * @param target target powercell.
     * @return percentage of screen not taken up by the target.
     */
    public double getInvertedAreaPercent(Target target) {
        return (SCREEN_AREA - target.getArea()) / SCREEN_AREA;
    }
}

class sortByArea implements Comparator<Target> {
    public int compare(Target a, Target b) {
        return (int) (b.getArea() - a.getArea());
    }
}