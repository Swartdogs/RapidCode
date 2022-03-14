package frc.robot.subsystems;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import frc.robot.Constants;
import frc.robot.abstraction.*;
import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.abstraction.Enumerations.State;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Vector;

public class Dashboard extends SwartdogSubsystem
{
    // Subsystems
    private Drive               _drive;
    private Ballpath            _ballpath;
    private Hanger              _hanger;
    private Pickup              _pickup;
    private Shooter             _shooter;

    // Robot values
    private ShuffleboardTab     _dashTab;
    private NetworkTableDouble  _flModuleRotation;
    private NetworkTableDouble  _flModuleDistance;
    private NetworkTableDouble  _frModuleRotation;
    private NetworkTableDouble  _frModuleDistance;
    private NetworkTableDouble  _blModuleRotation;
    private NetworkTableDouble  _blModuleDistance;
    private NetworkTableDouble  _brModuleRotation;
    private NetworkTableDouble  _brModuleDistance;
    private NetworkTableDouble  _robotRotation;
    private NetworkTableDouble  _ballCount;
    private NetworkTableString  _pickupDeployed;
    private NetworkTableString  _pickupActive;
    private NetworkTableString  _ballpathLowerActive;
    private NetworkTableString  _ballpathUpperActive;
    private NetworkTableString  _hangerReleased;
    private NetworkTableString  _hangerLatched;
    private NetworkTableDouble  _hangerPosition;
    private NetworkTableDouble  _shooterHoodPosition;
    private NetworkTableDouble  _shooterHoodTarget;
    private NetworkTableDouble  _shooterRPM;
    private NetworkTableBoolean _shooterOn;

    // Robot settings
    private ShuffleboardTab     _settingsTab;
    private NetworkTableDouble  _driveFLModuleOffset;
    private NetworkTableDouble  _driveFRModuleOffset;
    private NetworkTableDouble  _driveBLModuleOffset;
    private NetworkTableDouble  _driveBRModuleOffset;
    private NetworkTableDouble  _hangerMinPosition;
    private NetworkTableDouble  _hangerMaxPosition;
    private NetworkTableDouble  _hangerSpeed;
    private NetworkTableDouble  _pickupSpeed;
    private NetworkTableDouble  _ballpathSpeed;
    private NetworkTableDouble  _hoodMinPosition;
    private NetworkTableDouble  _hoodNearPosition;
    private NetworkTableDouble  _hoodFarPosition;
    private NetworkTableDouble  _hoodMaxPosition;
    private NetworkTableDouble  _shooterNearSpeed;
    private NetworkTableDouble  _shooterFarSpeed;

    public Dashboard(Drive drive, Ballpath ballpath, Hanger hanger, Pickup pickup, Shooter shooter)
    {
        _drive    = drive;
        _ballpath = ballpath;
        _hanger   = hanger;
        _pickup   = pickup;
        _shooter  = shooter;

        _dashTab = ShuffleboardTab.shuffleboardTab("Dashboard");

        _ballCount = _dashTab.addDoubleWidget("Cargo Count", 0, 23, 8, 10, 4, BuiltInWidgets.kDial, Map.of("Min", 0, "Max", Constants.Ballpath.MAX_CARGO_COUNT, "Show value", true));
        _dashTab.addAutonomousChooser(11, 0, 11, 2, BuiltInWidgets.kComboBoxChooser);

        ShuffleboardLayout ballpathLayout = _dashTab.addShuffleboardLayout("Ballpath", BuiltInLayouts.kList, 28, 0, 5, 3, Map.of("Label position", "LEFT"));
        _ballpathLowerActive = ballpathLayout.addStringWidget("Ballpath Lower Track", "On", BuiltInWidgets.kTextView, null);
        _ballpathUpperActive = ballpathLayout.addStringWidget("Ballpath Upper Track", "On", BuiltInWidgets.kTextView, null);

        ShuffleboardLayout driveLayout = _dashTab.addShuffleboardLayout("Drive", BuiltInLayouts.kGrid, 0, 0, 10, 7, Map.of("Number of columns", 2, "Number of rows", 2, "Label position", "LEFT"));
        
        ShuffleboardLayout fl = driveLayout.addShuffleboardLayout("Front Left Module", BuiltInLayouts.kList, 0, 0, Map.of("Label position", "LEFT"));
        _flModuleDistance = fl.addDoubleWidget("FL Distance", 0, BuiltInWidgets.kTextView, null);
        _flModuleRotation = fl.addDoubleWidget("FL Rotation", 0, BuiltInWidgets.kTextView, null);
        
        ShuffleboardLayout fr = driveLayout.addShuffleboardLayout("Front Right Module", BuiltInLayouts.kList, 1, 0, Map.of("Label position", "LEFT"));
        _frModuleDistance = fr.addDoubleWidget("FR Distance", 0, BuiltInWidgets.kTextView, null);
        _frModuleRotation = fr.addDoubleWidget("FR Rotation", 0, BuiltInWidgets.kTextView, null);
        
        ShuffleboardLayout bl = driveLayout.addShuffleboardLayout("Back Left Module", BuiltInLayouts.kList, 0, 1, Map.of("Label position", "LEFT"));
        _blModuleDistance = bl.addDoubleWidget("BL Distance", 0, BuiltInWidgets.kTextView, null);
        _blModuleRotation = bl.addDoubleWidget("BL Rotation", 0, BuiltInWidgets.kTextView, null);
        
        ShuffleboardLayout br = driveLayout.addShuffleboardLayout("Back Right Module", BuiltInLayouts.kList, 1, 1, Map.of("Label position", "LEFT"));
        _brModuleDistance = br.addDoubleWidget("BR Distance", 0, BuiltInWidgets.kTextView, null);
        _brModuleRotation = br.addDoubleWidget("BR Rotation", 0, BuiltInWidgets.kTextView, null);

        _robotRotation = _dashTab.addDoubleWidget("Gyro", 0, 0, 7, 10, 5, BuiltInWidgets.kNumberBar, Map.of("Min", -180, "Max", 180, "Center", 0, "Num tick marks", 7, "Show text", true));

        ShuffleboardLayout hangerLayout = _dashTab.addShuffleboardLayout("Hanger", BuiltInLayouts.kList, 28, 3, 5, 5, Map.of("Label position", "LEFT"));
        _hangerReleased = hangerLayout.addStringWidget("Deploy State",  "Deployed", BuiltInWidgets.kTextView, null);
        _hangerLatched  = hangerLayout.addStringWidget("Ratchet State", "Latched",  BuiltInWidgets.kTextView, null);
        _hangerPosition = hangerLayout.addDoubleWidget("Position",      0,          BuiltInWidgets.kTextView, Map.of("Color when false", "Lime", "Color when true", "Red"));

        ShuffleboardLayout pickupLayout = _dashTab.addShuffleboardLayout("Pickup", BuiltInLayouts.kList, 23, 0, 5, 3, Map.of("Label position", "LEFT"));
        _pickupDeployed = pickupLayout.addStringWidget("Deploy State", "Deployed", BuiltInWidgets.kTextView, null);
        _pickupActive   = pickupLayout.addStringWidget("Motor State",  "Active",   BuiltInWidgets.kTextView, null);

        ShuffleboardLayout shooterLayout = _dashTab.addShuffleboardLayout("Shooter", BuiltInLayouts.kList, 23, 3, 5, 5, Map.of("Label position", "LEFT"));
        _shooterOn           = shooterLayout.addBooleanWidget("Shooter On",   false, BuiltInWidgets.kBooleanBox, Map.of("Color when true", "Lime", "Color when false", "Red"));
        _shooterRPM          = shooterLayout.addDoubleWidget("Shooter RPM",   0,     BuiltInWidgets.kTextView,   null);
        _shooterHoodTarget   = shooterLayout.addDoubleWidget("Hood Target",   0,     BuiltInWidgets.kTextView,   null);
        _shooterHoodPosition = shooterLayout.addDoubleWidget("Hood Position", 0,     BuiltInWidgets.kTextView,   null);

        // ShuffleboardLayout shootSetting = settingsTab.addShuffleboardLayout("Shooter", BuiltInLayouts.kList, 1, 1, 11, 6, Map.of("Label position", "LEFT"));
        // _hoodMinPosition  = shootSetting.addDoubleWidget("Hood Low Limit",     Constants.DEFAULT_HOOD_MIN_POSITION,  BuiltInWidgets.kTextView, null);
        // _hoodMaxPosition  = shootSetting.addDoubleWidget("Hood High Limit",    Constants.DEFAULT_HOOD_MAX_POSITION,  BuiltInWidgets.kTextView, null);
        // _hoodNearPosition = shootSetting.addDoubleWidget("Hood Near Position", Constants.DEFAULT_HOOD_NEAR_TARGET,   BuiltInWidgets.kTextView, null);
        // _hoodFarPosition  = shootSetting.addDoubleWidget("Hood Far Position",  Constants.DEFAULT_HOOD_FAR_TARGET,    BuiltInWidgets.kTextView, null);
        // _shooterNearSpeed = shootSetting.addDoubleWidget("Shooter Near Speed", Constants.DEFAULT_SHOOTER_NEAR_SPEED, BuiltInWidgets.kTextView, null);
        // _shooterFarSpeed  = shootSetting.addDoubleWidget("Shooter Far Speed",  Constants.DEFAULT_SHOOTER_FAR_SPEED,  BuiltInWidgets.kTextView, null);

        // ShuffleboardLayout ballPathSetting = settingsTab.addShuffleboardLayout("Ball Path", BuiltInLayouts.kList, 1, 7, 11, 6, Map.of("Label position", "LEFT"));
        // _ballPathJamTime  = ballPathSetting.addDoubleWidget("Ball Path Jam Timeout", Constants.DEFAULT_BALLPATH_JAM_TIME,  BuiltInWidgets.kTextView, null);
        // _ballPathSpeed    = ballPathSetting.addDoubleWidget("Ball Path Speed",       Constants.DEFAULT_BALLPATH_SPEED,     BuiltInWidgets.kTextView, null);
        // _ballPathRampMin  = ballPathSetting.addDoubleWidget("Ball Path Ramp Min",    Constants.DEFAULT_BALLPATH_RAMP_MIN,  BuiltInWidgets.kTextView, null);
        // _ballPathRampStep = ballPathSetting.addDoubleWidget("Ball Path Ramp Step",   Constants.DEFAULT_BALLPATH_RAMP_STEP, BuiltInWidgets.kTextView, null);

        // ShuffleboardLayout hangerSetting = settingsTab.addShuffleboardLayout("Hanger", BuiltInLayouts.kList, 12, 1, 11, 6, Map.of("Label position", "LEFT"));
        // _hangerSpeed       = hangerSetting.addDoubleWidget("Hanger Speed",      Constants.DEFAULT_HANGER_SPEED,        BuiltInWidgets.kTextView, null);
        // _hangerMinPosition = hangerSetting.addDoubleWidget("Hanger Low Limit",  Constants.DEFAULT_HANGER_MIN_POSITION, BuiltInWidgets.kTextView, null);
        // _hangerMaxPosition = hangerSetting.addDoubleWidget("Hanger High Limit", Constants.DEFAULT_HANGER_MAX_POSITION, BuiltInWidgets.kTextView, null);

        // ShuffleboardLayout pickupSetting = settingsTab.addShuffleboardLayout("Pickup", BuiltInLayouts.kList, 12, 7, 11, 6, Map.of("Label position", "LEFT"));
        // _pickupSpeed     = pickupSetting.addDoubleWidget("Pickup Speed",      Constants.DEFAULT_PICKUP_SPEED,      BuiltInWidgets.kTextView, null);
        // _pickupClogTime  = pickupSetting.addDoubleWidget("Pickup Clog Time",  Constants.DEFAULT_PICKUP_CLOG_TIME,  BuiltInWidgets.kTextView, null);
        // _pickupStowDelay = pickupSetting.addDoubleWidget("Pickup Stow Delay", Constants.DEFAULT_PICKUP_STOW_DELAY, BuiltInWidgets.kTextView, null);

        // ShuffleboardLayout driveSetting = settingsTab.addShuffleboardLayout("Drive", BuiltInLayouts.kList, 23, 1, 11, 6, Map.of("Label position", "LEFT"));
        // _driveFLModuleOffset = driveSetting.addDoubleWidget("Drive FL Module Offset", Constants.DEFAULT_FL_MODULE_OFFSET, BuiltInWidgets.kTextView, null);
        // _driveFRModuleOffset = driveSetting.addDoubleWidget("Drive FR Module Offset", Constants.DEFAULT_FR_MODULE_OFFSET, BuiltInWidgets.kTextView, null);
        // _driveBLModuleOffset = driveSetting.addDoubleWidget("Drive BL Module Offset", Constants.DEFAULT_BL_MODULE_OFFSET, BuiltInWidgets.kTextView, null);
        // _driveBRModuleOffset = driveSetting.addDoubleWidget("Drive BR Module Offset", Constants.DEFAULT_BR_MODULE_OFFSET, BuiltInWidgets.kTextView, null);
    }

    public void addCamera(VideoSource videoSource)
    {
        _dashTab.addCamera(11, 2, 11, 10, videoSource, Map.of("Show crosshair", false, "Show controls", false, "Title", "Camera"));
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

    public void setHangerReleased(boolean released)
    {
        _hangerReleased.set(released ? "Deployed" : "Stowed");
    }

    public void setHangerLatched(boolean latched)
    {
        _hangerLatched.set(latched ? "Latched" : "Released");
    }

    public void setHangerPosition(double position)
    {
        _hangerPosition.set(position);
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
        _shooterRPM.set(RPM);
    }

    public void setShooterOn(boolean shooterOn)
    {
        _shooterOn.set(shooterOn);
    }

    public double getDriveFLModuleOffset()
    {
        return _driveFLModuleOffset.get();
    }

    public double getDriveFRModuleOffset()
    {
        return _driveFRModuleOffset.get();
    }

    public double getDriveBLModuleOffset()
    {
        return _driveBLModuleOffset.get();
    }

    public double getDriveBRModuleOffset()
    {
        return _driveBRModuleOffset.get();
    }

    public double getHangerMinPosition()
    {
        return _hangerMinPosition.get();
    }

    public double getHangerMaxPosition()
    {
        return _hangerMaxPosition.get();
    }

    public double getHangerSpeed()
    {
        return _hangerSpeed.get();
    }

    public double getPickupSpeed()
    {
        return _pickupSpeed.get();
    }

    public double getBallpathSpeed()
    {
        return _ballpathSpeed.get();
    }

    public double getHoodMinPosition()
    {
        return _hoodMinPosition.get();
    }

    public double getHoodNearPosition()
    {
        return _hoodNearPosition.get();
    }

    public double getHoodFarPosition()
    {
        return _hoodFarPosition.get();
    }

    public double getHoodMaxPosition()
    {
        return _hoodMaxPosition.get();
    }

    public double getShooterNearSpeed()
    {
        return _shooterNearSpeed.get();
    }

    public double getShooterFarSpeed()
    {
        return _shooterFarSpeed.get();
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

        // setHangerReleased(_hanger.isHangerReleased());
        // setHangerLatched(_hanger.isHangerLatched());
        // setHangerPosition(_hanger.getPosition());

        // setShooterHoodPosition(_shooter.getHoodPosition());
        // setShooterHoodTarget(_shooter.getHoodSetpoint());
        // setShooterRPM(_shooter.getShooterMotor());
        // setShooterOn(_shooter.isShooterOn());
    }
}
