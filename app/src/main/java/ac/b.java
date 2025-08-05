package aC;

import G.C0129l;
import bH.C;
import bH.W;
import com.intel.bluetooth.RemoteDeviceHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

/* loaded from: TunerStudioMS.jar:aC/b.class */
public class b {

    /* renamed from: a, reason: collision with root package name */
    static final Object f2294a = new Object();

    public static boolean a() {
        try {
            LocalDevice.getLocalDevice().getDiscoveryAgent();
            return true;
        } catch (Exception e2) {
            C.d("No Bluetooth Stack Detected.");
            return false;
        }
    }

    public static List b() {
        ArrayList arrayList = new ArrayList();
        try {
            RemoteDevice[] remoteDeviceArrRetrieveDevices = LocalDevice.getLocalDevice().getDiscoveryAgent().retrieveDevices(1);
            if (remoteDeviceArrRetrieveDevices != null) {
                for (RemoteDevice remoteDevice : remoteDeviceArrRetrieveDevices) {
                    arrayList.add(remoteDevice);
                }
            }
        } catch (BluetoothStateException e2) {
            C.a("Failed to retrieve BT Devices");
        }
        return arrayList;
    }

    public static List a(boolean z2) {
        ArrayList arrayList = new ArrayList();
        a(new c(arrayList, z2));
        C.c(arrayList.size() + " device(s) found");
        return arrayList;
    }

    public static void a(d dVar) {
        aK.a aVarB = aK.a.b();
        boolean zE = aVarB.e();
        if (zE) {
            aVarB.f();
        }
        try {
            synchronized (f2294a) {
                if (LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(10390323, dVar)) {
                    C.c("waiting for Bluetooth device inquiry to complete...");
                    f2294a.wait();
                }
            }
        } catch (InterruptedException e2) {
            C.b(e2.getMessage());
        } catch (BluetoothStateException e3) {
            C.b("Bluetooth error: " + e3.getLocalizedMessage());
        }
        if (zE) {
            try {
                aK.a.b().d();
            } catch (C0129l e4) {
                C.b("Failed to restart GpsController: " + e4.getLocalizedMessage());
            }
        }
    }

    public static List c() {
        String friendlyName;
        List<RemoteDevice> listA = a(false);
        ArrayList arrayList = new ArrayList();
        for (RemoteDevice remoteDevice : listA) {
            try {
                friendlyName = remoteDevice.getFriendlyName(false);
            } catch (IOException e2) {
                Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                friendlyName = "";
            }
            if (W.b(friendlyName, " ", "").toLowerCase().startsWith("efianal") && !remoteDevice.isTrustedDevice()) {
                arrayList.add(remoteDevice);
            }
        }
        return arrayList;
    }

    public static boolean a(RemoteDevice remoteDevice, String str) {
        try {
            return RemoteDeviceHelper.authenticate(remoteDevice, str);
        } catch (IOException e2) {
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return false;
        }
    }

    public static String a(RemoteDevice remoteDevice) {
        try {
            return remoteDevice.getFriendlyName(false) + " (" + remoteDevice.getBluetoothAddress() + ")";
        } catch (IOException e2) {
            return "(" + remoteDevice.getBluetoothAddress() + ")";
        }
    }
}
