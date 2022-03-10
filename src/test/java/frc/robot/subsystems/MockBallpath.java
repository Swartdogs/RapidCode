package frc.robot.subsystems;

import frc.robot.abstraction.MockMotor;
import frc.robot.abstraction.MockSwitch;

public class MockBallpath extends Ballpath
{
    public MockBallpath()
    {
        _lowerTrack    = new MockMotor();
        _upperTrack    = new MockMotor();

        _pickupSensor  = new MockSwitch();
        _shooterSensor = new MockSwitch();
    }

    public MockMotor getLowerTrackMotor()
    {
        return (MockMotor)_lowerTrack;
    }

    public MockMotor getUpperTrackMotor()
    {
        return (MockMotor)_upperTrack;
    }

    public MockSwitch getPickupSensor()
    {
        return (MockSwitch)_pickupSensor;
    }

    public MockSwitch getShooterSensor()
    {
        return (MockSwitch)_shooterSensor;
    }

    public void initialize() {}

    public boolean isInitialized()
    {
        return true;
    }
}
