package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hanger;
import frc.robot.abstraction.Joystick;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.commands.CmdBallpathLoad;
import frc.robot.commands.CmdHangerSetArmPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupReverse;
import frc.robot.commands.CmdPickupStow;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.hardware.HardwareBallpath;
import frc.robot.subsystems.hardware.HardwareHanger;
import frc.robot.subsystems.hardware.HardwarePickup;
import frc.robot.subsystems.hardware.HardwareShooter;


public class RobotContainer extends SubsystemBase
{
    private Joystick _driveJoystick;
    private Joystick _coDriveJoystick;

    private Ballpath _ballpath;
    private Hanger   _hanger;
    private Pickup   _pickup;
    private Shooter  _shooter;

    public RobotContainer()
    {
        _ballpath = new HardwareBallpath();
        _hanger   = new HardwareHanger();
        _pickup   = new HardwarePickup();
        _shooter  = new HardwareShooter();

        configureButtonBindings();
    }

    @Override
    public void periodic()
    {
        if(_ballpath.hasPickupSensorTransitionedTo(State.On))
        {
            CommandScheduler.getInstance().schedule(new CmdBallpathLoad(_ballpath));
        }
    }

    private void configureButtonBindings()
    {
        _coDriveJoystick.getButton(0).whenActivated(new CmdPickupDeploy(_pickup));
        _coDriveJoystick.getButton(1).whenActivated(new CmdPickupReverse(_pickup));
        _coDriveJoystick.getButton(2).whenActivated(new CmdPickupStow(_pickup));
    }

    public Command getAutonomousCommand()
    {
        return null;
    }
}
