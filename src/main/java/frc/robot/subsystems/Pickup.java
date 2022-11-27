package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.Solenoid;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.abstraction.Enumerations.State;

public abstract class Pickup extends SubsystemBase
{
    protected Motor     _pickupMotor;
    protected Solenoid  _deploySolenoid;

    private double      _pickupSpeed = Constants.Pickup.PICKUP_SPEED;

    private State       _motorState = State.Off;

    public void deploy()
    {
        _deploySolenoid.extend();
    }

    public void stow()
    {
        _deploySolenoid.retract();
    }

    public ExtendState getDeployState()
    {
        return _deploySolenoid.get();
    }

    public void startMotor()
    {
        _pickupMotor.set(_pickupSpeed);
        _motorState = State.On;
    }

    public void stopMotor()
    {
        _pickupMotor.set(0);
        _motorState = State.Off;
    }

    public void reverseMotor()
    {
        _pickupMotor.set(-_pickupSpeed);
        _motorState = State.Reverse;
    }

    public State getMotorState()
    {
        return _motorState;
    }

    public State getState()
    {
        State state = State.Off;

        if (_deploySolenoid.get() == ExtendState.Extended && _pickupMotor.get() > 0)
        {
            state = State.On;
        }

        if (_deploySolenoid.get() == ExtendState.Extended && _pickupMotor.get() < 0)
        {
            state = State.Reverse;
        }

        return state;
    }

    public void setPickupSpeed(double speed)
    {
        _pickupSpeed = speed;
    }
}
