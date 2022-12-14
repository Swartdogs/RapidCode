package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.Utils;
import frc.robot.subsystems.RobotLog;
import frc.robot.subsystems.hardware.MockHanger;

public class CmdHangerSetWinchPositionTests
{
    private MockHanger _hanger;
    private SubsystemContainer _subsystemContainer;
    private CmdHangerSetWinchPosition _command;

    private static Stream<Arguments> testCases()
    {
        double[] positions =
        { 78.14, 5, 617.478, 34, 92.7 };

        Stream<Arguments> args = Stream.of();

        for (double start : positions)
        {
            for (double target : positions)
            {
                args = Stream.concat(args, Stream.of(Arguments.of(start, target)));
            }
        }

        return args;
    }

    @BeforeEach
    public void init()
    {
        _hanger = new MockHanger();
        _subsystemContainer = new SubsystemContainer(null, null, null, _hanger, null, null, null, null, new RobotLog(""));
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testHangerSetWinchPosition(double start, double target)
    {
        _command = new CmdHangerSetWinchPosition(_subsystemContainer, target, () -> Constants.Hanger.WINCH_SPEED);
        
        _hanger.getWinchSensor().set(start);
        _command.initialize();
        _hanger.periodic();
        _command.execute();

        assertEquals(Utils.clamp(target - start, -1, 1) * Constants.Hanger.WINCH_SPEED, _hanger.getWinchMotor().get(), Constants.Testing.EPSILON);
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testHangerWinchAtPosition(double start, double target)
    {
        _command = new CmdHangerSetWinchPosition(_subsystemContainer, target, () -> Constants.Hanger.WINCH_SPEED);

        _hanger.getWinchSensor().set(start);
        _command.initialize();
        _hanger.periodic();
        _command.execute();

        assertEquals(target == start, _command.isFinished());
    }
}
