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

   //TODO: determine correct target speeds
   private double targetSpeedMax; 
   private double targetSpeedMin;
   private double targetSpeed;
   
   /**
    * @param drive        drivetrain object.
    * @param limelight    limelight object.
    * @param shooter      shooter object.
    * @param manipulation manipulation object.
    * @param autoaim      autoAim object.
    */
   public Autonomous(Drivetrain drive, Limelight limelight, Shooter shooter, Manipulation manipulation, AutoAim autoAim) {
      state = AutonomousState.AimShot1;
      this.drive = drive;
      this.limelight = limelight;
      this.shooter = shooter;
      this.manipulation = manipulation;
      this.autoAim = autoAim;
   }

   public void AimShot1(){
      autoAim.run();
      manipulation.setIndexLoad(true);
      if (getDoneAiming()) {
         state = AutonomousState.Shoot1;
      }
   }

   public void Shoot1(){
      shooter.autoControl(targetSpeed);
      shoot();
      if (getDoneShooting()) {
         state = AutonomousState.MoveTrench;
      }
   }

   public void MoveTrench(){
      //TODO: Add trajectory based movement.
      state = AutonomousState.BallCollect;
   }

   public void BallGrab(){
      manipulation.setIntakeExtend(true);
      manipulation.setIntakeSpin(true);
      drive.approachPowercell();
      if (getDoneCollecting()) {
         state = AutonomousState.MoveShoot;
      }
   }

   public void MoveShoot(){
      //TODO: Add trajectory based movement.
      autoAim.resetState();
      state = AutonomousState.AimShot2;
   }

   public void AimShot2(){
      autoAim.run();
      manipulation.setIndexLoad(true);
      if (getDoneAiming()) {
         state = AutonomousState.Shoot2;
      }
   }

   public void Shoot2(){
      manipulation.updateIndex();
      shooter.autoControl(targetSpeed);
      shoot();
      if (getDoneShooting()) {
         done = true;
      }
   }

   /**
    * Brings shooter up to speed and shoots.
    */
   private void shoot() {
      if(shooter.getLauncherRPM() <= targetSpeedMax && shooter.getLauncherRPM() < targetSpeedMin){
         manipulation.setIndexFeed(true);
      }
   }

   /**
    * Gets if done aiming.

    * @return true if done aiming, false if not done aiming.
    */
   private boolean getDoneAiming() {
      if (autoAim.getState() == AutoAim.AutoAimState.IDLE) {
         return true;
      } else {
         return false;
      }
   }

   /**
    * Gets if done shooting and stops shooting if so.

    * @return true if done shooting, false if not done shooting.
    */
   private boolean getDoneShooting() {
      if (manipulation.getBalls() == 0) {
         manipulation.setIndexLoad(false);
         manipulation.setIndexFeed(false);
         shooter.autoControl(0);
         return true;
      } else {
         return false;
      }
   }

   /**
    * Gets if done collecting powercells.

    * @return true if done collecting, false if not done.
    */
   private boolean getDoneCollecting() {
      if (manipulation.getBalls() >= 5) {
         manipulation.setIntakeSpin(false);
         manipulation.setIntakeExtend(false);
         return true;
      } else {
         return false;
      }
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