package frc.robot;

public class SquaredInput implements InputScaler
{
  private final double deadbandLimit;
  public SquaredInput(double deadbandLimit) {
    this.deadbandLimit = deadbandLimit;
  }

  public double scale(double input) {
    double numeratorFactor = (Math.abs(input)-deadbandLimit);
    double denominatorFactor = 1 - deadbandLimit;
    
    return (numeratorFactor * numeratorFactor) / (denominatorFactor * denominatorFactor);
  }
}