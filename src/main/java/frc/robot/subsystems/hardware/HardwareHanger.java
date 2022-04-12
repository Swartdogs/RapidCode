package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.Constants;
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
        
        _armSensor.setScalingFunction(raw -> Constants.Hanger.ARM_SLOPE * (raw - Constants.Hanger.ZERO_ANGLE));
        _winchSensor.setScalingFunction(raw -> Constants.Hanger.WINCH_SLOPE * (raw - Constants.Hanger.MIN_WINCH_POSITION) + Constants.Hanger.MIN_WINCH_LENGTH);
        
        _armPID.setCoefficient(Coefficient.P, 0, 0.008, 0);
        _armPID.setCoefficient(Coefficient.I, 0, 0,      0);
        _armPID.setCoefficient(Coefficient.D, 0, 0,      0);
        _armPID.setInputRange(-45, 90);
        _armPID.setOutputRange(-1, 1);
        _armPID.setSetpointDeadband(5);

        _winchPID.setCoefficient(Coefficient.P, 0, 0.1, 0);
        _winchPID.setCoefficient(Coefficient.I, 0, 0.05,  0);
        _winchPID.setCoefficient(Coefficient.D, 0, 0,  0);
        _winchPID.setInputRange(Constants.Hanger.MIN_WINCH_LENGTH, Constants.Hanger.MAX_WINCH_LENGTH);
        _winchPID.setOutputRange(-Constants.Hanger.WINCH_SPEED, Constants.Hanger.WINCH_SPEED);
        _winchPID.setSetpointDeadband(0.2);
        _winchPID.setOutputRamp(0.0, 0.01);
    }
}
