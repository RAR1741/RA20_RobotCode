package frc.robot;

public class Drivetrain {
    
    /**
     * {@value #DEADBAND_LIMIT} The limit for when to stop the motor running if the
     * motor speed is too low.
     */
    private static final double DEADBAND_LIMIT = 0.02;

    private DriveModule left;
    private DriveModule right;

    /**
     * Constructor
     * 
     * @param left The left drive module
     * @param right The right drive module
     */
    Drivetrain(DriveModule left, DriveModule right){
        this.left = left;
        this.right = right;
        left.setInverted(true);
    }

    /**
     * Drives the left side of the robot either forward or backward.
     * 
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveLeft(double speed, boolean boost){
        double sp = deadband(speed, boost);
        left.set(sp);
    }

    /**
     * Drives the right side of the robot either forward or backward.
     * 
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveRight(double speed, boolean boost){
        double sp = deadband(speed, boost);
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
    public void arcadeDrive(double xDrive, double yDrive, boolean boost){
        this.driveLeft(yDrive - xDrive, boost);
        this.driveRight(xDrive - yDrive, boost);
    }

    /**
     * Drives the robot with an tank style drive
     *
     * @param xDrive The speed to drive the left drivetrain (ranges
     *               from -1.0 to +1.0)
     * @param yDrive The speed to drive the right drivetrain (ranges
     *               from -1.0 to +1.0)
     */
    public void tankDrive(double leftDrive, double rightDrive, boolean boost){
        this.driveLeft(leftDrive, boost);
        this.driveRight(rightDrive, boost);
    }

    /**
     * Normalizes the input to 0.0 if it is below the value set by
     * {@link #DEADBAND_LIMIT} This is primarily used for reducing the strain on
     * motors.
     *
     * @param in the input to check
     * @param boost whether or not the boost button is being pressed 
     * @return 0.0 if {@code in} is less than abs(DEADBAND_LIMIT) else {@code in}
     */
    public double deadband(double in, boolean boost) {
        double out = ((Math.abs(in)-DEADBAND_LIMIT)*(Math.abs(in)-DEADBAND_LIMIT))/((1-DEADBAND_LIMIT)*(1-DEADBAND_LIMIT));
        return Math.abs(in) > DEADBAND_LIMIT ? (in > 0 ? (boost ? out : out*0.6) : (boost ? -out : -out*0.6)) : 0.0;
    }
}