package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hanger;
import frc.robot.abstraction.Joystick;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.commands.CmdBallpathLoad;
import frc.robot.commands.CmdDriveWithJoystick;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupReverse;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdRunBallPath;
import frc.robot.commands.CmdShootManual;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.hardware.HardwareBallpath;
import frc.robot.subsystems.hardware.HardwareDrive;
import frc.robot.subsystems.hardware.HardwareHanger;
import frc.robot.subsystems.hardware.HardwarePickup;
import frc.robot.subsystems.hardware.HardwareShooter;

public class RobotContainer extends SubsystemBase
{
    private Joystick _driveJoystick;
    private Joystick _coDriveJoystick;

    private Drive    _drive;
    private Ballpath _ballpath;
    private Hanger   _hanger;
    private Pickup   _pickup;
    private Shooter  _shooter;

    public RobotContainer()
    {
        _driveJoystick   = Joystick.joystick(0);
        _coDriveJoystick = Joystick.joystick(1);

        _drive    = new HardwareDrive();
        _ballpath = new HardwareBallpath();
        // _hanger   = new HardwareHanger();
        _pickup   = new HardwarePickup();
        _shooter  = new HardwareShooter();

        configureControls();
        configureDefaultCommands();
        configureButtonBindings();
        init();
    }

    @Override
    public void periodic()
    {
        if(_ballpath.hasPickupSensorTransitionedTo(State.On))
        {
            CommandScheduler.getInstance().schedule(false, new CmdBallpathLoad(_ballpath, _pickup));
        }
    }

    private void configureControls()
    {
        _driveJoystick.setXDeadband(0.05);
        _driveJoystick.setYDeadband(0.05);
        _driveJoystick.setZDeadband(0.1);
    }

    private void configureDefaultCommands()
    {
        _drive.setDefaultCommand
        (
            new CmdDriveWithJoystick
            (
                _drive, 
                () -> _driveJoystick.getY(), 
                () -> _driveJoystick.getX(), 
                () -> _driveJoystick.getZ()
            )
        );
    }

    private void configureButtonBindings()
    {
        _driveJoystick.getButton(11).whenActivated(new CmdPickupDeploy(_pickup, _ballpath));
        _driveJoystick.getButton( 9).whenActivated(new CmdPickupReverse(_pickup));
        _driveJoystick.getButton(10).whenActivated(new CmdPickupStow(_pickup));
        
        // _driveJoystick.getButton(12).whenActivated(SwartdogCommand.run(() -> _drive.setGyro(0)));

        _driveJoystick.getButton(6).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(1)));
        _driveJoystick.getButton(7).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(-1)));

        _driveJoystick.getButton(8).whenActivated(new CmdShootManual(_shooter, _ballpath));

        // _coDriveJoystick.getButton(10).whileActive(new CmdRunBallPath(_ballpath));

        // _driveJoystick.getButton( 9).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(1198)));
        // _driveJoystick.getButton(10).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(1618)));
        // _driveJoystick.getButton(8).whenActivated(SwartdogCommand.run(() -> _shooter.setShooterMotorSpeed(Constants.Shooter.MANUAL_SHOOTER_RPM)));
        // _driveJoystick.getButton(9).whenActivated(SwartdogCommand.run(() -> _shooter.setShooterMotorSpeed(0)));

        // _driveJoystick.getButton(11).whileActive(new CmdRunBallPath(_ballpath));
    }

    private void init()
    {
        _shooter.setHoodPosition(1198);
    }

    public Command getAutonomousCommand()
    {
        return null;
    }
}
