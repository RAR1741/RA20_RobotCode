import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import frc.robot.BoostInput;

public class BoostInputTest {
  private static double CLOSE_ENOUGH = 0.0001;

  @Test
  public void testBoostDisabled() {
    BoostInput boost = new BoostInput(0.5);
    assertEquals(0.4, boost.scale(0.8), CLOSE_ENOUGH);
  }

  @Test
  public void testBoostEnabled() {
    BoostInput boost = new BoostInput(0.5);
    boost.setEnabled(true);

    assertEquals(0.8, boost.scale(0.8), CLOSE_ENOUGH);
  }
}
