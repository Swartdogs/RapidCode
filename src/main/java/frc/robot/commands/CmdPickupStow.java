package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.*;

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
        RobotLog.getInstance().log("Reversing Pickup");
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
