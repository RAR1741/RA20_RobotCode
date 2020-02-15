/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.Limelight;

import java.io.File;
import java.nio.file.Paths;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.moandjiezana.toml.Toml;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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
  Limelight limelight;
  PhotoswitchSensor light;
  DigitalInput lightInput;
  Shooter shooter = null;
  Drivetrain drive = null;
  XboxController driver = null;
  DriveModule module = null;
  Compressor compressor = null;

  // Booleans for toggling different things...
  boolean limelightToggle = true;
  boolean photoswitchSensorToggle = true;
  boolean shooterToggle = true;
  boolean drivetrainToggle = true;

  public boolean boost = false;
  private static final double DEADBAND_LIMIT = 0;

  public double deadband(double in) {
    double out = ((Math.abs(in)-DEADBAND_LIMIT)*(Math.abs(in)-DEADBAND_LIMIT))/((1-DEADBAND_LIMIT)*(1-DEADBAND_LIMIT));
    return Math.abs(in) > DEADBAND_LIMIT ? (in > 0 ? out : -out) : 0.0;
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  @Override
  public void robotInit() {
    String path = localDeployPath("config.toml");
    config = new Toml().read(new File(path));
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

    if (this.drivetrainToggle) {
      System.out.print("Initializing drivetrain...");
      DriveModule leftModule = new DriveModule(
        new TalonFX(5),
        new TalonFX(6),
        new TalonFX(7),
        new Solenoid(2, 0)
      );
      DriveModule rightModule = new DriveModule(
        new TalonFX(8),
        new TalonFX(9),
        new TalonFX(10),
        new Solenoid(2, 1)
      );
      drive = new Drivetrain(leftModule, rightModule);
      System.out.println("done");
    } else {
      System.out.println("Drivetrain disabled. Skipping initialization...");
    }


    System.out.print("Initializing driver interface...");
    driver = new XboxController(0);
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
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    if (this.limelightToggle)
      limelight.update();

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

    if (this.drivetrainToggle) {
      double turnInput = deadband(driver.getX(Hand.kRight));
      double speedInput = deadband(driver.getY(Hand.kLeft));

      if (driver.getXButtonPressed()) {
        limelight.setLightEnabled(true);
      } else if (driver.getYButtonPressed()) {
        limelight.setLightEnabled(false);
      }

      drive.arcadeDrive(turnInput, speedInput, driver.getAButtonPressed());
    }

    if (this.photoswitchSensorToggle){
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
