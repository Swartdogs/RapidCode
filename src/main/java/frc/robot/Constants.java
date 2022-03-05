package frc.robot;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

import frc.robot.abstraction.Enumerations.ExtendState;
import frc.robot.subsystems.drive.Vector;

public final class Constants
{
    public static class Ballpath
    {
        public static final double BALLPATH_SPEED  = 1.0;
        public static final int    MAX_CARGO_COUNT = 2;
    }

    public static class Hanger
    {
        public static final double HANGER_ARM_EXTENDED_POSITION  = 1000.0;
        public static final double HANGER_ARM_RETRACTED_POSITION = 0.0;

        public static final Function<ExtendState, Double> HANGER_ARM_POSITION_LOOKUP = (state) ->
        {
            double position;

            switch (state)
            {
                case Extended:
                    position = HANGER_ARM_EXTENDED_POSITION;
                    break;

                case Retracted:
                    position = HANGER_ARM_RETRACTED_POSITION;
                    break;

                default:
                    throw new IllegalArgumentException("Invalid Hanger Arm State: " + state);
            }

            return position;
        };

        public static final double HANGER_WINCH_EXTENDED_POSITION  = 1000.0;
        public static final double HANGER_WINCH_RETRACTED_POSITION = 0.0;

        public static final Function<ExtendState, Double> HANGER_WINCH_POSITION_LOOKUP = (state) ->
        {
            double position;

            switch (state)
            {
                case Extended:
                    position = HANGER_WINCH_EXTENDED_POSITION;
                    break;

                case Retracted:
                    position = HANGER_WINCH_RETRACTED_POSITION;
                    break;

                default:
                    throw new IllegalArgumentException("Invalid Winch Arm State: " + state);
            }

            return position;
        };
    }
    
    public static class Pickup
    {
        public static final double PICKUP_SPEED = 0.5;
    }

    public static class Shooter
    {
        public static final double NEAR_LAUNCHPAD_HOOD_POSITION  = 1198;
        public static final double NEAR_LAUNCHPAD_SHOOTER_RPM    = 5086;
        public static final double FENDER_HOOD_POSITION          = 2038;
        public static final double FENDER_SHOOTER_RPM            = 4200;
        public static final double FLYWHEEL_SPEED                = 6379;
        public static final double EJECT_SPEED                   = 2000;
        public static final double SHOOTER_RPM_THRESHOLD         = 0.06;
        public static final Vector HUB_POSITION                  = new Vector(0,0); // the center of the coordinate system is at the hub

        public static final double HOOD_MAX_POSITION = 2040;
        public static final double HOOD_MIN_POSITION = 695;

        public enum ShootPosition
        {
            NearLaunchpad, 
            Fender
        }

        public static final DoubleUnaryOperator SHOOTER_SPEED_LOOKUP = (distance) ->
        {
            double speed = 0;
            if (distance > 0)
            {
                speed = NEAR_LAUNCHPAD_SHOOTER_RPM;
            }
            return speed;
        }; 

        public static final DoubleUnaryOperator SHOOTER_HOOD_LOOKUP = (distance) ->
        {
            double position = 0;
            if (distance > 0)
            {
                position = NEAR_LAUNCHPAD_HOOD_POSITION;
            }
            return position;
        }; 
    }
    
    public static class Drive
    {
        public static final double FL_MOTOR_OFFSET      = 234;
        public static final double FR_MOTOR_OFFSET      =  34;
        public static final double BL_MOTOR_OFFSET      = -37;
        public static final double BR_MOTOR_OFFSET      =  98;
        
        public static final double MODULE_ROTATE_SCALE  = 360 / 0.92;
        public static final double MODULE_ROTATE_OFFSET = (360 - MODULE_ROTATE_SCALE) / 2.0;
        public static final double ODOMETRY_SCALE       = (48.75) /* inches */ / (55854.18) /* encoder counts */;

        public static final double ALIGN_ROTATE_SPEED   = 0.7;
    }

    public static class Testing
    {
        public static final double EPSILON = 1e-6;
    }
}
