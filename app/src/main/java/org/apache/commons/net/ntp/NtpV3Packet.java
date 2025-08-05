package org.apache.commons.net.ntp;

import java.net.DatagramPacket;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ntp/NtpV3Packet.class */
public interface NtpV3Packet {
    public static final int NTP_PORT = 123;
    public static final int LI_NO_WARNING = 0;
    public static final int LI_LAST_MINUTE_HAS_61_SECONDS = 1;
    public static final int LI_LAST_MINUTE_HAS_59_SECONDS = 2;
    public static final int LI_ALARM_CONDITION = 3;
    public static final int MODE_RESERVED = 0;
    public static final int MODE_SYMMETRIC_ACTIVE = 1;
    public static final int MODE_SYMMETRIC_PASSIVE = 2;
    public static final int MODE_CLIENT = 3;
    public static final int MODE_SERVER = 4;
    public static final int MODE_BROADCAST = 5;
    public static final int MODE_CONTROL_MESSAGE = 6;
    public static final int MODE_PRIVATE = 7;
    public static final int NTP_MINPOLL = 4;
    public static final int NTP_MAXPOLL = 14;
    public static final int NTP_MINCLOCK = 1;
    public static final int NTP_MAXCLOCK = 10;
    public static final int VERSION_3 = 3;
    public static final int VERSION_4 = 4;
    public static final String TYPE_NTP = "NTP";
    public static final String TYPE_ICMP = "ICMP";
    public static final String TYPE_TIME = "TIME";
    public static final String TYPE_DAYTIME = "DAYTIME";

    DatagramPacket getDatagramPacket();

    void setDatagramPacket(DatagramPacket datagramPacket);

    int getLeapIndicator();

    void setLeapIndicator(int i2);

    int getMode();

    String getModeName();

    void setMode(int i2);

    int getPoll();

    void setPoll(int i2);

    int getPrecision();

    void setPrecision(int i2);

    int getRootDelay();

    void setRootDelay(int i2);

    double getRootDelayInMillisDouble();

    int getRootDispersion();

    void setRootDispersion(int i2);

    long getRootDispersionInMillis();

    double getRootDispersionInMillisDouble();

    int getVersion();

    void setVersion(int i2);

    int getStratum();

    void setStratum(int i2);

    String getReferenceIdString();

    int getReferenceId();

    void setReferenceId(int i2);

    TimeStamp getTransmitTimeStamp();

    TimeStamp getReferenceTimeStamp();

    TimeStamp getOriginateTimeStamp();

    TimeStamp getReceiveTimeStamp();

    void setTransmitTime(TimeStamp timeStamp);

    void setReferenceTime(TimeStamp timeStamp);

    void setOriginateTimeStamp(TimeStamp timeStamp);

    void setReceiveTimeStamp(TimeStamp timeStamp);

    String getType();
}
