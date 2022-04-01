package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.GameMode;

public class Robot extends TimedRobot
{
    private Command        _autonomousCommand;
    private RobotContainer _robotContainer;

    @Override
    public void robotInit()
    {
        _robotContainer = new RobotContainer();
        _robotContainer.setGameMode(GameMode.Init);
    }

    @Override
    public void robotPeriodic()
    {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit()
    {
        _robotContainer.setGameMode(GameMode.Disabled);
    }

    @Override
    public void autonomousInit()
    {
        _robotContainer.setGameMode(GameMode.Autonomous);

        _autonomousCommand = _robotContainer.getAutonomousCommand();

        if (_autonomousCommand != null)
        {
            _autonomousCommand.schedule();
        }
    }

    @Override
    public void teleopInit()
    {
        _robotContainer.setGameMode(GameMode.Teleop);

        if (_autonomousCommand != null)
        {
            _autonomousCommand.cancel();
        }
    }

    @Override
    public void testInit()
    {
        _robotContainer.setGameMode(GameMode.Test);

        CommandScheduler.getInstance().cancelAll();
    }
}
