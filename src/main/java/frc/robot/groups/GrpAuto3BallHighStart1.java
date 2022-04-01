package frc.robot.groups;

import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.*;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.*;

public class GrpAuto3BallHighStart1 extends SwartdogSequentialCommandGroup
{
    private static final double START_ANGLE      = -90;
    private static final double SHOOT_ANGLE      = -75;
    private static final double CARGO_1_ANGLE    = 135;
    private static final double CARGO_2_ANGLE    = -150;

    private static final Vector START_POSITION   = new Vector(90, 3);
    private static final Vector CARGO_POSITION   = new Vector(47, -35);
    private static final Vector SHOOT_POSITION   = new Vector(60, -24);
    private static final Vector CARGO_2_POSITION = new Vector(80, -131);

    public GrpAuto3BallHighStart1(Drive drive, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                drive.resetOdometer(START_POSITION);
                drive.setGyro(START_ANGLE);
            }),
            
            new CmdDriveToPosition(drive, new Vector(16, 0), 0, 0.5, 0.45, 0, false),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderPosition1),
            new CmdPickupDeploy(pickup, ballpath),
            new CmdDriveToPosition(drive, START_POSITION.add(CARGO_POSITION), CARGO_1_ANGLE, 0.5, 0.45, 0, true),
            new CmdWait(0.25),
            new CmdDriveToPosition(drive, CARGO_2_POSITION, CARGO_2_ANGLE, 0.45, 0.48, 0, true),
            new CmdWait(0.25),
            new CmdPickupStow(pickup),
            new CmdDriveToPosition(drive, SHOOT_POSITION, SHOOT_ANGLE, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal)
           
        );
    }
}
