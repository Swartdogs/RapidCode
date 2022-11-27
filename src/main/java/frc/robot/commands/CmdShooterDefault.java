package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.SubsystemContainer;
import frc.robot.subsystems.Dashboard;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;

public class CmdShooterDefault extends CommandBase
{
    private Shooter   _shooter;
    private Vision    _vision;
    private Dashboard _dashboard;

    private DoubleSupplier _command;

    public CmdShooterDefault(SubsystemContainer subsystemContainer, DoubleSupplier command)
    {
        _shooter   = subsystemContainer.getShooter();
        _vision    = subsystemContainer.getVision();
        _dashboard = subsystemContainer.getDashboard();
        _command   = command;

        addRequirements(_shooter);
    }

    @Override
    public void execute()
    {
        _shooter.setShooterMotorSpeed(_dashboard.getShooterFlywheelSpeed() * _command.getAsDouble());
        System.out.println(String.format("Distance: %6.2f, Hood: %4d, Shooter: %4d (%4d)", _vision.getTargetDistance(), (int)_shooter.getHoodSetpoint(), (int)_shooter.getShooterTargetRPM(), (int)_shooter.getShooterRPM()));
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
