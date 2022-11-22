package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.*;
import frc.robot.abstraction.Switch.SettableSwitch;

public class CmdShootWithVision extends SwartdogCommand
{
    private Drive          _drive;
    private Vision         _vision;
    private Pickup         _pickup;
    private Ballpath       _ballpath;
    private Shooter        _shooter;
    private SettableSwitch _compressor;
    private RobotLog       _log;

    private boolean        _atSpeed;

    public CmdShootWithVision(SubsystemContainer subsystemContainer)
    {
        _drive      = subsystemContainer.getDrive();
        _vision     = subsystemContainer.getVision();
        _pickup     = subsystemContainer.getPickup();
        _ballpath   = subsystemContainer.getBallpath();
        _shooter    = subsystemContainer.getShooter();
        _compressor = subsystemContainer.getCompressor();
        _log        = subsystemContainer.getRobotLog();

        _atSpeed    = false;
    }

    @Override
    public void initialize()
    {
        _atSpeed = false;

        if (_ballpath.getCargoCount() > 0)
        {
            double distance = _vision.getTargetDistance();

            _compressor.set(State.Off);
            _vision.setLEDs(State.On);
    
            _shooter.setShooterMotorSpeed(Constants.Shooter.SHOOTER_SPEED_LOOKUP.applyAsDouble(distance));
            _shooter.setHoodPosition(Constants.Shooter.SHOOTER_HOOD_LOOKUP.applyAsDouble(distance));

            _vision.rotateInit(Constants.Drive.ALIGN_ROTATE_SPEED);
            _drive.drive(0, 0, 0);

            _log.log(String.format("Starting Vision Shot; Ball Count: %d, Vision Distance: %6.2f, Vision Angle: %6.2f", _ballpath.getCargoCount(), distance, _vision.getTargetAngle()));
        }
    }

    @Override
    public void execute()
    {
        if (_ballpath.getCargoCount() > 0)
        {
            _vision.setLEDs(State.On);

            boolean onTarget = _vision.rotateAtSetpoint();
            double  distance = _vision.getTargetDistance();
            
            if (_vision.targetFound())
            {
                _drive.drive(0, 0, _vision.rotateExec());
            }
            else
            {
                _drive.drive(0, 0, 0); // TODO: Actually drive when camera doesn't see target
            }

            _shooter.setShooterMotorSpeed(Constants.Shooter.SHOOTER_SPEED_LOOKUP.applyAsDouble(distance));
            _shooter.setHoodPosition(Constants.Shooter.SHOOTER_HOOD_LOOKUP.applyAsDouble(distance));

            if (_shooter.isShooterReady() && onTarget)
            {
                _atSpeed = true;
            }

            if (!onTarget)
            {
                _atSpeed = false;
            }

            if (_ballpath.hasShooterSensorTransitionedTo(State.Off))
            {
                _ballpath.modifyCargoCount(-1);
                _log.log(String.format("Shots fired! New Ball Count: %d, Expected RPM: %6.0f, Current RPM: %6.0f, Expected Hood Angle: %6.0f, Current Hood Angle: %6.0f, Distance: %6.2f, Angle: %6.2f", _ballpath.getCargoCount(), _shooter.getShooterTargetRPM(), _shooter.getShooterRPM(), _shooter.getHoodSetpoint(), _shooter.getHoodPosition(), _vision.getTargetDistance(), _vision.getTargetAngle()));
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
    }

    @Override
    public void end(boolean interrupted)
    {
        _shooter.setShooterMotorSpeed(0);
        _shooter.setHoodPosition(RobotPosition.NearLaunchpad.getHoodPosition(TargetPosition.UpperHub));

        _ballpath.stop();
        _pickup.stopMotor();

        _vision.setLEDs(State.Off);
        _compressor.set(State.On);

        _drive.drive(0, 0, 0);

        _log.log("Shoot with Vision Complete.");
    }

    @Override
    public boolean isFinished()
    {
        return _ballpath.getCargoCount() <= 0;
    }
}
