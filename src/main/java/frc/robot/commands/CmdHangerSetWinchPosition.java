package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Hanger;

public class CmdHangerSetWinchPosition extends SwartdogCommand
{
    private Hanger _hanger;
    private double _position;
    private double _winchSpeed;
    
    public CmdHangerSetWinchPosition(Hanger hanger, double position, double winchSpeed)
    {
        _hanger     = hanger; 
        _position   = position;
        _winchSpeed = winchSpeed;
    }

    @Override
    public void initialize()
    {
        _hanger.winchInit(_position, _winchSpeed);
    }

    @Override
    public void execute()
    {
        _hanger.setWinchMotorSpeed(_hanger.runWinchPID());
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setWinchMotorSpeed(0);
    }

    @Override
    public boolean isFinished()
    {
        return _hanger.winchAtPostition();
    }
}
