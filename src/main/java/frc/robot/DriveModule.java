package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Solenoid;

public class DriveModule {
  private TalonFX master;
  private TalonFX slave1;
  private TalonFX slave2;

  private Solenoid cooler;

  private Solenoid pto;

  private final double MAX_TEMP = 35; //TODO: Determine optimal temperature

  DriveModule(TalonFX master, TalonFX slave1, TalonFX slave2, Solenoid pto, Solenoid cooler) {
    this.master = master;
    this.slave1 = slave1;
    this.slave2 = slave2;

    this.slave1.follow(this.master);
    this.slave2.follow(this.master);

    this.cooler = cooler;

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
  private double getTemp(TalonFX motor) {
    return motor.getTemperature();
  }

  /**
   * Cools falcons using pneumatic coolers if above recommended temperature.
   */
  public void coolTemp() {
    cooler.set(getTemp(master) > MAX_TEMP || getTemp(slave1) > MAX_TEMP || getTemp(slave2) > MAX_TEMP);
  }
}