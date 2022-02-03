package frc.robot.subsystems;

import PIDControl.PIDControl;
import frc.robot.abstraction.MockNetworkTableBoolean;
import frc.robot.abstraction.MockNetworkTableDouble;
import frc.robot.abstraction.MockSolenoid;

public class MockVision extends Vision
{
    public MockVision()
    {
        _xPosition   = new MockNetworkTableDouble();
        _yPosition   = new MockNetworkTableDouble();
        _targetFound = new MockNetworkTableBoolean();
        _lightRing   = new MockSolenoid();
        _rotatePID   = new PIDControl();
    }

    public MockNetworkTableDouble getXPosition()
    {
        return (MockNetworkTableDouble)_xPosition;
    }

    public MockNetworkTableDouble getYPosition()
    {
        return (MockNetworkTableDouble)_yPosition;
    }

    public MockNetworkTableBoolean getTargetFound()
    {
        return (MockNetworkTableBoolean)_targetFound;
    }

    public MockSolenoid getLightRing()
    {
        return (MockSolenoid)_lightRing;
    }
}
