package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class PhotoswitchSensor {
    DigitalInput input;

    public PhotoswitchSensor(DigitalInput input) {
        this.input = input;
    }

    /**
     * Get current light state.
     * 
     * @return true if it can see retroreflective tape.
     */
    public boolean getClear() {
        return input.get();
    }
}