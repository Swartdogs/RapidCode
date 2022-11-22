package frc.robot;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.DoubleUnaryOperator;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.subsystems.drive.Vector;

public final class Constants
{
    public static final double LOOPS_PER_SECOND    = 50;

    public static enum GameMode
    {
        Init,
        Disabled,
        Autonomous,
        Teleop,
        Test
    }
    
    public static class Assignments
    {
        // CAN IDs
        public static final int PNEUMATICS_MODULE_CAN_ID = 2;

        public static final int FL_DRIVE_CAN_ID          = 3;
        public static final int FL_ROTATE_CAN_ID         = 4;
        public static final int FR_DRIVE_CAN_ID          = 5;
        public static final int FR_ROTATE_CAN_ID         = 6;
        public static final int BL_DRIVE_CAN_ID          = 7;
        public static final int BL_ROTATE_CAN_ID         = 8;
        public static final int BR_DRIVE_CAN_ID          = 9;
        public static final int BR_ROTATE_CAN_ID         = 10;

        public static final int LEFT_BALLPATH_CAN_ID     = 11;
        public static final int RIGHT_BALLPATH_CAN_ID    = 12;
        public static final int VERTICAL_BALLPATH_CAN_ID = 13;

        public static final int LOW_SHOOTER_CAN_ID       = 15;
        public static final int HIGH_SHOOTER_CAN_ID      = 16;
        public static final int SHOOTER_HOOD_CAN_ID      = 17;

        public static final int WINCH_1_CAN_ID           = 18;
        public static final int ARM_CAN_ID               = 19;
        public static final int WINCH_2_CAN_ID           = 20;

        // Solenoid Ports
        public static final int HANGER_RATCHET_SOL_PORT = 5;
        public static final int PICKUP_DEPLOY_SOL_PORT  = 6;

        // Pneumatics Module Type
        public static final PneumaticsModuleType MODULE_TYPE = PneumaticsModuleType.CTREPCM;
        
        // Digital inputs
        public static final int PICKUP_SENSOR_DIO_PORT  = 0;
        public static final int SHOOTER_SENSOR_DIO_PORT = 1;

        public static final int WINCH_SENSOR_DIO_PORT   = 9;
        public static final int ARM_SENSOR_DIO_PORT     = 3;
    
        // Analog Inputs
        public static final int FL_POSITION_AIO_PORT   = 0;
        public static final int FR_POSITION_AIO_PORT   = 1;
        public static final int BL_POSITION_AIO_PORT   = 2;
        public static final int BR_POSITION_AIO_PORT   = 3;

        public static final int HOOD_POSITION_AIO_PORT = 4;

        // PWM Ports
        public static final int PICKUP_PWM_PORT = 9;

        // Joysticks
        public static final int DRIVE_JOYSTICK    = 0;
        public static final int CO_DRIVE_JOYSTICK = 1;
        public static final int BUTTON_BOX        = 2;
    }

    public static class Ballpath
    {
        public static final double BALLPATH_LOAD_SPEED  = 1.0;
        public static final double BALLPATH_SHOOT_SPEED = 0.85;
        public static final int    MAX_CARGO_COUNT      = 2;
        public static final double LOAD_TIMEOUT         = 6;
    }

    public static class Hanger
    {
        public static final double RELEASE_WAIT_TIME        = 0.1;
        public static final double WINCH_SPEED              = 0.4;
        public static final double UNHOOK_SPEED             = 0.2;
        public static final double PREP_SPEED               = 0.6;
        public static final double HIGH_PULL_SPEED          = 0.65;
        public static final double ZERO_ANGLE               = 0.3;
        public static final double RIGHT_ANGLE              = 0.557;
        public static final double ARM_SLOPE                = 90.0 / (RIGHT_ANGLE - ZERO_ANGLE);
        public static final double MIN_WINCH_LENGTH         = 8.75;
        public static final double MIN_WINCH_POSITION       = 0.0585;
        public static final double MAX_WINCH_LENGTH         = 47;
        public static final double MAX_WINCH_POSITION       = 0.7513;
        public static final double WINCH_SLOPE              = (MAX_WINCH_LENGTH - MIN_WINCH_LENGTH) / (MAX_WINCH_POSITION - MIN_WINCH_POSITION);
        public static final double ARM_SEGMENT_1_LENGTH     = 22.5;
        public static final double ARM_SEGMENT_2_LENGTH     = 25;
        public static final double WINCH_ANGLE_DENOMINATOR  = 1.0 / (2 * ARM_SEGMENT_1_LENGTH);
        public static final double WINCH_FACTOR             = ((ARM_SEGMENT_1_LENGTH * ARM_SEGMENT_1_LENGTH) - (ARM_SEGMENT_2_LENGTH * ARM_SEGMENT_2_LENGTH)) * WINCH_ANGLE_DENOMINATOR;
        public static final double ARM_ADJUST_ANGLE         = 20;

        public enum ArmPosition
        {
            Stow            (new Vector( 9,  -90, false), true,  true),
            LowRung         (new Vector(30,    0, false), true,  true),
            MidRung         (new Vector(41,    0, false), true,  true),
            Reach           (new Vector(44,   20, false), true,  true),
            Hook            (new Vector(42.5, 16, false), true,  true),
            TraversalReach  (new Vector(44.5, 20, false), true,  true),
            TraversalHook   (new Vector(43,   16, false), true,  true),
            Unhook          (new Vector(24,  -10, false), true,  true),
            MidPull         (new Vector(14,  -15, false), true,  true),
            HighPull        (new Vector(14,    0, false), false, true),
            TraversalPull   (new Vector(28,    0, false), false, false);

            private Vector  _position;
            private boolean _runPIDBefore;
            private boolean _runPIDAfter;

            private ArmPosition(Vector position, boolean runPIDBefore, boolean runPIDAfter)
            {
                _position     = position;
                _runPIDBefore = runPIDBefore;
                _runPIDAfter  = runPIDAfter;
            }

            public Vector getPosition()
            {
                return _position;
            }

            public boolean runPIDBefore()
            {
                return _runPIDBefore;
            }

            public boolean runPIDAfter()
            {
                return _runPIDAfter;
            }
        }
    }
    
    public static class Pickup
    {
        public static final double PICKUP_SPEED = 0.64;
    }

    public static class Shooter
    {
        public static final double FLYWHEEL_SPEED                = 6379;
        public static final double EJECT_SPEED                   = 2500;
        public static final double EJECT_HOOD_POSITION           = 1400;
        public static final double SHOOTER_RPM_THRESHOLD         = 0.025;
        public static final Vector HUB_POSITION                  = new Vector(0,0); // the center of the coordinate system is at the hub

        public static final double HOOD_MAX_POSITION = 1720;
        public static final double HOOD_MIN_POSITION = 447;

        public enum RobotPosition
        {
            NearLaunchpad(4684, 785, true, 0, 0, true), 
            Fender(3986, 1643, true, 2109, 1408, false),
            Tarmac(4036, 1203, true, 2912, 741, false),
            Position1(3986, 1320, true, 2686, 996, false),
            CargoRing(4260, 888, true, 0, 0, true);

            private double _upperHubShooterRPM;
            private double _upperHubHoodPosition;
            private boolean _upperHubWait;
            private double _lowerHubShooterRPM;
            private double _lowerHubHoodPosition;
            private boolean _lowerHubWait;

            private RobotPosition(double upperHubShooterRPM, double upperHubHoodPosition, boolean upperHubWait, double lowerHubShooterRPM, double lowerHubHoodPosition, boolean lowerHubWait)
            {
                _upperHubShooterRPM   = upperHubShooterRPM;
                _upperHubHoodPosition = upperHubHoodPosition;
                _upperHubWait         = upperHubWait;
                _lowerHubShooterRPM   = lowerHubShooterRPM;
                _lowerHubHoodPosition = lowerHubHoodPosition;
                _lowerHubWait         = lowerHubWait;
            }

            public double getShooterRPM(TargetPosition targetPosition)
            {
                double rpm = 0;

                switch (targetPosition)
                {
                    case UpperHub:
                        rpm = _upperHubShooterRPM;
                        break;

                    case LowerHub:
                        rpm = _lowerHubShooterRPM;
                        break;
                }

                return rpm;
            }

            public double getHoodPosition(TargetPosition targetPosition)
            {
                double position = 0;

                switch (targetPosition)
                {
                    case UpperHub:
                        position = _upperHubHoodPosition;
                        break;

                    case LowerHub:
                        position = _lowerHubHoodPosition;
                        break;
                }

                return position;
            }

            public boolean useWait(TargetPosition targetPosition)
            {
                boolean wait = true;

                switch (targetPosition)
                {
                    case UpperHub:
                        wait = _upperHubWait;
                        break;

                    case LowerHub:
                        wait = _lowerHubWait;
                        break;
                }

                return wait;
            }

        }

        public enum TargetPosition
        {
            UpperHub,
            LowerHub
        }

        private static final Map<Double, Double> SHOOTER_SPEEDS = Map.of
        (
            61.25,  3862.0,
            81.21,  4211.0, 
            121.76, 4410.0, 
            163.46, 4858.0, 
            206.42, 5133.0, 
            245.13, 5531.0 
        );
        
        public static final DoubleUnaryOperator SHOOTER_SPEED_LOOKUP = (distance) ->
        {
            TreeMap<Double, Double> map = new TreeMap<Double, Double>(SHOOTER_SPEEDS);

            return lookup(distance, map);
        }; 
        
        private static final Map<Double, Double> SHOOTER_HOOD_POSITIONS = Map.of
        (
            61.25,  1282.0,
            81.21,  1267.0, 
            121.76, 1020.0, 
            163.46, 877.0, 
            206.42, 743.0, 
            245.13, 642.0 
        );

        public static final DoubleUnaryOperator SHOOTER_HOOD_LOOKUP = (distance) -> 
        {
            TreeMap<Double, Double> map = new TreeMap<Double, Double>(SHOOTER_HOOD_POSITIONS);

            return lookup(distance, map);
        }; 
    
        private static double lookup(double distance, NavigableMap<Double, Double> table) 
        {
            Entry<Double, Double> start = table.floorEntry(distance);
            Entry<Double, Double> end   = table.ceilingEntry(distance);

            double output = 0;

            if (start == null)
            {
                output = table.firstEntry().getValue();
            }

            else if (end == null)
            {
                output = table.lastEntry().getValue();
            }
            
            else if (start.getKey() == end.getKey())
            {
                output = start.getValue();
            }

            else
            {
                output = ((start.getValue() * (end.getKey() - distance)) + (end.getValue() * (distance - start.getKey()))) / (end.getKey() - start.getKey());
            }

            return output;
        }
    }

    public static class RobotLog
    {
        public static final int NUM_DECIMAL_PLACES_IN_TIME = 2;
        public static final int NUM_DIGITS_IN_TIME         = 8;
        public static final int HEADING_WIDTH              = 80;
    }
    
    public static class Drive
    {
        public static final int    FL_INDEX             = 0;
        public static final int    FR_INDEX             = 1;
        public static final int    BL_INDEX             = 2;
        public static final int    BR_INDEX             = 3;

        public static final double FL_MOTOR_OFFSET      = 239.75;
        public static final double FR_MOTOR_OFFSET      =  33.75;
        public static final double BL_MOTOR_OFFSET      =  65.5;
        public static final double BR_MOTOR_OFFSET      =  78.5;
        
        public static final double MODULE_ROTATE_SCALE  = 360 / 0.92;
        public static final double MODULE_ROTATE_OFFSET = (360 - MODULE_ROTATE_SCALE) / 2.0;
        public static final double ODOMETRY_SCALE       = (48.75) /* inches */ / (55854.18) /* encoder counts */;

        public static final double ALIGN_ROTATE_SPEED   = 0.7;

        public static final double FIELD_ANGLE          = 21;
        public static final Vector FIELD_RESET_POSITION = new Vector(-18.47, -48.23);
    }

    public static class Vision
    {
        public static final double LIMELIGHT_LED_OFF          = 1;
        public static final double LIMELIGHT_LED_ON           = 3;

        public static final double GRASSHOPPER_PIPELINE       = 2;

        public static final double LIMELIGHT_TARGET_NOT_FOUND = 0;

        public static final double LIMELIGHT_HEIGHT           = 17.0;
        public static final double TARGET_HEIGHT              = 102.5;
        public static final double TARGET_HEIGHT_DELTA        = TARGET_HEIGHT - LIMELIGHT_HEIGHT;
        public static final double LIMELIGHT_ANGLE            = 35;

        public static final double TARGET_OFFSET              = 0;

        public static final double ROTATE_DEADBAND            = 1.0;
    }

    public static class Settings
    {
        public static final String DRIVE_FL_MODULE_OFFSET      = "DriveFLModuleOffset";
        public static final String DRIVE_FR_MODULE_OFFSET      = "DriveFRModuleOffset";
        public static final String DRIVE_BL_MODULE_OFFSET      = "DriveBLModuleOffset";
        public static final String DRIVE_BR_MODULE_OFFSET      = "DriveBRModuleOffset";

        public static final String PICKUP_SPEED                = "PickupSpeed";

        public static final String BALLPATH_LOAD_SPEED         = "BallpathLoadSpeed";
        public static final String BALLPATH_SHOOT_SPEED        = "BallpathShootSpeed";
        public static final String BALLPATH_LOAD_TIMEOUT       = "BallpathLoadTimeout";

        public static final String HANGER_RELEASE_WAIT_TIME    = "HangerReleaseWaitTime";
        public static final String HANGER_WINCH_SPEED          = "HangerWinchSpeed";
        public static final String HANGER_UNHOOK_SPEED         = "HangerUnhookSpeed";
        public static final String HANGER_PREP_SPEED           = "HangerPrepSpeed";
        public static final String HANGER_HIGH_PULL_SPEED      = "HangerHighPullSpeed";
        public static final String HANGER_ZERO_ANGLE           = "HangerArmZeroAnglePosition";
        public static final String HANGER_RIGHT_ANGLE          = "HangerArmRightAnglePosition";
        public static final String HANGER_MIN_WINCH_LENGTH     = "HangerMinimumWinchLength";
        public static final String HANGER_MIN_WINCH_POSITION   = "HangerMinimumWinchPosition";
        public static final String HANGER_MAX_WINCH_LENGTH     = "HangerMaximumWinchLength";
        public static final String HANGER_MAX_WINCH_POSITION   = "HangerMaximumWinchPosition";
        public static final String HANGER_ARM_ADJUST_ANGLE     = "HangerArmAdjustAngle";

        public static final String SHOOTER_FLYWHEEL_SPEED      = "ShooterFlywheelSpeed";
        public static final String SHOOTER_EJECT_SPEED         = "ShooterEjectSpeed";
        public static final String SHOOTER_EJECT_HOOD_POSITION = "ShooterEjectHoodPosition";
        public static final String SHOOTER_RPM_THRESHOLD       = "ShooterRPMThreshold";
        public static final String SHOOTER_HOOD_MAX_POSITION   = "ShooterMaximumHoodPosition";
        public static final String SHOOTER_HOOD_MIN_POSITION   = "ShooterMinimumHoodPosition";

        public static final String VISION_LIMELIGHT_HEIGHT     = "VisionLimelightHeight";
        public static final String VISION_LIMELIGHT_ANGLE      = "VisionLimelightAngle";
        public static final String VISION_TARGET_OFFSET        = "VisionTargetOffset";
        public static final String VISION_ROTATE_DEADBAND      = "VisionRotateDeadband";
    }

    public static class Testing
    {
        public static final double EPSILON = 1e-6;
    }
}
