package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.Constants;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.Assignments.*;

public class HardwareShooter extends Shooter
{
    public HardwareShooter()
    {
        _shooterMotor = Motor.compose(Motor.falconFlywheel(LOW_SHOOTER_CAN_ID, Constants.Shooter.FLYWHEEL_SPEED), Motor.falconFlywheel(HIGH_SHOOTER_CAN_ID, Constants.Shooter.FLYWHEEL_SPEED));
        _hoodMotor    = Motor.invert(Motor.talonSRX(SHOOTER_HOOD_CAN_ID));
        
        _hoodSensor   = PositionSensor.analogInput(HOOD_POSITION_AIO_PORT);

        _hoodPID      = new PIDControl();

        _hoodPID.setCoefficient(Coefficient.P, 0,  0.016, 0);
        _hoodPID.setCoefficient(Coefficient.I, 10, 0,     0.001);
        _hoodPID.setCoefficient(Coefficient.D, 0,  0,     0);
        _hoodPID.setInputRange(Constants.Shooter.HOOD_MIN_POSITION, Constants.Shooter.HOOD_MAX_POSITION);
        _hoodPID.setOutputRange(-1, 1);
        _hoodPID.setSetpointDeadband(3);
    }
}
