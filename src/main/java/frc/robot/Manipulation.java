package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Manipulation{

    private Talon intakeWheel;
    private DoubleSolenoid intakePneumatics;
    private Talon indexLoad;
    private Talon indexFeed;
    private PhotoswitchSensor shootGate;
    private PhotoswitchSensor intakeGate;

    private int balls;
    private boolean prevShootState;
    private boolean prevIntakeState;

    /**
     * Constructor
     * 
     * @param intakeWheel The CAN id of the talon for the intake system
     * @param intakePneumatics The intake solenoid
     * @param indexLoad The CAN id of the talon for the index loader
     * @param indexFeed The CAN id of the talon for the index feeder
     * @param shootGate The shooting photoswitch object
     * @param intakeGate The intake photoswitch object
     */
    Manipulation(Talon intakeWheel, DoubleSolenoid intakePneumatics, Talon indexLoad, Talon indexFeed, PhotoswitchSensor shootGate, PhotoswitchSensor intakeGate) {
        this.intakeWheel = intakeWheel;
        this.indexLoad = indexLoad;
        this.indexFeed = indexFeed;
        this.intakePneumatics = intakePneumatics;
        this.shootGate = shootGate;
        this.intakeGate = intakeGate;
    }

    /**
     * Rotates the intake motor.
     * 
     * @param spin true if it should spin, false if not.
     */
    public void setIntakeSpin(boolean spin) {
        //TODO: test if this power is right
        intakeWheel.set(spin ? 1 : 0);
    }

    /**
     * Moves the intake system.
     * 
     * @param extend true if it should extend, false if not.
     */
    public void setIntakeExtend(boolean extend) {
        intakePneumatics.set(extend ? Value.kForward : Value.kReverse);
    }

    /**
     * Moves power cells down indexing system.
     * 
     * @param load true if it should load
     */
    public void setIndexLoad(boolean load) {
        //TODO: test if this power is right.
        indexLoad.set(load ? 0.5: 0);
    }

    /**
     * Feeds power cells into the scoring system.
     * 
     * @param feed if it should feed the shooter power cells.
     */
    public void setIndexFeed(boolean feed) {
        indexFeed.set(feed ? 1: 0);
    }
    
    /**
     * Updates the index counting.
     */
    public void updateIndex() {
        if(intakeGate.getChange(prevIntakeState))
            balls++;
        if(shootGate.getChange(prevShootState))
            balls--;
        prevShootState = shootGate.getBlocked();
        prevIntakeState = intakeGate.getBlocked();
    }

    /**
     * Gets current amount of balls in the index system.
     * 
     * @return current amount of balls in the index system.
     */
    public int getBalls() {
        return balls;
    }
}