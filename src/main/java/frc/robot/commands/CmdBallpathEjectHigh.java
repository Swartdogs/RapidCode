package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Dashboard;
import frc.robot.subsystems.RobotLog;
import frc.robot.subsystems.Shooter;

public class CmdBallpathEjectHigh extends CommandBase
{
    private Ballpath  _ballpath;
    private Shooter   _shooter;
    private Dashboard _dashboard;
    private RobotLog  _log;

    private boolean   _ejected;
    private boolean   _loading;

    public CmdBallpathEjectHigh(SubsystemContainer subsystemContainer)
    {
        _ballpath  = subsystemContainer.getBallpath();
        _shooter   = subsystemContainer.getShooter();
        _dashboard = subsystemContainer.getDashboard();
        _log       = subsystemContainer.getRobotLog();

        _ejected   = false;
        _loading   = false;
    }

    @Override
    public void initialize()
    {
        _ejected = false;
        _loading = false;

        if (_ballpath.getCargoCount() > 0)
        {
            _ballpath.setUpperTrackTo(State.On);
            _shooter.setShooterMotorSpeed(_dashboard.getShooterEjectSpeed());
            _shooter.setHoodPosition(_dashboard.getShooterEjectHoodPosition());
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
        _shooter.setHoodPosition(RobotPosition.NearLaunchpad.getHoodPosition(TargetPosition.UpperHub));

        _log.log("Ejected Cargo (high), Current Count: " + _ballpath.getCargoCount());
    }

    @Override
    public boolean isFinished()
    {
        return (_ejected && !_loading) ||
               (_ballpath.getCargoCount() == 0);
    }
}
