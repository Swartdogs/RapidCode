package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class CmdWait extends CommandBase 
{
    private double _delay;
    private int    _counter;

    public CmdWait(double delay) 
    {
        _delay   = delay;
        _counter = 0;
    }

    @Override
    public void initialize()
    {
        _counter = Math.max(0, (int)(50 * _delay));
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
