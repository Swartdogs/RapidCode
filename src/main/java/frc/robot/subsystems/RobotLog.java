package frc.robot.subsystems;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import frc.robot.Constants;
import frc.robot.abstraction.SwartdogSubsystem;

public class RobotLog extends SwartdogSubsystem
{
    private static RobotLog _instance;

    public static RobotLog getInstance()
    {
        if (_instance == null)
        {
            _instance = new RobotLog();
        }
    
        return _instance;
    }

    public enum GameMode
    {
        Disabled,
        Autonomous,
        Teleop,
        Test,
        Unknown
    }

    private int         _robotTime;
    private int         _modeTime;

    private PrintWriter _writer;

    private boolean     _fileOpen;

    private GameMode    _mode;

    private RobotLog()
    {
        _robotTime = 0;
        _modeTime  = 0;

        _fileOpen  = false;

        _mode      = GameMode.Unknown;
    }

    @Override
    public void periodic()
    {
        _robotTime++;
        _modeTime++;
    }

    private String toSeconds(int counter)
    {
        return String.format("%8.2f", counter / Constants.RobotLog.LOOPS_PER_SECOND);
    }

    public void open(String filePath)
    {
        if (!_fileOpen)
        {
            try
            {
                File file = new File(filePath);
                _writer   = new PrintWriter(new FileWriter(filePath));
                _fileOpen = true;
            }
            catch (IOException e)
            {
                System.out.println("Error opening robot log!");
            }
        }
    }

    public void save()
    {
        if (_fileOpen)
        {
            _writer.flush();
        }
    }

    public void close()
    {
        if (_fileOpen)
        {
            _writer.close();
            _fileOpen = false;
            _mode     = GameMode.Unknown;
        }
    }

    public void log(String message)
    {
        log(message, true);
    }

    public void log(String message, boolean printTimeStamps)
    {
        String line = "";
        
        if (printTimeStamps)
        {
            line += "| " + toSeconds(_robotTime) + " | " + toSeconds(_modeTime) + " | ";
        } 

        line += message;

        _writer.println(line);
    }

    public void printHeading(String heading)
    {
        int len         = heading.length();
        int frontSpaces = (78 - len) / 2;
        int backSpaces  = frontSpaces;

        if (len % 2 == 1)
        {
            backSpaces++;
        }

        if (_mode != GameMode.Unknown)
        {
            _writer.println("+" + "-".repeat(10) + "+" + "-".repeat(10) + "+");
        }

        _writer.println();
        _writer.println(" ".repeat(24) + "+" + "-".repeat(78) + "+");
        _writer.println(" ".repeat(24) + "|" + " ".repeat(frontSpaces) + heading + " ".repeat(backSpaces) + "|");
        _writer.println(" ".repeat(24) + "+" + "-".repeat(78) + "+");
        _writer.println();
    }

    public void setGameMode(GameMode mode)
    {
        printHeading("Game Mode: " + mode.toString());
        _writer.println("+----------+----------+");
        _writer.println("| Game     | Mode     |");
        _writer.println("+----------+----------+");
        
        _modeTime = 0;
        _mode     = mode;
    }
}
