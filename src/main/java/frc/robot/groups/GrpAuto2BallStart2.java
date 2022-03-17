package frc.robot.groups;

import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.CmdDriveToPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.subsystems.drive.*;
import frc.robot.subsystems.*;

public class GrpAuto2BallStart2 extends SwartdogSequentialCommandGroup
{
    private static final double START_ANGLE    = 156;
    private static final double SHOOT_ANGLE    = -80;

    private static final Vector START_POSITION = new Vector(59, -59);
    private static final Vector CARGO_POSITION = new Vector(72, START_ANGLE, false);
    private static final Vector SHOOT_POSITION = new Vector(0, 36);

    public GrpAuto2BallStart2(Drive drive, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
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
            new CmdPickupStow(pickup),
            new CmdDriveToPosition(drive, START_POSITION.add(SHOOT_POSITION), SHOOT_ANGLE, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal)
        );
    }
}
