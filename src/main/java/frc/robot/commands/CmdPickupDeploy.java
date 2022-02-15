package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.RobotLog;

public class CmdPickupDeploy extends SwartdogCommand
{
    private Pickup _pickup;

    public CmdPickupDeploy(Pickup pickup)
    {
        _pickup = pickup;
    }

    @Override
    public void initialize()
    {
        _pickup.deploy();
        _pickup.startMotor();
        RobotLog.getInstance().log("Deploying Pickup");
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
