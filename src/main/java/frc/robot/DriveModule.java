package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveModule {
  private TalonFX master;
  private TalonFX slave1;
  private TalonFX slave2;

  private Solenoid pto;

  DriveModule(TalonFX master, TalonFX slave1, TalonFX slave2, Solenoid pto) {
    this.master = master;
    this.slave1 = slave1;
    this.slave2 = slave2;

    this.slave1.follow(this.master);
    this.slave2.follow(this.master);

    this.pto = pto;
  }

  public void setInverted(boolean isInverted) {
    master.setInverted(isInverted);
    slave1.setInverted(isInverted);
    slave2.setInverted(isInverted);
  }

  public void set(double input) {
    master.set(TalonFXControlMode.PercentOutput, input);
  }

  public void setPTO(boolean engaged) {
    pto.set(engaged);
  }

  /**
   * Adds the temperature of the motors to Smart Dashboard.
   */
  public void getTemp(String message) {
    double[] temp = {master.getTemperature(), slave1.getTemperature(), slave2.getTemperature()};
    SmartDashboard.putNumberArray(message, temp);
  }
}