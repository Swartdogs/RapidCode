package frc.robot;

import edu.wpi.first.util.net.PortForwarder;
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
        PortForwarder.add(5800, "limelight.local", 5800);
        PortForwarder.add(5801, "limelight.local", 5801);
        PortForwarder.add(5802, "limelight.local", 5802);
        PortForwarder.add(5803, "limelight.local", 5803);
        PortForwarder.add(5804, "limelight.local", 5804);
        PortForwarder.add(5805, "limelight.local", 5805);

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
