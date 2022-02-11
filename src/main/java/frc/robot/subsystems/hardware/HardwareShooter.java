package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.subsystems.Shooter;

public class HardwareShooter extends Shooter
{
    public HardwareShooter()
    {
        _shooterMotor = null;
        _hoodMotor    = null;
        
        _hoodSensor   = null;

        _hoodPID      = new PIDControl();

        _hoodPID.setCoefficient(Coefficient.P, 0, 0, 0);
        _hoodPID.setCoefficient(Coefficient.I, 0, 0, 0);
        _hoodPID.setCoefficient(Coefficient.D, 0, 0, 0);
        _hoodPID.setInputRange(0, 0);
        _hoodPID.setOutputRange(0, 0);
    }
}
