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
        public static final double HOOK_WAIT_TIME           = 0.3;
        public static final double RESET_WAIT_TIME          = 0.1;
        public static final double WINCH_SPEED              = 0.4;
        public static final double UNHOOK_SPEED             = 0.2;
        public static final double PREP_SPEED               = 0.6;
        public static final double HIGH_PULL_SPEED          = 0.65;
        public static final double ZERO_ANGLE               = 0.3;
        public static final double RIGHT_ANGLE              = 0.557;
        public static final double ARM_SLOPE                = 90.0 / (RIGHT_ANGLE - ZERO_ANGLE);
        public static final double MIN_WINCH_LENGTH         = 8;
        public static final double MIN_WINCH_POSITION       = 0.0732;
        public static final double MAX_WINCH_LENGTH         = 44.375;
        public static final double MAX_WINCH_POSITION       = 0.702;
        public static final double WINCH_SLOPE              = (MAX_WINCH_LENGTH - MIN_WINCH_LENGTH) / (MAX_WINCH_POSITION - MIN_WINCH_POSITION);
        public static final double ARM_SEGMENT_1_LENGTH     = 22.5;
        public static final double ARM_SEGMENT_2_LENGTH     = 25;
        public static final double WINCH_ANGLE_DENOMINATOR  = 1.0 / (2 * ARM_SEGMENT_1_LENGTH);
        public static final double WINCH_FACTOR             = ((ARM_SEGMENT_1_LENGTH * ARM_SEGMENT_1_LENGTH) - (ARM_SEGMENT_2_LENGTH * ARM_SEGMENT_2_LENGTH)) * WINCH_ANGLE_DENOMINATOR;
        public static final double ARM_ADJUST_ANGLE         = 20;

        public enum ArmPosition
        {
            Stow            (new Vector( 9, -85, false), true,  true),
            LowRung         (new Vector(30,   0, false), true,  true),
            MidRung         (new Vector(41,   0, false), true,  true),
            Reach           (new Vector(44,  20, false), true,  true),
            Hook            (new Vector(42,  18, false), true,  true),
            Unhook          (new Vector(24, -10, false), true,  true),
            MidPull         (new Vector(14, -15, false), true,  true),
            HighPull        (new Vector(14,   0, false), false, true),
            TraversePull    (new Vector(28,   0, false), false, false);

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
        public static final double SHOOTER_RPM_THRESHOLD         = 0.06;
        public static final Vector HUB_POSITION                  = new Vector(0,0); // the center of the coordinate system is at the hub

        public static final double HOOD_MAX_POSITION = 1720;
        public static final double HOOD_MIN_POSITION = 447;

        public enum RobotPosition
        {
            NearLaunchpad(4609, 786, true, 0, 0, true), 
            Fender(3986, 1665, true, 2260, 1408, false),
            Tarmac(4111, 1118, true, 3013, 746, false),
            Position1(3787, 1287, true, 2712, 965, false),
            CargoRing(4136, 996, true, 0, 0, true);

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
            51.65,  4200.0,
            71.74,  4000.0, 
            138.04, 4100.0, 
            168.92, 4300.0, 
            206.16, 4600.0, 
            243.59, 4950.0, 
            259.03, 5100.0
        );
        
        public static final DoubleUnaryOperator SHOOTER_SPEED_LOOKUP = (distance) ->
        {
            TreeMap<Double, Double> map = new TreeMap<Double, Double>(SHOOTER_SPEEDS);

            return lookup(distance, map);
        }; 
        
        private static final Map<Double, Double> SHOOTER_HOOD_POSITIONS = Map.of
        (
            51.65,  2038.0,
            71.74,  1518.0, 
            138.04, 1400.0, 
            168.92, 1277.0, 
            206.16, 1136.0, 
            243.59, 1136.0, 
            259.03, 1140.0
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
    
    public static class Drive
    {
        public static final int    FL_INDEX             = 0;
        public static final int    FR_INDEX             = 1;
        public static final int    BL_INDEX             = 2;
        public static final int    BR_INDEX             = 3;

        public static final double FL_MOTOR_OFFSET      = 238.5;
        public static final double FR_MOTOR_OFFSET      =  33.75;
        public static final double BL_MOTOR_OFFSET      = -16.25;
        public static final double BR_MOTOR_OFFSET      =  78.5;
        
        public static final double MODULE_ROTATE_SCALE  = 360 / 0.92;
        public static final double MODULE_ROTATE_OFFSET = (360 - MODULE_ROTATE_SCALE) / 2.0;
        public static final double ODOMETRY_SCALE       = (48.75) /* inches */ / (55854.18) /* encoder counts */;

        public static final double ALIGN_ROTATE_SPEED   = 0.7;

        public static final double FIELD_ANGLE          = 21;
        public static final Vector FIELD_RESET_POSITION = new Vector(-18.47, -48.23);
    }

    public static class Testing
    {
        public static final double EPSILON = 1e-6;
    }
}
