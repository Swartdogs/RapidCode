package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Hanger;

public class CmdHangerWinchUntilLatched extends SwartdogCommand
{
    private Hanger _hanger;
    private int    _hookTimer;

    public CmdHangerWinchUntilLatched(Hanger hanger)
    {
        _hanger    = hanger;
        _hookTimer = 0;
    }

    @Override
    public void initialize()
    {
        _hanger.setWinchMotorSpeed(-Constants.Hanger.WINCH_SPEED);
        _hookTimer = (int)(Constants.Hanger.HOOK_WAIT_TIME * Constants.Hanger.LOOPS_PER_SECOND);
    }

    @Override
    public void execute()
    {
        if (_hanger.isHooked())
        {
            _hookTimer--;
        }
        else
        {
            if (_hanger.getLeftSwitchState() == State.On && _hanger.getRightSwitchState() == State.On)
            {
                _hanger.hook();
            }
        }
        
    }

    @Override
    public void end(boolean interrupted)
    {}

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
