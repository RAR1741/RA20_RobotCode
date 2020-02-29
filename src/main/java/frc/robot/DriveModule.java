package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Solenoid;

public class DriveModule {
  private TalonFX master;
  private TalonFX slave1;
  private TalonFX slave2;

  private Solenoid cooling;

  DriveModule(TalonFX master, TalonFX slave1, TalonFX slave2, Solenoid cooling) { //TODO: changed the pto pneumatics to cooling. ptos will be declared seperate
    this.master = master;
    this.slave1 = slave1;
    this.slave2 = slave2;

    this.slave1.follow(this.master);
    this.slave2.follow(this.master);

    this.cooling = cooling;
  }

  public void setInverted(boolean isInverted) {
    master.setInverted(isInverted);
    slave1.setInverted(isInverted);
    slave2.setInverted(isInverted);
  }

  public void set(double input) {
    master.set(TalonFXControlMode.PercentOutput, input);
  }

}