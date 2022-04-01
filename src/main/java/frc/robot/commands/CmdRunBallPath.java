package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.drive.Drive;

public class CmdRunBallPath extends SwartdogCommand
{
    private Drive    _drive;
    private Ballpath _ballpath;

    public CmdRunBallPath(Drive drive, Ballpath ballpath)
    {
        _drive    = drive;
        _ballpath = ballpath;

        addRequirements(_drive, _ballpath);
    }

    @Override
    public void initialize()
    {
        _drive.drive(0, 0, 0);
        _ballpath.setUpperTrackTo(State.On);
        _ballpath.setLowerTrackTo(State.On);
    }

    @Override
    public void end(boolean interrupted)
    {
        _ballpath.setUpperTrackTo(State.Off);
        _ballpath.setLowerTrackTo(State.Off);
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
