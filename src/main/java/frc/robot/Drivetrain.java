package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Drivetrain {
    
    private CANSparkMax leftNeo;
    private CANSparkMax leftSlave1;
    private CANSparkMax leftSlave2;
    private CANSparkMax rightNeo;
    private CANSparkMax rightSlave1;
    private CANSparkMax rightSlave2;

    /**
     * Constructor
     * 
     * @param leftNeo1ID The CAN id of the first left neo
     * @param leftNeo2ID The CAN id of the second left neo
     * @param leftNeo3ID The CAN id of the third left neo
     * @param rightNeo1ID The CAN id of the first right neo.
     * @param rightNeo2ID The CAN id of the second right neo.
     * @param rightNeo3ID The CAN id of the third right neo.
     */
    Drivetrain(int leftNeo1ID, int leftNeo2ID, int leftNeo3ID, int rightNeo1ID, int rightNeo2ID, int rightNeo3ID){
        CANSparkMax leftNeo = new CANSparkMax(leftNeo1ID, MotorType.kBrushless);
        CANSparkMax leftSlave1 = new CANSparkMax(leftNeo2ID, MotorType.kBrushless);
        CANSparkMax leftSlave2 = new CANSparkMax(leftNeo3ID, MotorType.kBrushless);
        CANSparkMax rightNeo = new CANSparkMax(rightNeo1ID, MotorType.kBrushless);
        CANSparkMax rightSlave1 = new CANSparkMax(rightNeo2ID, MotorType.kBrushless);
        CANSparkMax rightSlave2 = new CANSparkMax(rightNeo3ID, MotorType.kBrushless);

        leftNeo.setInverted(true);
        leftSlave1.setInverted(true);
        leftSlave2.setInverted(true);

        leftSlave1.follow(leftNeo);
        leftSlave2.follow(leftNeo);
        rightSlave1.follow(rightNeo);
        rightSlave2.follow(rightNeo);

    }

    /**
     * Drives the left side of the robot either forward or backward.
     * 
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveLeft(double speed){
        leftNeo.set(speed);
        leftSlave1.set(speed);
        leftSlave2.set(speed);

    }

    /**
     * Drives the right side of the robot either forward or backward.
     * 
     * @param speed the speed at which to drive (ranges from -1.0 to +1.0)
     */
    public void driveRight(double speed){
        rightNeo.set(speed);
        rightSlave1.set(speed);
        rightSlave2.set(speed);

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
}