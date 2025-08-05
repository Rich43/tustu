package sun.management;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/HotSpotDiagnostic.class */
public class HotSpotDiagnostic implements HotSpotDiagnosticMXBean {
    private native void dumpHeap0(String str, boolean z2) throws IOException;

    @Override // com.sun.management.HotSpotDiagnosticMXBean
    public void dumpHeap(String str, boolean z2) throws SecurityException, IOException {
        String str2 = "jdk.management.heapdump.allowAnyFileSuffix";
        if (!((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.parseBoolean(System.getProperty(str2, "false")));
        })).booleanValue() && !str.endsWith(".hprof")) {
            throw new IllegalArgumentException("heapdump file must have .hprof extention");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(str);
            Util.checkControlAccess();
        }
        dumpHeap0(str, z2);
    }

    @Override // com.sun.management.HotSpotDiagnosticMXBean
    public List<VMOption> getDiagnosticOptions() {
        List<Flag> allFlags = Flag.getAllFlags();
        ArrayList arrayList = new ArrayList();
        for (Flag flag : allFlags) {
            if (flag.isWriteable() && flag.isExternal()) {
                arrayList.add(flag.getVMOption());
            }
        }
        return arrayList;
    }

    @Override // com.sun.management.HotSpotDiagnosticMXBean
    public VMOption getVMOption(String str) {
        if (str == null) {
            throw new NullPointerException("name cannot be null");
        }
        Flag flag = Flag.getFlag(str);
        if (flag == null) {
            throw new IllegalArgumentException("VM option \"" + str + "\" does not exist");
        }
        return flag.getVMOption();
    }

    @Override // com.sun.management.HotSpotDiagnosticMXBean
    public void setVMOption(String str, String str2) throws SecurityException {
        if (str == null) {
            throw new NullPointerException("name cannot be null");
        }
        if (str2 == null) {
            throw new NullPointerException("value cannot be null");
        }
        Util.checkControlAccess();
        Flag flag = Flag.getFlag(str);
        if (flag == null) {
            throw new IllegalArgumentException("VM option \"" + str + "\" does not exist");
        }
        if (!flag.isWriteable()) {
            throw new IllegalArgumentException("VM Option \"" + str + "\" is not writeable");
        }
        Object value = flag.getValue();
        if (value instanceof Long) {
            try {
                Flag.setLongValue(str, Long.parseLong(str2));
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("Invalid value: VM Option \"" + str + "\" expects numeric value", e2);
            }
        } else if (value instanceof Double) {
            try {
                Flag.setDoubleValue(str, Double.parseDouble(str2));
            } catch (NumberFormatException e3) {
                throw new IllegalArgumentException("Invalid value: VM Option \"" + str + "\" expects numeric value", e3);
            }
        } else {
            if (value instanceof Boolean) {
                if (!str2.equalsIgnoreCase("true") && !str2.equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Invalid value: VM Option \"" + str + "\" expects \"true\" or \"false\".");
                }
                Flag.setBooleanValue(str, Boolean.parseBoolean(str2));
                return;
            }
            if (value instanceof String) {
                Flag.setStringValue(str, str2);
                return;
            }
            throw new IllegalArgumentException("VM Option \"" + str + "\" is of an unsupported type: " + value.getClass().getName());
        }
    }

    @Override // java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return Util.newObjectName("com.sun.management:type=HotSpotDiagnostic");
    }
}
