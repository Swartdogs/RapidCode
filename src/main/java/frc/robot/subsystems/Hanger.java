package frc.robot.subsystems;

import PIDControl.PIDControl;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.abstraction.Solenoid;
import frc.robot.abstraction.SwartdogSubsystem;

public abstract class Hanger extends SwartdogSubsystem
{
    protected Motor          _armMotor;
    protected Motor          _winchMotor;

    protected Solenoid       _hookSolenoid;

    protected PositionSensor _armSensor;
    protected PositionSensor _winchSensor;

    protected PIDControl     _armPID;
    protected PIDControl     _winchPID;

    public void setArmPosition(double position) 
    {
        _armPID.setSetpoint(position, getArmPosition());
    }
    
    public void setWinchPosition(double position) 
    {
        _winchPID.setSetpoint(position, getWinchPosition());
    }

    public double getArmPosition() 
    {
        return _armSensor.get();
    }

    public double getWinchPosition() 
    {
        return _winchSensor.get();
    }

    public boolean armAtPosition() 
    {
        return _armPID.atSetpoint();
    }

    public boolean winchAtPostition() 
    {
        return _winchPID.atSetpoint();
    }

    public void hook()
    {
        _hookSolenoid.extend();
    }

    public void unhook()
    {
        _hookSolenoid.retract();
    }

    @Override
    public void periodic()
    {
        _armMotor.set(_armPID.calculate(getArmPosition()));
        _winchMotor.set(_winchPID.calculate(getWinchPosition()));
    }
}
