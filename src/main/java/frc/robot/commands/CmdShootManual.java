package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Shooter;

public class CmdShootManual extends SwartdogCommand 
{
    private Shooter       _shooter;
    private Ballpath      _ballpath;
    private ShootPosition _position;
    private boolean       _atSpeed;

    public CmdShootManual(Shooter shooter, Ballpath ballpath, ShootPosition position) 
    {
        _shooter  = shooter;
        _ballpath = ballpath;
        _position = position;
        _atSpeed  = false;

        addRequirements(_shooter, _ballpath);
    }

    @Override
    public void initialize() 
    {
        _atSpeed = false;
        
        if (_ballpath.getCargoCount() > 0) 
        {
            switch (_position)
            {
                case NearLaunchpad:
                    _shooter.setShooterMotorSpeed(Constants.Shooter.NEAR_LAUNCHPAD_SHOOTER_RPM);
                    _shooter.setHoodPosition(Constants.Shooter.NEAR_LAUNCHPAD_HOOD_POSITION);
                    break;
                    
                case Fender:
                    _shooter.setShooterMotorSpeed(Constants.Shooter.FENDER_SHOOTER_RPM);
                    _shooter.setHoodPosition(Constants.Shooter.FENDER_HOOD_POSITION);
                    break;
            }
        }
    }

    @Override
    public void execute() 
    {
        if (_shooter.isShooterReady()) 
        {
            _atSpeed = true;
        }
        
        if (_ballpath.hasShooterSensorTransitionedTo(State.Off))
        {
            _ballpath.modifyCargoCount(-1);
            _atSpeed = false;
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
    }

    @Override
    public void end(boolean interrupted) 
    {
        _shooter.setShooterMotorSpeed(0);
        _ballpath.setUpperTrackTo(State.Off);
        _ballpath.setLowerTrackTo(State.Off);
    }

    @Override
    public boolean isFinished() 
    {
        return _ballpath.getCargoCount() <= 0;
    }
}
