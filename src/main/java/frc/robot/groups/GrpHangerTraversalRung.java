package frc.robot.groups;

import java.util.function.BooleanSupplier;

import frc.robot.SubsystemContainer;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.commands.CmdWaitForCondition;

public class GrpHangerTraversalRung extends SwartdogSequentialCommandGroup
{
    public GrpHangerTraversalRung(SubsystemContainer subsystemContainer, BooleanSupplier confirm)
    {
        super
        (
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.TraversalReach, subsystemContainer.getDashboard()::getHangerWinchSpeed),
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.TraversalHook, subsystemContainer.getDashboard()::getHangerWinchSpeed),
            new CmdWaitForCondition(confirm),
            SwartdogCommand.run(()->{subsystemContainer.getPickup().stow();}),
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.TraversalPull, subsystemContainer.getDashboard()::getHangerWinchSpeed)
        );
    }
}
