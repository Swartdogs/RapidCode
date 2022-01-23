package frc.robot.subsystems.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import frc.robot.Constants;
import frc.robot.subsystems.MockShooter;

import static frc.robot.Utils.clamp;

public class ShooterTests
{
    private MockShooter _shooter;

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
        _shooter = new MockShooter();
    }

    @ParameterizedTest
    @ValueSource (doubles = {0, -17, 525, 1431, -3577, 5000})
    public void testSetShooterMotorSpeed(double speed)
    {
        _shooter.setShooterMotorSpeed(speed);
        
        assertEquals(speed, _shooter.getShooterMotor().get(), Constants.EPSILON);
    }

    @ParameterizedTest
    @MethodSource("PIDTestCases")
    public void testSetHoodPosition(double start, double target)
    {
        _shooter.getHoodSensor().set(start);
        _shooter.setHoodPosition(target);
        _shooter.periodic();

        assertEquals(clamp(target - start, -1.0, 1.0), _shooter.getHoodMotor().get(), Constants.EPSILON);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -17, 525, 1431, -3577, 5000})
    public void testGetHoodPosition(double position)
    {
        _shooter.getHoodSensor().set(position);
        
        assertEquals(position, _shooter.getHoodPosition());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -17, 525, 1431, -3577, 5000})
    public void testHoodAtPosition(double position)
    {
        _shooter.getHoodSensor().set(position);
        _shooter.setHoodPosition(position);
        _shooter.periodic();

        assertEquals(0.0, _shooter.getHoodMotor().get(), Constants.EPSILON);
        assertTrue(_shooter.hoodAtPosition());
    }
}
