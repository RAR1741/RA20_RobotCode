package frc.robot;

import java.util.List;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

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
   private Drivetrain drive;
   private Limelight limelight;
   private Shooter shooter;
   private double error;
   private double motorPower;
   private double degrees;
   private boolean done = false;
   private double targetRPM; //TODO: determine correct target RPM
   private TrajectoryConfig config;
   private TrajectoryGenerator tGenerator;
   private Pose2d start;
   private Pose2d end;
   private List<Translation2d> interiorWaypoints;
   

   /**
    * @param drive     drive train object.
    * @param limelight limelight object.
    * @param shooter   shooter object.
    */
   public Autonomous(Drivetrain drive, Limelight limelight, Shooter shooter, Manipulation manipulation) {
      state = AutonomousState.AimShot1;
      this.drive = drive;
      this.limelight = limelight;
      this.shooter = shooter;
   }

   public void AimShot1(){
      autoAim.run();
      state = AutonomousState.Shoot1;
   }

   public void Shoot1(){
      shooter.autoControl(targetRPM);
      state = AutonomousState.MoveTrench;
   }

   public void MoveTrench(){
      start = new Pose2d(, , );
      end = new Pose2d(, , );
      interiorWaypoints = new List<Translation2d>
      state = AutonomousState.BallCollect;
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

   public static void followTrajectory(Pose2d start, List<Translation2d> interiorWaypoints, Pose2d end, TrajectoryConfig config){
      TrajectoryGenerator.generateTrajectory(start, interiorWaypoints, end, config);
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