package frc.robot.commands;

import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;

public class CmdShootManual extends SwartdogCommand 
{
    private Shooter        _shooter;
    private Ballpath       _ballpath;
    private Pickup         _pickup;
    private SettableSwitch _compressor;
    private RobotPosition  _robotPosition;
    private TargetPosition _targetPosition;
    private boolean        _atSpeed;

    public CmdShootManual(Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor, RobotPosition robotPosition, TargetPosition targetPosition) 
    {
        _shooter        = shooter;
        _ballpath       = ballpath;
        _pickup         = pickup;
        _compressor     = compressor;
        _robotPosition  = robotPosition;
        _targetPosition = targetPosition;
        _atSpeed        = false;

        addRequirements(_shooter);
    }

    @Override
    public void initialize() 
    {
        _atSpeed = false;
        
        if (_ballpath.getCargoCount() > 0) 
        {
            _compressor.set(State.Off);

            _shooter.setShooterMotorSpeed(_robotPosition.getShooterRPM(_targetPosition));
            _shooter.setHoodPosition(_robotPosition.getHoodPosition(_targetPosition));            
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

            _atSpeed = !_robotPosition.useWait(_targetPosition);
        }

        if (_atSpeed)
        {
            _ballpath.shoot(_robotPosition, _targetPosition);
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
        _compressor.set(State.On);

        _shooter.setHoodPosition(RobotPosition.NearLaunchpad.getHoodPosition(TargetPosition.UpperHub));
    }

    @Override
    public boolean isFinished() 
    {
        return _ballpath.getCargoCount() <= 0;
    }
}
