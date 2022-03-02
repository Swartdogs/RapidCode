package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Dashboard.DashValue;
import frc.robot.Dashboard.RobotValue;
import frc.robot.abstraction.Joystick;
import frc.robot.abstraction.Enumerations.State;

public class Robot extends TimedRobot
{
    private Joystick _joystick;
    private Dashboard _dashboard;

    @Override
    public void robotInit()
    {
        _joystick = Joystick.joystick(0);
        _dashboard = new Dashboard("2019", 1, 20, 1, 63);
    }

    @Override
    public void robotPeriodic()
    {
        if (_joystick.getButton(6).get() == State.On)
        {
            _dashboard.setRobotValue(RobotValue.rvDriveGyro, 6);
        }

        else if (_joystick.getButton(7).get() == State.On)
        {
            _dashboard.setRobotValue(RobotValue.rvDriveGyro, 7);        }

        else
        {
            _dashboard.setRobotValue(RobotValue.rvDriveGyro, 0);
        }
    }

    @Override
    public void disabledInit()
    {}

    @Override
    public void disabledPeriodic()
    {}

    @Override
    public void autonomousInit()
    {

    }

    @Override
    public void autonomousPeriodic()
    {}

    @Override
    public void teleopInit()
    {

    }

    @Override
    public void teleopPeriodic()
    {
    }

    @Override
    public void testInit()
    {

    }

    @Override
    public void testPeriodic()
    {}
}
