package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.subsystems.hardware.MockHanger;

public class HangerTests 
{
    private MockHanger _hanger;

    @BeforeEach
    public void init() 
    {
        _hanger = new MockHanger();
    }

    @Test
    public void testHook()
    {
        _hanger.hook();

        assertEquals(ExtendState.Extended, _hanger.getHookSolenoid().get());
    }

    @Test
    public void testUnhook()
    {
        _hanger.unhook();

        assertEquals(ExtendState.Retracted, _hanger.getHookSolenoid().get());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 1.6, -100.2, 216, -360})
    public void testArmPosition(double position) 
    {
        _hanger.getArmSensor().set(position);

        assertEquals(position, _hanger.getArmPosition(), Constants.Testing.EPSILON);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 1.6, -100.2, 216, -360})
    public void testWinchPosition(double position) 
    {
        _hanger.getWinchSensor().set(position);

        assertEquals(position, _hanger.getWinchPosition(), Constants.Testing.EPSILON);
    }
}
