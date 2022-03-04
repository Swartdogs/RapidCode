package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.subsystems.Hanger;

public class HardwareHanger extends Hanger
{
    public HardwareHanger()
    {
        _armMotor     = null;
        _winchMotor   = null;

        _hookSolenoid = null;

        _armSensor    = null;
        _winchSensor  = null;

        _armPID       = new PIDControl();
        _winchPID     = new PIDControl();

        _armPID.setCoefficient(Coefficient.P, 0, 0, 0);
        _armPID.setCoefficient(Coefficient.I, 0, 0, 0);
        _armPID.setCoefficient(Coefficient.D, 0, 0, 0);
        _armPID.setInputRange(0, 0);
        _armPID.setOutputRange(0, 0);

        _winchPID.setCoefficient(Coefficient.P, 0, 01, 0);
        _winchPID.setCoefficient(Coefficient.I, 0, 0, 0);
        _winchPID.setCoefficient(Coefficient.D, 0, 0, 0);
        _winchPID.setInputRange(0, 0);
        _winchPID.setOutputRange(0, 0);
    }
}
