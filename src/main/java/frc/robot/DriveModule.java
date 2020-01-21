package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Solenoid;

public class DriveModule {
  private CANSparkMax master;
  private CANSparkMax slave1;
  private CANSparkMax slave2;

  private Solenoid pto;

  DriveModule(CANSparkMax master, CANSparkMax slave1, CANSparkMax slave2, Solenoid pto) {
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
    master.set(input);
  }

  public void setPTO(boolean engaged) {
    pto.set(engaged);
  }
}