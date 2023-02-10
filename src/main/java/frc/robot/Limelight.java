
package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
    NetworkTable limelight;
    HashMap<String, Object> limelightInfo;

    ArrayList<Double> defaultDoubleArray = new ArrayList<Double>();

    /**
     * Constructor
     */
    public Limelight() {
        limelight = NetworkTableInstance.getDefault().getTable("limelight");
        limelightInfo = new HashMap<String, Object>();
    }

    /**
     * Updates limelight values.
     */
    public HashMap<String, Object> update() {
        for (String key: limelight.getKeys()) {
            String type = limelight.getEntry(key).getType().name().substring(1);

            if (type.equals("String")) {
                limelightInfo.put(key, limelight.getEntry(key).getString("No value"));
            }
            else if (type.equals("Double")) {
                limelightInfo.put(key, limelight.getEntry(key).getDouble(Double.NaN));
            }
            else if (type.equals("DoubleArray")) {
                limelightInfo.put(key, limelight.getEntry(key).getDoubleArray(new double[6]));
            }

            SmartDashboard.putString(key, (type.equals("String") || type.equals("Double")) ? limelightInfo.get(key).toString() : Arrays.toString((double[]) limelightInfo.get(key)));
        }

        return limelightInfo;
    }

    /**
     * Enable the LEDs
     */
    public void setLightEnabled(boolean enabled) {
        limelight.getEntry("ledMode").setNumber(enabled ? 3 : 1);
    }
} 