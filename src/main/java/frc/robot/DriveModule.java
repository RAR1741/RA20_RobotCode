package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Solenoid;

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

  public void setVoltage(double voltage) {
    master.set(TalonFXControlMode.PercentOutput, voltage*(master.getMotorOutputPercent()/master.getMotorOutputVoltage()));
  }

  public void setRawSpeed(double encoderTicksPerSecond) {
    // This is divided by 10 because velocity is in "position change / 100 ms"
    master.set(TalonFXControlMode.Velocity, encoderTicksPerSecond / 10);
  }

  public void setPTO(boolean engaged) {
    pto.set(engaged);
  }

  public double getRawSpeed() {
    // This is multiplied by 10 because velocity is in "position change / 100 ms"
    return master.getSelectedSensorVelocity() * 10;
  }
}