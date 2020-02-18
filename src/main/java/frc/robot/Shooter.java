package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;

public class Shooter {
  private static final double HOMING_SPEED_DOWN = -0.3; // Speed at which we seek downward during homing
  private static final double HOME_POSITION = 0.0; // Angle at lower limit switch
  private static final double POSITION_TOLERANCE = 0.05; // Limit of being "close enough" on the angle

  private CANSparkMax launcher = null;
  private CANSparkMax angleMotor = null;
  PhotoswitchSensor light = null;

  private double targetAngle;

  public enum State {
    HomingDown, Idle, MovingToAngle, ManualControl
  }

  private State state;

  /**
   * Constructor
   * 
   * @param launcher   CAN Id for the launcher motor.
   * @param angleMotor CAN Id for the angle motor.
   */
  public Shooter(CANSparkMax launcher, CANSparkMax angleMotor, PhotoswitchSensor light) {
    this.launcher = launcher;
    this.angleMotor = angleMotor;
    launcher.setInverted(true);

    this.light = light;

    angleMotor.getPIDController().setP(1);
    angleMotor.getPIDController().setI(0.0);
    angleMotor.getPIDController().setD(0.0);
    angleMotor.getEncoder().setPositionConversionFactor(0.3765);
    angleMotor.getPIDController().setOutputRange(-0.5, 0.5);

    launcher.getPIDController().setP(0.0008);
    launcher.getPIDController().setI(0.0);
    launcher.getPIDController().setD(0.0);
    launcher.getEncoder().setPositionConversionFactor(1.0);
    launcher.getEncoder().setVelocityConversionFactor(1.0);
    launcher.setClosedLoopRampRate(3.0);

    angleMotor.getPIDController().setFeedbackDevice(angleMotor.getEncoder());
    state = State.HomingDown;
  }

  /**
   * Sets motor power.
   * 
   * @param power the power at which the shooter spins.
   */
  public void manualControl(double power, double angleMotorPower) {
    state = State.ManualControl;
    launcher.set(power);
    angleMotor.set(angleMotorPower);
  }

  public double getTargetAngle() {
    return targetAngle;
  }

  public boolean onTarget() {
    double error = angleMotor.getEncoder().getPosition() - targetAngle;
    return Math.abs(error) < POSITION_TOLERANCE;
  }

  /**
   * Run main state machine for semi-autonomous control of the robot.
   */
  public void update() {
    switch (state) {
    case HomingDown:
      angleMotor.set(HOMING_SPEED_DOWN);

      if (angleMotor.getReverseLimitSwitch(LimitSwitchPolarity.kNormallyOpen).get()) {
        // We've reached the lower limit of the screw assembly, we're now at a
        // known position. Set the absolute position to the encoder so we can deal
        // with easier units.
        angleMotor.set(0);
        angleMotor.getEncoder().setPosition(HOME_POSITION);
        state = State.Idle;
      }
      break;
    case Idle:
      // Idle!
      break;
    case MovingToAngle:
      if (onTarget()) {
        state = State.Idle;
      }
      break;
    case ManualControl:
      // Nothing
      break;
    }
  }

  public void reHome() {
    state = State.HomingDown;
  }

  public double getAngleMotorCurrent() {
    return angleMotor.getOutputCurrent();
  }

  public double getAngleMotorTemp() {
    return angleMotor.getMotorTemperature();
  }

  public void setLauncherRPM(double rpm) {
    rampRPM(rpm);
    launcher.getPIDController().setReference(rpm * 1.23, ControlType.kVelocity);
  }

  private void rampRPM(double rpm) {
    rpm = light.getBlocked() ? (rpm * 1.5) : rpm;
  }

  public double getLauncherMotorCurrent() {
    return launcher.getOutputCurrent();
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
   * @return degrees the angle motor is turned to
   */
  public double getAngleInDegrees() {
    return angleMotor.getEncoder().getPosition();
  }

  public double getEncoderCount() {
    return angleMotor.getEncoder().getPosition() / angleMotor.getEncoder().getPositionConversionFactor();
  }

  /**
   * Sets angle motor to a specified angle
   * 
   * @param degrees degrees to which the angle motor will be turned.
   */
  public void setAngle(double degrees) {
    state = State.MovingToAngle;
    targetAngle = degrees;
    angleMotor.getPIDController().setReference(targetAngle, ControlType.kPosition);
  }

  public boolean getReverseLimit() {
    return angleMotor.getReverseLimitSwitch(LimitSwitchPolarity.kNormallyOpen).get();
  }

  public boolean getForwardLimit() {
    return angleMotor.getForwardLimitSwitch(LimitSwitchPolarity.kNormallyOpen).get();
  }

  public State getState() {
    return state;
  }
}