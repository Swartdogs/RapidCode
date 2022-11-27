package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import frc.robot.Constants;
import frc.robot.subsystems.hardware.MockSwerveModule;
import frc.robot.drive.Vector;

public class SwerveModuleTests 
{
    private MockSwerveModule _swerveModule;

    private static Stream<Arguments> driveTestCases()
    {
        double initialRotations[] = 
        {
            0.0, 
            1.0,
            -0.0,
            -1.0,
            180.0,
            -180.0,
            90.0,
            -90.0,
            360.0,
            -360.0,
            270.0,
            -270.0,
            2.45,
            -3.78,
            525.0,
            -771.5
        };

        Vector moduleCommands[] = 
        {
            new Vector(0, 0),
            new Vector(-0, -0),
            new Vector(0, 1),
            new Vector(0, -1),
            new Vector(1, 0),
            new Vector(-1, 0),
            new Vector(1, 1),
            new Vector(-1, -1),
            new Vector(-1, 1),
            new Vector(1, -1),
            new Vector(0.5, -0.1)
        };

        Stream<Arguments> parameters = Stream.empty();

        for (int i = 0; i < initialRotations.length; i++)
        {
            for (int j = 0; j < moduleCommands.length; j++)
            {
                parameters = Stream.concat(parameters, Stream.of(Arguments.of(initialRotations[i], moduleCommands[j])));
            }
        }

        return parameters;
    }

    private static Stream<Arguments> odometryTestCases()
    {
        double initialRotations[] = 
        {
            0.0, 
            1.0,
            -0.0,
            -1.0,
            180.0,
            -180.0,
            90.0,
            -90.0,
            360.0,
            -360.0,
            270.0,
            -270.0,
            2.45,
            -3.78,
            525.0,
            -771.5
        };

        double positions[] = 
        {
            0.0,
            -100,
            1000,
            55.5,
            121,
            15.4,
            525,
            -6.7,
            -88
        };

        Stream<Arguments> parameters = Stream.empty();

        for (double initialRotation : initialRotations)
        {
            for (double initialPosition : positions) {
                for (double finalPosition : positions) 
                {
                    parameters = Stream.concat(parameters, Stream.of(Arguments.of(initialRotation, initialPosition, finalPosition)));
                }
            }
        }

        return parameters;
    }

    @BeforeEach
    public void init()
    {
        _swerveModule = new MockSwerveModule(10, 10, 0, 1);
    }

    @ParameterizedTest
    @MethodSource("driveTestCases")
    public void testDrive(double initialRotation, Vector moduleCommand)
    {
        moduleCommand.setR(Math.min(1, Math.max(-1, moduleCommand.getR())));

        _swerveModule.getPositionSensor().set(initialRotation);
        _swerveModule.drive(moduleCommand);

        double angleDifferenceRadians = Math.toRadians(initialRotation - moduleCommand.getTheta());
        
        double expectedDriveSpeed = Math.cos(angleDifferenceRadians) * moduleCommand.getR();
        double expectedRotateSpeed = Math.sin(angleDifferenceRadians) * Math.cos(angleDifferenceRadians) / -Math.abs(Math.cos(angleDifferenceRadians));

        assertEquals(expectedDriveSpeed, _swerveModule.getDriveMotor().get(), Constants.Testing.EPSILON);
        assertEquals(expectedRotateSpeed, _swerveModule.getRotateMotor().get(), Constants.Testing.EPSILON);
        assertFalse(Double.isNaN(_swerveModule.getDriveMotor().get()));
        assertFalse(Double.isNaN(_swerveModule.getRotateMotor().get()));
        assertEquals(moduleCommand.getR(), _swerveModule.getDriveSetpoint());
        assertEquals(moduleCommand.getTheta(), _swerveModule.getRotateSetpoint());
    }

    @ParameterizedTest
    @MethodSource("odometryTestCases")
    public void testOdometry(double initialRotation, double initialDrivePosition, double finalDrivePosition)
    {
        _swerveModule.getPositionSensor().set(initialRotation);
        _swerveModule.getDriveMotor().getPositionSensor().set(initialDrivePosition);
        _swerveModule.resetDrivePosition();
        _swerveModule.getDriveMotor().getPositionSensor().set(finalDrivePosition);

        Vector actualOffset = _swerveModule.getOffset();

        Vector expectedOffset = new Vector();
        expectedOffset.setR(finalDrivePosition - initialDrivePosition);
        expectedOffset.setTheta(initialRotation);

        assertEquals(expectedOffset.getX(), actualOffset.getX(), Constants.Testing.EPSILON);
        assertEquals(expectedOffset.getY(), actualOffset.getY(), Constants.Testing.EPSILON);
    }
}
