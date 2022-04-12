package frc.robot.commands;

import java.util.function.DoubleSupplier;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Hanger;

public class CmdHangerManual extends SwartdogCommand
{
    private Hanger _hanger;
    private DoubleSupplier _command;

    public CmdHangerManual(Hanger hanger, DoubleSupplier command)
    {
        _hanger = hanger;
        _command = command;
    }

    @Override
    public void execute()
    {
        double speed = _command.getAsDouble() * Constants.Hanger.WINCH_SPEED;

        if ((speed > 0 && _hanger.getWinchPosition() >= Constants.Hanger.MAX_WINCH_LENGTH) ||
            (speed < 0 && _hanger.getWinchPosition() <= Constants.Hanger.MIN_WINCH_LENGTH))
        {
            speed = 0;   
        }

        // if (speed < 0)
        // {
        //     _hanger.useArmPID(false);
        // }
        // else
        // {
        //     _hanger.useArmPID(true);
        // }

        _hanger.setWinchMotorSpeed(speed);
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setWinchMotorSpeed(0);
        // _hanger.useArmPID(true);
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
