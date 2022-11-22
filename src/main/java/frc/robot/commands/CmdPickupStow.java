package frc.robot.commands;

import frc.robot.SubsystemContainer;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.*;

public class CmdPickupStow extends SwartdogCommand
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
