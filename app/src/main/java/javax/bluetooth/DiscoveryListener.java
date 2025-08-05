package javax.bluetooth;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/DiscoveryListener.class */
public interface DiscoveryListener {
    public static final int INQUIRY_COMPLETED = 0;
    public static final int INQUIRY_TERMINATED = 5;
    public static final int INQUIRY_ERROR = 7;
    public static final int SERVICE_SEARCH_COMPLETED = 1;
    public static final int SERVICE_SEARCH_TERMINATED = 2;
    public static final int SERVICE_SEARCH_ERROR = 3;
    public static final int SERVICE_SEARCH_NO_RECORDS = 4;
    public static final int SERVICE_SEARCH_DEVICE_NOT_REACHABLE = 6;

    void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass);

    void servicesDiscovered(int i2, ServiceRecord[] serviceRecordArr);

    void serviceSearchCompleted(int i2, int i3);

    void inquiryCompleted(int i2);
}
