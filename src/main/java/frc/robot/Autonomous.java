package frc.robot;

import java.util.Timer;
import java.util.TimerTask;

public class Autonomous{

   public enum AutonomousState{
      StartTimer,
      AimShot1,
      Shoot1,
      Move;
   }

   private AutonomousState state;
   private AutoAim autoAim;
   private Manipulation manipulation;
   private Drivetrain drive;
   private Shooter shooter;

   private Timer timer;

   private boolean done = false;

   //TODO: determine correct target speeds
   private double targetSpeedMax; 
   private double targetSpeedMin;
   private double targetSpeed;

   private double leftPosition;
   private double rightPosition;

   /**
    * @param drive        drivetrain object.
    * @param shooter      shooter object.
    * @param manipulation manipulation object.
    * @param autoaim      autoAim object.
    */
   public Autonomous(Drivetrain drive, Shooter shooter, Manipulation manipulation, AutoAim autoAim) {
      state = AutonomousState.AimShot1;
      this.drive = drive;
      this.shooter = shooter;
      this.manipulation = manipulation;
      this.autoAim = autoAim;
      timer = new Timer();
   }

   private void StartTimer() {
      timer.schedule(new TimerTask() {
         @Override
         public void run() {
            state = AutonomousState.Move;
         }
      }, 10*1000);
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
   }

   public void Move(){
      leftPosition = drive.getLeftPosition() - 500;
      rightPosition = drive.getRightPosition() - 500;
      drive.setPositions(leftPosition, rightPosition);
      if (getDoneMoving()) {
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
    * Gets if done moving.

    * @return true if done moving, false if not done moving.
    */
   private boolean getDoneMoving() {
      if (Math.abs(leftPosition - drive.getLeftPosition()) <= 50 && Math.abs(rightPosition - drive.getRightPosition()) <= 50) {
         return true;
      } else {
         return false;
      }
   }

   public boolean Auto(){

      switch(state){

         case StartTimer:
         StartTimer();
         break;

         case AimShot1:
         AimShot1();
         break;

         case Shoot1:
         Shoot1();
         break;

         case Move:
         Move();
         break;
      }
      return done;
   }
}
