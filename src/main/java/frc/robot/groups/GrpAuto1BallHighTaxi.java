package frc.robot.groups;

import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.Constants.Shooter.RobotPosition;
import frc.robot.Constants.Shooter.TargetPosition;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.commands.CmdDriveToPosition;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdWaitAuto;
import frc.robot.subsystems.drive.Vector;

public class GrpAuto1BallHighTaxi extends SwartdogSequentialCommandGroup
{
    public GrpAuto1BallHighTaxi(SubsystemContainer subsystemContainer)
    {
        super
        (
            SwartdogCommand.run(() -> 
            {
                subsystemContainer.getDrive().resetOdometer(Constants.Drive.FIELD_RESET_POSITION);
                subsystemContainer.getDrive().setGyro(Constants.Drive.FIELD_ANGLE);
            }),

            new CmdWaitAuto(subsystemContainer),
            new CmdShootManual(subsystemContainer, RobotPosition.Fender, TargetPosition.UpperHub),
            new CmdDriveToPosition(subsystemContainer, new Vector(0, -120), 0, 0.2, 0.6, 0, false)
            
        );
    }
}
