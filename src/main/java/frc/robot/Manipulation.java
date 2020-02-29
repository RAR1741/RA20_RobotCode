package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import com.revrobotics.CANSparkMax;

public class Manipulation {

    private CANSparkMax intakeWheel;
    private DoubleSolenoid intakePneumatics;
    private CANSparkMax indexLoad;
    private CANSparkMax indexFeed;
    private PhotoswitchSensor shootGate;
    private PhotoswitchSensor intakeGate;
    private CANSparkMax indexPull;

    private int balls;
    private boolean prevShootState;
    private boolean prevIntakeState;

    /**
     * Constructor
     * 
     * @param intakeWheel      The CAN id of the spark for the intake system
     * @param intakePneumatics The intake solenoid
     * @param indexLoad        The CAN id of the spark for the index loader
     * @param indexFeed        The CAN id of the spark for the index feeder
     * @param shootGate        The shooting photoswitch object
     * @param intakeGate       The intake photoswitch object
     * @param indexPull        The CAN id of the spark for pulling balls into the
     *                         shooter
     */
    Manipulation(CANSparkMax intakeWheel, CANSparkMax indexLoad, CANSparkMax indexFeed, PhotoswitchSensor shootGate,
            PhotoswitchSensor intakeGate) {
        this.intakeWheel = intakeWheel;
        this.indexLoad = indexLoad;
        this.indexFeed = indexFeed;
        // this.intakePneumatics = intakePneumatics;
        this.shootGate = shootGate;
        this.intakeGate = intakeGate;
        // this.indexPull = indexPull;

        intakeWheel.setInverted(true);
    }

    /**
     * Rotates the intake motor.
     * 
     * @param spin true if it should spin, false if not.
     */
    public void setIntakeSpin(boolean spin) {
        // TODO: test if this power is right
        intakeWheel.set(spin ? -0.5 : 0);
    }

    /**
     * Moves the intake system.
     * 
     * @param extend true if it should extend, false if not.
     */
    public void setIntakeExtend(boolean extend) {
        // intakePneumatics.set(extend ? Value.kForward : Value.kReverse);
    }

    /**
     * Moves power cells down indexing system.
     * 
     * @param load true if it should load
     */
    public void setIndexLoad(boolean load) {
        // TODO: test if this power is right.
        indexLoad.set(load ? 0.25 : 0);
    }

    /**
     * Feeds power cells into the scoring system.
     * 
     * @param power the power thr motor pull turn at.
     */
    public void setIndexFeed(double power) {
        indexFeed.set(power);
    }

    public void setIndexPull(boolean pull) {
        // indexPull.set(pull ? 1 : 0);
    }

    public void shootAllTheThings(boolean fire) {
        indexFeed.set(fire ? 0.75 : 0);
        indexLoad.set(fire ? 0.75 : 0);
    }

    /**
     * Updates the index counting.
     */
    public void updateIndex() {
        if (intakeGate.getChange(prevIntakeState))
            balls++;
        if (shootGate.getChange(prevShootState))
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