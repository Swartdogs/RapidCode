package frc.robot.subsystems;

import PIDControl.PIDControl;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.abstraction.Solenoid;
import frc.robot.abstraction.SwartdogSubsystem;
import frc.robot.abstraction.Enumerations.ExtendState;

public abstract class Hanger extends SwartdogSubsystem
{
    protected Motor          _armMotor;
    protected Motor          _winchMotor;

    protected Solenoid       _ratchetSolenoid;

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

    public void resetWinchPosition(double position)
    {
        _winchSensor.set(position);
    }

    public double runArmPID()
    {
        return _armPID.calculate(getArmPosition());
    }

    public double runWinchPID()
    {
        return _winchPID.calculate(getWinchPosition());
    }

    public void setArmMotorSpeed(double speed)
    {
        _armMotor.set(speed);
    }

    public void setWinchMotorSpeed(double speed)
    {
        _winchMotor.set(speed);
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

    public void engageRatchet()
    {
        _ratchetSolenoid.extend();
    }

    public void disengageRatchet()
    {
        _ratchetSolenoid.retract();
    }

    public void setRatchetSolenoid(ExtendState state)
    {
        _ratchetSolenoid.set(state);
    }

    public boolean isRatchetEngaged()
    {
        return _ratchetSolenoid.get() == ExtendState.Extended;
    }

    @Override
    public void periodic()
    {
        setArmMotorSpeed(runArmPID());
    }
}
