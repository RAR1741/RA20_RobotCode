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

import java.io.File;
import java.nio.file.Paths;

import com.moandjiezana.toml.Toml;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
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
  private Toml config;
  Limelight limelight;
  PhotoswitchSensor lightShoot;
  PhotoswitchSensor lightIntake;
  DigitalInput lightShootInput;
  DigitalInput lightIntakeInput;
  Shooter shooter = null;
  Drivetrain drive = null;
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
   * Climber -> 4
   * Drive -> 5-10
   * Shooter -> 11
   * Shooter Hood -> 12
   * Intake -> 13
   * Index -> 14
   * Index -> 15
   * Pull Index -> 16 (Possible)
   * 
   * 
   * 
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
    String path = localDeployPath("config.toml");
    config = new Toml().read(new File(path));
    System.out.print("Initializing vision system (limelight)...");
    limelight = new Limelight();
    limelight.setLightEnabled(false);
    System.out.println("done");

    System.out.print("Initializing photoswitchs...");
    lightShootInput = new DigitalInput(0);
    lightShoot = new PhotoswitchSensor(lightShootInput);
    lightIntakeInput = new DigitalInput(1);
    lightIntake = new PhotoswitchSensor(lightIntakeInput);
    System.out.println("done");

    System.out.print("Initializing shooter...");
    shooter = new Shooter(new CANSparkMax(4, MotorType.kBrushless));
    System.out.println("done");

    System.out.print("Initializing manipulation...");
    manipulation = new Manipulation(new Talon(13), new DoubleSolenoid(1, 2), new Talon(14), new Talon(15), lightShoot, lightIntake);
    System.out.println("done");
    System.out.print("Initializing drivetrain...");
    drive = new Drivetrain(5, 6, 7, 8, 9, 10);
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

    double turnInput = driver.getX(Hand.kRight);
    double speedInput = driver.getY(Hand.kLeft);

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
      manipulation.intakeExtend(true);
    }

    if (driver.getBumperPressed(Hand.kLeft)) {
      manipulation.intakeExtend(false);
    }

    if (driver.getBButton()) {
      manipulation.indexFeed(true);
    } else {
      manipulation.indexFeed(false);
    }

    if (driver.getXButton()) {
      manipulation.indexLoad(true);
    } else {
      manipulation.indexLoad(false);
    }

    if (driver.getYButton()) {
      manipulation.intakeSpin(true);
    } else {
      manipulation.intakeSpin(false);
    }

    if (driver.getXButtonPressed()) {
      limelight.setLightEnabled(true);
    } else if (driver.getYButtonPressed()) {
      limelight.setLightEnabled(false);
    }

    drive.arcadeDrive(turnInput, speedInput);

    SmartDashboard.putNumber("IndexBalls", manipulation.getBalls());
    SmartDashboard.putNumber("ShooterPower", speed);
    SmartDashboard.putNumber("ShooterRPM", shooter.getLauncherRPM());
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }


  public static String localPath(String... paths) {
    File localPath = edu.wpi.first.wpilibj.Filesystem.getOperatingDirectory();
    return Paths.get(localPath.toString(), paths).toString();
  }

  public static String localDeployPath(String fileName) {
    if (!isReal()) {
      return Paths.get(localPath(), "src", "main", "deploy", fileName).toString();
    } else {
      return Paths.get(localPath(), "deploy", fileName).toString();
    }
  }
}
