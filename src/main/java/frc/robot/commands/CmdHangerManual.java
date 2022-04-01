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
        _hanger.setWinchMotorSpeed(_command.getAsDouble() * Constants.Hanger.RESET_SPEED);
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setWinchMotorSpeed(0);
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
