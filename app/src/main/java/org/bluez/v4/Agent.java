package org.bluez.v4;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;

@DBusInterfaceName("org.bluez.Agent")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Agent.class */
public interface Agent extends DBusInterface {
    void Release();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
    String RequestPinCode(Path path);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
    UInt32 RequestPasskey(Path path);

    void DisplayPasskey(Path path, UInt32 uInt32, byte b2);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
    void RequestConfirmation(Path path, UInt32 uInt32);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
    void Authorize(Path path, String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
    void ConfirmModeChange(String str);

    void Cancel();
}
