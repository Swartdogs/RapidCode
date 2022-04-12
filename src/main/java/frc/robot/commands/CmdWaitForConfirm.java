package frc.robot.commands;

import java.util.function.BooleanSupplier;

import frc.robot.abstraction.SwartdogCommand;

public class CmdWaitForConfirm extends SwartdogCommand
{
    private BooleanSupplier _confirm;

    public CmdWaitForConfirm(BooleanSupplier confirm)
    {
        _confirm = confirm;
    }

    @Override
    public boolean isFinished()
    {
        return _confirm.getAsBoolean();
    }
}
