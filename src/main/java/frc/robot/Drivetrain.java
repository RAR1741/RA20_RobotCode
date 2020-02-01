package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class Drivetrain {
    
    /**
     * {@value #DEADBAND_LIMIT} The limit for when to stop the motor running if the
     * motor speed is too low.
     */
    private static final double DEADBAND_LIMIT = 0.02;

    private TalonFX leftFalcon;
    private TalonFX leftSlave1;
    private TalonFX leftSlave2;
    private TalonFX rightFalcon;
    private TalonFX rightSlave1;
    private TalonFX rightSlave2;

    /**
     * Constructor
     * 
     * @param leftFalcon1Id The CAN id of the first left falcon.
     * @param leftFalcon2Id The CAN id of the second left falcon.
     * @param leftFalcon3Id The CAN id of the third left falcon.
     * @param rightFalcon1ID The CAN id of the first right falcon.
     * @param rightFalcon2ID The CAN id of the second right falcon.
     * @param rightFalcon3ID The CAN id of the third right falcon.
     */
    Drivetrain(int leftFalcon1Id, int leftFalcon2Id, int  leftFalcon3Id, int  rightFalcon1Id, int  rightFalcon2Id, int  rightFalcon3Id){
        leftFalcon = new TalonFX(leftFalcon1Id);
        leftSlave1 = new TalonFX(leftFalcon2Id);
        leftSlave2 = new TalonFX(leftFalcon3Id);
        rightFalcon = new TalonFX(rightFalcon1Id);
        rightSlave1 = new TalonFX(rightFalcon2Id);
        rightSlave2 = new TalonFX(rightFalcon3Id);

        leftFalcon.setInverted(true);
        leftSlave1.setInverted(true);
        leftSlave2.setInverted(true);

        leftSlave1.follow(leftFalcon);
        leftSlave2.follow(leftFalcon);
        rightSlave1.follow(rightFalcon);
        rightSlave2.follow(rightFalcon);

    }

    /**
     * Drives the left side of the robot either forward or backward.
     * 
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveLeft(double speed){
        double sp = deadband(speed);
        leftFalcon.set(sp);
    }

    /**
     * Drives the right side of the robot either forward or backward.
     * 
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveRight(double speed){
        double sp = deadband(speed);
        rightFalcon.set(sp);
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
}