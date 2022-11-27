package frc.robot.groups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SubsystemContainer;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootWithVision;
import frc.robot.commands.CmdWait;
import frc.robot.commands.CmdWaitAuto;
import frc.robot.drive.commands.CmdDriveRotate;
import frc.robot.drive.commands.CmdDriveToPosition;
import frc.robot.drive.*;

public class GrpAuto2BallHighStart5 extends SequentialCommandGroup
{
    private static final double START_ANGLE    = -126;
    private static final double CARGO_ANGLE    = -140;

    private static final Vector START_POSITION = new Vector(-64,  -64);
    private static final Vector CARGO_POSITION = new Vector(-88, -139);

    public GrpAuto2BallHighStart5(SubsystemContainer subsystemContainer)
    {

        super
        (
            new InstantCommand(() -> 
            {
                subsystemContainer.getDrive().resetOdometer(START_POSITION);
                subsystemContainer.getDrive().setGyro(START_ANGLE);
            }),

            new CmdWaitAuto(subsystemContainer),
            new CmdPickupDeploy(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, CARGO_POSITION, CARGO_ANGLE, 0.5, 0.5, 0, true),
            new CmdWait(1.5),
            new CmdPickupStow(subsystemContainer),
            new CmdDriveRotate(subsystemContainer, 26, 0.5, true),
            new CmdShootWithVision(subsystemContainer)
        ); 
    }
}
