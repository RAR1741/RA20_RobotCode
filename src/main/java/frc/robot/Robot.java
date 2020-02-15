/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;
import java.nio.file.Paths;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.moandjiezana.toml.Toml;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
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
  AHRS gyro;
  Limelight limelight;
  PhotoswitchSensor light;
  DigitalInput lightInput;
  Shooter shooter = null;
  Drivetrain drive = null;
  XboxController driver = null;
  Manipulation manipulation = null;
  Autonomous autonomous;
  //PowercellDetection detector = null;
  DriveModule module = null;
  Compressor compressor = null;

  // Booleans for toggling different things...
  boolean limelightToggle = true;
  boolean photoswitchSensorToggle = true;
  boolean shooterToggle = true;
  boolean drivetrainToggle = true;
  boolean navXToggle = true;
  boolean manipulationToggle = true;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  /**
   * CAN ID's:
   * 
   * PDP -> 1 PCM -> 2 Climber -> 4 Left Drive -> 5-7 Right Drive -> 8-10 Shooter
   * -> 11 Shooter Hood -> 12 Intake -> 13 Index Helix -> 14 Index Feeder -> 15
   * Index Pull -> 16 (Possible)
   * 
   * 
   * PCM Channels:
   * 
   * Drivetrain PTO's -> 0 Manipulation Forward -> 1 Manipulation Reverse -> 2
   */

  @Override
  public void robotInit() {
    String path = localDeployPath("config.toml");
    config = new Toml().read(new File(path));

    //detector = new PowercellDetection();

    System.out.print("Initializing manipulation...");
    manipulation = new Manipulation(new Talon(13), new DoubleSolenoid(1, 2), new Talon(14), new Talon(15));
    System.out.println("done");

    if (this.limelightToggle) {
      System.out.print("Initializing vision system (limelight)...");
      limelight = new Limelight();
      limelight.setLightEnabled(false);
      System.out.println("done");
    } else {
      System.out.println("Vision system (limelight) disabled. Skipping initialization...");
    }

    if (this.photoswitchSensorToggle) {
      System.out.print("Initializing photoswitch...");
      lightInput = new DigitalInput(0);
      light = new PhotoswitchSensor(lightInput);
      System.out.println("done");
    } else {
      System.out.println("Photoswitch disabled. Skipping initialization...");
    }

    if (this.shooterToggle) {
      System.out.print("Initializing shooter...");
      shooter = new Shooter(new CANSparkMax(2, MotorType.kBrushless));
      System.out.println("done");
    } else {
      System.out.println("Shooter disabled. Skipping initialization...");
    }

    if (this.manipulationToggle) {
      System.out.print("Initializing manipulation...");
      manipulation = new Manipulation(new Talon(13), new DoubleSolenoid(1, 2), new Talon(14), new Talon(15));
      System.out.println("done");
    } else {
      System.out.println("Manipulation disabled. Skipping initialization...");
    }

    if (this.drivetrainToggle) {
      System.out.print("Initializing drivetrain...");
      DriveModule leftModule = new DriveModule(new TalonFX(5), new TalonFX(6), new TalonFX(7), new Solenoid(2, 0));
      DriveModule rightModule = new DriveModule(new TalonFX(8), new TalonFX(9), new TalonFX(10), new Solenoid(2, 1));
      drive = new Drivetrain(leftModule, rightModule);
      System.out.println("done");
    } else {
      System.out.println("Drivetrain disabled. Skipping initialization...");
    }

  if (this.navXToggle) {
    System.out.print("Initializing gyro system (NavX)...");
    gyro = new AHRS(SPI.Port.kMXP);
    gyro.enableLogging(false);
    System.out.println("done");
  } else {
    System.out.println("Gyro system (NavX) disabled. Skipping initialization...");
  }

    System.out.print("Initializing driver interface...");
    driver = new XboxController(0);
    System.out.println("done");

    System.out.print("Initializing Autonomous...");
    autonomous = new Autonomous(drive, limelight, shooter, manipulation);
    System.out.println("done");

    System.out.print("Initializing compressor...");
    compressor = new Compressor(2);
    System.out.println("done");
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

    // pcDetection.update();
    limelight.update();
    if (autonomous.Auto()) {
      System.out.println("Autonomous Done");
    }
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    if (this.limelightToggle) {
      limelight.update();
      if (driver.getXButtonPressed()) {
        limelight.setLightEnabled(true);
      } else if (driver.getYButtonPressed()) {
        limelight.setLightEnabled(false);
      }
    }

    if (this.shooterToggle) {
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

      SmartDashboard.putNumber("ShooterPower", speed);
      SmartDashboard.putNumber("ShooterRPM", shooter.getLauncherRPM());
    }

    if (this.manipulationToggle) {
      if (driver.getBumperPressed(Hand.kRight)) {
        manipulation.intakeOut();
      }

      if (driver.getBumperPressed(Hand.kLeft)) {
        manipulation.intakeIn();
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
        manipulation.intakeSpin();
      } else {
        manipulation.intakeStop();
      }
    }

    if (this.drivetrainToggle) {
      double turnInput = driver.getX(Hand.kRight);
      double speedInput = driver.getY(Hand.kLeft);

      if (driver.getXButtonPressed()) {
        limelight.setLightEnabled(true);
      } else if (driver.getYButtonPressed()) {
        limelight.setLightEnabled(false);
      }

      drive.arcadeDrive(turnInput, speedInput);
    }

    if (this.photoswitchSensorToggle) {
      SmartDashboard.putBoolean("LightClear", light.getClear());
    }
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
