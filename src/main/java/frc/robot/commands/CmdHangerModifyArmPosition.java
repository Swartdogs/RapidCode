package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Hanger;

public class CmdHangerModifyArmPosition extends SwartdogCommand
{
    private Hanger _hanger;
    private double _modify;
    
    public CmdHangerModifyArmPosition(Hanger hanger, double modify)
    {
        _hanger = hanger;
        _modify = modify;
    }

    @Override
    public void initialize()
    {
        _hanger.setArmPosition(_hanger.getArmTarget() + _modify);
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setArmPosition(_hanger.getArmTarget() - _modify);
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
