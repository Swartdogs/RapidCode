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

    private State _lastPickupSensorState     = State.Off;
    private State _lastShooterSensorState    = State.Off;
    private State _currentPickupSensorState  = State.Off;
    private State _currentShooterSensorState = State.Off;

    private int _cargoCount = 0;

    @Override
    public void periodic()
    {
        _lastPickupSensorState  = _currentPickupSensorState;
        _lastShooterSensorState = _currentShooterSensorState;
      
        _currentPickupSensorState  = _pickupSensor.get();
        _currentShooterSensorState = _shooterSensor.get();
        
        //System.out.println(String.format("Pickup: %s, Shooter: %s, Cargo Count: %d", _lastPickupSensorState.toString(), _lastShooterSensorState.toString(), _cargoCount));
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
        return _currentPickupSensorState;
    }

    public boolean hasPickupSensorTransitionedTo(State state)
    {
        return _currentPickupSensorState == state && _lastPickupSensorState != state;
    }

    public State getShooterSensorState()
    {
        return _currentShooterSensorState;
    }

    public boolean hasShooterSensorTransitionedTo(State state)
    {
        return _currentShooterSensorState == state && _lastShooterSensorState != state;
    }

    public int getCargoCount()
    {
        return _cargoCount;
    }

    public void setCargoCount(int count) 
    {
        System.out.println("Current count: " + _cargoCount + " Setting to: " + count);
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
