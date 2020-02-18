package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class PhotoswitchSensor {
    DigitalInput input;

    private boolean previousState;

    public PhotoswitchSensor(DigitalInput input) {
        this.input = input;
    }

    public void update() {
        previousState = this.getBlocked();
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
     * @return true if the sensor became blocked since last check.
     */
    public boolean getChange() {
        return !previousState && this.getBlocked();
    }
}