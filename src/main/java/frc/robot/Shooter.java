package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

public class Shooter {
  private final double DEGREES_PER_REVOLUTION = 1.0; //TODO: Determine degrees per revolution of the angle motor.
  private final double REVOLUTIONS_PER_ENCODER = 1/42.0;

  private CANSparkMax launcher = null;
  private CANSparkMax angleMotor = null;

  /**
   * Constructor
   * 
   * @param launcher CAN Id for the launcher motor.
   * @param angleMotor CAN Id for the angle motor.
   */
  public Shooter(CANSparkMax launcher, CANSparkMax angleMotor) {
    this.launcher = launcher;
    this.angleMotor = angleMotor;
    launcher.setInverted(true);

    angleMotor.getPIDController().setP(1.0);
    angleMotor.getPIDController().setI(0.0);
    angleMotor.getPIDController().setD(0.0);

    angleMotor.getPIDController().setFeedbackDevice(angleMotor.getEncoder());
  }

  /**
   * Sets motor power.
   * 
   * @param power the power at which the shooter spins.
   */
  public void manualControl(double power) {
    launcher.set(power);
  }

  /**
   * Gets the shooter RPM
   * 
   * @return shooter RPM.
   */
  public double getLauncherRPM() {
    return launcher.getEncoder().getVelocity();
  }

  /**
   * Gets the angle the angle motor is turned
   * 
   * @return degrees the angle motor is turned/
   */
  public double getAngleDegree() {
    return angleMotor.getEncoder().getPosition() * REVOLUTIONS_PER_ENCODER * DEGREES_PER_REVOLUTION;
  }

  /**
   * Sets angle motor to a specified angle
   * 
   * @param degrees degrees to which the angle motor will be turned.
   */
  public void setAngle(double degrees) {
    angleMotor.getPIDController().setReference(degrees * REVOLUTIONS_PER_DEGREE, ControlType.kPosition);
  }
}