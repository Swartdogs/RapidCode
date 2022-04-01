package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import frc.robot.Constants;
import frc.robot.abstraction.VelocitySensor.MockVelocitySensor;
import frc.robot.subsystems.hardware.MockBallpath;
import frc.robot.subsystems.hardware.MockShooter;
import frc.robot.subsystems.hardware.MockDrive;
import frc.robot.subsystems.drive.Vector;

public class CmdShootWithOdometryTests {
    private MockDrive    _drive;
    private MockShooter  _shooter;
    private MockBallpath _ballpath;

    private CmdShootWithOdometry _command;

    @BeforeEach
    public void init()
    {
        _drive    = new MockDrive();
        _shooter  = new MockShooter();
        _ballpath = new MockBallpath();

        _command = new CmdShootWithOdometry(_drive, _shooter, _ballpath);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    public void testStart (int initialCargoCount)
    {
        _drive.resetOdometer(new Vector(10, 0));
        _drive.getGyro().set(270);                                          
        _shooter.getHoodSensor().set(Constants.Shooter.NEAR_LAUNCHPAD_HOOD_POSITION);
        _ballpath.setCargoCount(initialCargoCount);
        
        _command.initialize();

        assertTrue(_drive.rotateIsFinished());
        assertEquals(initialCargoCount > 0 ? 4200 : 0, _shooter.getShooterMotor().get(), Constants.Testing.EPSILON);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    public void testEnd (int initialCargoCount)
    {
        _drive.resetOdometer(new Vector(120, 0));
        _drive.getGyro().set(270);                                          
        _shooter.getHoodSensor().set(Constants.Shooter.NEAR_LAUNCHPAD_HOOD_POSITION);
        ((MockVelocitySensor)_shooter.getShooterMotor().getVelocitySensor()).set(Constants.Shooter.NEAR_LAUNCHPAD_SHOOTER_RPM);
        _ballpath.setCargoCount(initialCargoCount);
        
        _command.initialize();
        _ballpath.setCargoCount(0);
      
        if (_command.isFinished())
        {
            _command.end(false);
        }

        assertEquals(0, _shooter.getShooterMotor().get(),     Constants.Testing.EPSILON);
        assertEquals(0, _ballpath.getUpperTrackMotor().get(), Constants.Testing.EPSILON);
        assertEquals(0, _ballpath.getLowerTrackMotor().get(), Constants.Testing.EPSILON);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    public void testIsFinished (int initialCargoCount)
    {
        _ballpath.setCargoCount(initialCargoCount);

        _command.initialize();

        assertEquals(initialCargoCount <= 0, _command.isFinished());
    }
}
