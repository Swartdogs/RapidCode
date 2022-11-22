package frc.robot.groups;

import frc.robot.Constants;
import frc.robot.SubsystemContainer;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.SwartdogSequentialCommandGroup;
import frc.robot.commands.CmdDriveToPosition;
import frc.robot.commands.CmdShootWithVision;
import frc.robot.commands.CmdWaitAuto;
import frc.robot.subsystems.drive.Vector;

public class GrpAuto1BallHighTaxiFirst extends SwartdogSequentialCommandGroup
{
    public GrpAuto1BallHighTaxiFirst(SubsystemContainer subsystemContainer)
    {
        super
        (
            SwartdogCommand.run(() -> 
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
