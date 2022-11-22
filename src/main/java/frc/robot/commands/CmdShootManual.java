package frc.robot.commands;

import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.RobotLog;
import frc.robot.subsystems.Shooter;

public class CmdShootManual extends SwartdogCommand 
{
    private Shooter        _shooter;
    private Ballpath       _ballpath;
    private Pickup         _pickup;
    private RobotLog       _log;
    private SettableSwitch _compressor;
    private RobotPosition  _robotPosition;
    private TargetPosition _targetPosition;
    private boolean        _atSpeed;

    public CmdShootManual(SubsystemContainer subsystemContainer, RobotPosition robotPosition, TargetPosition targetPosition) 
    {
        _shooter        = subsystemContainer.getShooter();
        _ballpath       = subsystemContainer.getBallpath();
        _pickup         = subsystemContainer.getPickup();
        _log            = subsystemContainer.getRobotLog();
        _compressor     = subsystemContainer.getCompressor();
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

        _log.log(String.format("Starting Manual Shot; Ball Count: %d, Robot Position: %s, Target Position: %s", _ballpath.getCargoCount(), _robotPosition.toString(), _targetPosition.toString()));
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
            _log.log(String.format("Shots fired! (no vision) New Ball Count: %d, Expected RPM: %6.0f, Current RPM: %6.0f, Expected Hood Angle: %6.0f, Current Hood Angle: %6.0f", _ballpath.getCargoCount(), _shooter.getShooterTargetRPM(), _shooter.getShooterRPM(), _shooter.getHoodSetpoint(), _shooter.getHoodPosition()));

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

        _log.log("Shoot Manual complete.");
    }

    @Override
    public boolean isFinished() 
    {
        return _ballpath.getCargoCount() <= 0;
    }
}
