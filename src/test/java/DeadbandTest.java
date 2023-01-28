import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import frc.robot.Deadband;

public class DeadbandTest {
  private static double CLOSE_ENOUGH = 0.0001;
  private static final Deadband db = new Deadband(0.01);

  @Test
  public void testDeadbandNeutral() {
    Deadband db = new Deadband(0.01);
    assertEquals(0.0, db.scale(0.0), CLOSE_ENOUGH);
  }

  @Test
  public void testDeadbandExtreme() {
    assertEquals(1.0, db.scale(1.0), CLOSE_ENOUGH);
  }

  @Test
  public void testDeadbandLimitsCloseToZero() {
    assertEquals(0.0, db.scale(0.005), CLOSE_ENOUGH);
  }

  @Test
  public void testDeadBandHalf() {
    assertEquals(0.5, db.scale(0.5), CLOSE_ENOUGH);
  }
}