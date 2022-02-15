package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.*;

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
        RobotLog.getInstance().log("Reversing Pickup");
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
