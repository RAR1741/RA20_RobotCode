
package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    private double tv;
    private double tx;
    private double ty;
    private double ta;

    NetworkTable limelight;

    public Limelight(NetworkTable table) {

    }


    public void update(Limelight limelight) {
        this.tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        this.tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        this.ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        this.ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

        SmartDashboard.putNumber("LimelightX", this.getTx());
        SmartDashboard.putNumber("LimelightY", this.getTy());
        SmartDashboard.putNumber("LimelightArea", this.getTa());
        SmartDashboard.putNumber("LimelightTargeted", this.getTv());
    }

    public double getTv() {
        return this.tv;
    }
    public double getTx() {
        return this.tx;
    }
    public double getTy() {
        return this.ty;
    }
    public double getTa() {
        return this.ta;
    }
}