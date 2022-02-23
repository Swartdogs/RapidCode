package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.RobotLog;
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

            RobotLog.getInstance().log(String.format("Expected Shooter RPM: %4.0f, Expected Hood Position: %4.0f, Cargo Count: %2.0f" , Constants.Shooter.MANUAL_SHOOTER_RPM, Constants.Shooter.MANUAL_HOOD_POSITION, _ballpath.getCargoCount()));
        } 
        else 
        {
            RobotLog.getInstance().log("No Cargo, Not Shooting");
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

        RobotLog.getInstance().log("Shooter Off, Current Cargo Count: " + _ballpath.getCargoCount());
    }

    @Override
    public boolean isFinished() 
    {
        return _ballpath.getCargoCount() <= 0;
    }
}
