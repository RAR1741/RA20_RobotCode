package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class PowercellDetection {

    private double nb;
    private double[] boxes;

    NetworkTable detectionTable;

    /**
     * Constructor
     */
    public PowercellDetection () {
        detectionTable = NetworkTableInstance.getDefault().getTable("ML");
    }

    public void update() {
        this.nb = detectionTable.getEntry("nb_objects").getDouble(0);
        this.boxes = detectionTable.getEntry("boxes").getDoubleArray(new double[]{0.0, 0.0, 0.0, 0.0});
    }
}