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
    private Drivetrain drive;
    private Limelight limelight;
    private Shooter shooter;
    private double error;
    private double motorPower;
    private double degrees;
    boolean done = false;

    /**
     * @param drive     drive train object.
     * @param limelight limelight object.
     * @param shooter   shooter object.
     */
    public void Autonomous(Drivetrain drive, Limelight limelight, Shooter shooter) {
        state = AutonomousState.AimShot1;
        this.drive = drive;
        this.limelight = limelight;
     }

     public void AimShot1(){

        state = AutonomousState.Shoot1;
     }

     public void Shoot1(){
        
        state = AutonomousState.MoveTrench;
     }

     public void MoveTrench(){
        
        state = AutonomousState.BallCollect;
     }

     public void BallGrab(){

        state = AutonomousState.MoveShoot;
     }

     public void MoveShoot(){

        state = AutonomousState.AimShot2;
     }

     public void AimShot2(){

        state = AutonomousState.Shoot2;
     }

     public void Shoot2(){
        
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