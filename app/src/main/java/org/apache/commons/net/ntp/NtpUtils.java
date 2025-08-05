package org.apache.commons.net.ntp;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ntp/NtpUtils.class */
public final class NtpUtils {
    public static String getHostAddress(int address) {
        return ((address >>> 24) & 255) + "." + ((address >>> 16) & 255) + "." + ((address >>> 8) & 255) + "." + ((address >>> 0) & 255);
    }

    public static String getRefAddress(NtpV3Packet packet) {
        int address = packet == null ? 0 : packet.getReferenceId();
        return getHostAddress(address);
    }

    public static String getReferenceClock(NtpV3Packet message) {
        int refId;
        char c2;
        if (message == null || (refId = message.getReferenceId()) == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(4);
        for (int shiftBits = 24; shiftBits >= 0 && (c2 = (char) ((refId >>> shiftBits) & 255)) != 0; shiftBits -= 8) {
            if (!Character.isLetterOrDigit(c2)) {
                return "";
            }
            buf.append(c2);
        }
        return buf.toString();
    }

    public static String getModeName(int mode) {
        switch (mode) {
            case 0:
                return "Reserved";
            case 1:
                return "Symmetric Active";
            case 2:
                return "Symmetric Passive";
            case 3:
                return "Client";
            case 4:
                return "Server";
            case 5:
                return "Broadcast";
            case 6:
                return "Control";
            case 7:
                return "Private";
            default:
                return "Unknown";
        }
    }
}
