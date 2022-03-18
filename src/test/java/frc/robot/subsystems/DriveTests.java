package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import frc.robot.Constants;
import frc.robot.subsystems.hardware.MockDrive;
import frc.robot.subsystems.hardware.MockSwerveModule;
import frc.robot.subsystems.drive.Vector;

public class DriveTests 
{
    private MockDrive _drive;

    private static Stream<Arguments> driveTestCases()
    {
        Vector translateVectors[] = 
        {
            new Vector(),
            new Vector(1, 1),
            new Vector(-1, -1),
            new Vector(0, 0.54)
        };

        double rotations[] = {
            0,
            1,
            -1,
            0.5,
            0.75,
            -0.67
        };

        Vector origins[] = 
        {
            new Vector(),
            new Vector(55, 10)
        };

        double moduleRotations[][] = 
        {
            { 0, 0, 0, 0},
            { 45, 45, -45, -45 },
            { 0, 90, 180, 270 },
            { -30, -60, 0, 90}
        };

        Stream<Arguments> parameters = Stream.empty();

        for (Vector translateVector : translateVectors)
        {
            for (double rotate : rotations)
            {
                for (Vector origin : origins)
                {
                    for (double[] moduleRotation : moduleRotations)
                    {
                        parameters = Stream.concat(parameters, Stream.of(Arguments.of(translateVector, rotate, origin, moduleRotation)));
                    }
                }
            }
        }

        return parameters;
    }

    private static Stream<Arguments> driveAbsoluteTestCases()
    {
        Vector translateVectors[] = 
        {
            new Vector(),
            new Vector(1, 1),
            new Vector(-1, -1),
            new Vector(0, 0.54)
        };

        double rotations[] = {
            0,
            1,
            -1,
            0.5,
            0.75,
            -0.67
        };

        Vector origins[] = 
        {
            new Vector(),
            new Vector(55, 10)
        };

        double moduleRotations[][] = 
        {
            { 0, 0, 0, 0},
            { 45, 45, -45, -45 },
            { 0, 90, 180, 270 },
            { -30, -60, 0, 90}
        };

        double initialGyroRotations[] = {
            0,
            90,
            46.5
            -18
        };

        Stream<Arguments> parameters = Stream.empty();

        for (Vector translateVector : translateVectors)
        {
            for (double rotate : rotations)
            {
                for (Vector origin : origins)
                {
                    for (double[] moduleRotation : moduleRotations)
                    {
                        for (double initialGyroRotation : initialGyroRotations)
                        {
                            parameters = Stream.concat(parameters, Stream.of(Arguments.of(translateVector, rotate, origin, moduleRotation, initialGyroRotation)));
                        }
                    }
                }
            }
        }

        return parameters;
    }

    @BeforeEach
    public void init()
    {
        _drive = new MockDrive();
    }

    @ParameterizedTest
    @MethodSource("driveTestCases")
    public void testDrive(Vector translateVector, double rotate, Vector origin, double[] initialModuleRotations)
    {
        double expectedMaxModuleDistance = 0;

        for (int i = 0; i < 4; i++)
        {
            MockSwerveModule swerveModule = _drive.getSwerveModule(i);
            swerveModule.getPositionSensor().set(initialModuleRotations[i]);
            expectedMaxModuleDistance = Math.max(expectedMaxModuleDistance, Math.sqrt(Math.pow(swerveModule.getX()-origin.getX(), 2)+ Math.pow(swerveModule.getY() - origin.getY(), 2)));
        }
        
        _drive.setOrigin(origin);
        _drive.drive(translateVector, rotate, false);

        assertEquals(origin.getX(), _drive.getOrigin().getX(), Constants.Testing.EPSILON);
        assertEquals(origin.getY(), _drive.getOrigin().getY(), Constants.Testing.EPSILON);

        double maxModuleSpeed = 1;
        for (int i = 0; i < 4; i++)
        {
            MockSwerveModule swerveModule = _drive.getSwerveModule(i);
            Vector expectedModuleCommand = new Vector(swerveModule.getX()-origin.getX(), swerveModule.getY()-origin.getY());
            expectedModuleCommand.translatePolarPosition(0, 90);
            expectedModuleCommand = expectedModuleCommand.multiply(rotate / expectedMaxModuleDistance).add(translateVector);

            maxModuleSpeed = Math.max(maxModuleSpeed, Math.abs(expectedModuleCommand.getR()));
        }

        for (int i = 0; i < 4; i++)
        {
            MockSwerveModule swerveModule = _drive.getSwerveModule(i);
            Vector expectedModuleCommand = new Vector(swerveModule.getX()-origin.getX(), swerveModule.getY()-origin.getY());
            expectedModuleCommand.translatePolarPosition(0, 90);
            expectedModuleCommand = expectedModuleCommand.multiply(rotate / expectedMaxModuleDistance).add(translateVector).divide(maxModuleSpeed);
            double expectedRotateSetpoint = expectedModuleCommand.getTheta();
            double expectedDriveSetpoint = expectedModuleCommand.getR();

            double expectedDriveSpeed = Math.cos(Math.toRadians(swerveModule.getPosition() - expectedRotateSetpoint)) * expectedDriveSetpoint;

            assertEquals(expectedRotateSetpoint, swerveModule.getRotateSetpoint(), Constants.Testing.EPSILON);
            assertEquals(expectedDriveSetpoint, swerveModule.getDriveSetpoint(), Constants.Testing.EPSILON);
            assertEquals(expectedDriveSpeed, swerveModule.getDriveMotor().get(), Constants.Testing.EPSILON);
        }
    }

    @ParameterizedTest
    @MethodSource("driveAbsoluteTestCases")
    public void testAbsoluteDrive(Vector translateVector, double rotate, Vector origin, double[] initialModuleRotations, double initialGyroRotation)
    {
        _drive.setGyro(initialGyroRotation);
        assertEquals(initialGyroRotation, _drive.getGyro().get());

        Vector expectedTranslateVector = translateVector;
        expectedTranslateVector.translatePolarPosition(0, -initialGyroRotation);

        double expectedMaxModuleDistance = 0;

        for (int i = 0; i < 4; i++)
        {
            MockSwerveModule swerveModule = _drive.getSwerveModule(i);
            swerveModule.getPositionSensor().set(initialModuleRotations[i]);
            expectedMaxModuleDistance = Math.max(expectedMaxModuleDistance, Math.sqrt(Math.pow(swerveModule.getX()-origin.getX(), 2)+ Math.pow(swerveModule.getY() - origin.getY(), 2)));
        }
        
        _drive.setOrigin(origin);
        _drive.drive(translateVector, rotate, true);

        assertEquals(origin.getX(), _drive.getOrigin().getX(), Constants.Testing.EPSILON);
        assertEquals(origin.getY(), _drive.getOrigin().getY(), Constants.Testing.EPSILON);

        double maxModuleSpeed = 1;
        for (int i = 0; i < 4; i++)
        {
            MockSwerveModule swerveModule = _drive.getSwerveModule(i);
            Vector expectedModuleCommand = new Vector(swerveModule.getX()-origin.getX(), swerveModule.getY()-origin.getY());
            expectedModuleCommand.translatePolarPosition(0, 90);
            expectedModuleCommand = expectedModuleCommand.multiply(rotate / expectedMaxModuleDistance).add(expectedTranslateVector);

            maxModuleSpeed = Math.max(maxModuleSpeed, Math.abs(expectedModuleCommand.getR()));
        }

        for (int i = 0; i < 4; i++)
        {
            MockSwerveModule swerveModule = _drive.getSwerveModule(i);
            Vector expectedModuleCommand = new Vector(swerveModule.getX()-origin.getX(), swerveModule.getY()-origin.getY());
            expectedModuleCommand.translatePolarPosition(0, 90);
            expectedModuleCommand = expectedModuleCommand.multiply(rotate / expectedMaxModuleDistance).add(expectedTranslateVector).divide(maxModuleSpeed);
            double expectedRotateSetpoint = expectedModuleCommand.getTheta();
            double expectedDriveSetpoint = expectedModuleCommand.getR();

            double expectedDriveSpeed = Math.cos(Math.toRadians(swerveModule.getPosition() - expectedRotateSetpoint)) * expectedDriveSetpoint;

            assertEquals(expectedRotateSetpoint, swerveModule.getRotateSetpoint(), Constants.Testing.EPSILON);
            assertEquals(expectedDriveSetpoint, swerveModule.getDriveSetpoint(), Constants.Testing.EPSILON);
            assertEquals(expectedDriveSpeed, swerveModule.getDriveMotor().get(), Constants.Testing.EPSILON);
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = { 0, 180, 366, -256, 525, 90.0, 726.4, -1440, 270})
    public void testAngleNormalization(double gyroAngle)
    {
        _drive.setGyro(gyroAngle);
        assertEquals(gyroAngle, _drive.getGyro().get());
        assertTrue(_drive.getHeading() <= 180);
        assertTrue(_drive.getHeading() > -180);
        double expected = (((gyroAngle % 360) + 360) % 360);
        expected = expected > 180 ? expected - 360 : expected;
        assertEquals(expected, _drive.getHeading());
    }

    @Test
    public void testOdometry()
    {
        for (int j = 0; j < 360; j += 30)
        {
            _drive.resetOdometer();

            for (int i = 0; i < 4; i++)
            {
                _drive.getSwerveModule(i).getDriveMotor().getPositionSensor().set(12);
                _drive.getSwerveModule(i).getPositionSensor().set(j);
            }
    
            _drive.periodic();    

            System.out.println(_drive.getOdometer());
        }

        return;
    }
}
