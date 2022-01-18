package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Pickup;

public class CmdPickupReverse extends SwartdogCommand
{
    private Pickup _pickup;
    
    public CmdPickupReverse(Pickup pickup)
    {
        _pickup = pickup;
    }

    @Override
    public void initialize()
    {
        _pickup.deploy();
        _pickup.reverseMotor();
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
