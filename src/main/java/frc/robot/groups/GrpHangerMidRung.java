package frc.robot.groups;

import frc.robot.Constants;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Hanger;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;

public class GrpHangerMidRung extends SwartdogSequentialCommandGroup
{

    public GrpHangerMidRung(Hanger hanger, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
    {
        super
        (
            new GrpHangerSetPosition(hanger, shooter, ballpath, pickup, compressor, ArmPosition.MidPull, Constants.Hanger.WINCH_SPEED),
            new GrpHangerSetPosition(hanger, shooter, ballpath, pickup, compressor, ArmPosition.Unhook, Constants.Hanger.UNHOOK_SPEED)
        );
    }
}
