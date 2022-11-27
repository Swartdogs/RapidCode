package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.subsystems.RobotLog;
import frc.robot.subsystems.Hanger;

public class CmdHangerManual extends CommandBase
{
    private Hanger         _hanger;
    private RobotLog       _log;

    private DoubleSupplier _command;

    public CmdHangerManual(SubsystemContainer subsystemContainer, DoubleSupplier command)
    {
        _hanger  = subsystemContainer.getHanger();
        _log     = subsystemContainer.getRobotLog();

        _command = command;
    }

    @Override
    public void execute()
    {
        double speed = _command.getAsDouble() * Constants.Hanger.WINCH_SPEED;

        if ((speed > 0 && _hanger.getWinchPosition() >= Constants.Hanger.MAX_WINCH_LENGTH) ||
            (speed < 0 && _hanger.getWinchPosition() <= Constants.Hanger.MIN_WINCH_LENGTH))
        {
            speed = 0;   
        }

        _hanger.setWinchMotorSpeed(speed);
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setWinchMotorSpeed(0);

        _log.log(String.format("Winched Manually; Final Winch Position: %6.2f", _hanger.getWinchPosition()));
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
