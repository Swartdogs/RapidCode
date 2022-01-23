package frc.robot.subsystems.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import frc.robot.Constants;
import frc.robot.subsystems.MockHanger;

public class HangerTests 
{
    private MockHanger _hanger;

    private double clamp(double value, double min, double max) 
    {
        return Math.max(min, Math.min(max, value));
    }
    
    private static Stream<Arguments> PIDTestCases() 
    {
        return Stream.of
        (
            Arguments.of(  0.0,  50.0),
            Arguments.of(  0.0, -50.0),
            Arguments.of(-25.0,  25.0),
            Arguments.of( 25.0, -25.0),
            Arguments.of(  0.0,   0.5),
            Arguments.of(  0.0,  -0.5)
        );
    }

    @BeforeEach
    public void init() 
    {
        _hanger = new MockHanger();
    }

    @ParameterizedTest
    @MethodSource("PIDTestCases")
    public void testSetArmPosition(double start, double target) 
    {
        _hanger.getArmSensor().set(start);
        _hanger.setArmPosition(target);
        _hanger.periodic();

        assertEquals(clamp(target - start, -1.0, 1.0), _hanger.getArmMotor().get(), Constants.EPSILON);
    }

    @ParameterizedTest
    @MethodSource("PIDTestCases")
    public void testSetWinchPosition(double start, double target) 
    {
        _hanger.getWinchSensor().set(start);
        _hanger.setWinchPosition(target);
        _hanger.periodic();

        assertEquals(clamp(target - start, -1.0, 1.0), _hanger.getWinchMotor().get(), Constants.EPSILON);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 1.6, -100.2, 216, -360})
    public void testArmPosition(double position) 
    {
        _hanger.getArmSensor().set(position);

        assertEquals(position, _hanger.getArmPosition(), Constants.EPSILON);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 1.6, -100.2, 216, -360})
    public void testWinchPosition(double position) 
    {
        _hanger.getWinchSensor().set(position);

        assertEquals(position, _hanger.getWinchPosition(), Constants.EPSILON);
    }
}
