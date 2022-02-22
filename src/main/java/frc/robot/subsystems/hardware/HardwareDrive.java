package frc.robot.subsystems.hardware;

import frc.robot.Constants;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.abstraction.PositionSensor.IMUAxis;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.SwerveModule;

public class HardwareDrive extends Drive 
{
    public HardwareDrive()
    {
        _gyro = PositionSensor.navX(IMUAxis.Yaw);
        _drivePID = null;
        _rotatePID = null;

        Motor rotateFLMotor    = Motor.neo(4);
        Motor rotateFRMotor    = Motor.neo(6);
        Motor rotateBLMotor    = Motor.neo(8);
        Motor rotateBRMotor    = Motor.neo(10);

        Motor driveFLMotor     = Motor.falcon(3);
        Motor driveFRMotor     = Motor.falcon(5);
        Motor driveBLMotor     = Motor.falcon(7);
        Motor driveBRMotor     = Motor.falcon(9);

        PositionSensor positionFLSensor = PositionSensor.potentiometer(0, Constants.Drive.MODULE_ROTATE_SCALE, Constants.Drive.MODULE_ROTATE_OFFSET);
        PositionSensor positionFRSensor = PositionSensor.potentiometer(1, Constants.Drive.MODULE_ROTATE_SCALE, Constants.Drive.MODULE_ROTATE_OFFSET);
        PositionSensor positionBLSensor = PositionSensor.potentiometer(2, Constants.Drive.MODULE_ROTATE_SCALE, Constants.Drive.MODULE_ROTATE_OFFSET);
        PositionSensor positionBRSensor = PositionSensor.potentiometer(3, Constants.Drive.MODULE_ROTATE_SCALE, Constants.Drive.MODULE_ROTATE_OFFSET);

        SwerveModule fl = new HardwareSwerveModule(driveFLMotor, rotateFLMotor, positionFLSensor, -12.5,  12.0, Constants.Drive.FL_MOTOR_OFFSET, Constants.Drive.ODOMETRY_SCALE);
        SwerveModule fr = new HardwareSwerveModule(driveFRMotor, rotateFRMotor, positionFRSensor,  12.5,  12.0, Constants.Drive.FR_MOTOR_OFFSET, Constants.Drive.ODOMETRY_SCALE);
        SwerveModule bl = new HardwareSwerveModule(driveBLMotor, rotateBLMotor, positionBLSensor, -12.5, -12.0, Constants.Drive.BL_MOTOR_OFFSET, Constants.Drive.ODOMETRY_SCALE);
        SwerveModule br = new HardwareSwerveModule(driveBRMotor, rotateBRMotor, positionBRSensor,  12.5, -12.0, Constants.Drive.BR_MOTOR_OFFSET, Constants.Drive.ODOMETRY_SCALE);

        _swerveModules = new SwerveModule[] 
        { 
            fl, 
            fr,
            bl, 
            br 
        };

        init();
    }
}
