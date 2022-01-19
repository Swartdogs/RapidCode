package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.SwartdogSubsystem;
import frc.robot.abstraction.Switch;
import frc.robot.abstraction.Enumerations.State;

public abstract class Ballpath extends SwartdogSubsystem
{
    //Main Section of Ballpath
    protected Motor _lowerTrack;
    //Feeds up to Shooter
    protected Motor _upperTrack;

    protected Switch _pickupSensor;
    protected Switch _upperTrackSensor;

    private State _lastPickupSensorState     = State.Off;
    private State _lastUpperTrackSensorState = State.Off;

    @Override
    public void periodic()
    {
        _lastPickupSensorState     = _pickupSensor.get();
        _lastUpperTrackSensorState = _upperTrackSensor.get();
    }

    public void enable()
    {
        _lowerTrack.set(Constants.BALLPATH_SPEED);
        _upperTrack.set(Constants.BALLPATH_SPEED);
    }

    public void disable()
    {
        _lowerTrack.set(0.0);
        _upperTrack.set(0.0);
    }

    public void reverse()
    {
        _lowerTrack.set(-Constants.BALLPATH_SPEED);
        _upperTrack.set(-Constants.BALLPATH_SPEED);
    }

    public State getPickupSensorState()
    {
        return _pickupSensor.get();
    }

    public boolean hasPickupSensorTransitionedTo(State state)
    {
        return _pickupSensor.get() == state && _lastPickupSensorState != state;
    }

    public State getUpperTrackSensorState()
    {
        return _upperTrackSensor.get();
    }

    public boolean hasUpperTrackSensorTransitionedTo(State state)
    {
        return _upperTrackSensor.get() == state && _lastUpperTrackSensorState != state;
    }
}
