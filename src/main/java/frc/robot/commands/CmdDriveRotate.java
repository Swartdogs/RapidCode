package frc.robot.commands;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.drive.Drive;

public class CmdDriveRotate extends SwartdogCommand
{
    private Drive _drive;

    private double _heading;
    private double _maxRotateSpeed;

    private boolean _absolute;

    public CmdDriveRotate(Drive drive, double heading, double maxRotateSpeed, boolean absolute)
    {
        _drive          = drive;
        _heading        = heading;
        _maxRotateSpeed = maxRotateSpeed;
        _absolute       = absolute;
    }

    @Override
    public void initialize()
    {
        double heading = _heading;
        
        if (!_absolute)
        {
            heading += _drive.getHeading();
        }
        
        _drive.rotateInit(heading, _maxRotateSpeed);
    }

    @Override
    public void execute()
    {
        _drive.drive(0, 0, _drive.rotateExec());
    }

    @Override
    public void end(boolean interrupted)
    {
        _drive.drive(0, 0, 0);
    }

    @Override
    public boolean isFinished()
    {
        return _drive.rotateIsFinished();
    }
}
