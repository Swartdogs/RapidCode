package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.subsystems.drive.SwerveModule;

public class HardwareSwerveModule extends SwerveModule 
{
    public HardwareSwerveModule
    (
        Motor driveMotor,
        Motor rotateMotor,
        PositionSensor rotateSensor,
        double x, 
        double y,
        double relativeZero,
        double distanceScaler
    )
    {
        super(x, y, relativeZero, distanceScaler);

        _driveMotor = driveMotor;
        _rotateMotor = rotateMotor;
        _positionSensor = rotateSensor;

        _rotatePID = new PIDControl();

        _rotatePID.setCoefficient(Coefficient.P, 0, 0.9, 0);
        _rotatePID.setCoefficient(Coefficient.I, 0, 0, 0);
        _rotatePID.setCoefficient(Coefficient.D, 0, 0, 0);

        _rotatePID.setInputRange(-1, 1);
        _rotatePID.setOutputRange(-1, 1);

    }
}
