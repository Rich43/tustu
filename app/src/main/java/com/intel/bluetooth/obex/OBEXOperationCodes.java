package com.intel.bluetooth.obex;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXOperationCodes.class */
interface OBEXOperationCodes {
    public static final byte OBEX_VERSION = 16;
    public static final short OBEX_DEFAULT_MTU = 1024;
    public static final short OBEX_MINIMUM_MTU = 255;
    public static final short OBEX_MTU_HEADER_RESERVE = 11;
    public static final int OBEX_MAX_PACKET_LEN = 65535;
    public static final char FINAL_BIT = 128;
    public static final char CONNECT = 128;
    public static final char DISCONNECT = 129;
    public static final char PUT = 2;
    public static final char PUT_FINAL = 130;
    public static final char GET = 3;
    public static final char GET_FINAL = 131;
    public static final char SETPATH = 5;
    public static final char SETPATH_FINAL = 133;
    public static final char SESSION = 7;
    public static final char ABORT = 255;
    public static final int OBEX_RESPONSE_CONTINUE = 144;
    public static final int OBEX_RESPONSE_SUCCESS = 160;
}
