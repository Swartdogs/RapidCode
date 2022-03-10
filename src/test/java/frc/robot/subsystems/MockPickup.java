package frc.robot.subsystems;

import frc.robot.abstraction.MockMotor;
import frc.robot.abstraction.MockSolenoid;

public class MockPickup extends Pickup
{
    public MockPickup()
    {
        _pickupMotor    = new MockMotor();
        _deploySolenoid = new MockSolenoid();
    }

    public MockMotor getPickupMotor()
    {
        return (MockMotor)_pickupMotor;
    }

    public MockSolenoid getDeploySolenoid()
    {
        return (MockSolenoid)_deploySolenoid;
    }

    public void initialize() {}

    public boolean isInitialized()
    {
        return true;
    }
}