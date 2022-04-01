package frc.robot.groups;

import java.util.function.DoubleSupplier;

import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.commands.CmdHangerManual;
import frc.robot.commands.CmdHangerWinchDownUntilReleased;
import frc.robot.subsystems.Hanger;

public class GrpHangerManual extends SwartdogSequentialCommandGroup
{
    private Hanger _hanger;

    public GrpHangerManual(Hanger hanger, DoubleSupplier command)
    {
        super
        (
            new CmdHangerWinchDownUntilReleased(hanger),
            new CmdHangerManual(hanger, command)
        );

        _hanger = hanger;
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
