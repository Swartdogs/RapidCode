package frc.robot.commands;

import java.util.function.BooleanSupplier;

import frc.robot.abstraction.SwartdogCommand;

public class CmdWaitForCondition extends SwartdogCommand
{
    private BooleanSupplier _condition;

    public CmdWaitForCondition(BooleanSupplier confirm)
    {
        _condition = confirm;
    }

    @Override
    public boolean isFinished()
    {
        return _condition.getAsBoolean();
    }
}
