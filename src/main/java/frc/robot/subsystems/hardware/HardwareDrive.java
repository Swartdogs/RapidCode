package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.Constants;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.abstraction.PositionSensor.IMUAxis;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.SwerveModule;

import static frc.robot.Constants.Assignments.*;

public class HardwareDrive extends Drive 
{
    public HardwareDrive()
    {
        _gyro                           = PositionSensor.navX(IMUAxis.Yaw);

        Motor rotateFLMotor             = Motor.neo(FL_ROTATE_CAN_ID);
        Motor rotateFRMotor             = Motor.neo(FR_ROTATE_CAN_ID);
        Motor rotateBLMotor             = Motor.neo(BL_ROTATE_CAN_ID);
        Motor rotateBRMotor             = Motor.neo(BR_ROTATE_CAN_ID);

        Motor driveFLMotor              = Motor.falcon(FL_DRIVE_CAN_ID);
        Motor driveFRMotor              = Motor.falcon(FR_DRIVE_CAN_ID);
        Motor driveBLMotor              = Motor.falcon(BL_DRIVE_CAN_ID);
        Motor driveBRMotor              = Motor.falcon(BR_DRIVE_CAN_ID);

        PositionSensor positionFLSensor = PositionSensor.potentiometer(FL_POSITION_AIO_PORT, Constants.Drive.MODULE_ROTATE_SCALE, Constants.Drive.MODULE_ROTATE_OFFSET);
        PositionSensor positionFRSensor = PositionSensor.potentiometer(FR_POSITION_AIO_PORT, Constants.Drive.MODULE_ROTATE_SCALE, Constants.Drive.MODULE_ROTATE_OFFSET);
        PositionSensor positionBLSensor = PositionSensor.potentiometer(BL_POSITION_AIO_PORT, Constants.Drive.MODULE_ROTATE_SCALE, Constants.Drive.MODULE_ROTATE_OFFSET);
        PositionSensor positionBRSensor = PositionSensor.potentiometer(BR_POSITION_AIO_PORT, Constants.Drive.MODULE_ROTATE_SCALE, Constants.Drive.MODULE_ROTATE_OFFSET);

        SwerveModule fl                 = new HardwareSwerveModule(driveFLMotor, rotateFLMotor, positionFLSensor, -12.5,  12.0, Constants.Drive.FL_MOTOR_OFFSET, Constants.Drive.ODOMETRY_SCALE);
        SwerveModule fr                 = new HardwareSwerveModule(driveFRMotor, rotateFRMotor, positionFRSensor,  12.5,  12.0, Constants.Drive.FR_MOTOR_OFFSET, Constants.Drive.ODOMETRY_SCALE);
        SwerveModule bl                 = new HardwareSwerveModule(driveBLMotor, rotateBLMotor, positionBLSensor, -12.5, -12.0, Constants.Drive.BL_MOTOR_OFFSET, Constants.Drive.ODOMETRY_SCALE);
        SwerveModule br                 = new HardwareSwerveModule(driveBRMotor, rotateBRMotor, positionBRSensor,  12.5, -12.0, Constants.Drive.BR_MOTOR_OFFSET, Constants.Drive.ODOMETRY_SCALE);

        _swerveModules = new SwerveModule[] 
        { 
            fl, 
            fr,
            bl, 
            br 
        };

        _translatePID = new PIDControl();
        _rotatePID = new PIDControl();

        _translatePID.setCoefficient(Coefficient.P, 0, 0.015, 0);
        _translatePID.setCoefficient(Coefficient.I, 5, 0, 0.001);
        _translatePID.setCoefficient(Coefficient.D, 0, 0, 0);
        _translatePID.setInputRange(0, 500.0);
        _translatePID.setOutputRamp(0.1, 0.05);
        _translatePID.setSetpointDeadband(2);

        _rotatePID.setCoefficient(Coefficient.P, 0, 0.9, 0);
        _rotatePID.setCoefficient(Coefficient.I, Math.sin(Math.toRadians(20)), 0, 0.09);
        _rotatePID.setCoefficient(Coefficient.D, 0, 0, 0);
        _rotatePID.setInputRange(-1.0, 1.0);
        _rotatePID.setOutputRamp(0.1, 0.05);
        _rotatePID.setSetpointDeadband(Math.sin(Math.toRadians(2)));

        init();
    }
}
