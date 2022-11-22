package frc.robot.groups;

import frc.robot.SubsystemContainer;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;

public class GrpHangerStow extends SwartdogSequentialCommandGroup
{
    public GrpHangerStow(SubsystemContainer subsystemContainer)
    {
        super
        (
            new GrpHangerSetWinchPosition(subsystemContainer, ArmPosition.Stow.getPosition().getR(), subsystemContainer.getDashboard()::getHangerWinchSpeed),
            SwartdogCommand.run(() -> 
            {
                subsystemContainer.getHanger().useArmPID(true);
                subsystemContainer.getHanger().setArmPosition(ArmPosition.Stow.getPosition().getTheta());
            })
        );
    }
}
