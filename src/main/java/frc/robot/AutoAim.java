package frc.robot;

import com.kauailabs.navx.frc.AHRS;

public class AutoAim {

    private double error;
    private double motorPower;
    private double angleDegrees;
    private double gyroDegrees;

    private boolean sweeping;

    public enum AutoAimState {
        SET_ANGLES,
        AIM_X,
        AIM_ANGLE,
        IDLE;
    }

    private AutoAimState state;
    private Drivetrain drive;
    private Limelight limelight;
    private Shooter shooter;
    private AHRS gyro;


    /**
     * Constructor
     * 
     * @param drive drive train object.
     * @param limelight limelight object.
     * @param shooter shooter object.
     * @param gyro gyro object.
     */
    public AutoAim(Drivetrain drive, Limelight limelight, Shooter shooter, AHRS gyro) {
        state = AutoAimState.IDLE;
        this.drive = drive;
        this.limelight = limelight;
        this.shooter = shooter;
        this.gyro = gyro;
    }

    /**
     * Runs Automatic Aiming state machine.
     */
    public void run() {
        switch(state) {

            case SET_ANGLES:
                SetAngles();
                break;

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
     * Sets degrees to turn the robot to.
     */
    private void SetAngles() {
        if (limelight.isTargetVisible()) {
            sweeping = false;
            drive.stopSweep();
            gyroDegrees = gyro.getYaw() + limelight.getTargetXDegrees();
            state = AutoAimState.AIM_X;
        } else {
            if (!sweeping) {
                drive.resetState();
            }
            sweeping = true;
            drive.sweep();
        }
    }

    /**
     * Aims to robot to the X-coordinate of the power port.
     */
    private void AimX() {
        error = (gyroDegrees - gyro.getYaw())/29.8;
        motorPower = .5 * error;
        drive.driveLeft(motorPower);
        drive.driveRight(-motorPower);
        if (getWithinTolerance(gyroDegrees, gyro.getYaw(), 1)) {
            state = AutoAimState.AIM_ANGLE;
        }
    }

    /**
     * Aims the shooter angle to the appropriate angle, based on distance, to the power port.
     */
    private void AimAngle() {
        angleDegrees = 1 *limelight.getTargetVertical(); //TODO: Use a table from testing to create an equation to plug this into
        shooter.setAngle(angleDegrees);
        if (getWithinTolerance(angleDegrees, shooter.getAngleInDegrees(), 1)) {
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

    /**
     * Resets the state machine.
     */
    public void resetState() {
        state = AutoAimState.SET_ANGLES;
    }

    /**
     * Stops the state machine
     */
    public void stopState() {
        state = AutoAimState.IDLE;
    }

    /**
     * Gets the aiming state.
     */
    public AutoAimState getState() {
        return state;
    }
}