package frc.robot.groups;

import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.commands.CmdDriveToPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdWait;
import frc.robot.commands.CmdWaitAuto;
import frc.robot.subsystems.drive.*;

public class GrpAuto2BallStart1 extends SwartdogSequentialCommandGroup
{
    private static final double START_ANGLE = 90;
    private static final double SHOOT_ANGLE = -75;

    private static final Vector START_POSITION = new Vector(90, 3);
    private static final Vector CARGO_POSITION = new Vector(42, -42);
    private static final Vector SHOOT_POSITION = new Vector(-28, -15);
    private static final Vector END_POSITION = new Vector(49, -42);

    public GrpAuto2BallStart1(SubsystemContainer subsystemContainer)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                subsystemContainer.getDrive().resetOdometer(START_POSITION);
                subsystemContainer.getDrive().setGyro(START_ANGLE);
            }),
            
            new CmdWaitAuto(subsystemContainer),
            new CmdPickupDeploy(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, START_POSITION.add(CARGO_POSITION), START_ANGLE, 0.5, 0.5, 0, true),
            new CmdWait(2),
            new CmdPickupStow(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, START_POSITION.add(SHOOT_POSITION), SHOOT_ANGLE, 0.5, 0.5, 0, true),
            new CmdShootManual(subsystemContainer, RobotPosition.Fender, TargetPosition.LowerHub),
            new CmdDriveToPosition(subsystemContainer, START_POSITION.add(END_POSITION), SHOOT_ANGLE, 0.5, 0.5, 0, true)
        );
    }
}
