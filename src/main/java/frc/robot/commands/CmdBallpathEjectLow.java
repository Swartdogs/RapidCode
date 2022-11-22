package frc.robot.commands;

import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.RobotLog;
import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;

public class CmdBallpathEjectLow extends SwartdogCommand
{
    private Ballpath _ballpath;
    private Pickup   _pickup;
    private RobotLog _log;

    public CmdBallpathEjectLow(SubsystemContainer subsystemContainer)
    {
        _ballpath = subsystemContainer.getBallpath();
        _pickup   = subsystemContainer.getPickup();
        _log      = subsystemContainer.getRobotLog();
    }

    @Override
    public void initialize()
    {
        if (_ballpath.getCargoCount() >= Constants.Ballpath.MAX_CARGO_COUNT)
        {
            _ballpath.setLowerTrackTo(State.Reverse);
            _pickup.reverseMotor();
        }
    }

    @Override
    public void execute()
    {
        if (_ballpath.hasPickupSensorTransitionedTo(State.Off))
        {
            _ballpath.modifyCargoCount(-1);
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        _ballpath.setLowerTrackTo(State.Off);
        _pickup.stopMotor();

        _log.log("Ejected Cargo (low), Current Count: " + _ballpath.getCargoCount());
    }

    @Override
    public boolean isFinished()
    {
        return _ballpath.getCargoCount() < Constants.Ballpath.MAX_CARGO_COUNT;
    }
}
