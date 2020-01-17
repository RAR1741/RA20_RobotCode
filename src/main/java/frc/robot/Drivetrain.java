package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drivetrain {
    
    private int DeviceID1 = 1;
    private int DeviceID2 = 2;
    private int DeviceID3 = 3;
    private int DeviceID4 = 4;
    private int DeviceID5 = 5;
    private int DeviceID6 = 6;
    
    private CANSparkMax leftNeo = new CANSparkMax(DeviceID1, MotorType.kBrushless);
    private CANSparkMax leftSlave1 = new CANSparkMax(DeviceID2, MotorType.kBrushless);
    private CANSparkMax leftSlave2 = new CANSparkMax(DeviceID3, MotorType.kBrushless);
    private CANSparkMax rigthNeo = new CANSparkMax(DeviceID4, MotorType.kBrushless);
    private CANSparkMax rightSlave1 = new CANSparkMax(DeviceID5, MotorType.kBrushless);
    private CANSparkMax rightSlave2 = new CANSparkMax(DeviceID6, MotorType.kBrushless);


    public void driveLeft(double speed){
        leftNeo.set(speed);
        leftSlave1.set(speed);
        leftSlave2.set(speed);

    }


    public void driveRight(double speed){
        rigthNeo.set(speed);
        rightSlave1.set(speed);
        rightSlave2.set(speed);

    }


    public void arcadeDrive(double xDrive, double yDrive){
        this.driveLeft(yDrive - xDrive);
        this.driveRight(xDrive - yDrive);
    }


    public void tankDrive(double leftDrive, double rightDrive){
        this.driveLeft(leftDrive);
        this.driveRight(rightDrive);
    }
}