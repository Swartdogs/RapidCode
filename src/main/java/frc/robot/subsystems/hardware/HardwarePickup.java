package frc.robot.subsystems.hardware;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.Solenoid;
import frc.robot.subsystems.Pickup;

public class HardwarePickup extends Pickup
{
    public HardwarePickup()
    {
        _pickupMotor    = Motor.neo(14);
        _deploySolenoid = Solenoid.solenoid(2, PneumaticsModuleType.REVPH, 14);
    }
}
