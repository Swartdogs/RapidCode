package frc.robot.subsystems;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.abstraction.*;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.drive.Drive;
import frc.robot.drive.Vector;

public class Dashboard extends SubsystemBase
{
    // Subsystems
    private Drive                            _drive;
    private Ballpath                         _ballpath;
    private Hanger                           _hanger;
    private Pickup                           _pickup;
    private Shooter                          _shooter;
    private Vision                           _vision;

    // Robot values
    private NetworkTableDouble               _flModuleRotation;
    private NetworkTableDouble               _flModuleDistance;
    private NetworkTableDouble               _frModuleRotation;
    private NetworkTableDouble               _frModuleDistance;
    private NetworkTableDouble               _blModuleRotation;
    private NetworkTableDouble               _blModuleDistance;
    private NetworkTableDouble               _brModuleRotation;
    private NetworkTableDouble               _brModuleDistance;
    private NetworkTableDouble               _robotRotation;
    private NetworkTableDouble               _ballCount;
    private NetworkTableString               _pickupDeployed;
    private NetworkTableString               _pickupActive;
    private NetworkTableString               _ballpathLowerActive;
    private NetworkTableString               _ballpathUpperActive;
    private NetworkTableString               _hangerReleased;
    private NetworkTableDouble               _hangerArmPosition;
    private NetworkTableDouble               _hangerWinchPosition;
    private NetworkTableDouble               _shooterHoodPosition;
    private NetworkTableDouble               _shooterHoodTarget;
    private NetworkTableDouble               _shooterRPM;
    private NetworkTableDouble               _shooterTargetRPM;
    private NetworkTableBoolean              _visionTargetFound;
    private NetworkTableDouble               _visionTargetAngle;
    private NetworkTableDouble               _visionTargetDistance;

    private double                           _ballpathLoadTimeout;

    private double                           _hangerReleaseWaitTime;
    private double                           _hangerWinchSpeed;
    private double                           _hangerUnhookSpeed;
    private double                           _hangerPrepSpeed;
    private double                           _hangerHighPullSpeed;
    private double                           _hangerArmAdjustAngle;

    private double                           _shooterFlywheelSpeed;
    private double                           _shooterEjectSpeed;
    private double                           _shooterEjectHoodPosition;

    private SendableChooser<CommandBase> _autoChooser;
    private SendableChooser<Double>          _autoDelayChooser;

    public Dashboard(Drive drive, Ballpath ballpath, Hanger hanger, Pickup pickup, Shooter shooter, Vision vision)
    {
        _drive                = drive;
        _ballpath             = ballpath;
        _hanger               = hanger;
        _pickup               = pickup;
        _shooter              = shooter;
        _vision               = vision;

        _ballCount            = NetworkTableDouble.networkTableDouble("Ballpath", "Cargo Count");
        _ballpathLowerActive  = NetworkTableString.networkTableString("Ballpath", "Ballpath Lower Track");
        _ballpathUpperActive  = NetworkTableString.networkTableString("Ballpath", "Ballpath Upper Track");

        _flModuleDistance     = NetworkTableDouble.networkTableDouble("Drive", "FL Distance");
        _flModuleRotation     = NetworkTableDouble.networkTableDouble("Drive", "FL Rotation");
        _frModuleDistance     = NetworkTableDouble.networkTableDouble("Drive", "FR Distance");
        _frModuleRotation     = NetworkTableDouble.networkTableDouble("Drive", "FR Rotation");
        _blModuleDistance     = NetworkTableDouble.networkTableDouble("Drive", "BL Distance");
        _blModuleRotation     = NetworkTableDouble.networkTableDouble("Drive", "BL Rotation");
        _brModuleDistance     = NetworkTableDouble.networkTableDouble("Drive", "BR Distance");
        _brModuleRotation     = NetworkTableDouble.networkTableDouble("Drive", "BR Rotation");

        _robotRotation        = NetworkTableDouble.networkTableDouble("Drive", "Gyro");

        _hangerReleased       = NetworkTableString.networkTableString("Hanger", "Brake State");
        _hangerArmPosition    = NetworkTableDouble.networkTableDouble("Hanger", "Arm Position");
        _hangerWinchPosition  = NetworkTableDouble.networkTableDouble("Hanger", "Winch Position");

        _pickupDeployed       = NetworkTableString.networkTableString("Pickup", "Deploy State");
        _pickupActive         = NetworkTableString.networkTableString("Pickup", "Motor State");

        _shooterTargetRPM     = NetworkTableDouble.networkTableDouble("Shooter", "Shooter Target RPM");
        _shooterRPM           = NetworkTableDouble.networkTableDouble("Shooter", "Shooter RPM");
        _shooterHoodTarget    = NetworkTableDouble.networkTableDouble("Shooter", "Hood Target");
        _shooterHoodPosition  = NetworkTableDouble.networkTableDouble("Shooter", "Hood Position");

        _visionTargetFound    = NetworkTableBoolean.networkTableBoolean("Vision", "Target Found");
        _visionTargetAngle    = NetworkTableDouble.networkTableDouble("Vision", "Angle");
        _visionTargetDistance = NetworkTableDouble.networkTableDouble("Vision", "Distance");

        // Robot Settings

        // Drive
        initializeDoubleSetting(Constants.Settings.DRIVE_FL_MODULE_OFFSET, "Drive", Constants.Drive.FL_MOTOR_OFFSET, _drive.getSwerveModule(Constants.Drive.FL_INDEX)::setRelativeZero);
        initializeDoubleSetting(Constants.Settings.DRIVE_FR_MODULE_OFFSET, "Drive", Constants.Drive.FR_MOTOR_OFFSET, _drive.getSwerveModule(Constants.Drive.FR_INDEX)::setRelativeZero);
        initializeDoubleSetting(Constants.Settings.DRIVE_BL_MODULE_OFFSET, "Drive", Constants.Drive.BL_MOTOR_OFFSET, _drive.getSwerveModule(Constants.Drive.BL_INDEX)::setRelativeZero);
        initializeDoubleSetting(Constants.Settings.DRIVE_BR_MODULE_OFFSET, "Drive", Constants.Drive.BR_MOTOR_OFFSET, _drive.getSwerveModule(Constants.Drive.BR_INDEX)::setRelativeZero);

        // Pickup
        initializeDoubleSetting(Constants.Settings.PICKUP_SPEED, "Pickup", Constants.Pickup.PICKUP_SPEED, _pickup::setPickupSpeed);

        // Ballpath
        initializeDoubleSetting(Constants.Settings.BALLPATH_LOAD_SPEED,   "Ballpath", Constants.Ballpath.BALLPATH_LOAD_SPEED,  _ballpath::setLoadSpeed);
        initializeDoubleSetting(Constants.Settings.BALLPATH_SHOOT_SPEED,  "Ballpath", Constants.Ballpath.BALLPATH_SHOOT_SPEED, _ballpath::setShootSpeed);
        initializeDoubleSetting(Constants.Settings.BALLPATH_LOAD_TIMEOUT, "Ballpath", Constants.Ballpath.LOAD_TIMEOUT,         this::setBallpathLoadTimeout);

        // Hanger
        initializeDoubleSetting(Constants.Settings.HANGER_RELEASE_WAIT_TIME, "Hanger", Constants.Hanger.RELEASE_WAIT_TIME, this::setHangerReleaseWaitTime);
        initializeDoubleSetting(Constants.Settings.HANGER_WINCH_SPEED,       "Hanger", Constants.Hanger.WINCH_SPEED,       this::setHangerWinchSpeed);
        initializeDoubleSetting(Constants.Settings.HANGER_UNHOOK_SPEED,      "Hanger", Constants.Hanger.UNHOOK_SPEED,      this::setHangerUnhookSpeed);
        initializeDoubleSetting(Constants.Settings.HANGER_PREP_SPEED,        "Hanger", Constants.Hanger.PREP_SPEED,        this::setHangerPrepSpeed);
        initializeDoubleSetting(Constants.Settings.HANGER_HIGH_PULL_SPEED,   "Hanger", Constants.Hanger.HIGH_PULL_SPEED,   this::setHangerHighPullSpeed);
        initializeDoubleSetting(Constants.Settings.HANGER_ARM_ADJUST_ANGLE,  "Hanger", Constants.Hanger.ARM_ADJUST_ANGLE,  this::setHangerArmAdjustAngle);

        // Shooter
        initializeDoubleSetting(Constants.Settings.SHOOTER_FLYWHEEL_SPEED,      "Shooter", Constants.Shooter.FLYWHEEL_SPEED,        this::setShooterFlywheelSpeed);
        initializeDoubleSetting(Constants.Settings.SHOOTER_EJECT_SPEED,         "Shooter", Constants.Shooter.EJECT_SPEED,           this::setShooterEjectSpeed);
        initializeDoubleSetting(Constants.Settings.SHOOTER_EJECT_HOOD_POSITION, "Shooter", Constants.Shooter.EJECT_HOOD_POSITION,   this::setShooterEjectHoodPosition);
        initializeDoubleSetting(Constants.Settings.SHOOTER_RPM_THRESHOLD,       "Shooter", Constants.Shooter.SHOOTER_RPM_THRESHOLD, _shooter::setRPMThreshold);
        initializeDoubleSetting(Constants.Settings.SHOOTER_HOOD_MAX_POSITION,   "Shooter", Constants.Shooter.HOOD_MAX_POSITION,     _shooter::setHoodMaxPosition);
        initializeDoubleSetting(Constants.Settings.SHOOTER_HOOD_MIN_POSITION,   "Shooter", Constants.Shooter.HOOD_MIN_POSITION,     _shooter::setHoodMinPosition);

        // Vision
        initializeDoubleSetting(Constants.Settings.VISION_LIMELIGHT_HEIGHT, "Vision", Constants.Vision.LIMELIGHT_HEIGHT, _vision::setLimelightHeight);
        initializeDoubleSetting(Constants.Settings.VISION_LIMELIGHT_ANGLE,  "Vision", Constants.Vision.LIMELIGHT_ANGLE,  _vision::setLimelightAngle);
        initializeDoubleSetting(Constants.Settings.VISION_TARGET_OFFSET,    "Vision", Constants.Vision.TARGET_OFFSET,    _vision::setTargetOffset);
        initializeDoubleSetting(Constants.Settings.VISION_ROTATE_DEADBAND,  "Vision", Constants.Vision.ROTATE_DEADBAND,  _vision::setRotateDeadband);

        // Autonomous
        _autoChooser = new SendableChooser<CommandBase>();
        _autoDelayChooser = new SendableChooser<Double>();

        _autoDelayChooser.setDefaultOption("0", 0.0);

        for (int i = 1; i < 7; i++)
        {
            _autoDelayChooser.addOption(String.valueOf(i), (double)i);
        }

        SmartDashboard.putData(_autoChooser);
        SmartDashboard.putData(_autoDelayChooser);
    }

    public void addDefaultAutonomous(String name, CommandBase command)
    {
        _autoChooser.setDefaultOption(name, command);
    }

    public void addAutonomous(String name, CommandBase command)
    {
        _autoChooser.addOption(name, command);
    }

    public CommandBase getSelectedAutonomous()
    {
        return _autoChooser.getSelected();
    }

    public void setFrontLeftModuleRotation(double rotation)
    {
        _flModuleRotation.set(round(rotation));
    }

    public void setFrontLeftModuleDistance(double distance)
    {
        _flModuleDistance.set(round(distance));
    }

    public void setFrontRightModuleRotation(double rotation)
    {
        _frModuleRotation.set(round(rotation));
    }

    public void setFrontRightModuleDistance(double distance)
    {
        _frModuleDistance.set(round(distance));
    }

    public void setBackLeftModuleRotation(double rotation)
    {
        _blModuleRotation.set(round(rotation));
    }

    public void setBackLeftModuleDistance(double distance)
    {
        _blModuleDistance.set(round(distance));
    }

    public void setBackRightModuleRotation(double rotation)
    {
        _brModuleRotation.set(round(rotation));
    }

    public void setBackRightModuleDistance(double distance)
    {
        _brModuleDistance.set(round(distance));
    }

    public void setRobotRotation(double rotation)
    {
        rotation = Vector.normalizeAngle(rotation);

        if (rotation > 180)
        {
            rotation -= 360;
        }

        _robotRotation.set(rotation);
    }

    public void setCargoCount(int ballCount)
    {
        _ballCount.set(ballCount);
    }

    public void setPickupDeployed(ExtendState state)
    {
        _pickupDeployed.set(state.toString());
    }

    public void setPickupActive(State state)
    {
        _pickupActive.set(state.toString());
    }

    public void setBallpathLowerState(State state)
    {
        _ballpathLowerActive.set(state.toString());
    }

    public void setBallpathUpperState(State state)
    {
        _ballpathUpperActive.set(state.toString());
    }

    public void setHangerBrakeState(boolean brakeActive)
    {
        _hangerReleased.set(brakeActive ? "Engaged" : "Released");
    }

    public void setHangerArmPosition(double position)
    {
        _hangerArmPosition.set(round(position));
    }

    public void setHangerWinchPosition(double position)
    {
        _hangerWinchPosition.set(round(position));
    }

    public void setShooterHoodPosition(double position)
    {
        _shooterHoodPosition.set(position);
    }

    public void setShooterHoodTarget(double target)
    {
        _shooterHoodTarget.set(target);
    }

    public void setShooterRPM(double RPM)
    {
        _shooterRPM.set((int)RPM);
    }

    public void setShooterTargetRPM(double targetRPM)
    {
        _shooterTargetRPM.set((int)targetRPM);
    }

    public double getAutoDelay()
    {
        return _autoDelayChooser.getSelected();
    }

    public void setVisionTargetFound(boolean found)
    {
        _visionTargetFound.set(found);
    }

    public void setVisionTargetAngle(double angle)
    {
        _visionTargetAngle.set(round(angle));
    }

    public void setVisionTargetDistance(double distance)
    {
        _visionTargetDistance.set(round(distance));
    }

    public void setHangerReleaseWaitTime(double newTime)
    {
        _hangerReleaseWaitTime = newTime;
    }

    public double getHangerReleaseWaitTime()
    {
        return _hangerReleaseWaitTime;
    }

    public void setHangerWinchSpeed(double newSpeed)
    {
        _hangerWinchSpeed = newSpeed;
    }

    public double getHangerWinchSpeed()
    {
        return _hangerWinchSpeed;
    }

    public void setHangerUnhookSpeed(double newSpeed)
    {
        _hangerUnhookSpeed = newSpeed;
    }

    public double getHangerUnhookSpeed()
    {
        return _hangerUnhookSpeed;
    }

    public void setHangerPrepSpeed(double newSpeed)
    {
        _hangerPrepSpeed = newSpeed;
    }

    public double getHangerPrepSpeed()
    {
        return _hangerPrepSpeed;
    }

    public void setHangerHighPullSpeed(double newSpeed)
    {
        _hangerHighPullSpeed = newSpeed;
    }

    public double getHangerHighPullSpeed()
    {
        return _hangerHighPullSpeed;
    }

    public void setHangerArmAdjustAngle(double newAngle)
    {
        _hangerArmAdjustAngle = newAngle;
    }

    public double getHangerArmAdjustAngle()
    {
        return _hangerArmAdjustAngle;
    }

    public void setBallpathLoadTimeout(double newTimeout)
    {
        _ballpathLoadTimeout = newTimeout;
    }

    public double getBallpathLoadTimeout()
    {
        return _ballpathLoadTimeout;
    }

    public void setShooterFlywheelSpeed(double newSpeed)
    {
        _shooterFlywheelSpeed = newSpeed;
    }

    public double getShooterFlywheelSpeed()
    {
        return _shooterFlywheelSpeed;
    }

    public void setShooterEjectSpeed(double newSpeed)
    {
        _shooterEjectSpeed = newSpeed;
    }

    public double getShooterEjectSpeed()
    {
        return _shooterEjectSpeed;
    }

    public void setShooterEjectHoodPosition(double newPosition)
    {
        _shooterEjectHoodPosition = newPosition;
    }

    public double getShooterEjectHoodPosition()
    {
        return _shooterEjectHoodPosition;
    }

    private double round(double value)
    {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    @Override
    public void periodic() 
    {
        setFrontLeftModuleRotation(Vector.normalizeAngle(_drive.getSwerveModule(Constants.Drive.FL_INDEX).getPosition()));
        setFrontLeftModuleDistance(_drive.getSwerveModule(Constants.Drive.FL_INDEX).getDriveMotor().getPositionSensor().get());
        setFrontRightModuleRotation(Vector.normalizeAngle(_drive.getSwerveModule(Constants.Drive.FR_INDEX).getPosition()));
        setFrontRightModuleDistance(_drive.getSwerveModule(Constants.Drive.FR_INDEX).getDriveMotor().getPositionSensor().get());
        setBackLeftModuleRotation(Vector.normalizeAngle(_drive.getSwerveModule(Constants.Drive.BL_INDEX).getPosition()));
        setBackLeftModuleDistance(_drive.getSwerveModule(Constants.Drive.BL_INDEX).getDriveMotor().getPositionSensor().get());
        setBackRightModuleRotation(Vector.normalizeAngle(_drive.getSwerveModule(Constants.Drive.BR_INDEX).getPosition()));
        setBackRightModuleDistance(_drive.getSwerveModule(Constants.Drive.BR_INDEX).getDriveMotor().getPositionSensor().get());
        setRobotRotation(_drive.getHeading());

        setPickupDeployed(_pickup.getDeployState());
        setPickupActive(_pickup.getMotorState());

        setCargoCount(_ballpath.getCargoCount());
        setBallpathLowerState(_ballpath.getLowerTrackState());
        setBallpathUpperState(_ballpath.getUpperTrackState());

        setHangerBrakeState(_hanger.isRatchetEngaged());
        setHangerArmPosition(_hanger.getArmPosition());
        setHangerWinchPosition(_hanger.getWinchPosition());

        setShooterHoodPosition(_shooter.getHoodPosition());
        setShooterHoodTarget(_shooter.getHoodSetpoint());
        setShooterRPM(_shooter.getShooterRPM());
        setShooterTargetRPM(_shooter.getShooterTargetRPM());

        setVisionTargetFound(_vision.targetFound());
        setVisionTargetAngle(_vision.getTargetAngle());
        setVisionTargetDistance(_vision.getTargetDistance());
    }

    private NetworkTableDouble initializeDoubleSetting(String setting, String table, double initialValue, Consumer<Double> onChanged)
    {
        // Initialize the preference
        Preferences.initDouble(setting, initialValue);
        
        // Create the network table entry
        NetworkTableDouble ntd = NetworkTableDouble.networkTableDouble(table, setting);
        ntd.onChanged(newValue -> { 
            Preferences.setDouble(setting, newValue);
            onChanged.accept(newValue);
        });
        ntd.set(Preferences.getDouble(setting, initialValue));

        // Update the robot setting
        onChanged.accept(Preferences.getDouble(setting, initialValue));

        return ntd;
    }
}
