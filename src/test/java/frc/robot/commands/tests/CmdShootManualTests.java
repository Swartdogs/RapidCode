package frc.robot.commands.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import frc.robot.Constants;
import frc.robot.abstraction.MockVelocitySensor;
import frc.robot.commands.CmdShootManual;
import frc.robot.subsystems.MockBallpath;
import frc.robot.subsystems.MockShooter;

public class CmdShootManualTests {
    private MockShooter    _shooter;
    private MockBallpath   _ballpath;
    private CmdShootManual _command;
    
    public static Stream<Arguments> testCases()
    {
        int[] cargoCounts = { 0, 1, 2 };
        double[] shooterRPMs = { 0, Constants.Shooter.MANUAL_SHOOTER_RPM };

        Stream<Arguments> stream = Stream.of();

        for (int cargoCount : cargoCounts)
        {
            for (double rpm : shooterRPMs)
            {
                stream = Stream.concat(stream, Stream.of(Arguments.of(cargoCount, rpm)));
            }
        }

        return stream;
    }

    @BeforeEach
    public void init()
    {
        _shooter  = new MockShooter();
        _ballpath = new MockBallpath();
        _command  = new CmdShootManual(_shooter, _ballpath);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    public void testStart(int initialCargoCount)
    {
        _ballpath.setCargoCount(initialCargoCount);

        _command.initialize();

        assertEquals(initialCargoCount > 0 ? Constants.Shooter.MANUAL_SHOOTER_RPM : 0, _shooter.getShooterMotor().get(), Constants.Testing.EPSILON);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    public void testIsFinished(int cargoCount)
    {
        _ballpath.setCargoCount(cargoCount);

        _command.initialize();
        
        assertEquals(cargoCount <= 0, _command.isFinished());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    public void testEnd(int cargoCount)
    {
        _ballpath.setCargoCount(cargoCount);

        _command.initialize();
        _command.end(false);

        assertEquals(0, _shooter.getShooterMotor().get(),     Constants.Testing.EPSILON);
        assertEquals(0, _ballpath.getUpperTrackMotor().get(), Constants.Testing.EPSILON);
        assertEquals(0, _ballpath.getLowerTrackMotor().get(), Constants.Testing.EPSILON);
    }

    // @ParameterizedTest
    // @MethodSource("testCases")
    // public void testExecute(int initialCargoCount, double shooterRPM)
    // {
    //     _ballpath.setCargoCount(initialCargoCount);

    //     _command.initialize();

    //     ((MockVelocitySensor)_shooter.getShooterMotor().getVelocitySensor()).set(shooterRPM);

    //     _command.execute();

    //     if (shooterRPM > 0 && initialCargoCount > 0)
    //     {
    //         assertEquals(Constants.Ballpath.BALLPATH_SPEED, _ballpath.getUpperTrackMotor().get(), Constants.Testing.EPSILON);
    //         assertEquals(Constants.Ballpath.BALLPATH_SPEED, _ballpath.getLowerTrackMotor().get(), Constants.Testing.EPSILON);
    //     }

    //     else
    //     {
    //         assertEquals(0, _ballpath.getUpperTrackMotor().get(), Constants.Testing.EPSILON);
    //         assertEquals(0, _ballpath.getLowerTrackMotor().get(), Constants.Testing.EPSILON);
    //     }
    // }
}
