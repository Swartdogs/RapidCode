package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.SwartdogSubsystem;
import frc.robot.abstraction.Switch;
import frc.robot.abstraction.Enumerations.State;

public abstract class Ballpath extends SwartdogSubsystem
{
    protected Motor _lowerTrack;
    protected Motor _upperTrack;

    protected Switch _pickupSensor;
    protected Switch _shooterSensor;

    private State _lastPickupSensorState = State.Off;
    private State _lastShooterSensorState = State.Off;

    private int _cargoCount = 0;

    @Override
    public void periodic()
    {
        _lastPickupSensorState = _pickupSensor.get();
        _lastShooterSensorState = _shooterSensor.get();
        
        if (hasShooterSensorTransitionedTo(State.Off))
        {
            modifyCargoCount(-1);
        }
    }

    public void setUpperTrackTo(State state)
    {
        double speed = 0.0;

        switch (state)
        {
            case On:
                speed = Constants.Ballpath.BALLPATH_SPEED;
                break;
            
            case Reverse:
                speed = -Constants.Ballpath.BALLPATH_SPEED;
                break;

            default:
                speed = 0.0; 
        }

        _upperTrack.set(speed);
    }

    public void setLowerTrackTo(State state)
    {
        double speed = 0.0;

        switch (state)
        {
            case On:
                speed = Constants.Ballpath.BALLPATH_SPEED;
                break;
            
            case Reverse:
                speed = -Constants.Ballpath.BALLPATH_SPEED;
                break;

            default:
                speed = 0.0; 
        }
        
        _lowerTrack.set(speed);
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

    public int getCargoCount()
    {
        return _cargoCount;
    }

    public void setCargoCount(int count) 
    {
        if (count < 0)
        {
            _cargoCount = 0;
        }

        else if (count > Constants.Ballpath.MAX_CARGO_COUNT)
        {
            _cargoCount = Constants.Ballpath.MAX_CARGO_COUNT;
        }

        else
        {
            _cargoCount = count;
        }
    }

    public void modifyCargoCount(int mod)
    {
        setCargoCount(getCargoCount() + mod);
    }
}
