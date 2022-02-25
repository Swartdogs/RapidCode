package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Shooter;

public class CmdShootManual extends SwartdogCommand 
{
    private Shooter  _shooter;
    private Ballpath _ballpath;

    public CmdShootManual(Shooter shooter, Ballpath ballpath) 
    {
        _shooter  = shooter;
        _ballpath = ballpath;

        addRequirements(_shooter, _ballpath);
    }

    @Override
    public void initialize() 
    {
        if (_ballpath.getCargoCount() > 0) 
        {
            _shooter.setShooterMotorSpeed(Constants.Shooter.MANUAL_SHOOTER_RPM);
            _shooter.setHoodPosition(Constants.Shooter.MANUAL_HOOD_POSITION);
        }
    }

    @Override
    public void execute() 
    {
        if (_shooter.isShooterReady()) 
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
