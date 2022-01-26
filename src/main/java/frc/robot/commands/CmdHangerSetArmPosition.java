package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.subsystems.Hanger;

public class CmdHangerSetArmPosition extends SwartdogCommand
{
    private Hanger      _hanger;
    private ExtendState _state;

    public CmdHangerSetArmPosition(Hanger hanger, ExtendState state)
    {
        _hanger = hanger;
        _state  = state;
    }

    @Override
    public void initialize()
    {
        _hanger.setArmPosition(Constants.HANGER_ARM_POSITION_LOOKUP.apply(_state));
    }

    @Override
    public boolean isFinished()
    {
        return _hanger.armAtPosition();
    }
}
