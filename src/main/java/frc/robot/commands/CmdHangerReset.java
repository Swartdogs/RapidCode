package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.subsystems.Hanger;

public class CmdHangerReset extends SwartdogCommand
{
    private Hanger      _hanger;
    private ExtendState _direction;
    private int         _timer;

    public CmdHangerReset(Hanger hanger, ExtendState direction)
    {
        _hanger    = hanger;
        _direction = direction;
    }

    @Override
    public void initialize()
    {
        _timer = (int)(Constants.Hanger.RESET_WAIT_TIME * Constants.LOOPS_PER_SECOND);
        _hanger.disengageRatchet();
        _hanger.setWinchMotorSpeed(-Constants.Hanger.RESET_SPEED);
    }

    @Override
    public void execute()
    {
        if (_direction == ExtendState.Extended)
        {
            if (_timer > 0)
            {
                _timer--;
            }
            else
            {
                _hanger.setWinchMotorSpeed(Constants.Hanger.RESET_SPEED);
            }
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setWinchMotorSpeed(0);
        _hanger.engageRatchet();
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
