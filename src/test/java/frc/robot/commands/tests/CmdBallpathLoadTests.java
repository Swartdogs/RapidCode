package frc.robot.commands.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.commands.CmdBallpathLoad;
import frc.robot.subsystems.MockBallpath;
import frc.robot.subsystems.MockPickup;

public class CmdBallpathLoadTests 
{
    private MockBallpath    _ballpath;
    private MockPickup      _pickup;
    private CmdBallpathLoad _command;

    @BeforeEach
    public void init()
    {
        _ballpath = new MockBallpath();
        _pickup   = new MockPickup();
        _command  = new CmdBallpathLoad(_ballpath, _pickup);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testBegin(int initialCargoCount)
    {
        _ballpath.setCargoCount(initialCargoCount);

        // Trigger pickup light sensor
        _ballpath.getPickupSensor().set(State.On);
        _ballpath.periodic();

        // Start the command
        _command.initialize();

        double expectedLowerTrackSpeed = 0;
        double expectedUpperTrackSpeed = 0;
        int    expectedCargoCount      = 0;

        // Find expected track speeds
        switch(initialCargoCount)
        {
            case 0:
                expectedLowerTrackSpeed = Constants.Ballpath.BALLPATH_SPEED;
                expectedUpperTrackSpeed = Constants.Ballpath.BALLPATH_SPEED;
                expectedCargoCount      = 1;
                break;

            case 1:
                expectedLowerTrackSpeed = 0;
                expectedUpperTrackSpeed = 0;
                expectedCargoCount      = 2;
                break;

            case 2:
                expectedLowerTrackSpeed = 0;
                expectedUpperTrackSpeed = 0;
                expectedCargoCount      = 2;
                break;
        }

        assertEquals(expectedLowerTrackSpeed, _ballpath.getLowerTrackMotor().get(), Constants.Testing.EPSILON);
        assertEquals(expectedUpperTrackSpeed, _ballpath.getUpperTrackMotor().get(), Constants.Testing.EPSILON);
        assertEquals(expectedCargoCount,      _ballpath.getCargoCount());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testEnd(int initialCargoCount)
    {
        _ballpath.setCargoCount(initialCargoCount);

        // Trigger pickup light sensor
        _ballpath.getPickupSensor().set(State.On);
        _ballpath.periodic();

        // Start the command
        _command.initialize();

        // End command based on cargo count
        switch(initialCargoCount)
        {
            case 0:
                _ballpath.getShooterSensor().set(State.On);
                break;
            
            case 1:
                _ballpath.getPickupSensor().set(State.Off);
                break;
            
            default:
                break;
        }

        _ballpath.periodic();

        if(_command.isFinished())
        {
            _command.end(false);
        }

        assertEquals(0, _ballpath.getLowerTrackMotor().get(), Constants.Testing.EPSILON);
        assertEquals(0, _ballpath.getUpperTrackMotor().get(), Constants.Testing.EPSILON);
    }
}
