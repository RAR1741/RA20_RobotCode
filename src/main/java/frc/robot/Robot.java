/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty.PropertyReferring;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  private CANSparkMax m_testMotor;
  
  @Override
  public void robotInit() {
    int deviceID = 2;
    m_testMotor = new CANSparkMax(deviceID,  MotorType.kBrushless);

  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    m_testMotor.set(1);
    sleep(3000);
    m_testMotor.set(-1);
    sleep(3000);
    m_testMotor.set(0);

  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  protected void sleep(int ms) {
    try {
      wait(ms);
    } catch (Exception e) {
      //TODO: handle exception
    }
  }
}
