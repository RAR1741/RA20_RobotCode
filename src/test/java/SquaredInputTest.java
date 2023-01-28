import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import frc.robot.SquaredInput;

public class SquaredInputTest {
  private static double CLOSE_ENOUGH = 0.01;
  private static final SquaredInput sq = new SquaredInput(0.01);

  @Test
  public void testNeutral() {
    assertEquals(0.0, sq.scale(0.0), CLOSE_ENOUGH);
  }

  @Test
  public void testOne() {
    assertEquals(1.0, sq.scale(1.0), CLOSE_ENOUGH);
  }

  @Test
  public void testHalf() {
    assertEquals(0.25, sq.scale(0.5), CLOSE_ENOUGH);
  }
}