package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;

public class CmdShootManual extends SwartdogCommand 
{
    private Shooter       _shooter;
    private Ballpath      _ballpath;
    private Pickup        _pickup;
    private ShootPosition _position;
    private boolean       _atSpeed;

    public CmdShootManual(Shooter shooter, Ballpath ballpath, Pickup pickup, ShootPosition position) 
    {
        _shooter  = shooter;
        _ballpath = ballpath;
        _position = position;
        _pickup   = pickup;
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

                case FenderLowGoal:
                    _shooter.setShooterMotorSpeed(Constants.Shooter.FENDER_LOW_GOAL_SHOOTER_RPM);
                    _shooter.setHoodPosition(Constants.Shooter.FENDER_LOW_GOAL_HOOD_POSITION);
                    break;
            }
        }
    }

    @Override
    public void execute() 
    {
        /* 
         * The "_atSpeed" variable is used to prevent a problem where the cargo would slow the wheel down,
         * causing the ballpath to shut off while the cargo is contacting the wheel.
         */
        if (_shooter.isShooterReady()) 
        {
            _atSpeed = true;
        }
        
        if (_ballpath.hasShooterSensorTransitionedTo(State.Off))
        {
            _ballpath.modifyCargoCount(-1);

            if (_position != ShootPosition.FenderLowGoal)
            {
                _atSpeed = false;
            } 
        }

        if (_atSpeed)
        {
            _ballpath.shoot();
            _pickup.startMotor();
        }
        else
        {
            _ballpath.stop();
            _pickup.stopMotor();
        }
    }

    @Override
    public void end(boolean interrupted) 
    {
        _shooter.setShooterMotorSpeed(0);
        _ballpath.stop();
        _pickup.stopMotor();

        _shooter.setHoodPosition(Constants.Shooter.NEAR_LAUNCHPAD_HOOD_POSITION);
    }

    @Override
    public boolean isFinished() 
    {
        return _ballpath.getCargoCount() <= 0;
    }
}
