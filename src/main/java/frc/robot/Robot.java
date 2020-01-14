/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

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

  Shooter shooter = null;
  XboxController driver = null;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    System.out.print("Initializing shooter...");
    shooter = new Shooter(new CANSparkMax(1, MotorType.kBrushless));
    System.out.println("done");

    System.out.print("Initializing driver interface...");
    driver = new XboxController(0);
    System.out.println("done");
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
    double speed = 0;
    if (driver.getTriggerAxis(Hand.kRight) > 0.5) {
      speed = driver.getY(Hand.kRight);
    } else if (driver.getAButton()) {
      speed = 1;
    }

    shooter.manualControl(speed);
    System.out.printf("Input: %3.2f%%    RPM: %.2f\n", speed, 
      shooter.getLauncherRPM());

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
