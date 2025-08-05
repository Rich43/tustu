package javax.bluetooth;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/ServiceRecord.class */
public interface ServiceRecord {
    public static final int NOAUTHENTICATE_NOENCRYPT = 0;
    public static final int AUTHENTICATE_NOENCRYPT = 1;
    public static final int AUTHENTICATE_ENCRYPT = 2;

    DataElement getAttributeValue(int i2);

    RemoteDevice getHostDevice();

    int[] getAttributeIDs();

    boolean populateRecord(int[] iArr) throws IOException;

    String getConnectionURL(int i2, boolean z2);

    void setDeviceServiceClasses(int i2);

    boolean setAttributeValue(int i2, DataElement dataElement);
}
