package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hanger;
import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.Joystick;
import frc.robot.abstraction.NetworkTableString;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.CmdBallpathEjectHigh;
import frc.robot.commands.CmdBallpathEjectLow;
import frc.robot.commands.CmdBallpathLoad;
import frc.robot.commands.CmdDriveWithJoystick;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupReverse;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdRunBallPath;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdShootWithOdometry;
import frc.robot.commands.CmdShooterDefault;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Dashboard;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.hardware.HardwareBallpath;
import frc.robot.subsystems.hardware.HardwareDrive;
import frc.robot.subsystems.hardware.HardwarePickup;
import frc.robot.subsystems.hardware.HardwareShooter;
import frc.robot.subsystems.hardware.MockBallpath;
import frc.robot.subsystems.hardware.MockDrive;
import frc.robot.subsystems.hardware.MockHanger;
import frc.robot.subsystems.hardware.MockPickup;
import frc.robot.subsystems.hardware.MockShooter;

public class RobotContainer extends SubsystemBase
{
    private UsbCamera _driverCamera;
    private UsbCamera _hangerCamera;
    private VideoSink _server;

    private Joystick  _driveJoystick;
    private Joystick  _coDriveJoystick;

    private Drive     _drive;
    private Ballpath  _ballpath;
    private Hanger    _hanger;
    private Pickup    _pickup;
    private Shooter   _shooter;
    private SettableSwitch _compressor;
    private Dashboard _dashboard;

    private boolean  _usingDriverCamera;

    public RobotContainer()
    {
        _driveJoystick   = Joystick.joystick(0);
        _coDriveJoystick = Joystick.joystick(1);

        _drive     = new HardwareDrive();
        _ballpath  = new HardwareBallpath();
        // _hanger    = new HardwareHanger();
        _pickup    = new HardwarePickup();
        _shooter   = new HardwareShooter();
        // _drive     = new MockDrive();
        // _ballpath  = new MockBallpath();
        // _hanger    = new MockHanger();
        // _pickup    = new MockPickup();
        // _shooter   = new MockShooter();
        _compressor = SettableSwitch.compressor(2, PneumaticsModuleType.REVPH);
        _dashboard = new Dashboard(_drive, _ballpath, _hanger, _pickup, _shooter);

        _driverCamera = CameraServer.startAutomaticCapture(0);
        _hangerCamera = CameraServer.startAutomaticCapture(1);
        _server       = CameraServer.getServer();
        _usingDriverCamera = true;

        configureControls();
        configureDefaultCommands();
        configureButtonBindings();
        init();
    }

    @Override
    public void periodic()
    {
        // System.out.println(String.format("Distance: %6.2f, Hood position: %4d, Shooter RPM: %4d", _drive.getOdometer().getR(), (int)_shooter.getHoodPosition(), (int)_shooter._shooterMotor.get()));

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
        _driveJoystick.setSquareZ(true);
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

        // _shooter.setDefaultCommand
        // (
        //     new CmdShooterDefault(_shooter, () -> ((-_driveJoystick.getThrottle()) + 1) / 2.0)
        // );
    }

    private void configureButtonBindings()
    {
        CmdShootManual       shootNearLaunchpad = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.NearLaunchpad);
        CmdShootManual       shootFender        = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.Fender);
        CmdShootManual       shootFenderLowGoal = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.FenderLowGoal);
        CmdShootWithOdometry shootWithOdometry  = new CmdShootWithOdometry(_drive, _shooter, _ballpath);
        
        // Driver joystick button bindings
        _driveJoystick.getButton( 7).whenActivated(shootFenderLowGoal);                                                                  // Shoot using odometry-based aiming
        _driveJoystick.getButton( 8).whenActivated(shootFender);                                                                        // Shoot without aiming from the Fender
        // _driveJoystick.getButton( 9).whenActivated(SwartdogCommand.run(() -> System.out.println("Odometer: " + _drive.getOdometer()))); // Print odometer
        _driveJoystick.getButton(9).cancelWhenActivated(shootNearLaunchpad);                                                           // Cancel shooting
        _driveJoystick.getButton(9).cancelWhenActivated(shootFender);                                                                  // Cancel shooting
        _driveJoystick.getButton(9).cancelWhenActivated(shootFenderLowGoal);                                                            // Cancel shooting
        _driveJoystick.getButton(10).cancelWhenActivated(shootNearLaunchpad);                                                           // Cancel shooting
        _driveJoystick.getButton(10).cancelWhenActivated(shootFender);                                                                  // Cancel shooting
        _driveJoystick.getButton(10).cancelWhenActivated(shootFenderLowGoal);                                                            // Cancel shooting
        _driveJoystick.getButton(11).whenActivated(SwartdogCommand.run(() ->                                                            // Reset the gyroscope and odometer
        {
            _drive.setGyro(Constants.Drive.FIELD_ANGLE); 
            _drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION.clone()); 
        }));
        _driveJoystick.getButton(12).whenActivated(shootNearLaunchpad);                                                                 // Shoot without aiming from the Launchpad
        
        // _driveJoystick.getButton(1).whenActivated(SwartdogCommand.run(() ->
        // {
        //     if (_usingDriverCamera)
        //     {
        //         _server.setSource(_hangerCamera);
        //     }

        //     else
        //     {
        //         _server.setSource(_driverCamera);
        //     }

        //     _usingDriverCamera = !_usingDriverCamera;
        // }, true));

        // _driveJoystick.getButton(3).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() + 10)));
        // _driveJoystick.getButton(5).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() - 10)));

        // _coDriveJoystick.getButton(1).whileActive(new CmdRunBallPath(_ballpath));

        // Co-Driver joystick button bindings
        _coDriveJoystick.getButton(7).whenActivated(new CmdBallpathEjectHigh(_ballpath, _shooter));                     // Eject upper cargo via shooter, and load lower cargo into upper area if it is present
        _coDriveJoystick.getButton(5).whenActivated(new CmdPickupDeploy(_pickup, _ballpath));                           // Deploy the pickup
        _coDriveJoystick.getButton(9).whenActivated(new CmdBallpathEjectLow(_ballpath, _pickup));                       // Eject lower cargo via pickup
        _coDriveJoystick.getButton(3).whenActivated(new CmdPickupStow(_pickup));                                        // Stow the pickup
        _coDriveJoystick.getButton(4).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(-1), true));   // Manually decrement cargo count
        _coDriveJoystick.getButton(6).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(1), true));   // Manually increment cargo count 

        // _driveJoystick.getButton(2).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(-1), true));   // Manually decrement cargo count
        // _driveJoystick.getButton(3).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(1), true));   // Manually increment cargo count 

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
        _shooter.setHoodPosition(Constants.Shooter.NEAR_LAUNCHPAD_HOOD_POSITION);
        _server.setSource(_driverCamera);
        _dashboard.addCamera(_driverCamera);
    }

    public Command getAutonomousCommand()
    {
        _ballpath.setCargoCount(1);

        return new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.FenderLowGoal);
    }
}
