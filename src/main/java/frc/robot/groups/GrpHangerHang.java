package frc.robot.groups;

import java.util.function.BooleanSupplier;

import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.CmdWaitForConfirm;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Hanger;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;


public class GrpHangerHang extends SwartdogSequentialCommandGroup
{
    
    public GrpHangerHang(Hanger hanger, Pickup pickup, Shooter shooter, Ballpath ballpath, SettableSwitch compressor, BooleanSupplier confirm)
    {
        super
        (
            SwartdogCommand.run(() -> {
                shooter.setShooterMotorSpeed(0);
                ballpath.stop();
                pickup.stopMotor();
                pickup.deploy();
                compressor.set(State.On);
        
                shooter.setHoodPosition(RobotPosition.NearLaunchpad.getHoodPosition(TargetPosition.UpperHub));
            }),
            new GrpHangerMidRung(hanger, shooter, ballpath, pickup, compressor),
            new CmdWaitForConfirm(confirm),
            new GrpHangerHighRung( hanger,  shooter,  ballpath,  pickup,  compressor, confirm),
            new CmdWaitForConfirm(confirm),
            new GrpHangerTraversalRung( hanger,  shooter,  ballpath,  pickup,  compressor, confirm)
        );

        addRequirements(shooter);
    }
}
