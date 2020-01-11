package frc.robot;

import com.revrobotics.CANSparkMax;

public class Shooter {
  private CANSparkMax launcher = null;

  public Shooter(CANSparkMax motor) {
    launcher = motor;
  }

  public void manualControl(double power) {
    launcher.set(power);
  }

  public double getLauncherRPM() {
    return launcher.getEncoder().getVelocity();
  }
}