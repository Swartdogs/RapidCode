package frc.robot.groups;

import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.CmdDriveToPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdWait;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.*;

public class GrpAuto2BallStart1 extends SwartdogSequentialCommandGroup
{
    private static final double START_ANGLE = 90;
    private static final double SHOOT_ANGLE = -75;

    private static final Vector START_POSITION = new Vector(90, 3);
    private static final Vector CARGO_POSITION = new Vector(42, -42);
    private static final Vector SHOOT_POSITION = new Vector(-28, -15);
    private static final Vector END_POSITION = new Vector(49, -42);

    public GrpAuto2BallStart1(Drive drive, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                drive.resetOdometer(START_POSITION);
                drive.setGyro(START_ANGLE);
            }),
            
            new CmdPickupDeploy(pickup, ballpath),
            new CmdDriveToPosition(drive, START_POSITION.add(CARGO_POSITION), START_ANGLE, 0.5, 0.5, 0, true),
            new CmdWait(2),
            new CmdPickupStow(pickup),
            new CmdDriveToPosition(drive, START_POSITION.add(SHOOT_POSITION), SHOOT_ANGLE, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, RobotPosition.Fender, TargetPosition.LowerHub),
            new CmdDriveToPosition(drive, START_POSITION.add(END_POSITION), SHOOT_ANGLE, 0.5, 0.5, 0, true)
        );
    }
}
