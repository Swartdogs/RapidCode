package frc.robot.groups;

import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.commands.CmdDriveRotate;
import frc.robot.commands.CmdDriveToPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdShootWithVision;
import frc.robot.commands.CmdWait;
import frc.robot.commands.CmdWaitAuto;
import frc.robot.subsystems.drive.Vector;

public class GrpAuto2BallHighStart6 extends SwartdogSequentialCommandGroup
{
    public GrpAuto2BallHighStart6(SubsystemContainer subsystemContainer)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                subsystemContainer.getDrive().resetOdometer(Constants.Drive.FIELD_RESET_POSITION);
                subsystemContainer.getDrive().setGyro(Constants.Drive.FIELD_ANGLE);
            }),

            new CmdWaitAuto(subsystemContainer),
            new CmdShootManual(subsystemContainer, RobotPosition.Fender, TargetPosition.UpperHub),
            new CmdPickupDeploy(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, Constants.Drive.FIELD_RESET_POSITION.add(new Vector(-60, -80)), -115, 0.5, 0.5, 0, true),
            new CmdWait(1.5),
            new CmdPickupStow(subsystemContainer),
            new CmdDriveRotate(subsystemContainer, 21, 0.5, true),
            new CmdShootWithVision(subsystemContainer)
        );
    }
}
