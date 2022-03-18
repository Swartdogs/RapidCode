package frc.robot.groups;

import frc.robot.Constants.Shooter.ShootPosition;
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
    private static final double   START_ANGLE = 90;
    private static final double   SHOOT_ANGLE = -90;

    private static final Position START_POSITION = new Position(90, 3, START_ANGLE);
    private static final Position CARGO_POSITION = new Position(132, -39, START_ANGLE);
    private static final Position SHOOT_POSITION = new Position(69, -12, SHOOT_ANGLE);
    private static final Position END_POSITION   = new Position(139, -39, SHOOT_ANGLE);

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
            new CmdDriveToPosition(drive, CARGO_POSITION, 0.5, 0.5, 0, true),
            new CmdWait(0.5),
            new CmdPickupStow(pickup),
            new CmdDriveToPosition(drive, SHOOT_POSITION, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal),
            new CmdDriveToPosition(drive, END_POSITION, 0.5, 0.5, 0, true)
        );
    }
}
