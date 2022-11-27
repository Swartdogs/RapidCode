package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.SubsystemContainer;
import frc.robot.subsystems.*;

public class CmdPickupStow extends CommandBase
{
    private Pickup _pickup;
    private RobotLog _log;

    public CmdPickupStow(SubsystemContainer subsystemContainer)
    {
        _pickup = subsystemContainer.getPickup();
        _log = subsystemContainer.getRobotLog();
    }

    @Override
    public void initialize()
    {
        _pickup.stow();
        _pickup.stopMotor();
        _log.log("Stowing Pickup");
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
