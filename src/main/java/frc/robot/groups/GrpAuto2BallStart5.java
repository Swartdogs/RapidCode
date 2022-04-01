package frc.robot.groups;

import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.CmdDriveToPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.*;

public class GrpAuto2BallStart5 extends SwartdogSequentialCommandGroup
{
    private static final double START_ANGLE    = -126;
    private static final double CARGO_ANGLE    = -140;
    private static final double SHOOT_ANGLE    =   35;

    private static final Vector START_POSITION = new Vector(-64,  -64);
    private static final Vector CARGO_POSITION = new Vector(-88, -139);
    private static final Vector SHOOT_POSITION = new Vector(-20,  -64);

    public GrpAuto2BallStart5(Drive drive, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
    {

        super
        (
            SwartdogCommand.run(() -> 
            {
                drive.resetOdometer(START_POSITION);
                drive.setGyro(START_ANGLE);
            }),

            new CmdPickupDeploy(pickup, ballpath),
            new CmdDriveToPosition(drive, CARGO_POSITION, CARGO_ANGLE, 0.5, 0.5, 0, true),
            new CmdPickupStow(pickup),
            new CmdDriveToPosition(drive, SHOOT_POSITION, SHOOT_ANGLE, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal)
        ); 
    }
}
