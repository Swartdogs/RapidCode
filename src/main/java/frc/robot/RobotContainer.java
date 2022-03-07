package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hanger;
import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.Joystick;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.commands.CmdBallpathEjectHigh;
import frc.robot.commands.CmdBallpathEjectLow;
import frc.robot.commands.CmdBallpathLoad;
import frc.robot.commands.CmdDriveWithJoystick;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupReverse;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdShootWithOdometry;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.hardware.HardwareBallpath;
import frc.robot.subsystems.hardware.HardwareDrive;
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
        _driveJoystick.setZDeadband(0.15);

        _driveJoystick.setSquareX(true);
        _driveJoystick.setSquareY(true);
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
        CmdShootManual       shootNearLaunchpad = new CmdShootManual(_shooter, _ballpath, ShootPosition.NearLaunchpad);
        CmdShootManual       shootFender        = new CmdShootManual(_shooter, _ballpath, ShootPosition.Fender);
        CmdShootWithOdometry shootWithOdometry  = new CmdShootWithOdometry(_drive, _shooter, _ballpath);
        
        // Driver joystick button bindings
        _driveJoystick.getButton( 7).whenActivated(shootWithOdometry);                                                                  // Shoot using odometry-based aiming
        _driveJoystick.getButton( 8).whenActivated(shootFender);                                                                        // Shoot without aiming from the Fender
        _driveJoystick.getButton( 9).whenActivated(SwartdogCommand.run(() -> System.out.println("Odometer: " + _drive.getOdometer()))); // Print odometer
        _driveJoystick.getButton(10).cancelWhenActivated(shootNearLaunchpad);                                                           // Cancel shooting
        _driveJoystick.getButton(10).cancelWhenActivated(shootFender);                                                                  // Cancel shooting
        _driveJoystick.getButton(10).cancelWhenActivated(shootWithOdometry);                                                            // Cancel shooting
        _driveJoystick.getButton(11).whenActivated(SwartdogCommand.run(() ->                                                            // Reset the gyroscope and odometer
        {
            _drive.setGyro(Constants.Drive.FIELD_ANGLE); 
            _drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION.clone()); 
        }));
        _driveJoystick.getButton(12).whenActivated(shootNearLaunchpad);                                                                 // Shoot without aiming from the Launchpad
        
        // Co-Driver joystick button bindings
        _coDriveJoystick.getButton(7).whenActivated(new CmdBallpathEjectHigh(_ballpath, _shooter));             // Eject upper cargo via shooter, and load lower cargo into upper area if it is present
        _coDriveJoystick.getButton(5).whenActivated(new CmdPickupDeploy(_pickup, _ballpath));                   // Deploy the pickup
        _coDriveJoystick.getButton(9).whenActivated(new CmdBallpathEjectLow(_ballpath, _pickup));               // Eject lower cargo via pickup
        _coDriveJoystick.getButton(3).whenActivated(new CmdPickupStow(_pickup));                                // Stow the pickup
        _coDriveJoystick.getButton(4).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(-1))); // Manually decrement cargo count
        _coDriveJoystick.getButton(6).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(1)));  // Manually increment cargo count 
        
        // Various unused bindings for testing things
        // _coDriveJoystick.getButton( 9).whenActivated(new CmdPickupReverse(_pickup));

        // _coDriveJoystick.getButton(10).whileActive(new CmdRunBallPath(_ballpath));

        // _driveJoystick.getButton( 9).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(1198)));
        // _driveJoystick.getButton(10).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(1618)));
        // _driveJoystick.getButton( 8).whenActivated(SwartdogCommand.run(() -> _shooter.setShooterMotorSpeed(Constants.Shooter.MANUAL_SHOOTER_RPM)));
        // _driveJoystick.getButton( 9).whenActivated(SwartdogCommand.run(() -> _shooter.setShooterMotorSpeed(0)));

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
