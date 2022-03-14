package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.subsystems.hardware.MockBallpath;
import frc.robot.subsystems.hardware.MockPickup;

public class CmdPickupDeployTests 
{
    private MockPickup      _pickup;
    private MockBallpath    _ballpath;
    private CmdPickupDeploy _command;

    @BeforeEach
    public void init()
    {
        _pickup   = new MockPickup();
        _ballpath = new MockBallpath();
        _command  = new CmdPickupDeploy(_pickup, _ballpath);
    }

    @Test
    public void testDeploy()
    {
        _command.initialize();

        assertEquals(ExtendState.Extended, _pickup.getDeploySolenoid().get());
        assertEquals(Constants.Pickup.PICKUP_SPEED, _pickup.getPickupMotor().get(), Constants.Testing.EPSILON);
    }

    @Test
    public void testIsFinished()
    {
        _command.initialize();

        assertTrue(_command.isFinished());
    }
}
