import org.junit.Test;

import frc.robot.Robot;

import static org.junit.Assert.assertEquals;

public class DeadbandTest {
  // Dummy test for verifying the ability to run tests.
  @Test
  public void testDeadbandNeutral() {
    double closeEnough = 0.0001;
    
    assertEquals(0.0, Robot.deadband(0.0), closeEnough);
  }
}