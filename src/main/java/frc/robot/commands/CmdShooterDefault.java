package frc.robot.commands;

import java.util.function.DoubleSupplier;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Shooter;

public class CmdShooterDefault extends SwartdogCommand
{
    private Shooter _shooter;

    private DoubleSupplier _command;

    public CmdShooterDefault(Shooter shooter, DoubleSupplier command)
    {
        _shooter = shooter;
        _command = command;

        addRequirements(_shooter);
    }

    @Override
    public void execute()
    {
        _shooter.setShooterMotorSpeed(Constants.Shooter.FLYWHEEL_SPEED * _command.getAsDouble());
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
