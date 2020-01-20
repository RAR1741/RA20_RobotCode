package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Manipulation{

    private Talon intakeWheel;
    private DoubleSolenoid intakePneumatics;

    /**
     * Constructor
     * 
     * @param intakeWheel The CAN id of the talon for the intake system
     * @param intakePneumatics The intake solenoid
     */
    Manipulation(Talon intakeWheel, DoubleSolenoid intakePneumatics) {
        this.intakeWheel = intakeWheel;
        this.intakePneumatics = intakePneumatics;
    }

    /**
     * Rotates the intake motor.
     */
    public void intakeSpin(){
        //TODO: test if this power is right
        intakeWheel.set(1);
    }

    /**
     * Stops the rotation of the intake motor.
     */
    public void intakeStop(){
        intakeWheel.set(0);
    }

    /**
     * Extends the intake system.
     */
    public void intakeOut(){
        intakePneumatics.set(Value.kForward);
    }

    /**
     * Retracts the intake system.
     */
    public void intakeIn(){
        intakePneumatics.set(Value.kReverse);
    }
}