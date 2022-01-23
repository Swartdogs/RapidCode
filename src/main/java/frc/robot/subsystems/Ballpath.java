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
    protected Switch _shooterSensor;

    private State _lastPickupSensorState  = State.Off;
    private State _lastShooterSensorState = State.Off;

    @Override
    public void periodic()
    {
        _lastPickupSensorState  = _pickupSensor.get();
        _lastShooterSensorState = _shooterSensor.get();
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

    public State getShooterSensorState()
    {
        return _shooterSensor.get();
    }

    public boolean hasShooterSensorTransitionedTo(State state)
    {
        return _shooterSensor.get() == state && _lastShooterSensorState != state;
    }
}
