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
import frc.robot.drive.Drive;
import frc.robot.drive.commands.CmdDriveWithJoystick;
import frc.robot.subsystems.hardware.*;

import static frc.robot.Constants.Assignments.*;

import java.util.Calendar;

public class RobotContainer extends SubsystemBase
{
    private static final boolean TEST_MODE = false;

    private UsbCamera          _driverCamera;
    private VideoSink          _server;

    private Joystick           _driveJoystick;
    private Joystick           _coDriveJoystick;
    private Joystick           _buttonBox;

    private Drive              _drive;
    private Ballpath           _ballpath;
    private Hanger             _hanger;
    private Pickup             _pickup;
    private Shooter            _shooter;
    private Vision             _vision;
    private SettableSwitch     _compressor;
    private Dashboard          _dashboard;
    private RobotLog           _robotLog;
    private SubsystemContainer _subsystemContainer;

    private State              _lastConfirmState    = State.Off;
    private State              _currentConfirmState = State.Off;

    public RobotContainer()
    {
        _driveJoystick      = Joystick.joystick(DRIVE_JOYSTICK);
        _coDriveJoystick    = Joystick.joystick(CO_DRIVE_JOYSTICK);
        _buttonBox          = Joystick.joystick(BUTTON_BOX);

        _drive              = new HardwareDrive();
        _ballpath           = new HardwareBallpath();
        _hanger             = new HardwareHanger();
        _pickup             = new HardwarePickup();
        _shooter            = new HardwareShooter();
        _vision             = new HardwareVision();
        _compressor         = SettableSwitch.compressor(PNEUMATICS_MODULE_CAN_ID, MODULE_TYPE);
        _dashboard          = new Dashboard(_drive, _ballpath, _hanger, _pickup, _shooter, _vision);
        _robotLog           = new RobotLog(String.format("/home/lvuser/Log_%d.txt", Calendar.getInstance().getTime().getTime()));
        _subsystemContainer = new SubsystemContainer(_drive, _ballpath, _dashboard, _hanger, _pickup, _shooter, _vision, _compressor, _robotLog);

        _driverCamera       = CameraServer.startAutomaticCapture(0);
        _server             = CameraServer.getServer();

        configureControls();
        configureDefaultCommands();
        configureAutonomous();

        configureCommonControls();

        if (TEST_MODE)
        {
            configureTestControls();
        }
        else
        {
            configureNormalControls();
        }
    }

    @Override
    public void periodic()
    {
        _lastConfirmState    = _currentConfirmState;
        _currentConfirmState = _buttonBox.getButton(6).get();

        if(_ballpath.hasPickupSensorTransitionedTo(State.On))
        {
            CommandScheduler.getInstance().schedule(false, new CmdBallpathLoad(_subsystemContainer));
        }
    }

    private void configureControls()
    {
        _driveJoystick.getXAxis().setDeadband(0.05);
        _driveJoystick.getYAxis().setDeadband(0.05);
        _driveJoystick.getZAxis().setDeadband(0.15);

        _driveJoystick.getXAxis().setMotionThreshold(0.058);
        _driveJoystick.getYAxis().setMotionThreshold(0.058);
        _driveJoystick.getZAxis().setMotionThreshold(0.058);

        _driveJoystick.getXAxis().setExponent(4);
        _driveJoystick.getYAxis().setExponent(4);
        _driveJoystick.getZAxis().setExponent(16);
    }

    private void configureDefaultCommands()
    {
        _drive.setDefaultCommand
        (
            new CmdDriveWithJoystick
            (
                _subsystemContainer,
                () -> _driveJoystick.getY(),
                () -> _driveJoystick.getX(),
                () -> _driveJoystick.getZ(),
                () -> _driveJoystick.getButton(1).get() != State.On
            )
        );
    }

    private void configureAutonomous()
    {
        _ballpath.setCargoCount(1);
        _drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION);
        _drive.setGyro(Constants.Drive.FIELD_ANGLE);

        _dashboard.addDefaultAutonomous("Nothing", null);
        _dashboard.addAutonomous("Shoot low only", new CmdShootManual(_subsystemContainer, RobotPosition.Fender, TargetPosition.LowerHub));
        _dashboard.addAutonomous("Any Fender: Upper Hub; Taxi", new GrpAuto1BallHighTaxi(_subsystemContainer));
        _dashboard.addAutonomous("Any Fender: Taxi; Upper Hub", new GrpAuto1BallHighTaxiFirst(_subsystemContainer));
        _dashboard.addAutonomous("Position 1: 2 Ball; Lower Hub", new GrpAuto2BallStart1(_subsystemContainer));
        _dashboard.addAutonomous("Position 1: 2 Ball; Upper Hub", new GrpAuto2BallHighStart1(_subsystemContainer));
        _dashboard.addAutonomous("Position 1: 3 Ball; Lower Hub", new GrpAuto3BallStart1(_subsystemContainer));
        _dashboard.addAutonomous("Position 1: 3 Ball; Upper Hub", new GrpAuto3BallHighStart1(_subsystemContainer));
        _dashboard.addAutonomous("Position 1: 4 Ball; Upper Hub", new GrpAuto4BallStart1(_subsystemContainer));
        _dashboard.addAutonomous("Position 2: 2 Ball; Lower Hub", new GrpAuto2BallStart2(_subsystemContainer));
        _dashboard.addAutonomous("Position 2: 2 Ball; Upper Hub", new GrpAuto2BallHighStart2(_subsystemContainer));
        _dashboard.addAutonomous("Position 5: 2 Ball; Lower Hub", new GrpAuto2BallStart5(_subsystemContainer));
        _dashboard.addAutonomous("Position 5: 2 Ball; Upper Hub", new GrpAuto2BallHighStart5(_subsystemContainer));
        _dashboard.addAutonomous("Position 6: 2 Ball; Lower Hub", new GrpAuto2BallStart6(_subsystemContainer));
        _dashboard.addAutonomous("Position 6: 2 Ball; Upper Hub", new GrpAuto2BallHighStart6(_subsystemContainer));
    }

    private void configureCommonControls()
    {
        _driveJoystick.getButton(11).whenActivated(new InstantCommand(() ->                                          // Reset the odometer/gyro
        {
            _drive.setGyro(Constants.Drive.FIELD_ANGLE);
            _drive.resetOdometer(Constants.Drive.FIELD_RESET_POSITION.clone());
        }));

        _coDriveJoystick.getButton(3).whenActivated(new CmdPickupStow(_subsystemContainer));                          // Stow the pickup
        _coDriveJoystick.getButton(4).whenActivated(new InstantCommand(() -> _ballpath.modifyCargoCount(-1))); // Manually decrement cargo count
        _coDriveJoystick.getButton(5).whenActivated(new CmdPickupDeploy(_subsystemContainer));                        // Deploy the pickup
        _coDriveJoystick.getButton(6).whenActivated(new InstantCommand(() -> _ballpath.modifyCargoCount(1)));  // Manually increment cargo count
    }

    private void configureNormalControls()
    {
        CmdShootManual       shootNearLaunchpad          = new CmdShootManual(_subsystemContainer, RobotPosition.NearLaunchpad, TargetPosition.UpperHub);
        CmdShootManual       shootFender                 = new CmdShootManual(_subsystemContainer, RobotPosition.Fender,        TargetPosition.UpperHub);
        CmdShootManual       shootFenderLowGoal          = new CmdShootManual(_subsystemContainer, RobotPosition.Fender,        TargetPosition.LowerHub);
        CmdShootManual       shootTarmac                 = new CmdShootManual(_subsystemContainer, RobotPosition.Tarmac,        TargetPosition.UpperHub);
        CmdShootManual       shootTarmacLowGoal          = new CmdShootManual(_subsystemContainer, RobotPosition.Tarmac,        TargetPosition.LowerHub);
        CmdShootWithVision   shootWithVision             = new CmdShootWithVision(_subsystemContainer);

        CmdBallpathEjectHigh ballpathEjectHigh           = new CmdBallpathEjectHigh(_subsystemContainer);
        CmdBallpathEjectLow  ballpathEjectLow            = new CmdBallpathEjectLow(_subsystemContainer);

        // Driver joystick button bindings
        /* Button 1 defined in default commands */
        _driveJoystick.getButton( 2).cancelWhenActivated(shootNearLaunchpad); // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootFender);        // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootFenderLowGoal); // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootTarmac);        // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootTarmacLowGoal); // Cancel shooting
        _driveJoystick.getButton( 2).cancelWhenActivated(shootWithVision);    // Cancel shooting
        
        _driveJoystick.getButton( 5).whenActivated(shootWithVision);          // Shoot with vision, maybe

        _driveJoystick.getButton( 7).whenActivated(shootFenderLowGoal);       // Shoot from the fender into the low goal
        _driveJoystick.getButton( 8).whenActivated(shootFender);              // Shoot from the fender into the high goal
        _driveJoystick.getButton( 9).whenActivated(shootTarmacLowGoal);       // Shoot from the edge of the tarmac into te low goal
        _driveJoystick.getButton(10).whenActivated(shootTarmac);              // Shoot from the edge of the tarmac into the high goal
        /* Button 11 defined in common controls */
        _driveJoystick.getButton(12).whenActivated(shootNearLaunchpad);       // Shoot from the near launchapd into the high goal

        // Co-Driver joystick button bindings
        _coDriveJoystick.getButton( 1).whileActive(new GrpHangerManual(_subsystemContainer, () -> _coDriveJoystick.getY()));
        _coDriveJoystick.getButton( 2).whenActivated(new GrpHangerStow(_subsystemContainer));
        /* Buttons 3-6 defined in common controls*/
        _coDriveJoystick.getButton( 7).whenActivated(ballpathEjectLow);
        _coDriveJoystick.getButton( 8).whenActivated(ballpathEjectHigh);
        _coDriveJoystick.getButton( 9).whenActivated(ballpathEjectLow); 
        _coDriveJoystick.getButton(10).whenActivated(ballpathEjectHigh);                      
        _coDriveJoystick.getButton(11).whenActivated(ballpathEjectLow);
        _coDriveJoystick.getButton(12).whenActivated(ballpathEjectHigh);

        //Button Box Bindings
        _buttonBox.getButton(1).whileActive(new CmdHangerModifyArmPosition(_subsystemContainer, () -> -_dashboard.getHangerArmAdjustAngle()));
        _buttonBox.getButton(2).whileActive(new CmdHangerModifyArmPosition(_subsystemContainer, _dashboard::getHangerArmAdjustAngle));
        _buttonBox.getButton(3).whenActivated(new GrpHangerSetPosition(_subsystemContainer, ArmPosition.MidRung, _dashboard::getHangerPrepSpeed));
        _buttonBox.getButton(4).whenActivated(new GrpHangerSetPosition(_subsystemContainer, ArmPosition.LowRung, _dashboard::getHangerPrepSpeed));
        _buttonBox.getButton(5).whenActivated(new GrpHangerHang(_subsystemContainer, () -> _lastConfirmState != State.On && _currentConfirmState == State.On), false);
        /* Button 6 used for hanger confirm */
    }

    private void configureTestControls()
    {
        _shooter.setDefaultCommand
        (
            new CmdShooterDefault
            (
                _subsystemContainer,
                () -> ((_coDriveJoystick.getThrottle() * -1) + 1) / 2
            )
        );

        _coDriveJoystick.getButton(2).whileActive(new CmdRunBallPath(_subsystemContainer));

        _buttonBox.getButton(3).whenActivated(new InstantCommand(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() + 50)));
        _buttonBox.getButton(4).whenActivated(new InstantCommand(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() - 50)));
        _buttonBox.getButton(5).whenActivated(new InstantCommand(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() + 10)));
        _buttonBox.getButton(6).whenActivated(new InstantCommand(() -> _shooter.setHoodPosition(_shooter.getHoodPosition() - 10)));
    }

    public Command getAutonomousCommand()
    {
        return _dashboard.getSelectedAutonomous();
    }

    public void setGameMode(GameMode gameMode)
    {
        _robotLog.setGameMode(gameMode);

        switch (gameMode)
        {
            case Init:
                _shooter.setHoodPosition(RobotPosition.NearLaunchpad.getHoodPosition(TargetPosition.UpperHub));

                _server.setSource(_driverCamera);
                _hanger.setArmPosition(ArmPosition.Stow.getPosition().getTheta());
                break;

            case Autonomous:
                _ballpath.setCargoCount(1);
                _vision.init();
                break;

            case Teleop:
                _vision.init();

            case Disabled:
                _pickup.stopMotor();
                _pickup.stow();

                _robotLog.save();
                _robotLog.close();

                break;

            default:
                break;
        }
    }
}
