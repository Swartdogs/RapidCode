package frc.robot;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Dashboard 
{
    private static final int BUFFER_LEN = 1024;
    private static final int TCP_PORT   = 5801;

    public enum DashButton
    {
        dbRunPid,
        dbDataCapture,
        dbVisionAuto,
        dbSandStormAuto
    }

    public enum DashValue
    {
        dvAutoHatchPlace,
        dvAutoStop,
        dvPidSelect,
        dvPidSetpoint,
        dvPidMaxOut,
        dvPthreshold,
        dvPabove,
        dvPbelow,
        dvIthreshold,
        dvIabove,
        dvIbelow,
        dvDthreshold,
        dvDabove,
        dvDbelow,
        dvElevOffset,
        dvElevMin,
        dvElevMax,
        dvFloorSensorMin,
        dvElevRetracted,
        dvElevLevel2,
        dvElevLevel3,
        dvShoulderOffset,
        dvShoulderMin, 
        dvShoulderMax,
        dvShoulderClear,
        dvShoulderTravel,
        dvSCPickup,
        dvSCLoad,
        dvSCCargoShip,
        dvSCRocketLow,
        dvSCRocketMid,
        dvSCRocketHigh,
        dvSCCatch,
        dvSHRocketLow,
        dvSHRocketMid,
        dvSHRocketHigh,
        dvWristOffset,
        dvWristMin,
        dvWristMax,
        dvWristClear,
        dvWristTravel,
        dvWCPickup,
        dvWCLoad,
        dvWCCargoShip,
        dvWCRocketLow,
        dvWCRocketMid,
        dvWCRocketHigh,
        dvWCCatch,
        dvWHRocketLow,
        dvWHRocketMid,
        dvWHRocketHigh,
        dvCargoSpeedIn,
        dvCargoSpeedOut,
        dvCargoSpeedRotate,
        dvCargoRotateRatio,
        dvCargoEjectTime,
        dvCargoRotateTime,
        dvVisionTargetAngle,
        dvVisionCargoLoad,
        dvVisionHatchLoad,
        dvVisionHatchLow,
        dvVisionHatchMid,
        dvVisionHatchHigh
    }

    public enum RobotStatus
    {
        rsShifterLow,
        rsFootRetracted,
        rsFloorFront,
        rsFloorRear,
        rsCargo,
        rsHatchMode,
        rsHatchGrab,
        rsClimb,
        rsNoClimb,
        rsTargetFound, 
        rsCargoLoaded
    }

    public enum RobotValue
    {
        rvDriveGyro,
        rvDriveEncoderL,
        rvDriveEncoderR,
        rvDriveAmpsLeft1,
        rvDriveAmpsLeft2,
        rvDriveAmpsRight1,
        rvDriveAmpsRight2,
        rvElevatorPosition,
        rvElevatorSetpoint,
        rvElevatorAmps,
        rvShoulderPosition,
        rvShoulderSetpoint,
        rvShoulderAmps,
        rvWristPosition,
        rvWristSetpoint,
        rvWristAmps,
        rvVisionStatus,
        rvVisionSelect,
        rvVisionAngle,
        rvVisionDistance
    }

    public enum RobotMode
    {
        rmInit,					
        rmDisabled,
        rmAutonomous,
        rmTeleop,
        rmTest
    }

    private class Button
    {
        public int state;
        public int pressed;
    }

    private String _commandPrefix;
    private int    _dashboardButtonCount;
    private int    _dashboardValueCount;
    private int    _robotStatusCount;
    private int    _robotValueCount;
    private int    _robotMode;

    private char[]             _log;
    private ArrayList<Button>  _dashboardButton;
    private ArrayList<Double>  _dashboardValue;
    private ArrayList<Integer> _robotStatus;
    private ArrayList<Double>  _robotValue;

    public Dashboard(String commandPrefix, int robotStatusCount, int robotValueCount,
                     int dashButtonCount, int dashValueCount)
    {
        writeToLog("INIT");
        
        _robotMode = 0;

        _commandPrefix = commandPrefix;
        _robotStatusCount = robotStatusCount;
        _robotValueCount = robotValueCount;
        _dashboardButtonCount = dashButtonCount;
        _dashboardValueCount = dashValueCount;

        _robotStatus = new ArrayList<Integer>();
        _robotValue  = new ArrayList<Double>();
        _dashboardButton = new ArrayList<Button>();
        _dashboardValue  = new ArrayList<Double>();

        for (int i = 0; i < _robotStatusCount; i++) _robotStatus.add(0);
        for (int i = 0; i < _robotValueCount; i++) _robotValue.add(0.0);
        for (int i = 0; i < _dashboardButtonCount; i++)
        {
            Button button = new Button();
            button.state = 0;
            button.pressed = 0;

            _dashboardButton.add(button);
        }
        for (int i = 0; i < _dashboardValueCount; i++)
        {
            _dashboardValue.add(0.0);
        }

        startHost();

        /* TODO: read settings file */
    }

    public String countReply()
    {
        return "COUNT:" + dataString(_robotMode, 1) + dataString(_robotStatusCount, 1) + dataString(_robotValueCount, 1) +
                          dataString(_dashboardButtonCount, 1) + dataString(_dashboardValueCount, 0) + "\r\n";
    }

    public String dataString(int number, int delimiter)
    {
        String ds = Integer.toString(number);

        switch (delimiter)
        {
            case 1: ds += ","; break;
            case 2: ds += "|"; break;
            default: break;
        }

        return ds;
    }

    public String dataString(double number, int delimiter)
    {
        DecimalFormat format = new DecimalFormat("0.#");
        String ds = format.format(number);

        switch (delimiter)
        {
            case 1: ds += ","; break;
            case 2: ds += "|"; break;
            default: break;
        }

        return ds;
    }

    public String getCommandPrefix()
    {
        return _commandPrefix;
    }

    public boolean getDashButton(DashButton buttonIndex)
    {
        int group = buttonIndex.ordinal() / 16;
        int index = buttonIndex.ordinal() % 16;

        if (index < 16 && group < _dashboardButtonCount)
        {
            return (_dashboardButton.get(group).state & (1 << index)) != 0;
        }

        return false;
    }

    public boolean getDashButtonPress(DashButton buttonIndex)
    {
        int     group   = buttonIndex.ordinal() / 16;
        int     index   = buttonIndex.ordinal() % 16;
        boolean vReturn = false;

        if (group < _dashboardButtonCount)
        {
            int buttonValue = 1 << index;

            if ((_dashboardButton.get(group).state & buttonValue) != 0)
            {
                vReturn = (_dashboardButton.get(group).pressed & buttonValue) == 0;
                _dashboardButton.get(group).pressed |= buttonValue;
            }

            else if ((_dashboardButton.get(group).pressed & buttonValue) != 0)
            {
                _dashboardButton.get(group).pressed ^= buttonValue;
            }
        }

        return vReturn;
    }

    public double getDashValue(DashValue valueIndex)
    {
        if (valueIndex.ordinal() < _dashboardValueCount)
        {
            double value = _dashboardValue.get(valueIndex.ordinal());

            writeToLog("Get Dash Value: " + value);

            return value;
        }

        return 0;
    }

    public String getReply()
    {
        String data = "GET:" + dataString(_robotMode, 2);

        if (_robotStatusCount > 0)
        {
            for (int i = 0; i < _robotStatusCount; i++)
            {
                data += dataString(_robotStatus.get(i), 1);
            }

            data = data.substring(0, data.length() - 1);
        }

        data += "|";

        if (_robotValueCount > 0)
        {
            for (int i = 0; i < _robotValueCount; i++)
            {
                data += dataString(_robotValue.get(i), 1);
            }

            data = data.substring(0, data.length() - 1);
        }

        return data += "\r\n";
    }

    public boolean getRobotStatus(RobotStatus statusIndex)
    {
        int group = statusIndex.ordinal() / 16;
        int index = statusIndex.ordinal() % 16;

        if (index < 16 && group < _robotStatusCount)
        {
            return (_robotStatus.get(group) & (1 << index)) != 0;
        }

        return false;
    }

    public String pullReply()
    {
        String data = "PULL:" + dataString((double)_dashboardValueCount, 2);

        if (_dashboardValueCount > 0)
        {
            for (int i = 0; i < _dashboardValueCount; i++)
            {
                data += dataString(_dashboardValue.get(i), 1);
            }

            data = data.substring(0, data.length() - 1);
        }

        return data += "\r\n";
    }

    public void saveDashValues()
    {

    }

    public boolean setDashButton(int group, int value)
    {
        if (group < 0 || group >= _dashboardButtonCount)
        {
            return false;
        }

        _dashboardButton.get(group).state = value;

        return false;
    }

    public boolean setDashValue(int valueIndex, double value)
    {
        if (valueIndex < 0 || valueIndex >= _dashboardValueCount)
        {
            return false;
        }

        _dashboardValue.set(valueIndex, value);

        writeToLog("value set: " + value);

        return false;
    }

    public boolean setRobotStatus(RobotStatus statusIndex, boolean value)
    {
        int group = statusIndex.ordinal() / 16;
        int index = statusIndex.ordinal() % 16;

        if (group >= _robotStatusCount)
        {
            return false;
        }

        int statusValue = 1 << index;

        if (value)
        {
            _robotStatus.set(group, _robotStatus.get(group) | statusValue);
        }

        else if ((_robotStatus.get(group) & statusValue) != 0)
        {
            _robotStatus.set(group, _robotStatus.get(group) ^ statusValue);
        }

        return true;
    }

    public boolean setRobotValue(RobotValue valueIndex, double value)
    {
        if (valueIndex.ordinal() < 0 || valueIndex.ordinal() >= _robotValueCount)
        {
            return false;
        }

        _robotValue.set(valueIndex.ordinal(), value);
        return true;
    }

    public void setRobotMode(RobotMode mode)
    {
        _robotMode = mode.ordinal();
    }

    public void startHost()
    {
        writeToLog("Start host thread");

        Dashboard host = this;

        Thread hostTask = new Thread(new Runnable() 
        {
           @Override
           public void run()
           {
               Dashboard.tcpLoop(host);
           } 
        });

        hostTask.start();
    }

    public static void tcpLoop(Dashboard host)
    {
        String commandEnd = "\r\n";
        String commandCOUNT = host.getCommandPrefix() + "COUNT";
        String commandGET   = host.getCommandPrefix() + "GET";
        String commandPULL  = host.getCommandPrefix() + "PULL";
        String commandPUT   = host.getCommandPrefix() + "PUT";
        String commandSET   = host.getCommandPrefix() + "SET";
        String command;
        String reply;
        String clientMsg;
        String recMesg = "";
        int    position = 0;
        int    replySize;
        int    index;

        Socket socket;
        ServerSocket server;

        try
        {
            server = new ServerSocket(TCP_PORT);
        }

        catch (Exception e)
        {
            host.writeToLog("Host Socket Error");
            return;
        }

        byte[] recBuffer = new byte[BUFFER_LEN];
        byte[] sendBuffer = new byte[BUFFER_LEN];

        while (true)
        {
            try
            {
                socket = server.accept();
                host.writeToLog("Connection Accepted");

                while(socket.getInputStream().read(recBuffer, 0, BUFFER_LEN) > 0)
                {
                    recMesg += new String(recBuffer);
                    host.clearBuffer(recBuffer);

                    host.writeToLog(recMesg);

                    while ((position = recMesg.indexOf(commandEnd, 0)) >= 0)
                    {
                        clientMsg = recMesg.substring(0, position);
                        recMesg = recMesg.substring(position + 2);

                        if ((position = clientMsg.indexOf(":")) >= 0)
                        {
                            command = clientMsg.substring(0, position);
                            clientMsg = clientMsg.substring(position + 1);
                            reply = "";

                            host.writeToLog(String.format("command: %s, clientMsg: %s", command, clientMsg));

                            if (command.equals(commandCOUNT))
                            {
                                reply = host.countReply();
                            }

                            else if (command.equals(commandGET))
                            {
                                reply = host.getReply();
                            }

                            else if (command.equals(commandPULL))
                            {
                                host.writeToLog("pull request received");
                                reply = host.pullReply();
                            }

                            else if (command.equals(commandPUT))
                            {
                                reply = "PUT:";
                                boolean saveFile = false;

                                while ((position = clientMsg.indexOf("|")) >= 0)
                                {
                                    command = clientMsg.substring(0, position);
                                    clientMsg = clientMsg.substring(position + 1);

                                    if ((position = command.indexOf(",")) >= 0)
                                    {
                                        String group = command.substring(0, position);
                                        command = command.substring(position + 1);

                                        if ((position = command.indexOf(",")) >= 0)
                                        {
                                            index = Integer.parseInt(command.substring(0, position));

                                            if (group == "V")
                                            {
                                                if (host.setDashValue(index, Double.parseDouble(command.substring((position + 1)))))
                                                {
                                                    reply += "V," + host.dataString(index, 2);
                                                    saveFile = true;
                                                }
                                            }

                                            else if (group == "B")
                                            {
                                                if (host.setDashButton(index, Integer.parseInt(command.substring(position + 1))))
                                                {
                                                    reply += "B," + host.dataString(index, 2);
                                                }
                                            }
                                        }
                                    }
                                }

                                reply += "\r\n";

                                if (saveFile)
                                {
                                    host.saveDashValues();
                                }
                            }

                            else if (command.equals(commandSET))
                            {
                                host.writeToLog("New Robot Setting(s) from Dashboard");
                            }

                            replySize = reply.length();

                            if (replySize > 0)
                            {
                                sendBuffer = reply.getBytes();

                                socket.getOutputStream().write(sendBuffer);
                            }
                        }
                    }
                }

                socket.close();
                host.writeToLog("Connection Lost");

                host.clearBuffer(recBuffer);
                host.clearBuffer(sendBuffer);
            }

            catch (Exception e)
            {
                host.writeToLog("Error accepting connection");
            }
        }
    }

    public void writeToLog(String entry)
    {
        System.out.println("Dash:    " + entry);
    }

    private void clearBuffer(byte[] buffer)
    {
        for (int i = 0; i < buffer.length; i++)
        {
            buffer[i] = 0;
        }
    }
}
