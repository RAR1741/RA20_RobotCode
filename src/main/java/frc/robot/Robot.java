/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.Limelight;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  AHRS gyro;
  Limelight limelight;
  Shooter shooter = null;
  XboxController driver = null;

  double pitch;
  double roll;
  double yaw;
  double accelX;
  double accelY;
  double accelZ;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    System.out.print("Initializing vision system (limelight)...");
    limelight = new Limelight();
    limelight.setLightEnabled(false);
    System.out.println("done");

    System.out.print("Initializing gyro system (NavX)...");
    gyro = new AHRS(SPI.Port.kMXP);
    gyro.enableLogging(false);
    System.out.println("done");

    System.out.print("Initializing shooter...");
    shooter = new Shooter(new CANSparkMax(2, MotorType.kBrushless));
    System.out.println("done");

    System.out.print("Initializing driver interface...");
    driver = new XboxController(0);
    System.out.println("done");
  }

  public void gyroUpdate() {
    pitch = gyro.getPitch();
    roll = gyro.getRoll();
    yaw = gyro.getYaw();
    accelX = gyro.getRawAccelX();
    accelY = gyro.getRawAccelY();
    accelZ = gyro.getRawAccelZ();
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    gyroUpdate();
    limelight.update();
    double speed = 0;
    if (driver.getTriggerAxis(Hand.kRight) > 0.5) {
      speed = -1 * driver.getY(Hand.kRight);
    } else if (driver.getAButton()) {
      speed = 1;
    }

    if (Math.abs(speed) < 0.1) {
      speed = 0;
    }

    shooter.manualControl(speed);

    if (driver.getXButtonPressed()) {
      limelight.setLightEnabled(true);
    } else if (driver.getYButtonPressed()) {
      limelight.setLightEnabled(false);
    }

    SmartDashboard.putNumber("ShooterPower", speed);
    SmartDashboard.putNumber("ShooterRPM", shooter.getLauncherRPM());
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
