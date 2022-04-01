package frc.robot.subsystems.hardware;

import frc.robot.abstraction.Motor;
import frc.robot.abstraction.Solenoid;
import frc.robot.subsystems.Pickup;

import static frc.robot.Constants.Assignments.*;

public class HardwarePickup extends Pickup
{
    public HardwarePickup()
    {
        _pickupMotor    = Motor.invert(Motor.victorSP(PICKUP_PWM_PORT));
        _deploySolenoid = Solenoid.solenoid(PNEUMATICS_MODULE_CAN_ID, MODULE_TYPE, PICKUP_DEPLOY_SOL_PORT);
    }
}
