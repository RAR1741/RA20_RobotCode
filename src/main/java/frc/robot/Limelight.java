
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

    public Limelight() {
        // Assuming use of the default network table.
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

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

    public boolean isTargetVisible() {
        return this.tv > 0.0;
    }

    public double getTargetX() {
        return this.tx;
    }

    public double getTargetY() {
        return this.ty;
    }

    public double getTargetArea() {
        return this.ta;
    }

    public double getTargetVertical() {
        return this.tvert;
    }

}