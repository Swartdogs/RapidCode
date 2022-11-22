package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.subsystems.RobotLog;
import frc.robot.subsystems.hardware.MockDrive;

public class CmdDriveWithJoystickTests
{
    private MockDrive            _drive;
    private CmdDriveWithJoystick _command;

    public static Stream<Arguments> testCases()
    {
        return Stream.of(
            Arguments.of( 1.0,  0.0,  0.0,                          1.0,   0.0,   0.0,   0.0,   0.0),
            Arguments.of(-1.0,  0.0,  0.0,                          1.0, 180.0, 180.0, 180.0, 180.0),
            Arguments.of( 0.0,  1.0,  0.0,                          0.0,  90.0,  90.0,  90.0,  90.0),
            Arguments.of( 0.0, -1.0,  0.0,                          0.0, 270.0, 270.0, 270.0, 270.0),
            Arguments.of( 0.0,  0.0,  1.0, Math.cos(Math.toRadians(45)),  45.0, 135.0, 315.0, 225.0),
            Arguments.of( 0.0,  0.0, -1.0, Math.cos(Math.toRadians(45)), 225.0, 315.0, 135.0,  45.0)
        );
    }

    @BeforeEach
    public void init()
    {
        _drive   = new MockDrive();
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testDriveWithJoy(double drive, double strafe, double rotate, double speed, double flModule, double frModule, double blModule, double brModule)
    {
        _command = new CmdDriveWithJoystick(new SubsystemContainer(_drive, null, null, null, null, null, null, null, new RobotLog("")), () -> drive, () -> strafe, () -> rotate, () -> true);
        _command.execute();

        assertEquals(speed, Math.abs(_drive.getSwerveModule(0).getDriveMotor().get()), Constants.Testing.EPSILON);
        assertEquals(speed, Math.abs(_drive.getSwerveModule(1).getDriveMotor().get()), Constants.Testing.EPSILON);
        assertEquals(speed, Math.abs(_drive.getSwerveModule(2).getDriveMotor().get()), Constants.Testing.EPSILON);
        assertEquals(speed, Math.abs(_drive.getSwerveModule(3).getDriveMotor().get()), Constants.Testing.EPSILON);

        assertEquals(frModule, _drive.getSwerveModule(0).getRotateSetpoint(), Constants.Testing.EPSILON);
        assertEquals(flModule, _drive.getSwerveModule(1).getRotateSetpoint(), Constants.Testing.EPSILON);
        assertEquals(brModule, _drive.getSwerveModule(2).getRotateSetpoint(), Constants.Testing.EPSILON);
        assertEquals(blModule, _drive.getSwerveModule(3).getRotateSetpoint(), Constants.Testing.EPSILON);
    }
}
