package org.bluez.v3;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;

@DBusInterfaceName("org.bluez.AuthorizationAgent")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/AuthorizationAgent.class */
public interface AuthorizationAgent extends DBusInterface {
    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
    void Authorize(String str, String str2, String str3, String str4);

    void Cancel(String str, String str2, String str3, String str4);

    void Release();
}
