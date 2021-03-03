package frc.robot;

public class Drivetrain {

    /**
     * {@value #DEADBAND_LIMIT} The limit for when to stop the motor running if the
     * motor speed is too low.
     */
    private static final double DEADBAND_LIMIT = 0.02;

    private DriveModule left;
    private DriveModule right;
    private PowercellDetection detector;

    /**
     * Constructor
     *
     * @param left     The left drive module
     * @param right    The right drive module
     * @param detector The powercell detection object.
     */
    Drivetrain(DriveModule left, DriveModule right, PowercellDetection detector) {
        this.left = left;
        this.right = right;
        right.setInverted(true);

        this.detector = detector;

        // Set the PTO to default to driving mode
        this.setPTO(true);
    }

    /**
     * Drives the left side of the robot either forward or backward.
     *
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveLeft(double speed) {
        double sp = deadband(speed);
        left.set(sp);
    }

    /**
     * Drives the right side of the robot either forward or backward.
     *
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveRight(double speed) {
        double sp = deadband(speed);
        right.set(sp);
    }

    public void setPTO(boolean engaged) {
        left.setPTO(engaged);
        right.setPTO(engaged);
    }

    /**
     * Drives the robot with an arcade style drive
     *
     * @param xDrive The speed to drive the drivetrain in the x direction (ranges
     *               from -1.0 to +1.0)
     * @param yDrive The speed to drive the drivetrain in the y direction (ranges
     *               from -1.0 to +1.0)
     */
    public void arcadeDrive(double turnInput, double speedInput) {
        this.driveLeft(speedInput - turnInput);
        this.driveRight(speedInput + turnInput);
    }

    /**
     * Drives the robot with an tank style drive
     *
     * @param xDrive The speed to drive the left drivetrain (ranges from -1.0 to
     *               +1.0)
     * @param yDrive The speed to drive the right drivetrain (ranges from -1.0 to
     *               +1.0)
     */
    public void tankDrive(double leftDrive, double rightDrive) {
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
     * Moves the robot to intercept powercells
     *
     * @param x target X-coordinate
     */
    public void approachPC(double x) {
        // TODO: Determine division variable for percent of screen
        driveLeft(x >= 0 ? 0.75 : (0.75 * (detector.getTarget(0).getInvertedAreaPercent() / 4)));
        driveRight(x <= 0 ? 0.75 : (0.75 * (detector.getTarget(0).getInvertedAreaPercent() / 4)));
    }

    public double getLeftEncoder(){
        return left.getEncoderCount();
    }

    public double getRightEncoder(){
        return right.getEncoderCount();
    }
}
