package frc.robot;

public class BoostInput implements InputScaler
{
  private final double powerCap;
  private boolean enabled;

  public BoostInput(double powerCap) {
    this.powerCap = powerCap;
    this.enabled = false;
  }

  public double scale(double input) {
    return enabled ? input : input * powerCap;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}