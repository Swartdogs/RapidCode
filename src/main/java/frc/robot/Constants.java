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

        public static final int WINCH_SENSOR_DIO_PORT   = 2;
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
        public static final double WINCH_SPEED         = 1.0;
        public static final double HOOK_WAIT_TIME      = 0.3;
        public static final double RESET_WAIT_TIME     = 0.1;
        public static final double RESET_SPEED         = 0.4;
        public static final double ARM_STOW_SETPOINT   = 0.3;
        public static final double ARM_FIRST_SETPOINT  = 0.357;
        public static final double ARM_SECOND_SETPOINT = 0.414;
    }
    
    public static class Pickup
    {
        public static final double PICKUP_SPEED = 0.64;
    }

    public static class Shooter
    {
        public static final double NEAR_LAUNCHPAD_HOOD_POSITION  = 1171;
        public static final double NEAR_LAUNCHPAD_SHOOTER_RPM    = 4700;
        public static final double FENDER_LOW_GOAL_HOOD_POSITION = 1700;
        public static final double FENDER_LOW_GOAL_SHOOTER_RPM   = 2500;
        public static final double FENDER_LOW_GOAL_POSITION_1_HOOD_POSITION = 1200;
        public static final double FENDER_LOW_GOAL_POSITION_1_SHOOTER_RPM   = 3000;
        public static final double FENDER_HOOD_POSITION          = 2000;
        public static final double FENDER_SHOOTER_RPM            = 4000;
        public static final double FENDER_POSITION_1_HOOD_POSITION = 1494;
        public static final double FENDER_POSITION_1_SHOOTER_RPM   = 4100;
        public static final double FLYWHEEL_SPEED                = 6379;
        public static final double EJECT_SPEED                   = 2500;
        public static final double EJECT_HOOD_POSITION           = 1400;
        public static final double SHOOTER_RPM_THRESHOLD         = 0.06;
        public static final Vector HUB_POSITION                  = new Vector(0,0); // the center of the coordinate system is at the hub

        public static final double HOOD_MAX_POSITION = 2040;
        public static final double HOOD_MIN_POSITION = 695;

        public enum ShootPosition
        {
            NearLaunchpad, 
            Fender,
            FenderLowGoal,
            FenderLowGoalPosition1,
            FenderPosition1
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

        public static final double FL_MOTOR_OFFSET      = 234;
        public static final double FR_MOTOR_OFFSET      =  34;
        public static final double BL_MOTOR_OFFSET      = -37;
        public static final double BR_MOTOR_OFFSET      =  98;
        
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
