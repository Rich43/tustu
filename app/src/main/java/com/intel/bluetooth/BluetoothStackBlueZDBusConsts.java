package com.intel.bluetooth;

/* loaded from: bluecove-bluez-2.1.1.jar:com/intel/bluetooth/BluetoothStackBlueZDBusConsts.class */
abstract class BluetoothStackBlueZDBusConsts {
    public static final int NOT_DISCOVERABLE = 0;
    public static final int GIAC = 10390323;
    public static final int LIAC = 10390272;
    static final int INQUIRY_COMPLETED = 0;
    static final int INQUIRY_TERMINATED = 5;
    static final int INQUIRY_ERROR = 7;
    static final int SERVICE_SEARCH_COMPLETED = 1;
    static final int SERVICE_SEARCH_TERMINATED = 2;
    static final int SERVICE_SEARCH_ERROR = 3;
    static final int SERVICE_SEARCH_NO_RECORDS = 4;
    static final int SERVICE_SEARCH_DEVICE_NOT_REACHABLE = 6;
    static final int NOAUTHENTICATE_NOENCRYPT = 0;
    static final int AUTHENTICATE_NOENCRYPT = 1;
    static final int AUTHENTICATE_ENCRYPT = 2;
    static final int DataElement_NULL = 0;
    static final int DataElement_U_INT_1 = 8;
    static final int DataElement_U_INT_2 = 9;
    static final int DataElement_U_INT_4 = 10;
    static final int DataElement_U_INT_8 = 11;
    static final int DataElement_U_INT_16 = 12;
    static final int DataElement_INT_1 = 16;
    static final int DataElement_INT_2 = 17;
    static final int DataElement_INT_4 = 18;
    static final int DataElement_INT_8 = 19;
    static final int DataElement_INT_16 = 20;
    static final int DataElement_URL = 64;
    static final int DataElement_UUID = 24;
    static final int DataElement_BOOL = 40;
    static final int DataElement_STRING = 32;
    static final int DataElement_DATSEQ = 48;
    static final int DataElement_DATALT = 56;
    static final int CONNECTION_ERROR_UNKNOWN_PSM = 1;
    static final int CONNECTION_ERROR_SECURITY_BLOCK = 2;
    static final int CONNECTION_ERROR_NO_RESOURCES = 3;
    static final int CONNECTION_ERROR_FAILED_NOINFO = 4;
    static final int CONNECTION_ERROR_TIMEOUT = 5;
    static final int CONNECTION_ERROR_UNACCEPTABLE_PARAMS = 6;

    BluetoothStackBlueZDBusConsts() {
    }
}
