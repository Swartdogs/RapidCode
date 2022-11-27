package frc.robot.groups;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.commands.CmdWaitForCondition;

public class GrpHangerTraversalRung extends SequentialCommandGroup
{
    public GrpHangerTraversalRung(SubsystemContainer subsystemContainer, BooleanSupplier confirm)
    {
        super
        (
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.TraversalReach, subsystemContainer.getDashboard()::getHangerWinchSpeed),
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.TraversalHook, subsystemContainer.getDashboard()::getHangerWinchSpeed),
            new CmdWaitForCondition(confirm),
            new InstantCommand(()->{subsystemContainer.getPickup().stow();}),
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.TraversalPull, subsystemContainer.getDashboard()::getHangerWinchSpeed)
        );
    }
}
