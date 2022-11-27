package frc.robot.groups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdWait;
import frc.robot.commands.CmdWaitAuto;
import frc.robot.drive.commands.CmdDriveRotate;
import frc.robot.drive.commands.CmdDriveToPosition;
import frc.robot.drive.*;

public class GrpAuto2BallHighStart2 extends SequentialCommandGroup
{
    private static final double START_ANGLE    = 156;

    private static final Vector START_POSITION = new Vector(59, -59);
    private static final Vector CARGO_POSITION = new Vector(72, START_ANGLE, false);

    public GrpAuto2BallHighStart2(SubsystemContainer subsystemContainer)
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
            new CmdDriveToPosition(subsystemContainer, START_POSITION.add(CARGO_POSITION), START_ANGLE, 0.5, 0.5, 0, true),
            new CmdWait(1),
            new CmdPickupStow(subsystemContainer),
            new CmdDriveRotate(subsystemContainer, -40, 0.5, true),
            new CmdShootManual(subsystemContainer, RobotPosition.CargoRing, TargetPosition.UpperHub)
        );
    }
}
