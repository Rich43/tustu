package sun.management;

import com.sun.management.VMOption;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:sun/management/Flag.class */
class Flag {
    private String name;
    private Object value;
    private VMOption.Origin origin;
    private boolean writeable;
    private boolean external;

    private static native String[] getAllFlagNames();

    private static native int getFlags(String[] strArr, Flag[] flagArr, int i2);

    private static native int getInternalFlagCount();

    static native synchronized void setLongValue(String str, long j2);

    static native synchronized void setDoubleValue(String str, double d2);

    static native synchronized void setBooleanValue(String str, boolean z2);

    static native synchronized void setStringValue(String str, String str2);

    private static native void initialize();

    Flag(String str, Object obj, boolean z2, boolean z3, VMOption.Origin origin) {
        this.name = str;
        this.value = obj == null ? "" : obj;
        this.origin = origin;
        this.writeable = z2;
        this.external = z3;
    }

    Object getValue() {
        return this.value;
    }

    boolean isWriteable() {
        return this.writeable;
    }

    boolean isExternal() {
        return this.external;
    }

    VMOption getVMOption() {
        return new VMOption(this.name, this.value.toString(), this.writeable, this.origin);
    }

    static Flag getFlag(String str) {
        List<Flag> flags = getFlags(new String[]{str}, 1);
        if (flags.isEmpty()) {
            return null;
        }
        return flags.get(0);
    }

    static List<Flag> getAllFlags() {
        return getFlags(null, getInternalFlagCount());
    }

    private static List<Flag> getFlags(String[] strArr, int i2) {
        Flag[] flagArr = new Flag[i2];
        getFlags(strArr, flagArr, i2);
        ArrayList arrayList = new ArrayList();
        for (Flag flag : flagArr) {
            if (flag != null) {
                arrayList.add(flag);
            }
        }
        return arrayList;
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.management.Flag.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("management");
                return null;
            }
        });
        initialize();
    }
}
