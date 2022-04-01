package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import frc.robot.abstraction.SwartdogCommand;
import frc.robot.subsystems.drive.Drive;

public class CmdDriveWithJoystick extends SwartdogCommand 
{
    private Drive           _drive;
    private DoubleSupplier  _driveInput;
    private DoubleSupplier  _strafeInput;
    private DoubleSupplier  _rotateInput;
    private BooleanSupplier _isAbsolute;

    public CmdDriveWithJoystick(Drive drive, DoubleSupplier driveInput, DoubleSupplier strafeInput, DoubleSupplier rotateInput, BooleanSupplier isAbsolute)
    {
        _drive       = drive;
        _driveInput  = driveInput;
        _strafeInput = strafeInput;
        _rotateInput = rotateInput;
        _isAbsolute  = isAbsolute;

        addRequirements(_drive);
    }

    @Override
    public void execute() 
    {
        _drive.drive(_driveInput.getAsDouble(), _strafeInput.getAsDouble(), _rotateInput.getAsDouble(), _isAbsolute.getAsBoolean());
    }

    @Override
    public boolean isFinished() 
    {
        return false;
    }
}
