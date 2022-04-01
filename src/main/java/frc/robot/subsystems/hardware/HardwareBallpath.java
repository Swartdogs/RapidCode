package frc.robot.subsystems.hardware;

import frc.robot.abstraction.Motor;
import frc.robot.abstraction.Switch;
import frc.robot.subsystems.Ballpath;

import static frc.robot.Constants.Assignments.*;

public class HardwareBallpath extends Ballpath
{
    public HardwareBallpath()
    {
        _lowerTrack    = Motor.compose(Motor.invert(Motor.talonSRX(LEFT_BALLPATH_CAN_ID)), Motor.talonSRX(RIGHT_BALLPATH_CAN_ID));
        _upperTrack    = Motor.neo(VERTICAL_BALLPATH_CAN_ID);

        _pickupSensor  = Switch.lightSensor(PICKUP_SENSOR_DIO_PORT);
        _shooterSensor = Switch.lightSensor(SHOOTER_SENSOR_DIO_PORT);
    }    
}
