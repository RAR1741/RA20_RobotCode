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
     * @param left The left drive module
     * @param right The right drive module
     * @param detector The powercell detection object.
     */
    Drivetrain(DriveModule left, DriveModule right, PowercellDetection detector){
        this.left = left;
        this.right = right;
        left.setInverted(true);

        this.detector = detector;
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
     * Moves the robot to intercept powercells.
     */
    public void approachPowercell() {
        double x = detector.getTarget(0).getCenterX();
        //TODO: Determine division variable for percent of screen
        double areaMod = detector.getTarget(0).getInvertedAreaPercent()/4;
        driveLeft(x >= 0 ? 0.75 : (0.75 * areaMod));
        driveRight(x <= 0 ? 0.75 : (0.75 * areaMod));
    }
}