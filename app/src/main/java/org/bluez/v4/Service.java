package org.bluez.v4;

import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.UInt32;

@DBusInterfaceName("org.bluez.Service")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Service.class */
public interface Service extends org.bluez.v3.Service {
    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed] */
    UInt32 AddRecord(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable, org.bluez.Error$Failed] */
    void UpdateRecord(UInt32 uInt32, String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAuthorized, org.bluez.Error$DoesNotExist, org.bluez.Error$Failed] */
    void RemoveRecord(UInt32 uInt32);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAuthorized, org.bluez.Error$DoesNotExist, org.bluez.Error$Failed] */
    void RequestAuthorization(String str, UInt32 uInt32);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAuthorized, org.bluez.Error$DoesNotExist, org.bluez.Error$Failed] */
    void CancelAuthorization();
}
