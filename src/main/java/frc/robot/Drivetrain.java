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
    
    private CANSparkMax m1 = new CANSparkMax(DeviceID1, MotorType.kBrushless);
    private CANSparkMax m2 = new CANSparkMax(DeviceID2, MotorType.kBrushless);
    private CANSparkMax m3 = new CANSparkMax(DeviceID3, MotorType.kBrushless);
    private CANSparkMax m4 = new CANSparkMax(DeviceID4, MotorType.kBrushless);
    private CANSparkMax m5 = new CANSparkMax(DeviceID5, MotorType.kBrushless);
    private CANSparkMax m6 = new CANSparkMax(DeviceID6, MotorType.kBrushless);


    public void driveLeft(double speed){
        m1.set(speed);
        m2.set(speed);
        m3.set(speed);

    }


    public void driveRight(double speed){
        m4.set(speed);
        m5.set(speed);
        m6.set(speed);

    }


    public void arcadeDrive(double xDrive, double yDrive){
        this.driveLeft(yDrive-xDrive);
        this.driveRight(xDrive-yDrive);
    }


    public void tankDrive(double leftDrive, double rightDrive){
        this.driveLeft(leftDrive);
        this.driveRight(rightDrive);
    }
}