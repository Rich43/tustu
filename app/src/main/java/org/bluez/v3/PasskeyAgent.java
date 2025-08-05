package org.bluez.v3;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;

@DBusInterfaceName("org.bluez.PasskeyAgent")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/PasskeyAgent.class */
public interface PasskeyAgent extends DBusInterface {
    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
    String Request(String str, String str2);

    void Cancel(String str, String str2);

    void Release();
}
