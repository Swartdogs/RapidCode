package frc.robot.subsystems.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.MockVision;

public class VisionTests 
{
    private MockVision _vision;
   
    @BeforeEach
    public void init()
    {
        _vision = new MockVision();
    }
    
    @ParameterizedTest
    @EnumSource(State.class)
    public void testSetLEDs(State state)
    {
        ExtendState expected;
        switch(state)
        {
            case On:
                expected = ExtendState.Extended;
                break;
            
            default:
                expected = ExtendState.Retracted;
                break;
        }

        _vision.setLEDs(state);

        assertEquals(expected, _vision.getLightRing().get());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testTargetFound(boolean found) 
    {
        _vision.getTargetFound().set(found);

        assertEquals(found, _vision.targetFound());
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {30, -9.1, 0, -2, 10.5})
    public void testGetTargetAngle(double angle)
    {
        _vision.getXPosition().set(angle);

        assertEquals(angle, _vision.getTargetAngle());
    }

    @ParameterizedTest
    @ValueSource(doubles = {30, 10, 0, 2, 10.5})
    public void testGetTargetDistance(double angle)
    {
        _vision.getYPosition().set(angle);

        assertEquals(Constants.Vision.HEIGHT_DELTA / Math.tan(Math.toRadians(Constants.Vision.CAMERA_ANGLE + _vision.getYPosition().get())), _vision.getTargetDistance());
    }
}
