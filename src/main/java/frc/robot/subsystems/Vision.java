package frc.robot.subsystems;

import java.util.Arrays;

import PIDControl.PIDControl;
import frc.robot.Constants;
import frc.robot.abstraction.NetworkTableDouble;
import frc.robot.abstraction.SwartdogSubsystem;
import frc.robot.abstraction.Enumerations.State;

public abstract class Vision extends SwartdogSubsystem
{
    protected NetworkTableDouble _xPosition;
    protected NetworkTableDouble _yPosition;
    protected NetworkTableDouble _targetFound;
    protected NetworkTableDouble _ledMode;
    protected NetworkTableDouble _pipeline;

    protected PIDControl         _rotatePID;

    private   double             _limelightAngle    = Constants.Vision.LIMELIGHT_ANGLE;
    private   double             _targetHeightDelta = Constants.Vision.TARGET_HEIGHT - Constants.Vision.LIMELIGHT_HEIGHT;
    private   double             _targetOffset      = Constants.Vision.TARGET_OFFSET;

    private static final int SAMPLE_COUNT = 4;
    private int _sampleIndex = 0;
    private double[] _xSamples = new double[SAMPLE_COUNT];
    private double[] _ySamples = new double[SAMPLE_COUNT];

    public void init()
    {
        // _pipeline.set(Constants.Vision.GRASSHOPPER_PIPELINE);

        // setLEDs(State.Off);
    }

    @Override
    public void periodic()
    {
        _xSamples[_sampleIndex] = _xPosition.get();
        _ySamples[_sampleIndex] = _yPosition.get();

        _sampleIndex = (_sampleIndex + 1) % SAMPLE_COUNT;
    }

    public void setLimelightHeight(double newHeight)
    {
        _targetHeightDelta = Constants.Vision.TARGET_HEIGHT - newHeight;
    }

    public void setLimelightAngle(double newAngle)
    {
        _limelightAngle = newAngle;
    }

    public void setTargetOffset(double newOffset)
    {
        _targetOffset = newOffset;
    }

    public void setRotateDeadband(double newDeadband)
    {
        _rotatePID.setSetpointDeadband(newDeadband);
    }

    public void setLEDs(State state)
    {
        // switch (state)
        // {
        //     case On:
        //         _ledMode.set(Constants.Vision.LIMELIGHT_LED_ON);
        //         break;

        //     case Off:
        //         _ledMode.set(Constants.Vision.LIMELIGHT_LED_OFF);
        //         break;

        //     default:
        //         break;
        // }
    }

    public boolean targetFound()
    {
        return _targetFound.get() > Constants.Vision.LIMELIGHT_TARGET_NOT_FOUND;
    }

    public double getTargetAngle()
    {
        return Arrays.stream(_xSamples).average().getAsDouble();
    }

    public double getTargetDistance()
    {
        double yPosition = Arrays.stream(_ySamples).average().getAsDouble();
        return _targetHeightDelta / 
               Math.tan(Math.toRadians(_limelightAngle + yPosition));
    }

    public void rotateInit(double maxSpeed)
    {
        maxSpeed = Math.abs(maxSpeed);

        double PIDPosition = Math.toRadians(_targetOffset + getTargetAngle());
        PIDPosition /= 2;
        PIDPosition = Math.sin(PIDPosition) * (Math.cos(PIDPosition) / -Math.abs(Math.cos(PIDPosition)));

        _rotatePID.setSetpoint(0, PIDPosition);
        _rotatePID.setOutputRange(-maxSpeed, maxSpeed);
    }

    public double rotateExec()
    {
        double PIDPosition = Math.toRadians(_targetOffset + getTargetAngle());
        PIDPosition /= 2;
        PIDPosition = Math.sin(PIDPosition) * (Math.cos(PIDPosition) / -Math.abs(Math.cos(PIDPosition)));
        return _rotatePID.calculate(PIDPosition);
    }

    public boolean rotateAtSetpoint()
    {
        return _rotatePID.atSetpoint() && targetFound();
    }
}
