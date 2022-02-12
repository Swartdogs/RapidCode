package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.abstraction.Joystick;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.Enumerations.State;

public class Robot extends TimedRobot
{

    private       Motor    _shooter;
    private       Motor    _ballpath;
    private       Joystick _joystick;
    private final double   MAX_RPM = 5398;

    @Override
    public void   robotInit()
    {
        _joystick = Joystick.joystick(0);
        _shooter = Motor.compose(Motor.falconFlywheel(10,MAX_RPM),Motor.falconFlywheel(11, MAX_RPM));
        _ballpath = Motor.compose(Motor.victorSP(0),Motor.victorSP(1),Motor.neo(12));
    }

    @Override
    public void robotPeriodic()
    {

    }

    @Override
    public void disabledInit()
    {
        _shooter.set(0);
        _ballpath.set(0);
    }

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
        _shooter.set((((-_joystick.getThrottle())+1)/2)*MAX_RPM);

        if(_joystick.getButton(11).get() == State.On) 
        {
            _ballpath.set(1);
        }
        else if(_joystick.getButton(10).get() == State.On)
        {
            _ballpath.set(0.5);
        }
        else
        {
            _ballpath.set(0);
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
