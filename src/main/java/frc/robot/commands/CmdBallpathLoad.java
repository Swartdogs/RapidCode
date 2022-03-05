
package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Pickup;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;

public class CmdBallpathLoad extends SwartdogCommand
{
    private Ballpath _ballpath;
    private Pickup   _pickup;
    private int      _initialCargoCount;

    public CmdBallpathLoad(Ballpath ballpath, Pickup pickup)
    {
        _ballpath          = ballpath; 
        _pickup            = pickup;
        _initialCargoCount = 0;

        addRequirements(_ballpath);
    }

    @Override
    public void initialize()
    {
        _initialCargoCount = _ballpath.getCargoCount();
        
        if(_initialCargoCount == 0)
        {
            _ballpath.setLowerTrackTo(State.On);
            _ballpath.setUpperTrackTo(State.On);
        }

        
        _ballpath.modifyCargoCount(1);
    }

    @Override
    public void end(boolean interrupted)
    {
        _ballpath.setUpperTrackTo(State.Off);
        _ballpath.setLowerTrackTo(State.Off);
        if (_ballpath.getCargoCount() >= Constants.Ballpath.MAX_CARGO_COUNT && _pickup.getState() == State.On)
        {
            _pickup.stow();
            _pickup.stopMotor();
        }
    }

    @Override
    public boolean isFinished()
    {
        return (_ballpath.hasShooterSensorTransitionedTo(State.On)) || 
               (_initialCargoCount > 0);
    }
}
