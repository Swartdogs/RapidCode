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
import frc.robot.subsystems.drive.Vector;

public class GrpAuto2BallStart6 extends SwartdogSequentialCommandGroup
{
    public GrpAuto2BallStart6(Drive drive, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION);
                drive.setGyro(Constants.Drive.FIELD_ANGLE);
            }),

            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal),
            new CmdPickupDeploy(pickup, ballpath),
            new CmdDriveToPosition(drive, Constants.Drive.FIELD_RESET_POSITION.add(new Vector(-54, -95)), -105, 0.5, 0.5, 0, true),
            new CmdPickupStow(pickup),
            new CmdDriveToPosition(drive, Constants.Drive.FIELD_RESET_POSITION.add(new Vector(-12, -12)), 20, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal)
        );
    }
}
