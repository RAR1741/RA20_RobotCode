package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Manipulation{

    private Talon intakeWheel;
    private DoubleSolenoid intakePneumatics;

    Manipulation(Talon intakeWheel, DoubleSolenoid intakePneumatics) {
        this.intakeWheel = intakeWheel;
        this.intakePneumatics = intakePneumatics;
    }

    public void intakeSpin(){
        //TODO: test if this power is right
        intakeWheel.set(1);
    }

    public void intakeOut(){
        intakePneumatics.set(Value.kForward);
    }

    public void intakeIn(){
        intakePneumatics.set(Value.kReverse);
    }
}