package frc.robot.commands.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import frc.robot.Constants;
import frc.robot.Utils;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.commands.CmdHangerSetWinchPosition;
import frc.robot.subsystems.MockHanger;

public class CmdHangerSetWinchPositionTests
{
    private MockHanger _hanger;
    private CmdHangerSetWinchPosition _command;

    private static Stream<Arguments> testCases()
    {
        ExtendState[] states =
        { ExtendState.Extended, ExtendState.Retracted };

        double[] starts =
        { 78.14, 5, 617.478, 34, 92.7 };

        Stream<Arguments> args = Stream.of();

        for (double d : starts)
        {
            for (ExtendState e : states)
            {
                args = Stream.concat(args, Stream.of(Arguments.of(e, d)));
            }
        }

        return args;
    }

    @BeforeEach
    public void init()
    {
        _hanger = new MockHanger();
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testHangerSetWinchPosition(ExtendState state, double position)
    {
        _command = new CmdHangerSetWinchPosition(_hanger, state);
        
        _hanger.getWinchSensor().set(position);
        _command.initialize();
        _hanger.periodic();

        assertEquals(Utils.clamp(Constants.Hanger.HANGER_WINCH_POSITION_LOOKUP.apply(state) - position, -1, 1), _hanger.getWinchMotor().get(), Constants.Testing.EPSILON);
    }

    @ParameterizedTest
    @EnumSource(ExtendState.class)
    public void testHangerWinchAtPosition(ExtendState state)
    {
        _command = new CmdHangerSetWinchPosition(_hanger, state);

        _hanger.getWinchSensor().set(Constants.Hanger.HANGER_WINCH_POSITION_LOOKUP.apply(state));
        _command.initialize();
        _hanger.periodic();

        assertEquals(0.0, _hanger.getWinchMotor().get(), Constants.Testing.EPSILON);
        assertTrue(_command.isFinished());
    }
}
