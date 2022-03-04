package frc.robot.subsystems.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.MockBallpath;

public class BallpathTests 
{
    private MockBallpath _ballpath;

    private static Stream<Arguments> stateTestCases()
    {
        State[] states = 
        {
            State.On, State.Off, State.Reverse
        };

        Stream<Arguments> stream = Stream.of();

        for (State first : states) {
            for (State second : states) {
                for (State third : states) {
                    stream = Stream.concat(Stream.of(Arguments.of(first, second, third)), stream);
                }
            }
        }

        return stream;
    }

    @BeforeEach
    public void init()
    {
        _ballpath = new MockBallpath();
    }

    @ParameterizedTest
    @EnumSource(State.class)
    public void testSetUpperTrack(State state)
    {
        double speed = 0.0;

        _ballpath.setUpperTrackTo(state);

        switch (state)
        {
            case On:
                speed = Constants.Ballpath.BALLPATH_SPEED;
                break;
            
            case Reverse:
                speed = -Constants.Ballpath.BALLPATH_SPEED;
                break;

            default:
                speed = 0.0;            
        }

        assertEquals(speed, _ballpath.getUpperTrackMotor().get(), Constants.Testing.EPSILON);
    }

    @ParameterizedTest
    @EnumSource(State.class)
    public void testSetLowerTrack(State state)
    {
        double speed = 0.0;

        _ballpath.setLowerTrackTo(state);

        switch (state)
        {
            case On:
                speed = Constants.Ballpath.BALLPATH_SPEED;
                break;
            
            case Reverse:
                speed = -Constants.Ballpath.BALLPATH_SPEED;
                break;

            default:
                speed = 0.0;            
        }

        assertEquals(speed, _ballpath.getLowerTrackMotor().get(), Constants.Testing.EPSILON);
    }

    @ParameterizedTest
    @EnumSource(State.class)
    public void testGetPickupSensor(State state)
    {
        _ballpath.getPickupSensor().set(state);

        _ballpath.periodic();

        assertEquals(state, _ballpath.getPickupSensorState());
    }

    @ParameterizedTest
    @EnumSource(State.class)
    public void testGetShooterSensor(State state)
    {
        _ballpath.getShooterSensor().set(state);

        _ballpath.periodic();

        assertEquals(state, _ballpath.getShooterSensorState());
    }

    @ParameterizedTest
    @MethodSource("stateTestCases")
    public void testPickupSensorTransitionedTo(State first, State second, State third)
    {
        _ballpath.getPickupSensor().set(first);
        _ballpath.periodic();
        _ballpath.getPickupSensor().set(second);
        _ballpath.periodic();

        assertEquals(first != second && second == third, _ballpath.hasPickupSensorTransitionedTo(third));
    }

    @ParameterizedTest
    @MethodSource("stateTestCases")
    public void testShooterSensorTranstitionedTo(State first, State second, State third)
    {
        _ballpath.getShooterSensor().set(first);
        _ballpath.periodic();
        _ballpath.getShooterSensor().set(second);
        _ballpath.periodic();

        assertEquals(first != second && second == third, _ballpath.hasShooterSensorTransitionedTo(third));
    }
}
