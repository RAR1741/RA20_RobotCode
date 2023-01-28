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

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.moandjiezana.toml.Toml;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // private Toml config;
  AHRS gyro;
  Limelight limelight;
  PhotoswitchSensor lightShoot;
  PhotoswitchSensor lightIntake;
  DigitalInput lightShootInput;
  DigitalInput lightIntakeInput;
  PowercellDetection detector;
  Shooter shooter = null;
  Drivetrain drive = null;
  XboxController driver = null;
  Manipulation manipulation = null;
  XboxController operator = null;
  DriveModule module = null;
  Compressor compressor = null;

  // Booleans for toggling different things...
  boolean limelightToggle = true;
  boolean photoswitchSensorToggle = false;
  boolean shooterToggle = true;
  boolean drivetrainToggle = true;
  boolean manipulationToggle = true;
  boolean navXToggle = false;
  boolean powercellDetectorToggle = false;

  boolean aimToggle = false;

  boolean ptoEngaged = false;
  boolean overrideEngaged = false;
  boolean intakeEntended = false;

  private static final double DEADBAND_LIMIT = 0.01;
  private static final double SPEED_CAP = 0.30;
  InputScaler joystickDeadband = new Deadband(DEADBAND_LIMIT);
  InputScaler joystickSquared = new SquaredInput(DEADBAND_LIMIT);
  BoostInput boost = new BoostInput(SPEED_CAP);

  public double deadband(double in) {
    double out = joystickSquared.scale(in);
    return joystickDeadband.scale(out);
  }

  /**
   * CAN ID's:
   *
   * PDP -> 1 PCM -> 2 Climber -> 4 Drive -> 5-10 Shooter -> 11 Shooter Hood -> 12
   * Intake -> 13 Helix Index -> 14 Feeder Index -> 15 Pull Index -> 16
   *
   *
   *
   *
   *
   * PCM Channels:
   *
   * Drivetrain PTO's -> 0 Manipulation Forward -> 1 Manipulation Reverse -> 2
   */

  @Override
  public void robotInit() {
    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    PneumaticsModuleType moduleType = PneumaticsModuleType.CTREPCM;
    // String path = localDeployPath("config.toml");
    // config = new Toml().read(new File(path));
    if (this.limelightToggle) {
      System.out.print("Initializing vision system (limelight)...");
      limelight = new Limelight();
      limelight.setLightEnabled(false);
      System.out.println("done");
    } else {
      System.out.println("Vision system (limelight) disabled. Skipping initialization...");
    }

    if (this.manipulationToggle) {
      System.out.print("Initializing manipulation...");
      // manipulation = new Manipulation(new CANSparkMax(13, MotorType.kBrushless),
      // new DoubleSolenoid(1, 2),
      // new CANSparkMax(14, MotorType.kBrushless), new CANSparkMax(15,
      // MotorType.kBrushless), lightShoot, lightIntake,
      // new CANSparkMax(16, MotorType.kBrushless));
      manipulation = new Manipulation(new DoubleSolenoid(2, moduleType, 0, 1), new CANSparkMax(13, MotorType.kBrushless),
          new CANSparkMax(14, MotorType.kBrushless), new TalonSRX(16), lightShoot,
          lightIntake);

      System.out.println("done");
    } else {
      System.out.println("Manipulation disabled. Skipping initialization...");
    }

    if (this.photoswitchSensorToggle) {
      System.out.print("Initializing photoswitches...");
      lightShootInput = new DigitalInput(0);
      lightShoot = new PhotoswitchSensor(lightShootInput);
      lightIntakeInput = new DigitalInput(1);
      lightIntake = new PhotoswitchSensor(lightIntakeInput);
      System.out.println("done");
    } else {
      System.out.println("Photoswitches disabled. Skipping initialization...");
    }

    if (this.powercellDetectorToggle) {
      System.out.print("Initializing powercell detection...");
      detector = new PowercellDetection();
      System.out.println("done");
    } else {
      System.out.println("Powercell detection disabled. Skipping initialization...");
    }

    if (this.shooterToggle) {
      System.out.print("Initializing shooter...");
      shooter = new Shooter(new CANSparkMax(11, MotorType.kBrushless), new CANSparkMax(12, MotorType.kBrushless));
      System.out.println("done");
    } else {
      System.out.println("Shooter disabled. Skipping initialization...");
    }

    if (this.drivetrainToggle) {
      System.out.print("Initializing drivetrain...");
      Solenoid pto = new Solenoid(2, moduleType, 2);
      DriveModule leftModule = new DriveModule(new TalonFX(5), new TalonFX(6), new TalonFX(7), pto);
      DriveModule rightModule = new DriveModule(new TalonFX(8), new TalonFX(9), new TalonFX(10), pto);
      drive = new Drivetrain(leftModule, rightModule, detector);
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
    operator = driver; //new XboxController(1);
    System.out.println("done");

    System.out.print("Initializing compressor...");
    compressor = new Compressor(2, moduleType);
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
    // shooter.reHome();
  }

  @Override
  public void teleopPeriodic() {

    /*if (operator.getBButtonPressed()) {
      overrideEngaged = !overrideEngaged;
    }*/

    if (this.limelightToggle) {
      limelight.update();

      if (driver.getXButtonPressed())
        limelight.setLightEnabled(true);
      else if (driver.getYButtonPressed())
        limelight.setLightEnabled(false);
    }

    if (this.powercellDetectorToggle) {
      detector.update();
    }

    if (this.shooterToggle) {
      double speed = 0;
      double shooterAngleSpeed = 0;

      speed = operator.getRightTriggerAxis() * .8;

      if(operator.getXButtonPressed()) {
        aimToggle = !aimToggle;
      }
      shooterAngleSpeed = aimToggle ? operator.getLeftY() : 0; // TODO Replace controls with something better

      if (Math.abs(speed) < 0.1) {
        speed = 0;
      }

      // if (operator.getLeftBumper() && operator.getRightBumper()) {
      //   shooter.reHome();
      // }

      if (shooter.getState() == Shooter.State.Idle || shooter.getState() == Shooter.State.ManualControl) {
        shooter.manualControl(-1 * speed * 0.7, shooterAngleSpeed);
      }
      shooter.update();

      // SmartDashboard.putNumber("ShooterPower", speed);
      // SmartDashboard.putNumber("ShooterRPM", shooter.getLauncherRPM());
      // SmartDashboard.putNumber("ShooterAngle", shooter.getAngleInDegrees());
      // SmartDashboard.putNumber("ShooterAngleEncoder", shooter.getEncoderCount());
      // SmartDashboard.putBoolean("ShooterAngleForwardLimit", shooter.getForwardLimit());
      // SmartDashboard.putBoolean("ShooterAngleReverseLimit", shooter.getReverseLimit());
      // SmartDashboard.putString("ShooterState", shooter.getState().toString());
    }

    if (this.manipulationToggle) {
      if (operator.getLeftBumperPressed()) {
        manipulation.setIntakeExtend(intakeEntended = !intakeEntended);
      }

      if (operator.getRightBumper()) {
        manipulation.shootAllTheThings(true);
      } else {
        manipulation.shootAllTheThings(false);
        manipulation.setIntakeSpin(operator.getYButton());

        //manipulation.setIndexFeed(operator.getAButton() ? -0.25 : 0);

        manipulation.setIndexLoad(operator.getAButton());
      }
    }

    if (this.drivetrainToggle && !aimToggle) {
      double turnInput = deadband(overrideEngaged ? operator.getLeftX() : driver.getLeftX());
      double speedInput = deadband(overrideEngaged ? operator.getLeftY() : driver.getLeftY());

      // Limit speed input to a lower percentage unless boost mode is on
      boost.setEnabled(false);
      speedInput = boost.scale(speedInput);

      drive.arcadeDrive(turnInput*0.3, speedInput);

      /*if (operator.getXButtonPressed()) {
        ptoEngaged = !ptoEngaged;
        drive.setPTO(ptoEngaged);
      }*/
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
