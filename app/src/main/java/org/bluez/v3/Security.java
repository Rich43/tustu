package org.bluez.v3;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;

@DBusInterfaceName("org.bluez.Security")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Security.class */
public interface Security extends DBusInterface {
    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$AlreadyExists] */
    void RegisterDefaultPasskeyAgent(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$DoesNotExist] */
    void UnregisterDefaultPasskeyAgent(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$AlreadyExists] */
    void RegisterPasskeyAgent(String str, String str2);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$DoesNotExist] */
    void UnregisterPasskeyAgent(String str, String str2);

    void RegisterDefaultAuthorizationAgent(String str);

    void UnregisterDefaultAuthorizationAgent(String str);
}
