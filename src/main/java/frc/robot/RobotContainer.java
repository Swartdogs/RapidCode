package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hanger;
import frc.robot.Constants.GameMode;
import frc.robot.Constants.Shooter.ShootPosition;
import frc.robot.abstraction.Joystick;
import frc.robot.abstraction.SwartdogCommand;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.CmdBallpathEjectHigh;
import frc.robot.commands.CmdBallpathEjectLow;
import frc.robot.commands.CmdBallpathLoad;
import frc.robot.commands.CmdDriveWithJoystick;
import frc.robot.commands.CmdHangerReset;
import frc.robot.commands.CmdHangerSetArmPosition;
import frc.robot.commands.CmdPickupDeploy;
import frc.robot.commands.CmdPickupStow;
import frc.robot.commands.CmdRunBallPath;
import frc.robot.commands.CmdShootManual;
import frc.robot.commands.CmdShooterDefault;
import frc.robot.groups.GrpAuto2BallStart1;
import frc.robot.groups.GrpAuto2BallStart2;
import frc.robot.groups.GrpAuto2BallStart5;
import frc.robot.groups.GrpAuto3BallStart1;
import frc.robot.groups.GrpHangerManual;
import frc.robot.groups.GrpAuto2BallStart6;
import frc.robot.groups.GrpAuto3BallHighStart1;
import frc.robot.subsystems.Ballpath;
import frc.robot.subsystems.Dashboard;
import frc.robot.subsystems.Pickup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.hardware.HardwareBallpath;
import frc.robot.subsystems.hardware.HardwareDrive;
import frc.robot.subsystems.hardware.HardwareHanger;
import frc.robot.subsystems.hardware.HardwarePickup;
import frc.robot.subsystems.hardware.HardwareShooter;

import static frc.robot.Constants.Assignments.*;

public class RobotContainer extends SubsystemBase
{
    private static final boolean TEST_MODE = false;

    private UsbCamera      _driverCamera;
    private VideoSink      _server;

    private Joystick       _driveJoystick;
    private Joystick       _coDriveJoystick;
    private Joystick       _buttonBox;

    private Drive          _drive;
    private Ballpath       _ballpath;
    private Hanger         _hanger;
    private Pickup         _pickup;
    private Shooter        _shooter;
    private SettableSwitch _compressor;
    private Dashboard      _dashboard;

    public RobotContainer()
    {
        _driveJoystick   = Joystick.joystick(DRIVE_JOYSTICK);
        _coDriveJoystick = Joystick.joystick(CO_DRIVE_JOYSTICK);
        _buttonBox       = Joystick.joystick(BUTTON_BOX);

        _drive           = new HardwareDrive();
        _ballpath        = new HardwareBallpath();
        _hanger          = new HardwareHanger();
        _pickup          = new HardwarePickup();
        _shooter         = new HardwareShooter();
        _compressor      = SettableSwitch.compressor(PNEUMATICS_MODULE_CAN_ID, MODULE_TYPE);
        _dashboard       = new Dashboard(_drive, _ballpath, _hanger, _pickup, _shooter);

        _driverCamera    = CameraServer.startAutomaticCapture(0);
        _server          = CameraServer.getServer();

        configureControls();
        configureDefaultCommands();
        configureAutonomous();

        if (TEST_MODE)
        {
            configureTestCode();
        }
        else
        {
            configureButtonBindings();
        }
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
                () -> _driveJoystick.getZ(),
                () -> _driveJoystick.getButton(1).get() != State.On
            )
        );
    }

    private void configureButtonBindings()
    {
        CmdShootManual       shootNearLaunchpad          = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.NearLaunchpad);
        CmdShootManual       shootFender                 = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.Fender);
        CmdShootManual       shootFenderLowGoal          = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.FenderLowGoal);
        CmdShootManual       shootFenderLowGoalPosition1 = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.FenderLowGoalPosition1);
        CmdShootManual       shootFenderPosition1        = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.FenderPosition1);

        CmdBallpathEjectHigh ballpathEjectHigh           = new CmdBallpathEjectHigh(_ballpath, _shooter);
        CmdBallpathEjectLow  ballpathEjectLow            = new CmdBallpathEjectLow(_ballpath, _pickup);

        CmdHangerReset       hangerResetExtended         = new CmdHangerReset(_hanger, ExtendState.Extended);
        CmdHangerReset       hangerResetRetracted        = new CmdHangerReset(_hanger, ExtendState.Retracted);

        CmdHangerSetArmPosition hangerFirstPosition      = new CmdHangerSetArmPosition(_hanger, Constants.Hanger.ARM_FIRST_SETPOINT);
        CmdHangerSetArmPosition hangerSecondPosition     = new CmdHangerSetArmPosition(_hanger, Constants.Hanger.ARM_SECOND_SETPOINT);

        // Driver joystick button bindings
        _driveJoystick.getButton( 2).cancelWhenActivated(shootNearLaunchpad);                                                           // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootFender);                                                                  // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootFenderLowGoal);                                                           // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootFenderLowGoalPosition1);                                                  // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootFenderPosition1); 
        
        _driveJoystick.getButton( 7).whenActivated(shootFenderLowGoal);                                                                 // Shoot using odometry-based aiming
        _driveJoystick.getButton( 8).whenActivated(shootFender);                                                                        // Shoot without aiming from the Fender into the high goal
        _driveJoystick.getButton( 9).whenActivated(shootFenderLowGoalPosition1);                                                        // Shoot from the edge of the tarmac
        _driveJoystick.getButton(10).whenActivated(shootFenderPosition1);
        _driveJoystick.getButton(11).whenActivated(SwartdogCommand.run(() ->                                                            // Reset the gyroscope and odometer
        {
            _drive.setGyro(Constants.Drive.FIELD_ANGLE);
            _drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION.clone());
        }));
        _driveJoystick.getButton(12).whenActivated(shootNearLaunchpad);                                                                 // Shoot without aiming from the Launchpad

        // Co-Driver joystick button bindings
        _coDriveJoystick.getButton( 1).whileActive(new GrpHangerManual(_hanger, () -> _coDriveJoystick.getY()));
        _coDriveJoystick.getButton( 2).whenActivated(SwartdogCommand.run(() -> _hanger.resetWinchPosition(0), true));
        _coDriveJoystick.getButton( 3).whenActivated(new CmdPickupStow(_pickup));                                        // Stow the pickup
        _coDriveJoystick.getButton( 4).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(-1), true));   // Manually decrement cargo count
        _coDriveJoystick.getButton( 5).whenActivated(new CmdPickupDeploy(_pickup, _ballpath));                           // Deploy the pickup
        _coDriveJoystick.getButton( 6).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(1), true));    // Manually increment cargo count
        _coDriveJoystick.getButton( 7).whenActivated(ballpathEjectHigh);                     // Eject upper cargo via shooter, and load lower cargo into upper area if it is present
        _coDriveJoystick.getButton( 8).whileActive(hangerResetExtended);                   // Manually Winch Arms In
        _coDriveJoystick.getButton( 9).whenActivated(ballpathEjectLow);                       // Eject lower cargo via pickup
        _coDriveJoystick.getButton(10).whileActive(hangerResetRetracted);
        _coDriveJoystick.getButton(11).whenActivated(hangerSecondPosition);
        _coDriveJoystick.getButton(12).whenActivated(hangerFirstPosition);

        //Button Box Bindings
        _buttonBox.getButton(1).whileActive(hangerResetExtended);
        _buttonBox.getButton(2).whenActivated(ballpathEjectHigh);
        _buttonBox.getButton(3).whileActive(hangerResetRetracted);
        _buttonBox.getButton(4).whenActivated(ballpathEjectLow);
        _buttonBox.getButton(5).whenActivated(hangerFirstPosition);
        _buttonBox.getButton(6).whenActivated(hangerSecondPosition);
    }

    private void configureAutonomous()
    {
        _ballpath.setCargoCount(1);
        _drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION);
        _drive.setGyro(Constants.Drive.FIELD_ANGLE);

        _dashboard.addDefaultAutonomous("Nothing", null);
        _dashboard.addAutonomous("Shoot low only", new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, ShootPosition.FenderLowGoal));
        _dashboard.addAutonomous("Position 1: 2 Ball", new GrpAuto2BallStart1(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 1: 3 Ball", new GrpAuto3BallStart1(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 1: 3 Ball; Upper Hub", new GrpAuto3BallHighStart1(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 2: 2 Ball", new GrpAuto2BallStart2(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 5: 2 Ball", new GrpAuto2BallStart5(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 6: 2 Ball", new GrpAuto2BallStart6(_drive, _shooter, _ballpath, _pickup, _compressor));
    }

    private void configureTestCode()
    {
        _driveJoystick.getButton(11).whenActivated(SwartdogCommand.run(() ->                                                            // Reset the gyroscope and odometer
        {
            _drive.setGyro(Constants.Drive.FIELD_ANGLE);
            _drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION.clone());
        }));

        _shooter.setDefaultCommand
        (
            new CmdShooterDefault
            (
                _shooter,
                () -> ((_coDriveJoystick.getThrottle() * -1) + 1) / 2
            )
        );

        _coDriveJoystick.getButton(2).whileActive(new CmdRunBallPath(_drive, _ballpath));
        _coDriveJoystick.getButton(3).whenActivated(new CmdPickupStow(_pickup));                                        // Stow the pickup
        _coDriveJoystick.getButton(4).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(-1), true));   // Manually decrement cargo count
        _coDriveJoystick.getButton(5).whenActivated(new CmdPickupDeploy(_pickup, _ballpath));                           // Deploy the pickup
        _coDriveJoystick.getButton(6).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(1), true));    // Manually increment cargo count
        _coDriveJoystick.getButton(7).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() + 10)));
        _coDriveJoystick.getButton(9).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() - 10)));
    }

    public Command getAutonomousCommand()
    {
        return _dashboard.getSelectedAutonomous();
    }

    public void setGameMode(GameMode gameMode)
    {
        switch (gameMode)
        {
            case Init:
                _shooter.setHoodPosition(Constants.Shooter.NEAR_LAUNCHPAD_HOOD_POSITION);

                _hanger.resetWinchPosition(0);
                _hanger.setArmPosition(Constants.Hanger.ARM_STOW_SETPOINT);

                _server.setSource(_driverCamera);
                _dashboard.addCamera(_driverCamera);
                break;

            case Autonomous:
                _ballpath.setCargoCount(1);
                break;

            case Disabled:
                _pickup.stopMotor();
                _pickup.stow();

                _hanger.setArmPosition(Constants.Hanger.ARM_STOW_SETPOINT);
                break;

            default:
                break;
        }
    }
}
