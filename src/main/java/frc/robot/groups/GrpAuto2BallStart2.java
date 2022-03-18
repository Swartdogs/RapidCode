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
    private static final double   START_ANGLE    = 156;
    private static final double   SHOOT_ANGLE    = -80;

    private static final Position START_POSITION = new Position(59, -59, START_ANGLE);
    private static final Position CARGO_POSITION = new Position(START_POSITION.add(new Vector(72, START_ANGLE, false)), START_ANGLE);
    private static final Position SHOOT_POSITION = new Position(59, -23, SHOOT_ANGLE);

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
            new CmdDriveToPosition(drive, CARGO_POSITION, 0.5, 0.5, 0, true),
            new CmdPickupStow(pickup),
            new CmdDriveToPosition(drive, SHOOT_POSITION, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal)
        );
    }
}
