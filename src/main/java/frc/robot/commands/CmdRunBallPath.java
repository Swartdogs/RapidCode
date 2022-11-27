package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.SubsystemContainer;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.drive.Drive;

public class CmdRunBallPath extends CommandBase
{
    private Drive          _drive;
    private Ballpath       _ballpath;
    private Pickup         _pickup;
    private SettableSwitch _compressor;

    public CmdRunBallPath(SubsystemContainer subsystemContainer)
    {
        _drive      = subsystemContainer.getDrive();
        _ballpath   = subsystemContainer.getBallpath();
        _pickup     = subsystemContainer.getPickup();
        _compressor = subsystemContainer.getCompressor();

        addRequirements(_drive, _ballpath);
    }

    @Override
    public void initialize()
    {
        _drive.drive(0, 0, 0);
        _ballpath.setUpperTrackTo(State.On);
        _ballpath.setLowerTrackTo(State.On);
        _pickup.startMotor();
        _compressor.set(State.Off);
    }

    @Override
    public void execute()
    {
        if(_ballpath.hasShooterSensorTransitionedTo(State.Off))
        {
            _ballpath.modifyCargoCount(-1);
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        _ballpath.setUpperTrackTo(State.Off);
        _ballpath.setLowerTrackTo(State.Off);
        _pickup.stopMotor();
        _compressor.set(State.On);
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
