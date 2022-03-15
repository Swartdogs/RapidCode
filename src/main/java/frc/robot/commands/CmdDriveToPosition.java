package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Vector;

public class CmdDriveToPosition extends SwartdogCommand 
{
    private Drive   _drive;
    
    private Vector  _finalTranslation;
    private double  _finalHeading;

    private boolean _absolute;

    public CmdDriveToPosition(Drive drive, Vector finalPosition, double finalRotation, boolean absolute)
    {
        _drive       = drive;
        
        _finalTranslation = finalPosition;
        _finalHeading = finalRotation;

        _absolute = absolute;

        addRequirements(_drive);
    }

    @Override
    public void initialize()
    {
        if (_absolute)
        {
            _finalTranslation.add(_drive.getOdometer());
            _finalHeading += _drive.getHeading();
        }

        _drive.translateInit(_finalTranslation, 0.5, 0, false);
        _drive.rotateInit(_finalHeading, 0.5);
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
