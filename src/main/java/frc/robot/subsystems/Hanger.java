package frc.robot.subsystems;

import PIDControl.PIDControl;
import frc.robot.Constants;
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

    private   double         _armTarget;

    private   boolean        _useArmPID   = true;

    public void setArmPosition(double position) 
    {        
        _armTarget = position;
    }

    public double getArmTarget()
    {
        return _armTarget;
    }

    public double getCurrentPositionTarget()
    {
        return getArmPosition() - getWinchAngle();
    }
    
    public void winchInit(double position, double speed)
    {
        _winchPID.setOutputRange(-speed, speed);
        _winchPID.setSetpoint(position, getWinchPosition());
    }

    public void useArmPID(boolean use)
    {
        _useArmPID = use;
    }

    public boolean useArmPID()
    {
        return _useArmPID;
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
        double winchAngle = getWinchAngle();

        _armPID.setSetpoint(_armTarget + winchAngle, getArmPosition());

        if (_useArmPID)
        {
            setArmMotorSpeed(runArmPID());
        }
        else
        {
            setArmMotorSpeed(0);
        }
    }

    private double getWinchAngle()
    {
        double winchLength = _winchSensor.get();

        return Math.toDegrees(Math.acos(clamp((winchLength * Constants.Hanger.WINCH_ANGLE_DENOMINATOR) + ((1.0 / winchLength) * Constants.Hanger.WINCH_FACTOR), -1, 1)));
    }

    private double clamp(double value, double min, double max)
    {
        return Math.max(min, Math.min(max, value));
    }
}
