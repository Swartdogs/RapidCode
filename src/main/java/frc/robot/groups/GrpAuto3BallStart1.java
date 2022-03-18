package frc.robot.groups;

import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.*;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.*;

public class GrpAuto3BallStart1 extends SwartdogSequentialCommandGroup
{
    private static final double   START_ANGLE      = -90;
    private static final double   CARGO_1_ANGLE    = 135;
    private static final double   CARGO_2_ANGLE    = -150;
    private static final double   SHOOT_ANGLE      = -75;
    
    private static final Position START_POSITION   = new Position(90, 3, START_ANGLE);
    private static final Position CARGO_1_POSITION = new Position(137, -29, CARGO_1_ANGLE);
    private static final Position CARGO_2_POSITION = new Position(80, -131, CARGO_2_ANGLE);
    private static final Position SHOOT_POSITION   = new Position(60, -24, SHOOT_ANGLE);

    public GrpAuto3BallStart1(Drive drive, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                drive.resetOdometer(START_POSITION);
                drive.setGyro(START_ANGLE);
            }),
            
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoalPosition1),
            new CmdPickupDeploy(pickup, ballpath),
            new CmdDriveToPosition(drive, CARGO_1_POSITION, 0.5, 0.5, 0, true),
            new CmdWait(0.25),
            new CmdDriveToPosition(drive, CARGO_2_POSITION, 0.5, 0.5, 0, true),
            new CmdPickupStow(pickup),
            new CmdWait(0.25),
            new CmdDriveToPosition(drive, SHOOT_POSITION, 0.5, 0.5, 0, true),
            new CmdShootManual(shooter, ballpath, pickup, compressor, ShootPosition.FenderLowGoal)
           
        );
    }
}
