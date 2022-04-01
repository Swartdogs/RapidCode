package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Hanger;

public class CmdHangerSetPosition extends SwartdogCommand
{
    private Hanger _hanger;
    private double _armPosition;
    private double _winchPosition;

    public CmdHangerSetPosition(Hanger hanger, double armPosition, double winchPosition)
    {
        _hanger        = hanger;
        _armPosition   = armPosition;
        _winchPosition = winchPosition;
    }

    @Override
    public void initialize()
    {
        _hanger.setArmPosition(_armPosition);
        _hanger.setWinchPosition(_winchPosition);
    }

    @Override
    public void execute()
    {
        _hanger.setArmMotorSpeed(_hanger.runArmPID());
        _hanger.setWinchMotorSpeed(_hanger.runWinchPID());
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setArmMotorSpeed(0);
        _hanger.setWinchMotorSpeed(0);
    }

    @Override
    public boolean isFinished()
    {
        return _hanger.armAtPosition() && _hanger.winchAtPostition();
    }
}
