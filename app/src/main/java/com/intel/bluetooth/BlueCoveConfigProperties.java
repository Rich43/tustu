package com.intel.bluetooth;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BlueCoveConfigProperties.class */
public interface BlueCoveConfigProperties {
    public static final String PROPERTY_DEBUG = "bluecove.debug";
    public static final String PROPERTY_DEBUG_STDOUT = "bluecove.debug.stdout";
    public static final String PROPERTY_DEBUG_LOG4J = "bluecove.debug.log4j";
    public static final String PROPERTY_STACK = "bluecove.stack";
    public static final String PROPERTY_NATIVE_PATH = "bluecove.native.path";
    public static final String PROPERTY_LOCAL_DEVICE_ID = "bluecove.deviceID";
    public static final String PROPERTY_EMULATOR_RMI_REGISTRY = "bluecove.emu.rmiRegistry";
    public static final String PROPERTY_CONNECT_TIMEOUT = "bluecove.connect.timeout";
    public static final String PROPERTY_CONNECT_UNREACHABLE_RETRY = "bluecove.connect.unreachable_retry";
    public static final String PROPERTY_INQUIRY_DURATION = "bluecove.inquiry.duration";
    public static final int PROPERTY_INQUIRY_DURATION_DEFAULT = 11;
    public static final String PROPERTY_INQUIRY_REPORT_ASAP = "bluecove.inquiry.report_asap";
    public static final String PROPERTY_OBEX_MTU = "bluecove.obex.mtu";
    public static final String PROPERTY_OBEX_TIMEOUT = "bluecove.obex.timeout";
    public static final String PROPERTY_JSR_82_PSM_MINIMUM_OFF = "bluecove.jsr82.psm_minimum_off";
    public static final String PROPERTY_SDP_STRING_ENCODING_ASCII = "bluecove.sdp.string_encoding_ascii";
    public static final String PROPERTY_STACK_FIRST = "bluecove.stack.first";
    public static final String PROPERTY_NATIVE_RESOURCE = "bluecove.native.resource";
    public static final String PROPERTY_BLUEZ_CLASS = "bluecove.bluez.class";
    public static final String PROPERTY_LOCAL_DEVICE_ADDRESS = "bluecove.deviceAddress";
    public static final String PROPERTY_EMULATOR_CLASS = "bluecove.emulator.class";
    public static final String PROPERTY_EMULATOR_HOST = "bluecove.emu.rmiRegistryHost";
    public static final String PROPERTY_EMULATOR_PORT = "bluecove.emu.rmiRegistryPort";
    public static final String[] INITIALIZATION_PROPERTIES = {"bluecove.stack", PROPERTY_STACK_FIRST, PROPERTY_NATIVE_RESOURCE, PROPERTY_NATIVE_RESOURCE, PROPERTY_BLUEZ_CLASS, "bluecove.deviceID", PROPERTY_LOCAL_DEVICE_ADDRESS, PROPERTY_EMULATOR_CLASS, PROPERTY_EMULATOR_HOST, PROPERTY_EMULATOR_PORT};
}
