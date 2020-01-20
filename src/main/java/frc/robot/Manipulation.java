package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Manipulation{

    private Talon intakeWheel;
    private DoubleSolenoid intakePneumatics;
    private Talon indexLoad;
    private Talon indexFeed;

    /**
     * Constructor
     * 
     * @param intakeWheel The CAN id of the talon for the intake system
     * @param intakePneumatics The intake solenoid
     * @param indexLoad The CAN id of the talon for the index loader
     * @param indexFeed The CAN id of the talon for the index feeder
     */
    Manipulation(Talon intakeWheel, DoubleSolenoid intakePneumatics, Talon indexLoad, Talon indexFeed) {
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

    /**
     * Moves power cells down indexing system.
     */
    public void indexLoad(){
        //TODO: test if this power is right
        indexLoad.set(0.5);
    }

    /**
     * Feeds power cells into the scoring system.
     */
    public void indexFeed(){
        indexFeed.set(1);
    }
}