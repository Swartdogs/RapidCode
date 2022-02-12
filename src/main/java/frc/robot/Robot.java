package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.abstraction.Joystick;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.Enumerations.State;

public class Robot extends TimedRobot
{
    private Joystick _joystick;

    private Motor _motor;

    @Override
    public void robotInit()
    {
        _joystick = Joystick.joystick(0);
        _motor    = Motor.neo(2);
    }

    @Override
    public void robotPeriodic()
    {

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
        if (_joystick.getButton(11).get() == State.On)
        {
            _motor.set(1);
        }

        else if (_joystick.getButton(10).get() == State.On)
        {
            _motor.set(0.5);
        }

        else
        {
            _motor.set(0);
        }
    }

    @Override
    public void testInit()
    {

    }

    @Override
    public void testPeriodic()
    {}
}
