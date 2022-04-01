package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Hanger;

public class CmdHangerSetArmPosition extends SwartdogCommand
{
    private Hanger _hanger;
    private double _position;

    public CmdHangerSetArmPosition(Hanger hanger, double position)
    {
        _hanger   = hanger;
        _position = position;
    }

    @Override
    public void initialize()
    {
        _hanger.setArmPosition(_position);
    }

    @Override
    public void execute()
    {
        _hanger.setArmMotorSpeed(_hanger.runArmPID());
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setArmMotorSpeed(0);
    }

    @Override
    public boolean isFinished()
    {
        return _hanger.armAtPosition();
    }
}
