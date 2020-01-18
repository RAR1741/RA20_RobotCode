
package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    private double tv;
    private double tx;
    private double ty;
    private double ta;
    private double tvert;

    NetworkTable limelightTable;

    /**
     * Constructor
     */
    public Limelight() {
        // Assuming use of the default network table.
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    /**
     * Updates limelight values.
     */
    public void update() {
        this.tv = limelightTable.getEntry("tv").getDouble(0);
        this.tx = limelightTable.getEntry("tx").getDouble(0);
        this.ty = limelightTable.getEntry("ty").getDouble(0);
        this.ta = limelightTable.getEntry("ta").getDouble(0);
        this.tvert = limelightTable.getEntry("tvert").getDouble(0);

        SmartDashboard.putNumber("LimelightX", this.getTargetX());
        SmartDashboard.putNumber("LimelightY", this.getTargetY());
        SmartDashboard.putNumber("LimelightArea", this.getTargetArea());
        SmartDashboard.putBoolean("LimelightTargeted", this.isTargetVisible());
    }

    /**
     * @return if target is visible.
     */
    public boolean isTargetVisible() {
        return this.tv > 0.0;
    }

    /**
     * @return current target x-coordinate.
     */
    public double getTargetX() {
        return this.tx;
    }

    /**
     * @return current target y-coordinate.
     */
    public double getTargetY() {
        return this.ty;
    }

    /**
     * @return current target area of screen (0 to 100 percent).
     */
    public double getTargetArea() {
        return this.ta;
    }

    /**
     * @return current target vertical length.
     */
    public double getTargetVertical() {
        return this.tvert;
    }

}