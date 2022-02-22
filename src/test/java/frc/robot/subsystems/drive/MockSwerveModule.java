package frc.robot.subsystems.drive;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.abstraction.MockMotor;
import frc.robot.abstraction.MockPositionSensor;

public class MockSwerveModule extends SwerveModule 
{
    public MockSwerveModule(double x, double y, double relativeZero, double distanceScaler)
    {
        super(x, y, relativeZero, distanceScaler);

        _driveMotor     = new MockMotor();
        _rotateMotor    = new MockMotor();
        _positionSensor = new MockPositionSensor();
        _rotatePID      = new PIDControl();

        _rotatePID.setCoefficient(Coefficient.P, 0, 1, 0);
        _rotatePID.setCoefficient(Coefficient.I, 0, 0, 0);
        _rotatePID.setCoefficient(Coefficient.D, 0, 0, 0);
        _rotatePID.setInputRange(-1, 1);
        _rotatePID.setOutputRange(-1, 1);
    }

    public MockMotor getDriveMotor()
    {
        return (MockMotor)_driveMotor;
    }

    public MockMotor getRotateMotor()
    {
        return (MockMotor)_rotateMotor;
    }

    public MockPositionSensor getPositionSensor()
    {
        return (MockPositionSensor)_positionSensor;
    }

    public PIDControl getRotatePID()
    {
        return _rotatePID;
    }
}
