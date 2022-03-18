package frc.robot.groups;

import frc.robot.Constants;
import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.CmdDriveToPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Position;
import frc.robot.subsystems.drive.Vector;

public class GrpAuto2BallStart6 extends SwartdogSequentialCommandGroup
{
    private static final double START_ANGLE = Constants.Drive.FIELD_ANGLE;
    private static final double CARGO_ANGLE = -105;
    private static final double SHOOT_ANGLE = 20;

    private static final Position START_POSITION = new Position(Constants.Drive.FIELD_RESET_POSITION, START_ANGLE);
    private static final Position CARGO_POSITION = new Position(START_POSITION.add(new Vector(-54, -95)), CARGO_ANGLE);
    private static final Position SHOOT_POSITION = new Position(START_POSITION.add(new Vector(-12, -12)), SHOOT_ANGLE);

    public GrpAuto2BallStart6(Drive drive, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                drive.resetOdometer(START_POSITION);
                drive.setGyro(START_ANGLE);
            }),

            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal),
            new CmdPickupDeploy(pickup, ballpath),
            new CmdDriveToPosition(drive, CARGO_POSITION, 0.5, 0.5, 0, true),
            new CmdPickupStow(pickup),
            new CmdDriveToPosition(drive, SHOOT_POSITION, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal)
        );
    }
}
