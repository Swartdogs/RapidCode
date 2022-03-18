package frc.robot.subsystems;

import PIDControl.PIDControl;
import frc.robot.Constants;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.abstraction.SwartdogSubsystem;

public abstract class Shooter extends SwartdogSubsystem
{
    public Motor          _shooterMotor;
    protected Motor          _hoodMotor;

    protected PositionSensor _hoodSensor;

    protected PIDControl     _hoodPID;

    private   double         _hoodSetpoint;

    @Override
    public void periodic()
    {
        _hoodMotor.set(_hoodPID.calculate(getHoodPosition()));      
    }

    public void setShooterMotorSpeed(double speed)
    {
        _shooterMotor.set(speed);
    }

    public boolean isShooterReady()
    {
        double actual  = _shooterMotor.getVelocitySensor().get();
        double target  = _shooterMotor.get();

        return (actual >= (1 - Constants.Shooter.SHOOTER_RPM_THRESHOLD) * target) && 
               (actual <= (1 + Constants.Shooter.SHOOTER_RPM_THRESHOLD) * target) &&
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
