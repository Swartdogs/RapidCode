package frc.robot.groups;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SubsystemContainer;
import frc.robot.commands.CmdHangerSetWinchPosition;
import frc.robot.commands.CmdHangerWinchDownUntilReleased;
import frc.robot.subsystems.Hanger;

public class GrpHangerSetWinchPosition extends SequentialCommandGroup
{
    private Hanger _hanger;

    public GrpHangerSetWinchPosition(SubsystemContainer subsystemContainer, double position, DoubleSupplier winchSpeed)
    {
        super
        (
            new CmdHangerWinchDownUntilReleased(subsystemContainer),
            new CmdHangerSetWinchPosition(subsystemContainer, position, winchSpeed)
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
