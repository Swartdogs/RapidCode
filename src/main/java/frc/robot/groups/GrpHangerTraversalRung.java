

package frc.robot.groups;

import java.util.function.BooleanSupplier;

import frc.robot.Constants;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.CmdWaitForConfirm;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Hanger;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;

public class GrpHangerTraversalRung extends SwartdogSequentialCommandGroup
{
    public GrpHangerTraversalRung(Hanger hanger, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor, BooleanSupplier confirm)
    {
        super
        (
            new GrpHangerSetPosition( hanger,  shooter,  ballpath,  pickup,  compressor, ArmPosition.Reach, Constants.Hanger.WINCH_SPEED),
            new GrpHangerSetPosition( hanger,  shooter,  ballpath,  pickup,  compressor, ArmPosition.Hook, Constants.Hanger.WINCH_SPEED),
            new CmdWaitForConfirm(confirm),
            new GrpHangerSetPosition( hanger,  shooter,  ballpath,  pickup,  compressor, ArmPosition.TraversePull, Constants.Hanger.WINCH_SPEED)
        );
    }
}
