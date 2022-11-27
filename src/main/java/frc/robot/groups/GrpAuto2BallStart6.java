package frc.robot.groups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdWait;
import frc.robot.commands.CmdWaitAuto;
import frc.robot.drive.commands.CmdDriveToPosition;
import frc.robot.drive.Vector;

public class GrpAuto2BallStart6 extends SequentialCommandGroup
{
    public GrpAuto2BallStart6(SubsystemContainer subsystemContainer)
    {
        super
        (
            new InstantCommand(() -> 
            {
                subsystemContainer.getDrive().resetOdometer(Constants.Drive.FIELD_RESET_POSITION);
                subsystemContainer.getDrive().setGyro(Constants.Drive.FIELD_ANGLE);
            }),

            new CmdWaitAuto(subsystemContainer),
            new CmdShootManual(subsystemContainer, RobotPosition.Fender, TargetPosition.LowerHub),
            new CmdPickupDeploy(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, Constants.Drive.FIELD_RESET_POSITION.add(new Vector(-60, -80)), -115, 0.5, 0.5, 0, true),
            new CmdWait(1.5),
            new CmdPickupStow(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, Constants.Drive.FIELD_RESET_POSITION.add(new Vector(-6, -6)), 25, 0.5, 0.5, 0, true),
            new CmdShootManual(subsystemContainer, RobotPosition.Fender, TargetPosition.LowerHub)
        );
    }
}
