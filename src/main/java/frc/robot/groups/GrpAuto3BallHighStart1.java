package frc.robot.groups;

import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.*;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.*;

public class GrpAuto3BallHighStart1 extends SwartdogSequentialCommandGroup
{
    private static final double START_ANGLE      = -90;
    private static final double SHOOT_ANGLE      = -40;
    private static final double CARGO_1_ANGLE    = 125;
    private static final double CARGO_2_ANGLE    = -165;

    private static final Vector START_POSITION   = new Vector(90, 3);
    private static final Vector CARGO_1_POSITION = new Vector(137, -32);
    private static final Vector CARGO_2_POSITION = new Vector(86, -131);

    public GrpAuto3BallHighStart1(Drive drive, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                drive.resetOdometer(START_POSITION);
                drive.setGyro(START_ANGLE);
            }),
            
            new CmdShootManual(shooter, ballpath, pickup, compressor, RobotPosition.Position1, TargetPosition.UpperHub),
            new CmdPickupDeploy(pickup, ballpath),
            new CmdDriveToPosition(drive, CARGO_1_POSITION, CARGO_1_ANGLE, 0.5, 0.37, 0, true),
            new CmdWait(0.25),
            new CmdDriveToPosition(drive, CARGO_2_POSITION, CARGO_2_ANGLE, 0.45, 0.48, 0, true),
            new CmdWait(0.25),
            new CmdPickupStow(pickup),
            new CmdDriveRotate(drive, SHOOT_ANGLE, 0.5, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, RobotPosition.CargoRing, TargetPosition.UpperHub)
           
        );
    }
}
