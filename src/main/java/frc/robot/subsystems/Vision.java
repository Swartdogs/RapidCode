package frc.robot.subsystems;

import PIDControl.PIDControl;
import frc.robot.Constants;
import frc.robot.abstraction.NetworkTableBoolean;
import frc.robot.abstraction.NetworkTableDouble;
import frc.robot.abstraction.Solenoid;
import frc.robot.abstraction.SwartdogSubsystem;
import frc.robot.abstraction.Enumerations.State;

public abstract class Vision extends SwartdogSubsystem
{
    protected NetworkTableDouble  _xPosition;
    protected NetworkTableDouble  _yPosition;
    protected NetworkTableBoolean _targetFound;
    protected Solenoid            _lightRing;
    protected PIDControl          _rotatePID;

    public void setLEDs(State state)
    {
        switch (state)
        {
            case On:
                _lightRing.extend();
                break;

            case Off:
                _lightRing.retract();
                break;

            default:
                break;
        }
    }

    public boolean targetFound()
    {
        return _targetFound.get();
    }

    public double getTargetAngle()
    {
        return _xPosition.get();
    }

    public double getTargetDistance()
    {
        return Constants.Vision.HEIGHT_DELTA / 
               Math.tan(Math.toRadians(Constants.Vision.CAMERA_ANGLE + _yPosition.get()));
    }

    public void rotateInit()
    {
        _rotatePID.setSetpoint(0, getTargetAngle());
    }

    public double rotateExec()
    {
        return _rotatePID.calculate(getTargetAngle());
    }

    public boolean rotateIsFinished()
    {
        return _rotatePID.atSetpoint();
    }
}
