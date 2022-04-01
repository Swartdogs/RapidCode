package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.abstraction.Solenoid;
import frc.robot.subsystems.Hanger;

import static frc.robot.Constants.Assignments.*;

public class HardwareHanger extends Hanger
{
    public HardwareHanger()
    {
        _armMotor        = Motor.invert(Motor.neo(ARM_CAN_ID));
        _winchMotor      = Motor.compose(Motor.invert(Motor.neo(WINCH_1_CAN_ID)), Motor.neo(WINCH_2_CAN_ID));

        _ratchetSolenoid = Solenoid.invert(Solenoid.solenoid(PNEUMATICS_MODULE_CAN_ID, MODULE_TYPE, HANGER_RATCHET_SOL_PORT));

        _armSensor       = PositionSensor.dutyCycleEncoder(ARM_SENSOR_DIO_PORT);
        _winchSensor     = PositionSensor.dutyCycleEncoder(WINCH_SENSOR_DIO_PORT);

        _armPID          = new PIDControl();
        _winchPID        = new PIDControl();

        _winchSensor.setScalingFunction(raw -> -raw);

        _armPID.setCoefficient(Coefficient.P, 0, 1, 0);
        _armPID.setCoefficient(Coefficient.I, 0, 0,      0);
        _armPID.setCoefficient(Coefficient.D, 0, 0,      0);
        _armPID.setInputRange(0.3, 0.47);
        _armPID.setOutputRange(-1, 1);
        _armPID.setSetpointDeadband(0.02);

        _winchPID.setCoefficient(Coefficient.P, 0, 01, 0);
        _winchPID.setCoefficient(Coefficient.I, 0, 0,  0);
        _winchPID.setCoefficient(Coefficient.D, 0, 0,  0);
        _winchPID.setInputRange(0, 0);
        _winchPID.setOutputRange(0, 0);
    }
}
