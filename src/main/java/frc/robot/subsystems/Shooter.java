package frc.robot.subsystems;

import PIDControl.PIDControl;
import frc.robot.Constants;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.abstraction.SwartdogSubsystem;

public abstract class Shooter extends SwartdogSubsystem
{
    protected Motor          _shooterMotor;
    protected Motor          _hoodMotor;

    protected PositionSensor _hoodSensor;

    protected PIDControl     _hoodPID;

    private   double         _hoodSetpoint;

    private   double         _shooterRPMThreshold = Constants.Shooter.SHOOTER_RPM_THRESHOLD;
    private   double         _maxHoodPosition     = Constants.Shooter.HOOD_MAX_POSITION;
    private   double         _minHoodPosition     = Constants.Shooter.HOOD_MIN_POSITION;

    @Override
    public void periodic()
    {
        _hoodMotor.set(_hoodPID.calculate(getHoodPosition()));      
    }

    public void setRPMThreshold(double newThreshold)
    {
        _shooterRPMThreshold = newThreshold;
    }

    public void setHoodMaxPosition(double newMax)
    {
        _maxHoodPosition = newMax;
        _hoodPID.setInputRange(_minHoodPosition, _maxHoodPosition);
    }

    public void setHoodMinPosition(double newMin)
    {
        _minHoodPosition = newMin;
        _hoodPID.setInputRange(_minHoodPosition, _maxHoodPosition);
    }

    public void setShooterMotorSpeed(double speed)
    {
        _shooterMotor.set(speed);
    }

    public double getShooterRPM()
    {
        return _shooterMotor.getVelocitySensor().get();
    }

    public double getShooterTargetRPM()
    {
        return _shooterMotor.get();
    }

    public boolean isShooterReady()
    {
        double actual  = _shooterMotor.getVelocitySensor().get();
        double target  = _shooterMotor.get();

        return (actual >= (1 - _shooterRPMThreshold) * target) && 
               (actual <= (1 + _shooterRPMThreshold) * target) &&
               (target > 0)                                                       &&
               (_hoodPID.atSetpoint());
    }

    // Hood
    public double getHoodPosition()
    {
        return _hoodSensor.get();
    }

    public boolean hoodAtPosition()
    {
        return _hoodPID.atSetpoint();
    }

    public void setHoodPosition(double position)
    {
        _hoodSetpoint = position;
        _hoodPID.setSetpoint(_hoodSetpoint, getHoodPosition());
    }

    public double getHoodSetpoint()
    {
        return _hoodSetpoint;
    }
}
