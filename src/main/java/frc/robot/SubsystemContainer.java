package frc.robot;

import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.subsystems.*;
import frc.robot.drive.*;

public class SubsystemContainer 
{
    private Drive          _drive;
    private Ballpath       _ballpath;
    private Dashboard      _dashboard;
    private Hanger         _hanger;
    private Pickup         _pickup;
    private Shooter        _shooter;
    private Vision         _vision;
    private SettableSwitch _compressor;
    private RobotLog       _log;

    public SubsystemContainer(Drive drive, Ballpath ballpath, Dashboard dashboard, Hanger hanger, Pickup pickup, Shooter shooter, Vision vision, SettableSwitch compressor, RobotLog log)
    {
        _drive      = drive;
        _ballpath   = ballpath;
        _dashboard  = dashboard;
        _hanger     = hanger;
        _pickup     = pickup;
        _shooter    = shooter;
        _vision     = vision;
        _compressor = compressor;
        _log        = log;
    }

    public Drive getDrive()
    {
        return _drive;
    }

    public Ballpath getBallpath()
    {
        return _ballpath;
    }

    public Dashboard getDashboard()
    {
        return _dashboard;
    }

    public Hanger getHanger()
    {
        return _hanger;
    }

    public Pickup getPickup()
    {
        return _pickup;
    }

    public Shooter getShooter()
    {
        return _shooter;
    }

    public Vision getVision()
    {
        return _vision;
    }

    public SettableSwitch getCompressor()
    {
        return _compressor;
    }

    public RobotLog getRobotLog()
    {
        return _log;
    }
}
