/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import frc.robot.Limelight;
import frc.robot.Shooter;
import frc.robot.Manipulation;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
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
  Limelight limelight;
  Shooter shooter = null;
  XboxController driver = null;
  Manipulation manipulation = null;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */


  /**
   * CAN ID's:
   * 
   * PDP -> 1
   * PCM -> 2
   * Shooter -> 4
   * Drivetrain Left -> 5,6,7
   * Drivetrain Right -> 8,9,10
   * Manipulation Intake -> 11
   * Manipulation Index -> 12
   * 
   * 
   * PCM Channels:
   * 
   * Drivetrain PTO's -> 0
   * Manipulation Forward -> 1
   * Manipulation Reverse -> 2
   */


  
  @Override
  public void robotInit() {
    System.out.print("Initializing vision system (limelight)...");
    limelight = new Limelight();
    System.out.println("done");

    System.out.print("Initializing shooter...");
    shooter = new Shooter(new CANSparkMax(4, MotorType.kBrushless));
    System.out.println("done");

    System.out.print("Initializing intake...");
    manipulation = new Manipulation(new Talon(11), new DoubleSolenoid(1, 2), new Talon(12), new Talon(13));
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

    if (driver.getBumperPressed(Hand.kRight)) {
      manipulation.intakeOut();
    }

    if (driver.getBumperPressed(Hand.kLeft)) {
      manipulation.intakeIn();
    }

    if (driver.getXButton()) {
      manipulation.intakeSpin();
    } else {
      manipulation.intakeStop();
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
