package frc.robot;


public class AutoAim {

    private double error;
    private double motorPower;
    private double degrees;

    public enum AutoAimState {
        AIM_X,
        AIM_ANGLE,
        IDLE;
    }

    private AutoAimState state;
    private Drivetrain drive;
    private Limelight limelight;
    private Shooter shooter;

    public AutoAim(Drivetrain drive, Limelight limelight) {
        state = AutoAimState.AIM_X;
        this.drive = drive;
        this.limelight = limelight;
    }

    public void run() {
        switch(state) {

            case AIM_X:
                aimX(limelight.getTargetX());
                if(Math.abs(limelight.getTargetX()) < 1)
                    state = AutoAimState.AIM_ANGLE;
                break;

            case AIM_ANGLE:
                aimAngle(limelight.getTargetVertical());
                if (Math.abs(degrees * DEGREES_PER_REVOLUTIONS * REVOLUTIONS_PER_ENCODER - shooter.getAngleDegrees) < 1)
                    state = AutoAimState.IDLE;
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

    private void aimAngle(double h) {
        degrees = 0; //TODO: Use a table from testing to create an equation to plug this into
        shooter.setAngle(degrees);
    }
}