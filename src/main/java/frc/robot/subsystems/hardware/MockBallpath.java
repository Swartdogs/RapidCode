package frc.robot.subsystems.hardware;

import frc.robot.abstraction.Motor.MockMotor;
import frc.robot.abstraction.Switch.MockSwitch;
import frc.robot.subsystems.Ballpath;

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
}
