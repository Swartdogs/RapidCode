
package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.RobotLog;

public class CmdBallpathLoad extends SwartdogCommand
{
    private Ballpath _ballpath;
    private int      _initialCargoCount;

    public CmdBallpathLoad(Ballpath ballpath)
    {
        _ballpath          = ballpath; 
        _initialCargoCount = 0;
    }
    
    @Override
    public void initialize()
    {
        if(_ballpath.getCargoCount() < Constants.Ballpath.MAX_CARGO_COUNT)
        {
            _ballpath.setLowerTrackTo(State.On);
        }

        if(_ballpath.getCargoCount() == 0)
        {
            _ballpath.setUpperTrackTo(State.On);
        }

        _initialCargoCount = _ballpath.getCargoCount();
        _ballpath.modifyCargoCount(1);
        RobotLog.getInstance().log("Loading Cargo, Current Count: " + _initialCargoCount);
    }

    @Override
    public void end(boolean interrupted)
    {
        _ballpath.setUpperTrackTo(State.Off);
        _ballpath.setLowerTrackTo(State.Off);
        RobotLog.getInstance().log("Finished Loading, New Count: " + _ballpath.getCargoCount());
    }

    @Override
    public boolean isFinished()
    {
        return (_initialCargoCount == 0 && _ballpath.hasShooterSensorTransitionedTo(State.On)) || 
               (_initialCargoCount == 1 && _ballpath.hasPickupSensorTransitionedTo(State.Off)) ||
               (_initialCargoCount == 2);
    }
}
