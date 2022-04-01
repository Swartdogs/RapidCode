package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.subsystems.hardware.MockPickup;

public class PickupTests
{
    private MockPickup _pickup;

    @BeforeEach
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

        assertEquals(Constants.Pickup.PICKUP_SPEED, _pickup.getPickupMotor().get(), Constants.Testing.EPSILON);
    }

    @Test
    public void testStopMotor()
    {
        _pickup.stopMotor();

        assertEquals(0.0, _pickup.getPickupMotor().get(), Constants.Testing.EPSILON);
    }

    @Test
    public void testReverseMotor()
    {
        _pickup.reverseMotor();

        assertEquals(-Constants.Pickup.PICKUP_SPEED, _pickup.getPickupMotor().get(), Constants.Testing.EPSILON);
    }
}
