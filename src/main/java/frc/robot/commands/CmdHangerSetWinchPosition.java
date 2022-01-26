package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.subsystems.Hanger;

public class CmdHangerSetWinchPosition extends SwartdogCommand
{
    private Hanger      _hanger;
    private ExtendState _state;
    public CmdHangerSetWinchPosition(Hanger hanger, ExtendState state)
    {
        _hanger = hanger; 
        _state  = state; 
    }

    @Override
    public void initialize()
    {
        _hanger.setWinchPosition(Constants.HANGER_WINCH_POSITION_LOOKUP.apply(_state));
    }

    @Override
    public boolean isFinished()
    {
        return _hanger.winchAtPostition();
    }
}