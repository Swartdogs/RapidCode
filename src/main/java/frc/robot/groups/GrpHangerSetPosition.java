package frc.robot.groups;

import java.util.function.DoubleSupplier;

import frc.robot.SubsystemContainer;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.abstraction.Enumerations.State;

public class GrpHangerSetPosition extends SwartdogSequentialCommandGroup
{
    public GrpHangerSetPosition(SubsystemContainer subsystemContainer, ArmPosition hookPosition, DoubleSupplier winchSpeed)
    {
        super
        (
            SwartdogCommand.run(() -> {
                subsystemContainer.getShooter().setShooterMotorSpeed(0);
                subsystemContainer.getBallpath().stop();
                subsystemContainer.getPickup().stopMotor();
                subsystemContainer.getCompressor().set(State.On);
        
                subsystemContainer.getShooter().setHoodPosition(RobotPosition.NearLaunchpad.getHoodPosition(TargetPosition.UpperHub));

                subsystemContainer.getHanger().useArmPID(hookPosition.runPIDBefore());
                subsystemContainer.getHanger().setArmPosition(hookPosition.getPosition().getTheta());
            }),
            new GrpHangerSetWinchPosition(subsystemContainer, hookPosition.getPosition().getR(), winchSpeed),
            SwartdogCommand.run(() -> subsystemContainer.getHanger().useArmPID(hookPosition.runPIDAfter()))
        );
    }
}
