package frc.robot.subsystems;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.abstraction.MockMotor;
import frc.robot.abstraction.MockPositionSensor;

public class MockShooter extends Shooter
{
    public MockShooter()
    {
        _shooterMotor   = new MockMotor();
        _hoodMotor      = new MockMotor();

        _hoodSensor     = new MockPositionSensor();

        _hoodPID        = new PIDControl();

        _hoodPID.setCoefficient(Coefficient.P, 0.0, 1.0, 0.0);
        _hoodPID.setCoefficient(Coefficient.I, 0.0, 0.0, 0.0);
        _hoodPID.setCoefficient(Coefficient.D, 0.0, 0.0, 0.0);
        _hoodPID.setInputRange(-10000.0, 10000.0);
        _hoodPID.setOutputRange(-1.0, 1.0);
    }

    public MockMotor getShooterMotor()
    {
        return (MockMotor)_shooterMotor;
    }

    public MockMotor getHoodMotor()
    {
        return (MockMotor)_hoodMotor;
    }

    public MockPositionSensor getHoodSensor()
    {
        return (MockPositionSensor)_hoodSensor;
    }
}
