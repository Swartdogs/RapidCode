package frc.robot.groups;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.commands.CmdWaitForCondition;

public class GrpHangerHighRung extends SequentialCommandGroup
{
    public GrpHangerHighRung(SubsystemContainer subsystemContainer, BooleanSupplier confirm)
    {
        super
        (
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.Reach, subsystemContainer.getDashboard()::getHangerWinchSpeed),
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.Hook, subsystemContainer.getDashboard()::getHangerWinchSpeed),
            new CmdWaitForCondition(confirm),
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.HighPull, subsystemContainer.getDashboard()::getHangerHighPullSpeed),
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.Unhook, subsystemContainer.getDashboard()::getHangerUnhookSpeed)
        );
    }
}
