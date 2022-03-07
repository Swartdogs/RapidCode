package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.*;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.*;

public class CmdShootWithOdometry extends SwartdogCommand
{
    private Drive    _drive;
    private Shooter  _shooter;
    private Ballpath _ballpath;

    private boolean  _atSpeed;

    private double _target;

    public CmdShootWithOdometry(Drive drive, Shooter shooter, Ballpath ballpath)
    {
        _drive    = drive;
        _shooter  = shooter;
        _ballpath = ballpath;

        _atSpeed  = false;

        addRequirements(_drive, _shooter, _ballpath);
    }

    @Override
    public void initialize()
    {
        if (_ballpath.getCargoCount() > 0)
        {
            Vector target   = Constants.Shooter.HUB_POSITION.clone();
            target.subtract(_drive.getOdometer());
            double distance = target.getR();
            
            _shooter.setShooterMotorSpeed(Constants.Shooter.SHOOTER_SPEED_LOOKUP.applyAsDouble(distance));
            _shooter.setHoodPosition(Constants.Shooter.SHOOTER_HOOD_LOOKUP.applyAsDouble(distance));

            _target = target.getTheta();

            _drive.rotateInit(_target, Constants.Drive.ALIGN_ROTATE_SPEED);
        }
    }

    @Override
    public void execute()
    {
        _drive.drive(0, 0, _drive.rotateExec());
        
        /* 
         * The "_atSpeed" variable is used to prevent a problem where the cargo would slow the wheel down,
         * causing the ballpath to shut off while the cargo is contacting the wheel.
         */
        if (_shooter.isShooterReady() && _drive.rotateIsFinished())
        {
            _atSpeed = true;
        }

        if (_ballpath.hasShooterSensorTransitionedTo(State.Off)) 
        {
            _atSpeed = false;
            _ballpath.modifyCargoCount(-1);
        }

        if (_atSpeed) 
        {
            _ballpath.setUpperTrackTo(State.On);
            _ballpath.setLowerTrackTo(State.On);
        }
        else
        {
            _ballpath.setUpperTrackTo(State.Off);
            _ballpath.setLowerTrackTo(State.Off);
        }

        // System.out.println("Target: " + _target + ", Actual: " + _drive.getHeading());
    }

    @Override
    public void end(boolean interrupted)
    {
        _shooter.setShooterMotorSpeed(0);
        _ballpath.setUpperTrackTo(State.Off);
        _ballpath.setLowerTrackTo(State.Off);
        _drive.drive(0, 0, 0);

        _shooter.setHoodPosition(Constants.Shooter.NEAR_LAUNCHPAD_HOOD_POSITION);
    }

    @Override
    public boolean isFinished()
    {
        return _ballpath.getCargoCount() <= 0;
    }
}
