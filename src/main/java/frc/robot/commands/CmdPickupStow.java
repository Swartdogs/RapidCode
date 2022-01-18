package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Pickup;

public class CmdPickupStow extends SwartdogCommand
{
    private Pickup _pickup;

    public CmdPickupStow(Pickup pickup)
    {
        _pickup = pickup;
    }

    @Override
    public void initialize()
    {
        _pickup.stow();
        _pickup.stopMotor();
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
