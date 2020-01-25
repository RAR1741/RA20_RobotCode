package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

public class Shooter {
  private final double REVOLUTIONS_PER_DEGREE = 1.0;

  private CANSparkMax launcher = null;
  private CANSparkMax angleMotor = null;

  public Shooter(CANSparkMax launcher, CANSparkMax angleMotor) {
    this.launcher = launcher;
    this.angleMotor = angleMotor;
    launcher.setInverted(true);

    angleMotor.getPIDController().setP(1.0);
    angleMotor.getPIDController().setI(0.0);
    angleMotor.getPIDController().setD(0.0);

    angleMotor.getPIDController().setFeedbackDevice(angleMotor.getEncoder());
  }

  public void manualControl(double power) {
    launcher.set(power);
  }

  public double getLauncherRPM() {
    return launcher.getEncoder().getVelocity();
  }

  public void setAngle(double degrees) {
    angleMotor.getPIDController().setReference(degrees * REVOLUTIONS_PER_DEGREE, ControlType.kPosition);
  }
}