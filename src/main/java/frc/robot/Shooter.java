package frc.robot;

import com.revrobotics.CANSparkMax;

public class Shooter {
  private CANSparkMax launcher = null;

  public Shooter(CANSparkMax motor) {
    launcher = motor;
    launcher.setInverted(true);
  }

  public void manualControl(double power) {
    launcher.set(power);

  }
  public void autoControl(double power) {
    launcher.set(power);
  }

  public double getLauncherRPM() {
    return launcher.getEncoder().getVelocity();
  }
}