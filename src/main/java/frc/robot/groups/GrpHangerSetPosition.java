package frc.robot.groups;

import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Hanger;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;

public class GrpHangerSetPosition extends SwartdogSequentialCommandGroup
{
    public GrpHangerSetPosition(Hanger hanger, Shooter shooter, Ballpath ballpath, Pickup pickup, SettableSwitch compressor, ArmPosition hookPosition, double winchSpeed)
    {
        super
        (
            SwartdogCommand.run(() -> {
                shooter.setShooterMotorSpeed(0);
                ballpath.stop();
                pickup.stopMotor();
                compressor.set(State.On);
        
                shooter.setHoodPosition(RobotPosition.NearLaunchpad.getHoodPosition(TargetPosition.UpperHub));
            }),
            SwartdogCommand.run(() -> hanger.useArmPID(hookPosition.runPIDBefore())),
            SwartdogCommand.run(() -> hanger.setArmPosition(hookPosition.getPosition().getTheta())),
            new GrpHangerSetWinchPosition(hanger, hookPosition.getPosition().getR(), winchSpeed),
            SwartdogCommand.run(() -> hanger.useArmPID(hookPosition.runPIDAfter()))
        );
    }
}
