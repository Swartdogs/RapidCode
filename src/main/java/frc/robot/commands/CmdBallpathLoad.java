
package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.subsystems.Pickup;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Dashboard;
import frc.robot.subsystems.RobotLog;

public class CmdBallpathLoad extends SwartdogCommand
{
    private Ballpath  _ballpath;
    private Pickup    _pickup;
    private Dashboard _dashboard;
    private RobotLog  _log;
    private int       _initialCargoCount;
    private int       _timer;

    public CmdBallpathLoad(SubsystemContainer subsystemContainer)
    {
        _ballpath          = subsystemContainer.getBallpath(); 
        _pickup            = subsystemContainer.getPickup();
        _dashboard         = subsystemContainer.getDashboard();
        _log               = subsystemContainer.getRobotLog();
        _initialCargoCount = 0;

        addRequirements(_ballpath);
    }
    
    @Override
    public void initialize()
    {
        _initialCargoCount = _ballpath.getCargoCount();
        _timer             = (int)(Constants.LOOPS_PER_SECOND * _dashboard.getBallpathLoadTimeout());
        
        if(_initialCargoCount == 0)
        {
            _ballpath.setLowerTrackTo(State.On);
            _ballpath.setUpperTrackTo(State.On);
        }
        
        _ballpath.modifyCargoCount(1);
        _log.log("Loading Cargo, Current Count: " + _initialCargoCount);
    }

    @Override
    public void execute()
    {
        _timer--;
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

        if (_timer < 0)
        {
            _ballpath.modifyCargoCount(-1);
        }
        
        _log.log(String.format("Finished Loading, Ball Count: %d, Timed Out: %b, Shooter Sensor: %s, Pickup Sensor %s", _ballpath.getCargoCount(), _timer < 0, _ballpath.getShooterSensorState().toString(), _ballpath.getPickupSensorState().toString()));
    }

    @Override
    public boolean isFinished()
    {
        return (_ballpath.hasShooterSensorTransitionedTo(State.On)) || 
               (_initialCargoCount > 0) ||
               (_timer < 0);
    }
}
