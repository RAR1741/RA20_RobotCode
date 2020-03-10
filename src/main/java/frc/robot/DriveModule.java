package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Solenoid;

public class DriveModule {

  private final double MAX_CURRENT = 10; // TODO: Determine max current to prevent brownouts

  private TalonFX master;
  private TalonFX slave1;
  private TalonFX slave2;

  private Solenoid pto;

  DriveModule(TalonFX master, TalonFX slave1, TalonFX slave2, Solenoid pto) {
    this.master = master;
    this.slave1 = slave1;
    this.slave2 = slave2;

    // this.slave1.follow(this.master);
    // this.slave2.follow(this.master);

    this.pto = pto;
  }

  public void setInverted(boolean isInverted) {
    master.setInverted(isInverted);
    slave1.setInverted(isInverted);
    slave2.setInverted(isInverted);
  }

  public void set(double input) {
    master.set(TalonFXControlMode.PercentOutput, limitedCurrent(master, input));
    slave1.set(TalonFXControlMode.PercentOutput, limitedCurrent(slave1, input));
    slave2.set(TalonFXControlMode.PercentOutput, limitedCurrent(slave2, input));
  }

  public double limitedCurrent(TalonFX motor, double input) {
    return motor.getSupplyCurrent() < MAX_CURRENT ? input : (motor.getMotorOutputPercent() * (MAX_CURRENT / motor.getSupplyCurrent()));
  }

  public void setPTO(boolean engaged) {
    pto.set(engaged);
  }
}