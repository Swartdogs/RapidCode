package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.SubsystemContainer;

public class CmdWaitAuto extends CommandBase 
{
    private SubsystemContainer _subsystemContainer;

    private int _counter;

    public CmdWaitAuto(SubsystemContainer subsystemContainer)
    {
        _subsystemContainer = subsystemContainer;

        _counter = 0;
    }

    @Override
    public void initialize()
    {
        double delay = _subsystemContainer.getDashboard().getAutoDelay();
        _counter = Math.max(0, (int)(50 * delay));
        _subsystemContainer.getRobotLog().log(String.format("Auto Delay: %6.1f", delay));
    }

    @Override
    public void execute() 
    {
        _counter--;
    }

    @Override
    public boolean isFinished() 
    {
        return _counter <= 0;
    }
}
