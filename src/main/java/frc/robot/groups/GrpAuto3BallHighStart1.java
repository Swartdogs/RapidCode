package frc.robot.groups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.commands.*;
import frc.robot.drive.commands.CmdDriveRotate;
import frc.robot.drive.commands.CmdDriveToPosition;
import frc.robot.drive.*;

public class GrpAuto3BallHighStart1 extends SequentialCommandGroup
{
    private static final double START_ANGLE      = -90;
    private static final double SHOOT_ANGLE      = -40;
    private static final double CARGO_1_ANGLE    = 125;
    private static final double CARGO_2_ANGLE    = -165;

    private static final Vector START_POSITION   = new Vector(90, 3);
    private static final Vector CARGO_1_POSITION = new Vector(137, -32);
    private static final Vector CARGO_2_POSITION = new Vector(86, -131);

    public GrpAuto3BallHighStart1(SubsystemContainer subsystemContainer)
    {
        super
        (
            new InstantCommand(() -> 
            {
                subsystemContainer.getDrive().resetOdometer(START_POSITION);
                subsystemContainer.getDrive().setGyro(START_ANGLE);
            }),
            
            new CmdWaitAuto(subsystemContainer),
            new CmdShootManual(subsystemContainer, RobotPosition.Position1, TargetPosition.UpperHub),
            new CmdPickupDeploy(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, CARGO_1_POSITION, CARGO_1_ANGLE, 0.5, 0.37, 0, true),
            new CmdWait(0.25),
            new CmdDriveToPosition(subsystemContainer, CARGO_2_POSITION, CARGO_2_ANGLE, 0.45, 0.48, 0, true),
            new CmdWait(0.25),
            new CmdPickupStow(subsystemContainer),
            new CmdDriveRotate(subsystemContainer, SHOOT_ANGLE, 0.5, true),
            new CmdShootManual(subsystemContainer, RobotPosition.CargoRing, TargetPosition.UpperHub) 
        );
    }
}
