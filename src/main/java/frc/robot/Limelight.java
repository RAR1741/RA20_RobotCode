
package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedHashMap;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
    NetworkTable limelight;
    LinkedHashMap<String, Object> limelightInfo;

    /**
     * Constructor
     */
    public Limelight() {
        limelight = NetworkTableInstance.getDefault().getTable("limelight");
        limelightInfo = new LinkedHashMap<String, Object>();
    }

    /**
     * Updates limelight values.
     */
    public void update() {
        for (String key: limelight.getKeys()) {
            String type = limelight.getEntry(key).getType().name();
            SmartDashboard.putString(key, type);
        }
    }

    /**
     * Enable the LEDs
     */
    public void setLightEnabled(boolean enabled) {
        limelight.getEntry("ledMode").setNumber(enabled ? 3: 1);
    }
}