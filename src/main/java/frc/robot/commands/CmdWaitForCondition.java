package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class CmdWaitForCondition extends CommandBase
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
