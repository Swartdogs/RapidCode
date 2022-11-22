package frc.robot.groups;

import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.*;
import frc.robot.commands.*;
import frc.robot.subsystems.drive.*;

public class GrpAuto4BallStart1 extends SwartdogSequentialCommandGroup
{
    private static final double START_ANGLE       = -90;
    private static final double SHOOT_ANGLE       = -40;
    private static final double CARGO_1_ANGLE     = 125;
    private static final double CARGO_2_ANGLE     = -165;
    private static final double TERMINAL_ANGLE    = 165;

    private static final Vector START_POSITION    = new Vector(90, 3);
    private static final Vector CARGO_1_POSITION  = new Vector(137, -32);
    private static final Vector CARGO_2_POSITION  = new Vector(86, -131);
    private static final Vector TERMINAL_POSITION = new Vector(94, -305);
    private static final Vector END_POSITION      = new Vector(90, -210);

    public GrpAuto4BallStart1(SubsystemContainer subsystemContainer)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                subsystemContainer.getDrive().resetOdometer(START_POSITION);
                subsystemContainer.getDrive().setGyro(START_ANGLE);
            }), 

            new CmdWaitAuto(subsystemContainer),
            new CmdShootManual(subsystemContainer, RobotPosition.Position1, TargetPosition.UpperHub),
            new CmdPickupDeploy(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, CARGO_1_POSITION, CARGO_1_ANGLE, 0.5, 0.37, 0, true),
            new CmdDriveToPosition(subsystemContainer, CARGO_2_POSITION, CARGO_2_ANGLE, 0.45, 0.5, 0, true),
            new CmdWait(0.25),
            new CmdPickupStow(subsystemContainer),
            new CmdDriveRotate(subsystemContainer, SHOOT_ANGLE, 0.5, true),
            new CmdShootManual(subsystemContainer, RobotPosition.CargoRing, TargetPosition.UpperHub),
            new CmdPickupDeploy(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, TERMINAL_POSITION, TERMINAL_ANGLE, 0.5, 84, 0.85, 0.45, 0, true),
            new CmdWait(0.25),
            new CmdPickupStow(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, END_POSITION, SHOOT_ANGLE, 0.5, 1, 0, true),
            new CmdShootWithVision(subsystemContainer)
        );
    }
}
