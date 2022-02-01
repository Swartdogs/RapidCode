package frc.robot;

import java.util.function.Function;

import frc.robot.abstraction.Enumerations.ExtendState;

public final class Constants
{
    public static class Ballpath
    {
        public static final double BALLPATH_SPEED  = 0.5;
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
        public static final double PICKUP_SPEED = 1.0;
    }

    public static class Shooter
    {
        public static final double FLYWHEEL_SPEED = 5800;
    }
    
    public static class Testing
    {
        public static final double EPSILON = 1e-6;
    }
}
