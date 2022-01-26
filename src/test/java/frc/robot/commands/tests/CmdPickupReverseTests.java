package frc.robot.commands.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.commands.CmdPickupReverse;
import frc.robot.subsystems.MockPickup;

public class CmdPickupReverseTests
{
    private MockPickup _pickup;
    private CmdPickupReverse _command;

    @BeforeEach
    public void init()
    {
        _pickup  = new MockPickup();
        _command = new CmdPickupReverse(_pickup);
    }

    @Test
    public void testReverse()
    {
        _command.initialize();

        assertEquals(ExtendState.Extended, _pickup.getDeploySolenoid().get());
        assertEquals(-Constants.PICKUP_SPEED, _pickup.getPickupMotor().get(), Constants.EPSILON);
    }

    @Test
    public void testIsFinished()
    {
        _command.initialize();

        assertTrue(_command.isFinished());
    }
}
