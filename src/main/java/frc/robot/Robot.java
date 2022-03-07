package frc.robot;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.Solenoid;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.abstraction.Joystick;

public class Robot extends TimedRobot
{
    private Command _autonomousCommand;

    private RobotContainer _robotContainer;

    private Motor    _winch;
    private Motor    _arms;
    private Solenoid _hooks;
    private Solenoid _locks;
    private Joystick _joystick;
    private PowerDistribution _pdh;

    @Override
    public void robotInit()
    {
        _robotContainer = new RobotContainer();   
        // _pdh            = new PowerDistribution(1, ModuleType.kRev);
        // _winch          = Motor.neo(18);
        // _arms           = Motor.neo(19);
        // _hooks          = Solenoid.invert(Solenoid.solenoid(2, PneumaticsModuleType.REVPH, 15));
        // _locks          = Solenoid.invert(Solenoid.solenoid(2, PneumaticsModuleType.REVPH, 9));
        // _joystick       = Joystick.joystick(0);

        // _joystick.setXDeadband(0.05);
        // _joystick.setYDeadband(0.05);
    }

    @Override
    public void robotPeriodic()
    {
        CommandScheduler.getInstance().run();
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
        _autonomousCommand = _robotContainer.getAutonomousCommand();

        if (_autonomousCommand != null)
        {
            _autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic()
    {}

    @Override
    public void teleopInit()
    {
        if (_autonomousCommand != null)
        {
            _autonomousCommand.cancel();
        }

    }

    @Override
    public void teleopPeriodic()
    {
        // _arms.set(_joystick.getX());
        // if (_locks.get() == ExtendState.Retracted)
        // {
        //     _winch.set(_joystick.getY());
        // }
        // else
        // {
        //     _winch.set(0);
        // }

        // if (_joystick.getButton(8).get() == State.On) 
        // {
        //     _hooks.extend();
        // }

        // if (_joystick.getButton(10).get() == State.On) 
        // {
        //     _hooks.retract();
        // }

        // if (_joystick.getButton(1).get() == State.Off)
        // {
        //     _locks.extend();
        // }
        // else
        // {
        //     _locks.retract();
        // }

        // System.out.println(_pdh.getCurrent(5) + " " + _winch.get());
    }

    @Override
    public void testInit()
    {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic()
    {}
}
