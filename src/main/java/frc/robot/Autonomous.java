package frc.robot;

import java.nio.file.Paths;
import java.util.List;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.RamseteCommand;;

public class Autonomous{

   public enum AutonomousState{
      AimShot1,
      Shoot1,
      MoveTrench,
      BallCollect,
      MoveShoot,
      AimShot2,
      Shoot2;
   }

   private AutonomousState state;
   private AutoAim autoAim;
   private Manipulation manipulation;
   private final Drivetrain drive;
   private final Limelight limelight;
   private final Shooter shooter;
   private double error;
   private double motorPower;
   private double degrees;
   private boolean done = false;
   private double targetRPM; // TODO: determine correct target RPM
   private TrajectoryConfig config;
   private TrajectoryGenerator tGenerator;
   private Pose2d start;
   private Pose2d end;
   private List<Translation2d> interiorWaypoints;

   /**
    * TODO: get measurements for trajectories
    * https://docs.wpilib.org/en/latest/docs/software/examples-tutorials/trajectory-tutorial/characterizing-drive.html
    */
   private double ksVolts;
   private double kvVoltSecondsPerMeter;
   private double kaVoltSecondsSquaredPerMeter;
   private Object kDriveKinematics;
   private double kRamseteZeta;
   private double kRamseteB;
   private double kPDriveVel;

   /**
    * @param drive     drive train object.
    * @param limelight limelight object.
    * @param shooter   shooter object.
    */
   public Autonomous(final Drivetrain drive, final Limelight limelight, final Shooter shooter,
         final Manipulation manipulation) {
      state = AutonomousState.AimShot1;
      this.drive = drive;
      this.limelight = limelight;
      this.shooter = shooter;
   }

   public void AimShot1() {
      autoAim.run();
      state = AutonomousState.Shoot1;
   }

   public void Shoot1() {
      shooter.autoControl(targetRPM);
      state = AutonomousState.MoveTrench;
   }

   public void MoveTrench() {
      RamseteCommand ramseteCommand = new RamseteCommand(
         TrajectoryUtil.fromPathweaverJson(Paths.get("MoveTrenchS.json")), start,
         new RamseteController(kRamseteB, kRamseteZeta),
         new SimpleMotorFeedforward(ksVolts,
                                    kvVoltSecondsPerMeter,
                                    kaVoltSecondsSquaredPerMeter),
                                    kDriveKinematics,
         drive::getWheelSpeeds,
         new PIDController(kPDriveVel, 0, 0), //left side
         new PIDController(kPDriveVel, 0, 0), //right side
         // RamseteCommand passes volts to the callback
         drive::setVoltage, drive
     );
   }

   public void BallGrab(){
      manipulation.intakeOut();
      manipulation.intakeSpin();
      /**
       * TODO: Coral Vision Prossesing and Drivetrain instructions go here
       */
      manipulation.intakeStop();
      manipulation.intakeIn();
      state = AutonomousState.MoveShoot;
   }

   public void MoveShoot(){
      drive.tankDrive(leftDrive, rightDrive);//TODO: Determine correct numbers for driving
      state = AutonomousState.AimShot2;
   }

   public void AimShot2(){
      autoAim.run();
      state = AutonomousState.Shoot2;
   }

   public void Shoot2(){
      shooter.autoControl(targetRPM);      
      done = true;
   }

   public boolean Auto(){

      switch(state){

         case AimShot1:
         AimShot1();
         break;

         case Shoot1:
         Shoot1();
         break;

         case MoveTrench:
         MoveTrench();
         break;

         case BallCollect:
         BallGrab();
         break;

         case MoveShoot:
         MoveShoot();
         break;

         case AimShot2:
         AimShot2();
         break;

         case Shoot2:
         Shoot2();
         break;
      }
      return done;
   }
}