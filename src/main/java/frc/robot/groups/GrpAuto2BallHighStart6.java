package frc.robot.groups;

import frc.robot.Constants;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.CmdDriveRotate;
import frc.robot.commands.CmdDriveToPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdWait;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Vector;

public class GrpAuto2BallHighStart6 extends SwartdogSequentialCommandGroup
{
    public GrpAuto2BallHighStart6(Drive drive, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION);
                drive.setGyro(Constants.Drive.FIELD_ANGLE);
            }),

            new CmdShootManual(shooter, ballpath, pickup, compressor, RobotPosition.Fender, TargetPosition.UpperHub),
            new CmdPickupDeploy(pickup, ballpath),
            new CmdDriveToPosition(drive, Constants.Drive.FIELD_RESET_POSITION.add(new Vector(-60, -80)), -115, 0.5, 0.5, 0, true),
            new CmdWait(1.5),
            new CmdPickupStow(pickup),
            new CmdDriveRotate(drive, 21, 0.5, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, RobotPosition.CargoRing, TargetPosition.UpperHub)
            // new CmdDriveToPosition(drive, Constants.Drive.FIELD_RESET_POSITION.add(new Vector(-6, -6)), 25, 0.5, 0.5, 0, true),
        );
    }
}
