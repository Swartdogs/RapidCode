package frc.robot.subsystems.hardware;

import frc.robot.abstraction.Motor.MockMotor;
import frc.robot.abstraction.Solenoid.MockSolenoid;
import frc.robot.subsystems.Pickup;

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
}