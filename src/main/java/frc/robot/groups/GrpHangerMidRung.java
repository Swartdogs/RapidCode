package frc.robot.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Hanger.ArmPosition;

public class GrpHangerMidRung extends SequentialCommandGroup
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
