package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class PhotoswitchSensor {
    AnalogInput input;
    private final double LIGHT_THRESHOLD = 25;

    public PhotoswitchSensor(AnalogInput input) {
        this.input = input;
    }

    /**
     * Get current light state.
     * 
     * @return true if it can see retroreflective tape.
     */
    public boolean getClear() {
        double voltage = input.getVoltage();

        return voltage > LIGHT_THRESHOLD;
    }
}