package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.Solenoid;
import frc.robot.abstraction.SwartdogSubsystem;

public abstract class Pickup extends SwartdogSubsystem
{
    protected Motor    _pickupMotor;
    protected Solenoid _deploySolenoid;

    public void deploy()
    {
        _deploySolenoid.extend();
    }

    public void stow()
    {
        _deploySolenoid.retract();
    }

    public void startMotor()
    {
        _pickupMotor.set(Constants.PICKUP_SPEED);
    }

    public void stopMotor()
    {
        _pickupMotor.set(0);
    }

    public void reverseMotor()
    {
        _pickupMotor.set(-Constants.PICKUP_SPEED);
    }
}
