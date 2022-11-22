package frc.robot.groups;

import frc.robot.SubsystemContainer;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;

public class GrpHangerMidRung extends SwartdogSequentialCommandGroup
{

    public GrpHangerMidRung(SubsystemContainer subsystemContainer)
    {
        super
        (
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.MidPull, subsystemContainer.getDashboard()::getHangerWinchSpeed),
            new GrpHangerSetPosition(subsystemContainer, ArmPosition.Unhook, subsystemContainer.getDashboard()::getHangerUnhookSpeed)
        );
    }
}
