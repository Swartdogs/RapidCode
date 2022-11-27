package frc.robot.groups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.commands.CmdShootWithVision;
import frc.robot.commands.CmdWaitAuto;
import frc.robot.drive.commands.CmdDriveToPosition;
import frc.robot.drive.Vector;

public class GrpAuto1BallHighTaxiFirst extends SequentialCommandGroup
{
    public GrpAuto1BallHighTaxiFirst(SubsystemContainer subsystemContainer)
    {
        super
        (
            new InstantCommand(() -> 
            {
                subsystemContainer.getDrive().resetOdometer(Constants.Drive.FIELD_RESET_POSITION);
                subsystemContainer.getDrive().setGyro(Constants.Drive.FIELD_ANGLE);
            }),

            new CmdWaitAuto(subsystemContainer),
            new CmdDriveToPosition(subsystemContainer, new Vector(0, -120), 0, 0.2, 0.6, 0, false),
            new CmdShootWithVision(subsystemContainer)
            
        );
    }
}
