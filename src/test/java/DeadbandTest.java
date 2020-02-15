import org.junit.Test;

import frc.robot.Robot;

import static org.junit.Assert.assertEquals;

public class DeadbandTest {
  private static double CLOSE_ENOUGH = 0.0001;
  @Test
  public void testDeadbandNeutral() {
    assertEquals(0.0, Robot.deadband(0.0), CLOSE_ENOUGH);
  }

  @Test
  public void testDeadbandExtreme() {
    assertEquals(1.0, Robot.deadband(1.0), CLOSE_ENOUGH);
  }

  @Test
  public void testDeadbandLimitsCloseToZero() {
    assertEquals(0.0, Robot.deadband(0.005), CLOSE_ENOUGH);
  }
}