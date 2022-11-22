package frc.robot.commands;

import java.util.function.DoubleSupplier;

import frc.robot.SubsystemContainer;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.Hanger;
import frc.robot.subsystems.RobotLog;

public class CmdHangerModifyArmPosition extends SwartdogCommand
{
    private Hanger         _hanger;
    private RobotLog       _log;

    private DoubleSupplier _modify;
    private double         _currentModifier;
    
    public CmdHangerModifyArmPosition(SubsystemContainer subsystemContainer, DoubleSupplier modify)
    {
        _hanger = subsystemContainer.getHanger();
        _log    = subsystemContainer.getRobotLog();

        _modify = modify;
    }

    @Override
    public void initialize()
    {
        _currentModifier = _modify.getAsDouble();
        _hanger.setArmPosition(_hanger.getArmTarget() + _currentModifier);

        _log.log(String.format("Starting Hanger Modify Arm Position; Current Arm Position: %6.2f, Modifier: %6.2f", _hanger.getArmPosition(), _currentModifier));
    }

    @Override
    public void end(boolean interrupted)
    {
        _hanger.setArmPosition(_hanger.getArmTarget() - _currentModifier);

        _log.log(String.format("Hanger Modify Arm Position complete; Arm Position: %6.2f", _hanger.getArmPosition()));
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
