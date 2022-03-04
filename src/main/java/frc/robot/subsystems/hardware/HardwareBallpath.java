package frc.robot.subsystems.hardware;

import frc.robot.abstraction.Motor;
import frc.robot.abstraction.Switch;
import frc.robot.subsystems.Ballpath;

public class HardwareBallpath extends Ballpath
{
    public HardwareBallpath()
    {
        _lowerTrack    = Motor.compose(Motor.invert(Motor.talonSRX(11)), Motor.talonSRX(12));
        _upperTrack    = Motor.neo(13);

        _pickupSensor  = Switch.lightSensor(0);
        _shooterSensor = Switch.lightSensor(1);
    }    
}
