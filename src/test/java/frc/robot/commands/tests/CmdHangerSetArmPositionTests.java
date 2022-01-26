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
import frc.robot.commands.CmdHangerSetArmPosition;
import frc.robot.subsystems.MockHanger;

public class CmdHangerSetArmPositionTests
{
    private MockHanger _hanger;
    private CmdHangerSetArmPosition _command;

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
    public void testHangerSetArmPosition(ExtendState state, double position)
    {
        _command = new CmdHangerSetArmPosition(_hanger, state);
        
        _hanger.getArmSensor().set(position);
        _command.initialize();
        _hanger.periodic();

        assertEquals(Utils.clamp(Constants.HANGER_ARM_POSITION_LOOKUP.apply(state) - position, -1, 1), _hanger.getArmMotor().get(), Constants.EPSILON);
    }

    @ParameterizedTest
    @EnumSource(ExtendState.class)
    public void testHangerArmAtPosition(ExtendState state)
    {
        _command = new CmdHangerSetArmPosition(_hanger, state);

        _hanger.getArmSensor().set(Constants.HANGER_ARM_POSITION_LOOKUP.apply(state));
        _command.initialize();
        _hanger.periodic();

        assertEquals(0.0, _hanger.getArmMotor().get(), Constants.EPSILON);
        assertTrue(_command.isFinished());
    }
}
