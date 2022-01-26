package frc.robot.subsystems;

import PIDControl.PIDControl;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.abstraction.SwartdogSubsystem;

public abstract class Shooter extends SwartdogSubsystem
{
    protected Motor          _shooterMotor;
    protected Motor          _hoodMotor;

    protected PositionSensor _hoodSensor;

    protected PIDControl     _hoodPID;

    @Override
    public void periodic()
    {
        _hoodMotor.set(_hoodPID.calculate(getHoodPosition()));
    }

    public void setShooterMotorSpeed(double speed)
    {
        _shooterMotor.set(speed);
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
        _hoodPID.setSetpoint(position, getHoodPosition());
    }
}