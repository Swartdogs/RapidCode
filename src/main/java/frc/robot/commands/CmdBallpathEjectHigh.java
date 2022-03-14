package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Shooter;

public class CmdBallpathEjectHigh extends SwartdogCommand
{
    private Ballpath _ballpath;
    private Shooter  _shooter;

    private boolean  _ejected;
    private boolean  _loading;

    public CmdBallpathEjectHigh(Ballpath ballpath, Shooter shooter)
    {
        _ballpath = ballpath;
        _shooter  = shooter;

        _ejected = false;
        _loading = false;
    }

    @Override
    public void initialize()
    {
        _ejected = false;
        _loading = false;

        if (_ballpath.getCargoCount() > 0)
        {
            _ballpath.setUpperTrackTo(State.On);
            _shooter.setShooterMotorSpeed(Constants.Shooter.EJECT_SPEED);
            _shooter.setHoodPosition(Constants.Shooter.EJECT_HOOD_POSITION);
        }
    }

    @Override
    public void execute()
    {
        if (!_ejected)
        {
            if (_ballpath.hasShooterSensorTransitionedTo(State.Off))
            {
                _ejected = true;
                _ballpath.modifyCargoCount(-1);

                if (_ballpath.getCargoCount() > 0) 
                {
                    _loading = true;
                    _ballpath.setLowerTrackTo(State.On);
                }
            } 
        }
        else if (_loading)
        {
            if (_ballpath.hasShooterSensorTransitionedTo(State.On))
            {
                _loading = false;
            }
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        _ballpath.setUpperTrackTo(State.Off);
        _ballpath.setLowerTrackTo(State.Off);
        _shooter.setShooterMotorSpeed(0);
        _shooter.setHoodPosition(Constants.Shooter.NEAR_LAUNCHPAD_HOOD_POSITION);
    }

    @Override
    public boolean isFinished()
    {
        return (_ejected && !_loading) ||
               (_ballpath.getCargoCount() == 0);
    }
}
