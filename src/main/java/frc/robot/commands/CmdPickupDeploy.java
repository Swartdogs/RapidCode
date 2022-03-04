package frc.robot.commands;

import frc.robot.subsystems.Ballpath;
import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Pickup;

public class CmdPickupDeploy extends SwartdogCommand
{
    private Pickup _pickup;
    private Ballpath _ballpath;

    public CmdPickupDeploy(Pickup pickup, Ballpath ballpath)
    {
        _pickup = pickup;
        _ballpath = ballpath;
    }

    @Override
    public void initialize()
    {
        if (_ballpath.getCargoCount() < Constants.Ballpath.MAX_CARGO_COUNT)
        {
            _pickup.deploy();
            _pickup.startMotor();
        }
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
