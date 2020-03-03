package frc.robot;

import com.kauailabs.navx.frc.AHRS;

public class Drivetrain {
    
    /**
     * {@value #DEADBAND_LIMIT} The limit for when to stop the motor running if the
     * motor speed is too low.
     */
    private static final double DEADBAND_LIMIT = 0.02;

    private enum SweepState {
        SET_ANGLE,
        SWEEP_LEFT,
        SWEEP_RIGHT,
        IDLE;
    }

    private SweepState sweepState;

    private DriveModule left;
    private DriveModule right;
    private PowercellDetection detector;
    private Limelight limelight;
    private AHRS gyro;

    private double gyroAngle;

    /**
     * Constructor
     * 
     * @param left The left drive module
     * @param right The right drive module
     * @param detector The powercell detection object.
     */
    Drivetrain(DriveModule left, DriveModule right, PowercellDetection detector, Limelight limelight, AHRS gyro){
        this.left = left;
        this.right = right;
        left.setInverted(true);

        this.detector = detector;
        this.limelight = limelight;
        this.gyro = gyro;

        sweepState = SweepState.IDLE;
    }

    /**
     * Drives the left side of the robot either forward or backward.
     * 
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveLeft(double speed){
        double sp = deadband(speed);
        left.set(sp);
    }

    /**
     * Drives the right side of the robot either forward or backward.
     * 
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveRight(double speed){
        double sp = deadband(speed);
        right.set(sp);
    }

    /**
     * Drives to given positions.
     * 
     * @param leftPosition position of left drive module.
     * @param rightPosition position of right drive module.
     */
    public void setPositions(double leftPosition, double rightPosition){
        left.setPosition(leftPosition);
        right.setPosition(rightPosition);
    }

    /**
     * Gets position of left drive module.
     * 
     * @return position of left drive module.
     */
    public double getLeftPosition(){
        return left.getPosition();
    }

    /**
     * Gets position of right drive module.
     * 
     * @return position of right drive module.
     */
    public double getRightPosition(){
        return right.getPosition();
    }

    /**
     * Drives the robot with an arcade style drive
     *
     * @param xDrive The speed to drive the drivetrain in the x direction (ranges
     *               from -1.0 to +1.0)
     * @param yDrive The speed to drive the drivetrain in the y direction (ranges
     *               from -1.0 to +1.0)
     */
    public void arcadeDrive(double xDrive, double yDrive){
        this.driveLeft(yDrive - xDrive);
        this.driveRight(xDrive - yDrive);
    }

    /**
     * Drives the robot with an tank style drive
     *
     * @param xDrive The speed to drive the left drivetrain (ranges
     *               from -1.0 to +1.0)
     * @param yDrive The speed to drive the right drivetrain (ranges
     *               from -1.0 to +1.0)
     */
    public void tankDrive(double leftDrive, double rightDrive){
        this.driveLeft(leftDrive);
        this.driveRight(rightDrive);
    }

    /**
     * Normalizes the input to 0.0 if it is below the value set by
     * {@link #DEADBAND_LIMIT} This is primarily used for reducing the strain on
     * motors.
     *
     * @param in the input to check
     * @return 0.0 if {@code in} is less than abs(DEADBAND_LIMIT) else {@code in}
     */
    public double deadband(double in) {
        return Math.abs(in) > DEADBAND_LIMIT ? in : 0.0;
    }

    /**
     * Runs the sweeping procedure.
     */
    public void sweep() {
        switch(sweepState) {
            
            case SET_ANGLE:
                sweepAngle();
                break;

            case SWEEP_LEFT:
                sweepLeft();
                break;

            case SWEEP_RIGHT:
                sweepRight();
                break;

            case IDLE:
                break;
        }
    }

    /**
     * Sets home gyro angle.
     */
    private void sweepAngle() {
        gyroAngle = gyro.getAngle();
        sweepState = SweepState.SWEEP_LEFT;
    }

    /**
     * Sweeps to the left 80 degrees.
     */
    private void sweepLeft() {
        if (gyro.getAngle() < gyroAngle + 80) {
            driveLeft(-0.50);
            driveRight(0.50);
        } else {
            sweepState = SweepState.SWEEP_RIGHT;
        }
    }

    /**
     * Sweeps to the right 80 degrees.

     */
    private void sweepRight() {
        if (gyro.getAngle() > gyroAngle - 80) {
            driveLeft(0.50);
            driveRight(-0.50);
        } else {
            sweepState = SweepState.SWEEP_LEFT;
        }
    }

    /**
     * Resets to the state to setting the angle.
     */
    public void resetState() {
        sweepState = SweepState.SET_ANGLE;
    }

    /**
     * Sets the state to idle.
     */
    public void stopSweep() {
        sweepState = SweepState.IDLE;
    }

    /**
     * Moves the robot to intercept powercells
     */
    public void approachPowercell() {
        if (detector.getNumber() != 0) {
            stopSweep();
            double x = detector.getTarget(0).getCenterX();
            double areaMod = detector.getTarget(0).getInvertedAreaPercent()/4;
            //TODO: Determine division variable for percent of screen
            driveLeft(x >= 0 ? 0.75 : (0.75 * areaMod));
            driveRight(x <= 0 ? 0.75 : (0.75 * areaMod));
        } else {
            if (sweepState == SweepState.IDLE) {
                resetState();
            }
            sweep();
        }
    }
}