package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class PhotoswitchSensor {
    DigitalInput input;

    /**
     * Constructor
     * 
     * @param input The DIO port of the photoswitch sensor.
     */
    public PhotoswitchSensor(DigitalInput input) {
        this.input = input;
    }

    /**
     * Get current light state.
     * 
     * @return true if it's line of sight is blocked.
     */
    public boolean getBlocked() {
        return input.get();
    }

    /**
     * Gets if the state has changed from false to true.
     * 
     * @param previousState previous state of the sensor.
     * @return true if the sensor became blocked since last check.
     */
    public boolean getChange(boolean previousState) {
        return !previousState && this.getBlocked();
    }
}