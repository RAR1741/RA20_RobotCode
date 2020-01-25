package frc.robot;


public class AutoAim {

    private double error;
    private double motorPower;

    public enum AutoAimState {
        AIM_X,
        AIM_ANGLE,
        IDLE;
    }

    private AutoAimState state;
    private Drivetrain drive;
    private Limelight limelight;

    public AutoAim(Drivetrain drive, Limelight limelight) {
        state = AutoAimState.AIM_X;
        this.drive = drive;
        this.limelight = limelight;
    }

    public void run() {
        switch(state) {

            case AIM_X:
                aimX(limelight.getTargetX());
                break;

            case AIM_ANGLE:
                //aimAngle(limelight.getTargetVertical());
                break;

            case IDLE:
                break;
        }
    }

    private void aimX(double x) {
        error = x * 1;
        motorPower = .5 * error;
        drive.driveLeft(-motorPower);
        drive.driveRight(motorPower);
    }
}