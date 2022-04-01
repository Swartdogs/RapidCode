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
    }

    public void addDefaultAutonomous(String name, SwartdogCommand command)
    {
        _dashTab.addDefaultAutonomous(name, command);
    }

    public void addAutonomous(String name, SwartdogCommand command)
    {
        _dashTab.addAutonomous(name, command);
    }

    public SwartdogCommand getSelectedAutonomous()
    {
        return _dashTab.getSelectedAutonomous();
    }

    public void addCamera(VideoSource videoSource)
    {
        _dashTab.addCamera(11, 2, 11, 10, videoSource, Map.of("Show crosshair", true, "Show controls", false, "Rotation", "90 degrees clockwise", "Title", "Camera"));
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

        setShooterHoodPosition(_shooter.getHoodPosition());
        setShooterHoodTarget(_shooter.getHoodSetpoint());
        // setShooterRPM(_shooter.getShooterMotor());
        // setShooterOn(_shooter.isShooterOn());
    }
}
