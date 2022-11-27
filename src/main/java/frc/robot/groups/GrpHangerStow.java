package frc.robot.groups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Hanger.ArmPosition;

public class GrpHangerStow extends SequentialCommandGroup
{
    public GrpHangerStow(SubsystemContainer subsystemContainer)
    {
        super
        (
            new GrpHangerSetWinchPosition(subsystemContainer, ArmPosition.Stow.getPosition().getR(), subsystemContainer.getDashboard()::getHangerWinchSpeed),
            new InstantCommand(() -> 
            {
                subsystemContainer.getHanger().useArmPID(true);
                subsystemContainer.getHanger().setArmPosition(ArmPosition.Stow.getPosition().getTheta());
            })
        );
    }
}
