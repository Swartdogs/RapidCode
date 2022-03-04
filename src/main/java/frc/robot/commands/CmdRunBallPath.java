package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;

public class CmdRunBallPath extends SwartdogCommand
{
    private Ballpath _ballpath;

    public CmdRunBallPath(Ballpath ballpath)
    {
        _ballpath = ballpath;
    }

    @Override
    public void initialize()
    {
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
