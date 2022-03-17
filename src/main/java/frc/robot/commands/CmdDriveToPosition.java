package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Vector;

public class CmdDriveToPosition extends SwartdogCommand 
{
    private Drive   _drive;
    
    private Vector  _finalTranslation;
    private double  _finalHeading;

    private double _maxRotateSpeed;
    private double _maxDriveSpeed;
    private double _minDriveSpeed;

    private boolean _absolute;

    public CmdDriveToPosition(Drive drive, Vector finalPosition, double finalRotation, double maxRotateSpeed, double maxDriveSpeed, double minDriveSpeed, boolean absolute)
    {
        _drive       = drive;
        
        _finalTranslation = finalPosition;
        _finalHeading = finalRotation;

        _maxRotateSpeed = maxRotateSpeed;
        _maxDriveSpeed = maxDriveSpeed;
        _minDriveSpeed = minDriveSpeed;

        _absolute = absolute;

        addRequirements(_drive);
    }

    @Override
    public void initialize()
    {
        Vector translation = _finalTranslation;

        if (!_absolute)
        {
            translation = translation.add(_drive.getOdometer());
            _finalHeading += _drive.getHeading();
        }

        System.out.println(translation + " " + _drive.getOdometer());

        _drive.translateInit(translation, _maxDriveSpeed, _minDriveSpeed, false);
        _drive.rotateInit(_finalHeading, _maxRotateSpeed);
    }

    @Override
    public void execute() 
    {   
        double rotateSpeed = _drive.rotateExec();
        Vector translateVector = _drive.translateExec();
        
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
        return _drive.translateIsFinished() && _drive.rotateIsFinished();
    }
}
