package frc.robot.commands.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.subsystems.MockPickup;

public class CmdPickupDeployTests 
{
    private MockPickup      _pickup;
    private CmdPickupDeploy _command;

    @Before
    public void init()
    {
        _pickup  = new MockPickup();
        _command = new CmdPickupDeploy(_pickup);
    }

    @Test
    public void testDeploy()
    {
        _command.initialize();

        assertEquals(ExtendState.Extended, _pickup.getDeploySolenoid().get());
        assertEquals(Constants.PICKUP_SPEED, _pickup.getPickupMotor().get(), Constants.EPSILON);
    }
}
