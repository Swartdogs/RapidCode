package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.Constants;
import frc.robot.abstraction.NetworkTableDouble;
import frc.robot.subsystems.Vision;

public class HardwareVision extends Vision
{
    public HardwareVision()
    {
        _xPosition   = NetworkTableDouble.networkTableDouble("limelight", "tx");
        _yPosition   = NetworkTableDouble.networkTableDouble("limelight", "ty");
        _targetFound = NetworkTableDouble.networkTableDouble("limelight", "tv");
        _ledMode     = NetworkTableDouble.networkTableDouble("limelight", "ledMode");
        _pipeline    = NetworkTableDouble.networkTableDouble("limelight", "pipeline");

        _rotatePID   = new PIDControl();

        _rotatePID.setCoefficient(Coefficient.P, Math.sin(Math.toRadians(0)), 0.9, 0.6);
        _rotatePID.setCoefficient(Coefficient.I, Math.sin(Math.toRadians(8)), 0, 0.025);
        _rotatePID.setCoefficient(Coefficient.D, 0, 0, 0);
        _rotatePID.setInputRange(-1.0, 1.0);
        _rotatePID.setOutputRamp(0.1, 0.05);
        _rotatePID.setSetpointDeadband(Math.sin(Math.toRadians(Constants.Vision.ROTATE_DEADBAND)));

        init();
    }
}
