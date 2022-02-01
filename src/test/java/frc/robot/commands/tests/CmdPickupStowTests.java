package frc.robot.commands.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.commands.CmdPickupStow;
import frc.robot.subsystems.MockPickup;

public class CmdPickupStowTests
{
    private MockPickup    _pickup;
    private CmdPickupStow _command;

    @BeforeEach
    public void init()
    {
        _pickup  = new MockPickup();
        _command = new CmdPickupStow(_pickup);
    }

    @Test
    public void testStow()
    {
        _command.initialize();

        assertEquals(ExtendState.Retracted, _pickup.getDeploySolenoid().get());
        assertEquals(0.0, _pickup.getPickupMotor().get(), Constants.Testing.EPSILON);
    }

    @Test
    public void testIsFinished()
    {
        _command.initialize();

        assertTrue(_command.isFinished());
    }
}
