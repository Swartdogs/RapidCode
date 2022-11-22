package frc.robot.groups;

import java.util.function.BooleanSupplier;

import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.commands.CmdWaitForCondition;

public class GrpHangerHang extends SwartdogSequentialCommandGroup
{
    public GrpHangerHang(SubsystemContainer subsystemContainer, BooleanSupplier confirm)
    {
        super
        (
            SwartdogCommand.run(() -> {
                subsystemContainer.getShooter().setShooterMotorSpeed(0);
                subsystemContainer.getBallpath().stop();
                subsystemContainer.getPickup().stopMotor();
                subsystemContainer.getPickup().deploy();
                subsystemContainer.getCompressor().set(State.On);
        
                subsystemContainer.getShooter().setHoodPosition(RobotPosition.NearLaunchpad.getHoodPosition(TargetPosition.UpperHub));
            }),
            new GrpHangerMidRung(subsystemContainer),
            new CmdWaitForCondition(confirm),
            new GrpHangerHighRung(subsystemContainer, confirm),
            new CmdWaitForCondition(confirm),
            new GrpHangerTraversalRung(subsystemContainer, confirm)
        );

        addRequirements(subsystemContainer.getShooter());
    }
}
