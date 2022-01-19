package frc.robot.subsystems.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import frc.robot.Constants;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.MockBallpath;

public class BallpathTests 
{
    private MockBallpath _ballpath;

    @Before
    public void init()
    {
        _ballpath = new MockBallpath();
    }

    @Test
    public void testBallpathEnable()
    {
        _ballpath.enable();

        assertEquals(Constants.BALLPATH_SPEED, _ballpath.getLowerTrackMotor().get(), Constants.EPSILON);
        assertEquals(Constants.BALLPATH_SPEED, _ballpath.getUpperTrackMotor().get(), Constants.EPSILON);
    }

    @Test
    public void testBallpathDisable()
    {
        _ballpath.disable();
        
        assertEquals(0.0, _ballpath.getLowerTrackMotor().get(), Constants.EPSILON);
        assertEquals(0.0, _ballpath.getUpperTrackMotor().get(), Constants.EPSILON);
    }

    @Test
    public void testBallpathReverse()
    {
        _ballpath.reverse();

        assertEquals(-Constants.BALLPATH_SPEED, _ballpath.getLowerTrackMotor().get(), Constants.EPSILON);
        assertEquals(-Constants.BALLPATH_SPEED, _ballpath.getUpperTrackMotor().get(), Constants.EPSILON);
    }

    @Test
    public void testPickupSensorOn()
    {
        _ballpath.getPickupSensor().set(State.On);

        assertEquals(State.On, _ballpath.getPickupSensorState());
    }

    @Test
    public void testPickupSensorOff()
    {
        _ballpath.getPickupSensor().set(State.Off);

        assertEquals(State.Off, _ballpath.getPickupSensorState());
    }

    @Test
    public void testPickupSensorReverse()
    {
        _ballpath.getPickupSensor().set(State.Reverse);

        assertEquals(State.Reverse, _ballpath.getPickupSensorState());
    }

    @Test
    public void testPickupSensorTransitionToOn()
    {
        _ballpath.getPickupSensor().set(State.Off);
        _ballpath.periodic();
        _ballpath.getPickupSensor().set(State.On);

        assertEquals(true, _ballpath.hasPickupSensorTransitionedTo(State.On));
    }

    @Test
    public void testPickupSensorTransitionToOff()
    {
        _ballpath.getPickupSensor().set(State.On);
        _ballpath.periodic();
        _ballpath.getPickupSensor().set(State.Off);

        assertEquals(true, _ballpath.hasPickupSensorTransitionedTo(State.Off));
    }

    @Test
    public void testPickupSensorTransitionToReverse()
    {
        _ballpath.getPickupSensor().set(State.Off);
        _ballpath.periodic();
        _ballpath.getPickupSensor().set(State.Reverse);

        assertEquals(true, _ballpath.hasPickupSensorTransitionedTo(State.Reverse));
    }

    @Test
    public void testUpperTrackSensorOn()
    {
        _ballpath.getUpperTrackSensor().set(State.On);

        assertEquals(State.On, _ballpath.getUpperTrackSensorState());
    }

    @Test
    public void testUpperTrackSensorOff()
    {
        _ballpath.getUpperTrackSensor().set(State.Off);

        assertEquals(State.Off, _ballpath.getUpperTrackSensorState());
    }

    @Test
    public void testUpperTrackSensorReverse()
    {
        _ballpath.getUpperTrackSensor().set(State.Reverse);

        assertEquals(State.Reverse, _ballpath.getUpperTrackSensorState());
    }

    @Test
    public void testUpperTrackSensorTransitionToOn()
    {
        _ballpath.getUpperTrackSensor().set(State.Off);
        _ballpath.periodic();
        _ballpath.getUpperTrackSensor().set(State.On);

        assertEquals(true, _ballpath.hasUpperTrackSensorTransitionedTo(State.On));
    }

    @Test
    public void testUpperTrackSensorTransitionToOff()
    {
        _ballpath.getUpperTrackSensor().set(State.On);
        _ballpath.periodic();
        _ballpath.getUpperTrackSensor().set(State.Off);

        assertEquals(true, _ballpath.hasUpperTrackSensorTransitionedTo(State.Off));
    }

    @Test
    public void testUpperTrackSensorTransitionToReverse()
    {
        _ballpath.getUpperTrackSensor().set(State.Off);
        _ballpath.periodic();
        _ballpath.getUpperTrackSensor().set(State.Reverse);

        assertEquals(true, _ballpath.hasUpperTrackSensorTransitionedTo(State.Reverse));
    }
}
