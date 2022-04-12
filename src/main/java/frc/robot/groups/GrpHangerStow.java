package frc.robot.groups;

import frc.robot.Constants;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.subsystems.Hanger;

public class GrpHangerStow extends SwartdogSequentialCommandGroup
{
    public GrpHangerStow(Hanger hanger)
    {
        super
        (
            new GrpHangerSetWinchPosition(hanger, ArmPosition.Stow.getPosition().getR(), Constants.Hanger.WINCH_SPEED),
            SwartdogCommand.run(() -> 
            {
                hanger.useArmPID(true);
                hanger.setArmPosition(ArmPosition.Stow.getPosition().getTheta());
            })
        );
    }
}
