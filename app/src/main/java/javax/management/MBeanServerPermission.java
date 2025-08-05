package javax.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.BasicPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.StringTokenizer;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/management/MBeanServerPermission.class */
public class MBeanServerPermission extends BasicPermission {
    private static final long serialVersionUID = -5661980843569388590L;
    private static final int CREATE = 0;
    private static final int FIND = 1;
    private static final int NEW = 2;
    private static final int RELEASE = 3;
    private static final int N_NAMES = 4;
    private static final int CREATE_MASK = 1;
    private static final int FIND_MASK = 2;
    private static final int NEW_MASK = 4;
    private static final int RELEASE_MASK = 8;
    private static final int ALL_MASK = 15;
    transient int mask;
    private static final String[] names = {"createMBeanServer", "findMBeanServer", "newMBeanServer", "releaseMBeanServer"};
    private static final String[] canonicalNames = new String[16];

    public MBeanServerPermission(String str) {
        this(str, null);
    }

    public MBeanServerPermission(String str, String str2) {
        super(getCanonicalName(parseMask(str)), str2);
        this.mask = parseMask(str);
        if (str2 != null && str2.length() > 0) {
            throw new IllegalArgumentException("MBeanServerPermission actions must be null: " + str2);
        }
    }

    MBeanServerPermission(int i2) {
        super(getCanonicalName(i2));
        this.mask = impliedMask(i2);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.mask = parseMask(getName());
    }

    static int simplifyMask(int i2) {
        if ((i2 & 1) != 0) {
            i2 &= -5;
        }
        return i2;
    }

    static int impliedMask(int i2) {
        if ((i2 & 1) != 0) {
            i2 |= 4;
        }
        return i2;
    }

    static String getCanonicalName(int i2) {
        if (i2 == 15) {
            return "*";
        }
        int iSimplifyMask = simplifyMask(i2);
        synchronized (canonicalNames) {
            if (canonicalNames[iSimplifyMask] == null) {
                canonicalNames[iSimplifyMask] = makeCanonicalName(iSimplifyMask);
            }
        }
        return canonicalNames[iSimplifyMask];
    }

    private static String makeCanonicalName(int i2) {
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < 4; i3++) {
            if ((i2 & (1 << i3)) != 0) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(names[i3]);
            }
        }
        return sb.toString().intern();
    }

    private static int parseMask(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new NullPointerException("MBeanServerPermission: target name can't be null");
        }
        String strTrim = str.trim();
        if (strTrim.equals("*")) {
            return 15;
        }
        if (strTrim.indexOf(44) < 0) {
            return impliedMask(1 << nameIndex(strTrim.trim()));
        }
        int iNameIndex = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(strTrim, ",");
        while (stringTokenizer.hasMoreTokens()) {
            iNameIndex |= 1 << nameIndex(stringTokenizer.nextToken().trim());
        }
        return impliedMask(iNameIndex);
    }

    private static int nameIndex(String str) throws IllegalArgumentException {
        for (int i2 = 0; i2 < 4; i2++) {
            if (names[i2].equals(str)) {
                return i2;
            }
        }
        throw new IllegalArgumentException("Invalid MBeanServerPermission name: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public int hashCode() {
        return this.mask;
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof MBeanServerPermission)) {
            return false;
        }
        MBeanServerPermission mBeanServerPermission = (MBeanServerPermission) permission;
        return (this.mask & mBeanServerPermission.mask) == mBeanServerPermission.mask;
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return (obj instanceof MBeanServerPermission) && this.mask == ((MBeanServerPermission) obj).mask;
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new MBeanServerPermissionCollection();
    }
}
