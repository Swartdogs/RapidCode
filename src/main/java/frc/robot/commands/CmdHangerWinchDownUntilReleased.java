package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Hanger;

public class CmdHangerWinchDownUntilReleased extends SwartdogCommand
{
    private Hanger _hanger;

    private int    _timer;

    public CmdHangerWinchDownUntilReleased(Hanger hanger)
    {
        _hanger = hanger;
    }

    @Override
    public void initialize()
    {
        _timer = (int)(Constants.LOOPS_PER_SECOND * Constants.Hanger.RESET_WAIT_TIME);
        _hanger.setWinchMotorSpeed(-Constants.Hanger.WINCH_SPEED);
    }

    @Override
    public void execute()
    {
        _timer--;
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setWinchMotorSpeed(0);
    }

    @Override
    public boolean isFinished()
    {
        return _timer <= 0;
    }
}
