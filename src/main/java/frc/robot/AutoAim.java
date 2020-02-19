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
                AimX();
                break;

            case AIM_ANGLE:
                AimAngle();
                break;

            case IDLE:
                break;
        }
    }

    /**
     * Aims to robot to the X-coordinate of the power port.
     */
    private void AimX() {
        error = limelight.getTargetX() * 1;
        motorPower = .5 * error;
        drive.driveLeft(motorPower);
        drive.driveRight(-motorPower);
        if (getWithinTolerance(0, limelight.getTargetX(), 1)) {
            state = AutoAimState.AIM_ANGLE;
        }
    }

    /**
     * Aims the shooter angle to the appropriate angle, based on distance, to the power port.
     */
    private void AimAngle() {
        degrees = 0; //TODO: Use a table from testing to create an equation to plug this into
        shooter.setAngle(degrees);
        if (getWithinTolerance(degrees, shooter.getAngleInDegrees(), 1)) {
            state = AutoAimState.IDLE;
        }
    }

    /**
     * Gets if input is within tolerance of its goal.
     * 
     * @param goal desired value of input.
     * @param current current value of input.
     * @param tolerance tolerance of current input to desired input.
     * @return true if within tolerance, false if not within tolerance.
     */
    private boolean getWithinTolerance(double goal, double current, double tolerance) {
        return Math.abs(goal - current) < tolerance;
    }
}