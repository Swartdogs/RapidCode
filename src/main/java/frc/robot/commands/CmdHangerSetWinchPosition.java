package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.SubsystemContainer;
import frc.robot.subsystems.Hanger;
import frc.robot.subsystems.RobotLog;

public class CmdHangerSetWinchPosition extends CommandBase
{
    private Hanger         _hanger;
    private double         _position;
    private DoubleSupplier _winchSpeed;
    private RobotLog       _log;
    
    public CmdHangerSetWinchPosition(SubsystemContainer subsystemContainer, double position, DoubleSupplier winchSpeed)
    {
        _hanger     = subsystemContainer.getHanger(); 
        _position   = position;
        _winchSpeed = winchSpeed;
        _log        = subsystemContainer.getRobotLog();
    }

    @Override
    public void initialize()
    {
        _hanger.winchInit(_position, _winchSpeed.getAsDouble());

        _log.log(String.format("Starting Winch To Position; Target Position: %6.2f", _position));
    }

    @Override
    public void execute()
    {
        _hanger.setWinchMotorSpeed(_hanger.runWinchPID());
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setWinchMotorSpeed(0);
        _log.log(String.format("Winch To Position complete; Actual Position: %6.2f", _hanger.getWinchPosition()));
    }

    @Override
    public boolean isFinished()
    {
        return _hanger.winchAtPostition();
    }
}
