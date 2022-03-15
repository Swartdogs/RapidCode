package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.abstraction.PositionSensor.MockPositionSensor;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.SwerveModule;

public class MockDrive extends Drive 
{
    public MockDrive()
    {
        _gyro = new MockPositionSensor();
        _translatePID = new PIDControl();
        _rotatePID = new PIDControl();

        _translatePID.setCoefficient(Coefficient.P, 0, 1, 0);
        _translatePID.setCoefficient(Coefficient.I, 0, 0, 0);
        _translatePID.setCoefficient(Coefficient.D, 0, 0, 0);
        _translatePID.setInputRange(-1000.0, 1000.0);
        _translatePID.setOutputRange(-1.0, 1.0);

        _rotatePID.setCoefficient(Coefficient.P, 0, 1, 0);
        _rotatePID.setCoefficient(Coefficient.I, 0, 0, 0);
        _rotatePID.setCoefficient(Coefficient.D, 0, 0, 0);
        _rotatePID.setInputRange(-1000.0, 1000.0);
        _rotatePID.setOutputRange(-1.0, 1.0);
        _rotatePID.setSetpointDeadband(Math.sin(Math.toRadians(1)));

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
}
