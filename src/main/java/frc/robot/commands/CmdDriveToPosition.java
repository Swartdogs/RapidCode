package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Position;
import frc.robot.subsystems.drive.Vector;

public class CmdDriveToPosition extends SwartdogCommand 
{
    private Drive    _drive;
    
    private Position _finalPosition;

    private double   _maxRotateSpeed;
    private double   _maxDriveSpeed;
    private double   _minDriveSpeed;

    private boolean  _absolute;

    public CmdDriveToPosition(Drive drive, Position finalPosition, double maxRotateSpeed, double maxDriveSpeed, double minDriveSpeed, boolean absolute)
    {
        _drive          = drive;
        
        _finalPosition  = finalPosition;

        _maxRotateSpeed = maxRotateSpeed;
        _maxDriveSpeed  = maxDriveSpeed;
        _minDriveSpeed  = minDriveSpeed;

        _absolute       = absolute;

        addRequirements(_drive);
    }

    @Override
    public void initialize()
    {
        Vector translation = _finalPosition;
        double heading     = _finalPosition.getAngle();

        if (!_absolute)
        {
            translation = translation.add(_drive.getOdometer());
            heading += _drive.getHeading();
        }

        _drive.autoDriveInit(new Position(translation, heading), _maxRotateSpeed, _maxDriveSpeed, _minDriveSpeed);
    }

    @Override
    public void execute() 
    {   
        Vector translateVector = _drive.translateExec();
        double rotateSpeed = _drive.rotateExec();
        
        _drive.drive(translateVector, rotateSpeed, true);
    }

    @Override
    public void end(boolean interrupted)
    {
        _drive.drive(0, 0, 0);
    }

    @Override
    public boolean isFinished() 
    {
        return _drive.autoDriveIsFinished();
    }
}
