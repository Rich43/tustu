package org.bluez.v3;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.UInt32;

@DBusInterfaceName("org.bluez.Database")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Database.class */
public interface Database extends DBusInterface {
    void RegisterService(String str, String str2, String str3);

    void UnregisterService(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed] */
    UInt32 AddServiceRecord(byte[] bArr);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed] */
    UInt32 AddServiceRecordFromXML(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable, org.bluez.Error$Failed] */
    void UpdateServiceRecord(UInt32 uInt32, byte[] bArr);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable, org.bluez.Error$Failed] */
    void UpdateServiceRecordFromXML(UInt32 uInt32, String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAuthorized, org.bluez.Error$DoesNotExist, org.bluez.Error$Failed] */
    void RemoveServiceRecord(UInt32 uInt32);

    void RequestAuthorization(String str, String str2);

    void CancelAuthorizationRequest(String str, String str2);
}
