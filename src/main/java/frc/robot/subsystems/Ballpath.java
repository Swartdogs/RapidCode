package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
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

    private State _lowerTrackState           = State.Off;
    private State _upperTrackState           = State.Off;

    private double _loadSpeed = Constants.Ballpath.BALLPATH_LOAD_SPEED;
    private double _shootSpeed = Constants.Ballpath.BALLPATH_SHOOT_SPEED;

    private int _cargoCount = 0;

    @Override
    public void periodic()
    {
        _lastPickupSensorState  = _currentPickupSensorState;
        _lastShooterSensorState = _currentShooterSensorState;
      
        _currentPickupSensorState  = _pickupSensor.get();
        _currentShooterSensorState = _shooterSensor.get();
    }

    public void setUpperTrackTo(State state)
    {
        double speed = 0.0;

        _upperTrackState = state;

        switch (state)
        {
            case On:
                speed = _loadSpeed;
                break;
            
            case Reverse:
                speed = -_loadSpeed;
                break;

            default:
                speed = 0.0; 
        }

        _upperTrack.set(speed);
    }

    public void setLowerTrackTo(State state)
    {
        double speed = 0.0;

        _lowerTrackState = state;

        switch (state)
        {
            case On:
                speed = _loadSpeed;
                break;
            
            case Reverse:
                speed = -_loadSpeed;
                break;

            default:
                speed = 0.0; 
        }
        
        _lowerTrack.set(speed);
    }

    public void shoot(RobotPosition robotPosition, TargetPosition targetPosition)
    {
        if (robotPosition == RobotPosition.Fender && targetPosition == TargetPosition.UpperHub)
        {
            _lowerTrack.set(_shootSpeed);
            _upperTrack.set(_shootSpeed);
        }
        else
        { 
            _lowerTrack.set(_loadSpeed);
            _upperTrack.set(_loadSpeed);
        }
    }

    public void shoot()
    {
        _lowerTrack.set(_shootSpeed);
        _upperTrack.set(_shootSpeed);
    }

    public void stop()
    {
        _lowerTrack.set(0.0);
        _upperTrack.set(0.0);
    }

    public State getUpperTrackState()
    {
        return _upperTrackState;
    }

    public State getLowerTrackState()
    {
        return _lowerTrackState;
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

    public void setLoadSpeed(double speed)
    {
        _loadSpeed = speed;
    }

    public void setShootSpeed(double speed)
    {
        _shootSpeed = speed;
    }
}
