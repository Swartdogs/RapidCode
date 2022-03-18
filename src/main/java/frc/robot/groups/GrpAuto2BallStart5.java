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
    private static final double   START_ANGLE    = -126;
    private static final double   CARGO_ANGLE    = -140;
    private static final double   SHOOT_ANGLE    =   42;

    private static final Position START_POSITION = new Position(-64, -64,  START_ANGLE);
    private static final Position CARGO_POSITION = new Position(-82, -139, CARGO_ANGLE);
    private static final Position SHOOT_POSITION = new Position(-16, -64,  SHOOT_ANGLE);

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
            new CmdDriveToPosition(drive, CARGO_POSITION, 0.5, 0.5, 0, true),
            new CmdPickupStow(pickup),
            new CmdDriveToPosition(drive, SHOOT_POSITION, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal)
        ); 
    }
}
