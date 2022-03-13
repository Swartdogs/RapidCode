package frc.robot.subsystems;

import PIDControl.PIDControl;
import frc.robot.abstraction.Motor;
import frc.robot.abstraction.PositionSensor;
import frc.robot.abstraction.Solenoid;
import frc.robot.abstraction.SwartdogSubsystem;
import frc.robot.abstraction.Switch;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.abstraction.Enumerations.State;

public abstract class Hanger extends SwartdogSubsystem
{
    protected Motor          _armMotor;
    protected Motor          _winchMotor;

    protected Solenoid       _hookSolenoid;
    protected Solenoid       _ratchetSolenoid;

    protected PositionSensor _armSensor;
    protected PositionSensor _winchSensor;

    protected PIDControl     _armPID;
    protected PIDControl     _winchPID;

    protected Switch         _leftSwitch;
    protected Switch         _rightSwitch;

    private State            _currentLeftSwitchState;
    private State            _lastLeftSwitchState;

    private State            _currentRightSwitchState;
    private State            _lastRightSwitchState;

    public void setArmPosition(double position) 
    {
        _armPID.setSetpoint(position, getArmPosition());
    }
    
    public void setWinchPosition(double position) 
    {
        _winchPID.setSetpoint(position, getWinchPosition());
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

    public void hook()
    {
        _hookSolenoid.extend();
    }

    public void unhook()
    {
        _hookSolenoid.retract();
    }

    public void setHookSolenoid(ExtendState state)
    {
        _hookSolenoid.set(state);
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

    public State getLeftSwitchState()
    {
        return _currentLeftSwitchState;
    }

    public boolean hasLeftSwitchTransitionedTo(State state)
    {
        return _currentLeftSwitchState == state && _lastLeftSwitchState != state;
    }

    public State getRightSwitchState()
    {
        return _currentRightSwitchState;
    }

    public boolean hasRightSwitchTransitionedTo(State state)
    {
        return _currentRightSwitchState == state && _lastRightSwitchState != state;
    }

    public boolean isHooked()
    {
        return _hookSolenoid.get() == ExtendState.Extended;
    }

    public boolean isRatchetEngaged()
    {
        return _ratchetSolenoid.get() == ExtendState.Extended;
    }
    
    @Override
    public void periodic()
    {
        _lastLeftSwitchState     = _currentLeftSwitchState;
        _lastRightSwitchState    = _currentRightSwitchState;

        _currentLeftSwitchState  = _leftSwitch.get();
        _currentRightSwitchState = _rightSwitch.get();
    }
}
