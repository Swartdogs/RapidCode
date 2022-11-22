package frc.robot.commands;

import frc.robot.subsystems.Ballpath;
import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.*;

public class CmdPickupDeploy extends SwartdogCommand
{
    private Pickup _pickup;
    private Ballpath _ballpath;
    private RobotLog _log;

    public CmdPickupDeploy(SubsystemContainer subsystemContainer)
    {
        _pickup   = subsystemContainer.getPickup();
        _ballpath = subsystemContainer.getBallpath();
        _log      = subsystemContainer.getRobotLog();
    }

    @Override
    public void initialize()
    {
        if (_ballpath.getCargoCount() < Constants.Ballpath.MAX_CARGO_COUNT)
        {
            _pickup.deploy();
            _pickup.startMotor();
            _log.log("Deploying Pickup");
        }
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
