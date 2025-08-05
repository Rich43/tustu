package com.intel.bluetooth;

import java.util.Hashtable;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.UUID;
import sun.security.pkcs11.wrapper.PKCS11Constants;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothConsts.class */
public abstract class BluetoothConsts {
    public static final String SHORT_UUID_BASE = "00001000800000805F9B34FB";
    public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
    public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";
    public static final int RFCOMM_CHANNEL_MIN = 1;
    public static final int RFCOMM_CHANNEL_MAX = 30;
    public static final int L2CAP_PSM_MIN = 5;
    public static final int L2CAP_PSM_MIN_JSR_82 = 4097;
    public static final int L2CAP_PSM_MAX = 65535;
    public static final int TCP_OBEX_DEFAULT_PORT = 650;
    public static final String PROPERTY_BLUETOOTH_API_VERSION = "bluetooth.api.version";
    public static final String PROPERTY_OBEX_API_VERSION = "obex.api.version";
    public static final String PROPERTY_BLUETOOTH_MASTER_SWITCH = "bluetooth.master.switch";
    public static final String PROPERTY_BLUETOOTH_SD_ATTR_RETRIEVABLE_MAX = "bluetooth.sd.attr.retrievable.max";
    public static final String PROPERTY_BLUETOOTH_CONNECTED_DEVICES_MAX = "bluetooth.connected.devices.max";
    public static final String PROPERTY_BLUETOOTH_L2CAP_RECEIVEMTU_MAX = "bluetooth.l2cap.receiveMTU.max";
    public static final String PROPERTY_BLUETOOTH_SD_TRANS_MAX = "bluetooth.sd.trans.max";
    public static final String PROPERTY_BLUETOOTH_CONNECTED_INQUIRY_SCAN = "bluetooth.connected.inquiry.scan";
    public static final String PROPERTY_BLUETOOTH_CONNECTED_PAGE_SCAN = "bluetooth.connected.page.scan";
    public static final String PROPERTY_BLUETOOTH_CONNECTED_INQUIRY = "bluetooth.connected.inquiry";
    public static final String PROPERTY_BLUETOOTH_CONNECTED_PAGE = "bluetooth.connected.page";
    public static final UUID SERIAL_PORT_UUID;
    public static final int BluetoothProfileDescriptorList = 9;
    public static final int BrowseGroupList = 5;
    public static final int ClientExecutableURL = 11;
    public static final int DocumentationURL = 10;
    public static final int IconURL = 12;
    public static final int LanguageBasedAttributeIDList = 6;
    public static final int ProtocolDescriptorList = 4;
    public static final int ProviderName = 2;
    public static final int ServiceAvailability = 8;
    public static final int ServiceClassIDList = 1;
    public static final int ServiceDatabaseState = 513;
    public static final int ServiceDescription = 1;
    public static final int ServiceID = 3;
    public static final int ServiceInfoTimeToLive = 7;
    public static final int AttributeIDServiceName = 256;
    public static final int ServiceName = 0;
    public static final int ServiceRecordHandle = 0;
    public static final int ServiceRecordState = 2;
    public static final int VersionNumberList = 512;
    public static final UUID L2CAP_PROTOCOL_UUID = new UUID(256);
    public static final UUID RFCOMM_PROTOCOL_UUID = new UUID(3);
    public static final UUID OBEX_PROTOCOL_UUID = new UUID(8);
    public static final UUID OBEXFileTransferServiceClass_UUID = new UUID(4358);
    static Hashtable obexUUIDs = new Hashtable();

    static {
        addObex(4356);
        addObex(4357);
        addObex(4358);
        addObex(4359);
        addObex(4379);
        SERIAL_PORT_UUID = new UUID(PKCS11Constants.CKM_DES_CBC_ENCRYPT_DATA);
    }

    private BluetoothConsts() {
    }

    private static void addObex(int uuid) {
        UUID u2 = new UUID(uuid);
        obexUUIDs.put(u2, u2);
    }

    public static String toString(DeviceClass dc) {
        return DeviceClassConsts.toString(dc);
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothConsts$DeviceClassConsts.class */
    public static class DeviceClassConsts {
        public static final int SERVICE_MASK = 16769024;
        public static final int MAJOR_MASK = 7936;
        public static final int MINOR_MASK = 252;
        public static final int FORMAT_VERSION_MASK = 3;
        public static final int LIMITED_DISCOVERY_SERVICE = 8192;
        public static final int RESERVED1_SERVICE = 16384;
        public static final int RESERVED2_SERVICE = 32768;
        public static final int POSITIONING_SERVICE = 65536;
        public static final int NETWORKING_SERVICE = 131072;
        public static final int RENDERING_SERVICE = 262144;
        public static final int CAPTURING_SERVICE = 524288;
        public static final int OBJECT_TRANSFER_SERVICE = 1048576;
        public static final int AUDIO_SERVICE = 2097152;
        public static final int TELEPHONY_SERVICE = 4194304;
        public static final int INFORMATION_SERVICE = 8388608;
        public static final int MAJOR_MISCELLANEOUS = 0;
        public static final int MAJOR_COMPUTER = 256;
        public static final int MAJOR_PHONE = 512;
        public static final int MAJOR_LAN_ACCESS = 768;
        public static final int MAJOR_AUDIO = 1024;
        public static final int MAJOR_PERIPHERAL = 1280;
        public static final int MAJOR_IMAGING = 1536;
        public static final int MAJOR_UNCLASSIFIED = 7936;
        public static final int COMPUTER_MINOR_UNCLASSIFIED = 0;
        public static final int COMPUTER_MINOR_DESKTOP = 4;
        public static final int COMPUTER_MINOR_SERVER = 8;
        public static final int COMPUTER_MINOR_LAPTOP = 12;
        public static final int COMPUTER_MINOR_HANDHELD = 16;
        public static final int COMPUTER_MINOR_PALM = 20;
        public static final int COMPUTER_MINOR_WEARABLE = 24;
        public static final int PHONE_MINOR_UNCLASSIFIED = 0;
        public static final int PHONE_MINOR_CELLULAR = 4;
        public static final int PHONE_MINOR_CORDLESS = 8;
        public static final int PHONE_MINOR_SMARTPHONE = 12;
        public static final int PHONE_MINOR_WIRED_MODEM = 16;
        public static final int PHONE_MINOR_ISDN = 20;
        public static final int PHONE_MINOR_BANANA = 24;
        public static final int LAN_MINOR_TYPE_MASK = 28;
        public static final int LAN_MINOR_ACCESS_MASK = 224;
        public static final int LAN_MINOR_UNCLASSIFIED = 0;
        public static final int LAN_MINOR_ACCESS_0_USED = 0;
        public static final int LAN_MINOR_ACCESS_17_USED = 32;
        public static final int LAN_MINOR_ACCESS_33_USED = 64;
        public static final int LAN_MINOR_ACCESS_50_USED = 96;
        public static final int LAN_MINOR_ACCESS_67_USED = 128;
        public static final int LAN_MINOR_ACCESS_83_USED = 160;
        public static final int LAN_MINOR_ACCESS_99_USED = 192;
        public static final int LAN_MINOR_ACCESS_FULL = 224;
        public static final int AUDIO_MINOR_UNCLASSIFIED = 0;
        public static final int AUDIO_MINOR_HEADSET = 4;
        public static final int AUDIO_MINOR_HANDS_FREE = 8;
        public static final int AUDIO_MINOR_MICROPHONE = 16;
        public static final int AUDIO_MINOR_LOUDSPEAKER = 20;
        public static final int AUDIO_MINOR_HEADPHONES = 24;
        public static final int AUDIO_MINOR_PORTABLE_AUDIO = 28;
        public static final int AUDIO_MINOR_CAR_AUDIO = 32;
        public static final int AUDIO_MINOR_SET_TOP_BOX = 36;
        public static final int AUDIO_MINOR_HIFI_AUDIO = 40;
        public static final int AUDIO_MINOR_VCR = 44;
        public static final int AUDIO_MINOR_VIDEO_CAMERA = 48;
        public static final int AUDIO_MINOR_CAMCORDER = 52;
        public static final int AUDIO_MINOR_VIDEO_MONITOR = 56;
        public static final int AUDIO_MINOR_VIDEO_DISPLAY_LOUDSPEAKER = 60;
        public static final int AUDIO_MINOR_VIDEO_DISPLAY_CONFERENCING = 64;
        public static final int AUDIO_MINOR_GAMING_TOY = 72;
        public static final int PERIPHERAL_MINOR_TYPE_MASK = 60;
        public static final int PERIPHERAL_MINOR_KEYBOARD_MASK = 64;
        public static final int PERIPHERAL_MINOR_POINTER_MASK = 128;
        public static final int PERIPHERAL_MINOR_UNCLASSIFIED = 0;
        public static final int PERIPHERAL_MINOR_JOYSTICK = 4;
        public static final int PERIPHERAL_MINOR_GAMEPAD = 8;
        public static final int PERIPHERAL_MINOR_REMOTE_CONTROL = 12;
        public static final int PERIPHERAL_MINOR_SENSING = 16;
        public static final int PERIPHERAL_MINOR_DIGITIZER = 20;
        public static final int PERIPHERAL_MINOR_CARD_READER = 24;
        public static final int IMAGING_MINOR_DISPLAY_MASK = 16;
        public static final int IMAGING_MINOR_CAMERA_MASK = 32;
        public static final int IMAGING_MINOR_SCANNER_MASK = 64;
        public static final int IMAGING_MINOR_PRINTER_MASK = 128;

        private static boolean append(StringBuffer buf, String str, boolean comma) {
            if (comma) {
                buf.append(',');
            }
            buf.append(str);
            return true;
        }

        public static String toString(DeviceClass dc) {
            StringBuffer buf = new StringBuffer();
            switch (dc.getMajorDeviceClass()) {
                case 0:
                    buf.append("Miscellaneous");
                    break;
                case 256:
                    buf.append("Computer");
                    switch (dc.getMinorDeviceClass()) {
                        case 0:
                            buf.append("/Unclassified");
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        case 9:
                        case 10:
                        case 11:
                        case 13:
                        case 14:
                        case 15:
                        case 17:
                        case 18:
                        case 19:
                        case 21:
                        case 22:
                        case 23:
                        default:
                            buf.append("/Unknown");
                            break;
                        case 4:
                            buf.append("/Desktop");
                            break;
                        case 8:
                            buf.append("/Server");
                            break;
                        case 12:
                            buf.append("/Laptop");
                            break;
                        case 16:
                            buf.append("/Handheld");
                            break;
                        case 20:
                            buf.append("/Palm");
                            break;
                        case 24:
                            buf.append("/Wearable");
                            break;
                    }
                case 512:
                    buf.append("Phone");
                    switch (dc.getMinorDeviceClass()) {
                        case 0:
                            buf.append("/Unclassified");
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        case 9:
                        case 10:
                        case 11:
                        case 13:
                        case 14:
                        case 15:
                        case 17:
                        case 18:
                        case 19:
                        case 21:
                        case 22:
                        case 23:
                        default:
                            buf.append("/Unknown");
                            break;
                        case 4:
                            buf.append("/Cellular");
                            break;
                        case 8:
                            buf.append("/Cordless");
                            break;
                        case 12:
                            buf.append("/Smartphone");
                            break;
                        case 16:
                            buf.append("/Wired Modem");
                            break;
                        case 20:
                            buf.append("/ISDN");
                            break;
                        case 24:
                            buf.append("/Ring ring ring ring ring ring ring");
                            break;
                    }
                case 768:
                    buf.append("LAN Access");
                    int minor = dc.getMinorDeviceClass();
                    switch (minor & 28) {
                        case 0:
                            buf.append("/Unclassified");
                            break;
                        default:
                            buf.append("/Unknown");
                            break;
                    }
                    switch (minor & 224) {
                        case 0:
                            buf.append("/0% used");
                            break;
                        case 32:
                            buf.append("/1-17% used");
                            break;
                        case 64:
                            buf.append("/18-33% used");
                            break;
                        case 96:
                            buf.append("/34-50% used");
                            break;
                        case 128:
                            buf.append("/51-67% used");
                            break;
                        case 160:
                            buf.append("/68-83% used");
                            break;
                        case 192:
                            buf.append("/84-99% used");
                            break;
                        case 224:
                            buf.append("/100% used");
                            break;
                    }
                case 1024:
                    buf.append("Audio");
                    switch (dc.getMinorDeviceClass()) {
                        case 0:
                            buf.append("/Unclassified");
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 17:
                        case 18:
                        case 19:
                        case 21:
                        case 22:
                        case 23:
                        case 25:
                        case 26:
                        case 27:
                        case 29:
                        case 30:
                        case 31:
                        case 33:
                        case 34:
                        case 35:
                        case 37:
                        case 38:
                        case 39:
                        case 41:
                        case 42:
                        case 43:
                        case 45:
                        case 46:
                        case 47:
                        case 49:
                        case 50:
                        case 51:
                        case 53:
                        case 54:
                        case 55:
                        case 57:
                        case 58:
                        case 59:
                        case 61:
                        case 62:
                        case 63:
                        case 65:
                        case 66:
                        case 67:
                        case 68:
                        case 69:
                        case 70:
                        case 71:
                        default:
                            buf.append("/Unknown");
                            break;
                        case 4:
                            buf.append("/Headset");
                            break;
                        case 8:
                            buf.append("/Hands-free");
                            break;
                        case 16:
                            buf.append("/Microphone");
                            break;
                        case 20:
                            buf.append("/Loudspeaker");
                            break;
                        case 24:
                            buf.append("/Headphones");
                            break;
                        case 28:
                            buf.append("/Portable");
                            break;
                        case 32:
                            buf.append("/Car");
                            break;
                        case 36:
                            buf.append("/Set-top Box");
                            break;
                        case 40:
                            buf.append("/HiFi");
                            break;
                        case 44:
                            buf.append("/VCR");
                            break;
                        case 48:
                            buf.append("/Video Camera");
                            break;
                        case 52:
                            buf.append("/Camcorder");
                            break;
                        case 56:
                            buf.append("/Video Monitor");
                            break;
                        case 60:
                            buf.append("/Video Display Loudspeaker");
                            break;
                        case 64:
                            buf.append("/Video Display Conferencing");
                            break;
                        case 72:
                            buf.append("/Gaming Toy");
                            break;
                    }
                case 1280:
                    buf.append("Peripheral");
                    int minor2 = dc.getMinorDeviceClass();
                    switch (minor2 & 192) {
                        case 0:
                            buf.append("/()");
                            break;
                        case 64:
                            buf.append("/(Keyboard)");
                            break;
                        case 128:
                            buf.append("/(Pointer)");
                            break;
                        case 192:
                            buf.append("/(Keyboard,Pointer)");
                            break;
                    }
                    switch (minor2 & 60) {
                        case 0:
                            buf.append("/Unclassified");
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        case 9:
                        case 10:
                        case 11:
                        case 13:
                        case 14:
                        case 15:
                        case 17:
                        case 18:
                        case 19:
                        case 21:
                        case 22:
                        case 23:
                        default:
                            buf.append("/Unknown");
                            break;
                        case 4:
                            buf.append("/Joystick");
                            break;
                        case 8:
                            buf.append("/Gamepad");
                            break;
                        case 12:
                            buf.append("/Remote Control");
                            break;
                        case 16:
                            buf.append("/Sensing");
                            break;
                        case 20:
                            buf.append("/Digitizer");
                            break;
                        case 24:
                            buf.append("/Card Reader");
                            break;
                    }
                case 1536:
                    buf.append("Peripheral/(");
                    int minor3 = dc.getMinorDeviceClass();
                    boolean comma = false;
                    if ((minor3 & 16) != 0) {
                        comma = append(buf, "Display", false);
                    }
                    if ((minor3 & 32) != 0) {
                        comma = append(buf, "Camera", comma);
                    }
                    if ((minor3 & 64) != 0) {
                        comma = append(buf, "Scanner", comma);
                    }
                    if ((minor3 & 128) != 0) {
                        append(buf, "Printer", comma);
                    }
                    buf.append(')');
                    break;
                case 7936:
                    buf.append("Unclassified");
                    break;
                default:
                    buf.append("Unknown");
                    break;
            }
            buf.append("/(");
            boolean comma2 = false;
            int record = dc.getServiceClasses();
            if ((record & 8192) != 0) {
                comma2 = append(buf, "Limited Discovery", false);
            }
            if ((record & 65536) != 0) {
                comma2 = append(buf, "Positioning", comma2);
            }
            if ((record & 131072) != 0) {
                comma2 = append(buf, "Networking", comma2);
            }
            if ((record & 262144) != 0) {
                comma2 = append(buf, "Rendering", comma2);
            }
            if ((record & 524288) != 0) {
                comma2 = append(buf, "Capturing", comma2);
            }
            if ((record & 1048576) != 0) {
                comma2 = append(buf, "Object Transfer", comma2);
            }
            if ((record & 2097152) != 0) {
                comma2 = append(buf, "Audio", comma2);
            }
            if ((record & 4194304) != 0) {
                comma2 = append(buf, "Telephony", comma2);
            }
            if ((record & 8388608) != 0) {
                append(buf, "Information", comma2);
            }
            buf.append(')');
            return buf.toString();
        }
    }
}
