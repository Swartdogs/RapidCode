package frc.robot.subsystems.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.subsystems.MockPickup;

public class PickupTests
{
    private MockPickup _pickup;

    @Before
    public void init()
    {
        _pickup = new MockPickup();
    }

    @Test
    public void testDeployPickup() 
    {
        _pickup.deploy();

        assertEquals(ExtendState.Extended, _pickup.getDeploySolenoid().get());
    }

    @Test
    public void testRetractPickup()
    {
        _pickup.stow();

        assertEquals(ExtendState.Retracted, _pickup.getDeploySolenoid().get());
    }

    @Test
    public void testStartMotor()
    {
        _pickup.startMotor();

        assertEquals(Constants.PICKUP_SPEED, _pickup.getPickupMotor().get(), Constants.EPSILON);
    }

    @Test
    public void testStopMotor()
    {
        _pickup.stopMotor();

        assertEquals(0.0, _pickup.getPickupMotor().get(), Constants.EPSILON);
    }

    @Test
    public void testReverseMotor()
    {
        _pickup.reverseMotor();

        assertEquals(-Constants.PICKUP_SPEED, _pickup.getPickupMotor().get(), Constants.EPSILON);
    }
}
