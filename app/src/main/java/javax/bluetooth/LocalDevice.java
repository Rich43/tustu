package javax.bluetooth;

import com.intel.bluetooth.BlueCoveImpl;
import com.intel.bluetooth.BlueCoveLocalDeviceProperties;
import com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess;
import com.intel.bluetooth.BluetoothConsts;
import com.intel.bluetooth.BluetoothStack;
import com.intel.bluetooth.RemoteDeviceHelper;
import com.intel.bluetooth.ServiceRecordsRegistry;
import com.intel.bluetooth.UtilsJavaSE;
import java.util.Hashtable;
import javax.microedition.io.Connection;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/LocalDevice.class */
public class LocalDevice {
    private static Hashtable localDevices = new Hashtable();
    private BluetoothStack bluetoothStack;
    private DiscoveryAgent discoveryAgent;
    private String addressStr;

    private LocalDevice(BluetoothStack stack) throws BluetoothStateException {
        this.bluetoothStack = stack;
        this.discoveryAgent = new DiscoveryAgent(this.bluetoothStack);
        this.addressStr = RemoteDeviceHelper.formatBluetoothAddress(this.bluetoothStack.getLocalDeviceBluetoothAddress());
    }

    private static synchronized LocalDevice getLocalDeviceInstance() throws BluetoothStateException {
        BluetoothStack stack = BlueCoveImpl.instance().getBluetoothStack();
        LocalDevice localDevice = (LocalDevice) localDevices.get(stack);
        if (localDevice == null) {
            localDevice = new LocalDevice(stack);
            localDevices.put(stack, localDevice);
        }
        return localDevice;
    }

    public static LocalDevice getLocalDevice() throws BluetoothStateException {
        return getLocalDeviceInstance();
    }

    public static boolean isPowerOn() {
        try {
            return BlueCoveImpl.instance().getBluetoothStack().isLocalDevicePowerOn();
        } catch (BluetoothStateException e2) {
            return false;
        }
    }

    public DiscoveryAgent getDiscoveryAgent() {
        return this.discoveryAgent;
    }

    public String getFriendlyName() {
        return this.bluetoothStack.getLocalDeviceName();
    }

    public DeviceClass getDeviceClass() {
        return this.bluetoothStack.getLocalDeviceClass();
    }

    public boolean setDiscoverable(int mode) throws BluetoothStateException {
        if (mode != 10390323 && mode != 10390272 && mode != 0 && (mode < 10390272 || mode > 10390335)) {
            throw new IllegalArgumentException("Invalid discoverable mode");
        }
        return this.bluetoothStack.setLocalDeviceDiscoverable(mode);
    }

    public static String getProperty(String property) {
        try {
            if (BluetoothConsts.PROPERTY_BLUETOOTH_API_VERSION.equals(property) || BluetoothConsts.PROPERTY_OBEX_API_VERSION.equals(property)) {
                return "1.1.1";
            }
            if ("bluecove".equals(property)) {
                return BlueCoveImpl.version;
            }
            if ("bluecove.stack".equals(property)) {
                return BlueCoveImpl.instance().getBluetoothStack().getStackID();
            }
            if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_PROPERTY_FEATURE_L2CAP.equals(property)) {
                return BlueCoveImpl.instance().getLocalDeviceFeature(1);
            }
            if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_PROPERTY_FEATURE_SERVICE_ATTRIBUTES.equals(property)) {
                return BlueCoveImpl.instance().getLocalDeviceFeature(2);
            }
            if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_PROPERTY_FEATURE_SET_DEVICE_SERVICE_CLASSES.equals(property)) {
                return BlueCoveImpl.instance().getLocalDeviceFeature(4);
            }
            if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_PROPERTY_FEATURE_RSSI.equals(property)) {
                return BlueCoveImpl.instance().getLocalDeviceFeature(8);
            }
            if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_PROPERTY_OPEN_CONNECTIONS.equals(property)) {
                return String.valueOf(RemoteDeviceHelper.openConnections());
            }
            return BlueCoveImpl.instance().getBluetoothStack().getLocalDeviceProperty(property);
        } catch (BluetoothStateException e2) {
            throw ((RuntimeException) UtilsJavaSE.initCause(new RuntimeException(e2.getMessage()), e2));
        }
    }

    public int getDiscoverable() {
        return this.bluetoothStack.getLocalDeviceDiscoverable();
    }

    public String getBluetoothAddress() {
        return this.addressStr;
    }

    public ServiceRecord getRecord(Connection notifier) {
        if (notifier == null) {
            throw new NullPointerException("notifier is null");
        }
        if (!(notifier instanceof BluetoothConnectionNotifierServiceRecordAccess)) {
            throw new IllegalArgumentException("connection is not a Bluetooth notifier");
        }
        return ((BluetoothConnectionNotifierServiceRecordAccess) notifier).getServiceRecord();
    }

    public void updateRecord(ServiceRecord srvRecord) throws ServiceRegistrationException {
        if (srvRecord == null) {
            throw new NullPointerException("Service Record is null");
        }
        ServiceRecordsRegistry.updateServiceRecord(srvRecord);
    }
}
