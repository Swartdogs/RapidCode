package frc.robot.groups;

import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.*;
import frc.robot.commands.*;
import frc.robot.subsystems.drive.*;

public class GrpAuto3BallStart1 extends SwartdogSequentialCommandGroup
{
    private static final double START_ANGLE      = -90;
    private static final double SHOOT_ANGLE      = -60;
    private static final double CARGO_1_ANGLE    = 125;
    private static final double CARGO_2_ANGLE    = -165;

    private static final Vector START_POSITION   = new Vector(90, 3);
    private static final Vector CARGO_POSITION   = new Vector(47, -35);
    private static final Vector SHOOT_POSITION   = new Vector(46, -36);
    private static final Vector CARGO_2_POSITION = new Vector(86, -131);

    public GrpAuto3BallStart1(SubsystemContainer subsystemContainer)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                subsystemContainer.getDrive().resetOdometer(START_POSITION);
                subsystemContainer.getDrive().setGyro(START_ANGLE);
            }),
            
            new CmdWaitAuto(subsystemContainer),
            new CmdShootManual(subsystemContainer, RobotPosition.Tarmac, TargetPosition.LowerHub),
            new CmdPickupDeploy(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, START_POSITION.add(CARGO_POSITION), CARGO_1_ANGLE, 0.5, 0.37, 0, true),
            new CmdWait(0.25),
            new CmdDriveToPosition(subsystemContainer, CARGO_2_POSITION, CARGO_2_ANGLE, 0.5, 0.5, 0, true),
            new CmdWait(0.25),
            new CmdPickupStow(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, SHOOT_POSITION, SHOOT_ANGLE, 0.5, 0.5, 0, true),
            new CmdShootManual(subsystemContainer, RobotPosition.Fender, TargetPosition.LowerHub) 
        );
    }
}
