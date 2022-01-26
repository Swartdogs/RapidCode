package frc.robot.subsystems.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    public void testBallpathEnable()
    {
        _ballpath.enable();

        assertEquals(Constants.BALLPATH_SPEED, _ballpath.getLowerTrackMotor().get(), Constants.EPSILON);
        assertEquals(Constants.BALLPATH_SPEED, _ballpath.getUpperTrackMotor().get(), Constants.EPSILON);
    }

    @Test
    public void testBallpathDisable()
    {
        _ballpath.disable();
        
        assertEquals(0.0, _ballpath.getLowerTrackMotor().get(), Constants.EPSILON);
        assertEquals(0.0, _ballpath.getUpperTrackMotor().get(), Constants.EPSILON);
    }

    @Test
    public void testBallpathReverse()
    {
        _ballpath.reverse();

        assertEquals(-Constants.BALLPATH_SPEED, _ballpath.getLowerTrackMotor().get(), Constants.EPSILON);
        assertEquals(-Constants.BALLPATH_SPEED, _ballpath.getUpperTrackMotor().get(), Constants.EPSILON);
    }

    @ParameterizedTest
    @EnumSource(State.class)
    public void testGetPickupSensor(State state)
    {
        _ballpath.getPickupSensor().set(state);

        assertEquals(state, _ballpath.getPickupSensorState());
    }

    @ParameterizedTest
    @EnumSource(State.class)
    public void testGetShooterSensor(State state)
    {
        _ballpath.getShooterSensor().set(state);

        assertEquals(state, _ballpath.getShooterSensorState());
    }

    @ParameterizedTest
    @MethodSource("stateTestCases")
    public void testPickupSensorTransitionedTo(State first, State second, State third)
    {
        _ballpath.getPickupSensor().set(first);
        _ballpath.periodic();
        _ballpath.getPickupSensor().set(second);

        assertEquals(first != second && second == third, _ballpath.hasPickupSensorTransitionedTo(third));
    }

    @ParameterizedTest
    @MethodSource("stateTestCases")
    public void testShooterSensorTranstitionedTo(State first, State second, State third)
    {
        _ballpath.getShooterSensor().set(first);
        _ballpath.periodic();
        _ballpath.getShooterSensor().set(second);

        assertEquals(first != second && second == third, _ballpath.hasShooterSensorTransitionedTo(third));
    }
}
