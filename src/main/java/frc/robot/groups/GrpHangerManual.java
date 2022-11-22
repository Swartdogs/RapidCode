package frc.robot.groups;

import java.util.function.DoubleSupplier;

import frc.robot.SubsystemContainer;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.commands.CmdHangerManual;
import frc.robot.commands.CmdHangerWinchDownUntilReleased;
import frc.robot.subsystems.Hanger;

public class GrpHangerManual extends SwartdogSequentialCommandGroup
{
    private Hanger _hanger;

    public GrpHangerManual(SubsystemContainer subsystemContainer, DoubleSupplier command)
    {
        super
        (
            new CmdHangerWinchDownUntilReleased(subsystemContainer),
            new CmdHangerManual(subsystemContainer, command)
        );

        _hanger = subsystemContainer.getHanger();
    }

    @Override
    public void initialize()
    {
        _hanger.disengageRatchet();

        super.initialize();
    }

    @Override
    public void end(boolean interrupted)
    {
        super.end(interrupted);

        _hanger.engageRatchet();
    }
}
