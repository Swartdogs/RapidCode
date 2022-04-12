package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.*;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants.GameMode;
import frc.robot.Constants.Hanger.ArmPosition;
import frc.robot.Constants.Shooter.*;
import frc.robot.abstraction.*;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.abstraction.Switch.SettableSwitch;
import frc.robot.commands.*;
import frc.robot.groups.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.hardware.*;

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

    private State          _lastConfirmState    = State.Off;
    private State          _currentConfirmState = State.Off;

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
        _lastConfirmState    = _currentConfirmState;
        _currentConfirmState = _buttonBox.getButton(6).get();

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
        _driveJoystick.setSquareZ(false);
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
                () -> Math.pow(_driveJoystick.getZ(), 3) * 0.85,
                () -> _driveJoystick.getButton(1).get() != State.On
            )
        );
    }

    private void configureButtonBindings()
    {

        CmdShootManual       shootNearLaunchpad          = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, RobotPosition.NearLaunchpad, TargetPosition.UpperHub);
        CmdShootManual       shootFender                 = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, RobotPosition.Fender, TargetPosition.UpperHub);
        CmdShootManual       shootFenderLowGoal          = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, RobotPosition.Fender, TargetPosition.LowerHub);
        CmdShootManual       shootTarmac                 = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, RobotPosition.Tarmac, TargetPosition.UpperHub);
        CmdShootManual       shootTarmacLowGoal          = new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, RobotPosition.Tarmac, TargetPosition.LowerHub);

        CmdBallpathEjectHigh ballpathEjectHigh           = new CmdBallpathEjectHigh(_ballpath, _shooter);
        CmdBallpathEjectLow  ballpathEjectLow            = new CmdBallpathEjectLow(_ballpath, _pickup);

        // Driver joystick button bindings
        _driveJoystick.getButton( 2).cancelWhenActivated(shootNearLaunchpad);                                                           // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootFender);                                                                  // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootFenderLowGoal);                                                           // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootTarmac);                                                  // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootTarmacLowGoal); 
        
        _driveJoystick.getButton( 7).whenActivated(shootFenderLowGoal);                                                                 // Shoot using odometry-based aiming
        _driveJoystick.getButton( 8).whenActivated(shootFender);                                                                        // Shoot without aiming from the Fender into the high goal
        _driveJoystick.getButton( 9).whenActivated(shootTarmacLowGoal);                                                        // Shoot from the edge of the tarmac
        _driveJoystick.getButton(10).whenActivated(shootTarmac);
        _driveJoystick.getButton(11).whenActivated(SwartdogCommand.run(() ->                                                            // Reset the gyroscope and odometer
        {
            _drive.setGyro(Constants.Drive.FIELD_ANGLE);
            _drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION.clone());
        }));
        _driveJoystick.getButton(12).whenActivated(shootNearLaunchpad);                                                                 // Shoot without aiming from the Launchpad

        // Co-Driver joystick button bindings
        _coDriveJoystick.getButton( 1).whileActive(new GrpHangerManual(_hanger, () -> _coDriveJoystick.getY()));
        _coDriveJoystick.getButton( 2).whenActivated(new GrpHangerStow(_hanger));
        _coDriveJoystick.getButton( 3).whenActivated(new CmdPickupStow(_pickup));                                        // Stow the pickup
        _coDriveJoystick.getButton( 4).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(-1), true));   // Manually decrement cargo count
        _coDriveJoystick.getButton( 5).whenActivated(new CmdPickupDeploy(_pickup, _ballpath));                           // Deploy the pickup
        _coDriveJoystick.getButton( 6).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(1), true));    // Manually increment cargo count
        _coDriveJoystick.getButton( 7).whenActivated(ballpathEjectLow);
        _coDriveJoystick.getButton( 8).whenActivated(ballpathEjectHigh);
        _coDriveJoystick.getButton( 9).whenActivated(ballpathEjectLow); 
        _coDriveJoystick.getButton(10).whenActivated(ballpathEjectHigh);                      
        _coDriveJoystick.getButton(11).whenActivated(ballpathEjectLow);
        _coDriveJoystick.getButton(12).whenActivated(ballpathEjectHigh);

        //Button Box Bindings
        _buttonBox.getButton(1).whileActive(new CmdHangerModifyArmPosition(_hanger, -Constants.Hanger.ARM_ADJUST_ANGLE));
        _buttonBox.getButton(2).whileActive(new CmdHangerModifyArmPosition(_hanger, Constants.Hanger.ARM_ADJUST_ANGLE));
        _buttonBox.getButton(3).whenActivated(new GrpHangerSetPosition(_hanger, _shooter, _ballpath, _pickup, _compressor, ArmPosition.MidRung, Constants.Hanger.PREP_SPEED));
        _buttonBox.getButton(4).whenActivated(new GrpHangerSetPosition(_hanger, _shooter, _ballpath, _pickup, _compressor, ArmPosition.LowRung, Constants.Hanger.PREP_SPEED));
        _buttonBox.getButton(5).whenActivated(new GrpHangerHang(_hanger, _pickup, _shooter, _ballpath, _compressor, () -> _lastConfirmState != State.On && _currentConfirmState == State.On), false);
    }

    private void configureAutonomous()
    {
        _ballpath.setCargoCount(1);
        _drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION);
        _drive.setGyro(Constants.Drive.FIELD_ANGLE);

        _dashboard.addDefaultAutonomous("Nothing", null);
        _dashboard.addAutonomous("Shoot low only", new CmdShootManual(_shooter, _ballpath, _pickup, _compressor, RobotPosition.Fender, TargetPosition.LowerHub));
        _dashboard.addAutonomous("Position 1: 2 Ball; Lower Hub", new GrpAuto2BallStart1(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 1: 2 Ball; Upper Hub", new GrpAuto2BallHighStart1(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 1: 3 Ball; Lower Hub", new GrpAuto3BallStart1(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 1: 3 Ball; Upper Hub", new GrpAuto3BallHighStart1(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 1: 4 Ball; Upper Hub", new GrpAuto4BallStart1(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 2: 2 Ball; Lower Hub", new GrpAuto2BallStart2(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 2: 2 Ball; Upper Hub", new GrpAuto2BallHighStart2(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 5: 2 Ball; Lower Hub", new GrpAuto2BallStart5(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 5: 2 Ball; Upper Hub", new GrpAuto2BallHighStart5(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 6: 2 Ball; Lower Hub", new GrpAuto2BallStart6(_drive, _shooter, _ballpath, _pickup, _compressor));
        _dashboard.addAutonomous("Position 6: 2 Ball; Upper Hub", new GrpAuto2BallHighStart6(_drive, _shooter, _ballpath, _pickup, _compressor));
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

        _coDriveJoystick.getButton(2).whileActive(new CmdRunBallPath(_drive, _ballpath, _pickup, _compressor));
        _coDriveJoystick.getButton(3).whenActivated(new CmdPickupStow(_pickup));                                        // Stow the pickup
        _coDriveJoystick.getButton(4).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(-1), true));   // Manually decrement cargo count
        _coDriveJoystick.getButton(5).whenActivated(new CmdPickupDeploy(_pickup, _ballpath));                           // Deploy the pickup
        _coDriveJoystick.getButton(6).whenActivated(SwartdogCommand.run(() -> _ballpath.modifyCargoCount(1), true));    // Manually increment cargo count
        _coDriveJoystick.getButton(7).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() + 10)));
        _coDriveJoystick.getButton(9).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() - 10)));

        _buttonBox.getButton(3).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() + 50)));
        _buttonBox.getButton(4).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() - 50)));
        _buttonBox.getButton(5).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() + 10)));
        _buttonBox.getButton(6).whenActivated(SwartdogCommand.run(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() - 10)));
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
                _shooter.setHoodPosition(RobotPosition.NearLaunchpad.getHoodPosition(TargetPosition.UpperHub));

                _server.setSource(_driverCamera);
                _dashboard.addCamera(_driverCamera);
                _hanger.setArmPosition(ArmPosition.Stow.getPosition().getTheta());
                break;

            case Autonomous:
                _ballpath.setCargoCount(1);
                break;

            case Disabled:
                _pickup.stopMotor();
                _pickup.stow();
                break;

            default:
                break;
        }
    }
}
