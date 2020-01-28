package frc.robot;


public class AutoAim {

    private double REVOLUTIONS_PER_DEGREE = 0; //TODO: Determine revolutions of motor required to turn angle motor a degree
    private double ENCODERS_PER_REVOLUTION = 42;

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

    /**
     * Constructor
     * 
     * @param drive drive train object.
     * @param limelight limelight object.
     */
    public AutoAim(Drivetrain drive, Limelight limelight) {
        state = AutoAimState.AIM_X;
        this.drive = drive;
        this.limelight = limelight;
    }

    /**
     * Runs Automatic Aiming state machine.
     */
    public void run() {
        switch(state) {

            case AIM_X:
                aimX(limelight.getTargetX());
                if(Math.abs(limelight.getTargetX()) < 1)
                    state = AutoAimState.AIM_ANGLE;
                break;

            case AIM_ANGLE:
                aimAngle(limelight.getTargetVertical());
                if (Math.abs(degrees * REVOLUTIONS_PER_DEGREE * ENCODERS_PER_REVOLUTION - shooter.getAngleDegree) < 1)
                    state = AutoAimState.IDLE;
                break;

            case IDLE:
                break;
        }
    }

    /**
     * Aims to robot to the X-coordinate of the power port.
     * 
     * @param x current X-coordinate of the power port.
     */
    private void aimX(double x) {
        error = x * 1;
        motorPower = .5 * error;
        drive.driveLeft(-motorPower);
        drive.driveRight(motorPower);
    }

    /**
     * Aims the shooter angle to the appropriate angle, based on distance, to the power port.
     * 
     * @param h current height of the power port.
     */
    private void aimAngle(double h) {
        degrees = 0; //TODO: Use a table from testing to create an equation to plug this into
        shooter.setAngle(degrees);
    }
}