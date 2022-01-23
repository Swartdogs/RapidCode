package frc.robot.subsystems;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.abstraction.MockMotor;
import frc.robot.abstraction.MockPositionSensor;

public class MockHanger extends Hanger 
{
    public MockHanger() 
    {
        _armMotor   = new MockMotor();
        _winchMotor  = new MockMotor();

        _armSensor  = new MockPositionSensor();
        _winchSensor = new MockPositionSensor();

        _armPID     = new PIDControl();
        _winchPID    = new PIDControl();

        _armPID.setCoefficient(Coefficient.P, 0, 1, 0);
        _armPID.setCoefficient(Coefficient.I, 0, 0, 0);
        _armPID.setCoefficient(Coefficient.D, 0, 0, 0);
        _armPID.setInputRange(-1000, 1000);
        _armPID.setOutputRange(-1, 1);

        _winchPID.setCoefficient(Coefficient.P, 0, 1, 0);
        _winchPID.setCoefficient(Coefficient.I, 0, 0, 0);
        _winchPID.setCoefficient(Coefficient.D, 0, 0, 0);
        _winchPID.setInputRange(-1000, 1000);
        _winchPID.setOutputRange(-1, 1);
    }

    public MockMotor getArmMotor() 
    {
        return (MockMotor)_armMotor;
    }

    public MockMotor getWinchMotor() 
    {
        return (MockMotor)_winchMotor;
    }

    public MockPositionSensor getArmSensor() 
    {
        return (MockPositionSensor)_armSensor;
    }

    public MockPositionSensor getWinchSensor() 
    {
        return (MockPositionSensor)_winchSensor;
    }
}
