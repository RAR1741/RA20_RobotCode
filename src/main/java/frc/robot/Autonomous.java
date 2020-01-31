package frc.robot;

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
      drive.tankDrive(leftDrive, rightDrive);//TODO: Determine correct numbers for driving
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