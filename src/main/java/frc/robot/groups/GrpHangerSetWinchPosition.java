package frc.robot.groups;

import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.commands.CmdHangerSetWinchPosition;
import frc.robot.commands.CmdHangerWinchDownUntilReleased;
import frc.robot.subsystems.Hanger;

public class GrpHangerSetWinchPosition extends SwartdogSequentialCommandGroup
{
    private Hanger _hanger;

    public GrpHangerSetWinchPosition(Hanger hanger, double position, double winchSpeed)
    {
        super
        (
            new CmdHangerWinchDownUntilReleased(hanger),
            new CmdHangerSetWinchPosition(hanger, position, winchSpeed)
        );

        _hanger = hanger;
    }

    @Override
    public void initialize()
    {
        _hanger.disengageRatchet();
        
        super.initialize();
    }

    @Override
    public void end(boolean interrupted)
    {
        super.end(interrupted);

        _hanger.engageRatchet();
    }
}
