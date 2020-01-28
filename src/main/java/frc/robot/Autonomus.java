package frc.robot;

public class Autonomus
{

    public enum AutoAimState 
    {
        AimShot1,
        Shoot1,
        MoveTrench,
        BallCollect,
        MoveShoot,
        AimShot2,
        Shoot2;
    }
    /**
    * @param drive drive train object.
    * @param limelight limelight object.
    */
    
    public void AutoAim(Drivetrain drive, Limelight limelight) 
    {
       state = AutoAimState.AimShot1;
       this.drive = drive;
       this.limelight = limelight;
    }
private AutoAimState state;
private Drivetrain drive;
private Limelight limelight;
private Shooter shooter;
private double error;
private double motorPower;
private double degrees;
}