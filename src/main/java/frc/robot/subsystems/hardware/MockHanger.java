package frc.robot.subsystems.hardware;

import PIDControl.PIDControl;
import PIDControl.PIDControl.Coefficient;
import frc.robot.abstraction.Motor.MockMotor;
import frc.robot.abstraction.PositionSensor.MockPositionSensor;
import frc.robot.abstraction.Solenoid.MockSolenoid;
import frc.robot.abstraction.Switch.MockSwitch;
import frc.robot.subsystems.Hanger;

public class MockHanger extends Hanger 
{
    public MockHanger() 
    {
        _armMotor        = new MockMotor();
        _winchMotor      = new MockMotor();

        _hookSolenoid    = new MockSolenoid();
        _ratchetSolenoid = new MockSolenoid();

        _armSensor       = new MockPositionSensor();
        _winchSensor     = new MockPositionSensor();

        _armPID          = new PIDControl();
        _winchPID        = new PIDControl();

        _leftSwitch      = new MockSwitch();
        _rightSwitch     = new MockSwitch();

        _armPID.setCoefficient(Coefficient.P, 0, 1, 0);
        _armPID.setCoefficient(Coefficient.I, 0, 0, 0);
        _armPID.setCoefficient(Coefficient.D, 0, 0, 0);
        _armPID.setInputRange(-1000, 1000);
        _armPID.setOutputRange(-1, 1);

        _winchPID.setCoefficient(Coefficient.P, 0, 1, 0);
        _winchPID.setCoefficient(Coefficient.I, 0, 0, 0);
        _winchPID.setCoefficient(Coefficient.D, 0, 0, 0);
        _winchPID.setInputRange(-1000, 1000);
        _winchPID.setOutputRange(-1, 1);
    }

    public MockMotor getArmMotor() 
    {
        return (MockMotor)_armMotor;
    }

    public MockMotor getWinchMotor() 
    {
        return (MockMotor)_winchMotor;
    }

    public MockSolenoid getHookSolenoid()
    {
        return (MockSolenoid)_hookSolenoid;
    }

    public MockSolenoid getRatchetSolenoid()
    {
        return (MockSolenoid)_ratchetSolenoid;
    }

    public MockPositionSensor getArmSensor() 
    {
        return (MockPositionSensor)_armSensor;
    }

    public MockPositionSensor getWinchSensor() 
    {
        return (MockPositionSensor)_winchSensor;
    }

    public MockSwitch getLeftSwitch()
    {
        return (MockSwitch)_leftSwitch;
    }

    public MockSwitch getRightSwitch()
    {
        return (MockSwitch)_rightSwitch;
    }
}
