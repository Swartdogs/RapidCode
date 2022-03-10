package frc.robot.subsystems.drive;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.abstraction.MockPositionSensor;

public class MockDrive extends Drive 
{
    public MockDrive()
    {
        _gyro = new MockPositionSensor();
        _drivePID = new PIDControl();
        _rotatePID = new PIDControl();

        _drivePID.setCoefficient(Coefficient.P, 0, 1, 0);
        _drivePID.setCoefficient(Coefficient.I, 0, 0, 0);
        _drivePID.setCoefficient(Coefficient.D, 0, 0, 0);
        _drivePID.setInputRange(-1000.0, 1000.0);
        _drivePID.setOutputRange(-1.0, 1.0);

        _rotatePID.setCoefficient(Coefficient.P, 0, 1, 0);
        _rotatePID.setCoefficient(Coefficient.I, 0, 0, 0);
        _rotatePID.setCoefficient(Coefficient.D, 0, 0, 0);
        _rotatePID.setInputRange(-1000.0, 1000.0);
        _rotatePID.setOutputRange(-1.0, 1.0);

        _swerveModules = new SwerveModule[] 
        { 
            new MockSwerveModule( 10,  10, 0, 1),
            new MockSwerveModule(-10,  10, 0, 1),
            new MockSwerveModule( 10, -10, 0, 1),
            new MockSwerveModule(-10, -10, 0, 1)
        };

        init();
    }

    public MockPositionSensor getGyro()
    {
        return (MockPositionSensor)_gyro;
    }

    public MockSwerveModule getSwerveModule(int index)
    {
        return (MockSwerveModule)_swerveModules[index];
    }

    public void initialize() {}

    public boolean isInitialized()
    {
        return true;
    }
}
