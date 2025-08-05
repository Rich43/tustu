package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.BasicPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/util/PropertyPermission.class */
public final class PropertyPermission extends BasicPermission {
    private static final int READ = 1;
    private static final int WRITE = 2;
    private static final int ALL = 3;
    private static final int NONE = 0;
    private transient int mask;
    private String actions;
    private static final long serialVersionUID = 885438825399942851L;

    private void init(int i2) {
        if ((i2 & 3) != i2) {
            throw new IllegalArgumentException("invalid actions mask");
        }
        if (i2 == 0) {
            throw new IllegalArgumentException("invalid actions mask");
        }
        if (getName() == null) {
            throw new NullPointerException("name can't be null");
        }
        this.mask = i2;
    }

    public PropertyPermission(String str, String str2) {
        super(str, str2);
        init(getMask(str2));
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof PropertyPermission)) {
            return false;
        }
        PropertyPermission propertyPermission = (PropertyPermission) permission;
        return (this.mask & propertyPermission.mask) == propertyPermission.mask && super.implies(propertyPermission);
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PropertyPermission)) {
            return false;
        }
        PropertyPermission propertyPermission = (PropertyPermission) obj;
        return this.mask == propertyPermission.mask && getName().equals(propertyPermission.getName());
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public int hashCode() {
        return getName().hashCode();
    }

    private static int getMask(String str) {
        int i2;
        char c2;
        int i3 = 0;
        if (str == null) {
            return 0;
        }
        if (str == "read") {
            return 1;
        }
        if (str == "write") {
            return 2;
        }
        if (str == SecurityConstants.PROPERTY_RW_ACTION) {
            return 3;
        }
        char[] charArray = str.toCharArray();
        int length = charArray.length - 1;
        if (length < 0) {
            return 0;
        }
        while (length != -1) {
            while (length != -1 && ((c2 = charArray[length]) == ' ' || c2 == '\r' || c2 == '\n' || c2 == '\f' || c2 == '\t')) {
                length--;
            }
            if (length >= 3 && ((charArray[length - 3] == 'r' || charArray[length - 3] == 'R') && ((charArray[length - 2] == 'e' || charArray[length - 2] == 'E') && ((charArray[length - 1] == 'a' || charArray[length - 1] == 'A') && (charArray[length] == 'd' || charArray[length] == 'D'))))) {
                i2 = 4;
                i3 |= 1;
            } else if (length >= 4 && ((charArray[length - 4] == 'w' || charArray[length - 4] == 'W') && ((charArray[length - 3] == 'r' || charArray[length - 3] == 'R') && ((charArray[length - 2] == 'i' || charArray[length - 2] == 'I') && ((charArray[length - 1] == 't' || charArray[length - 1] == 'T') && (charArray[length] == 'e' || charArray[length] == 'E')))))) {
                i2 = 5;
                i3 |= 2;
            } else {
                throw new IllegalArgumentException("invalid permission: " + str);
            }
            boolean z2 = false;
            while (length >= i2 && !z2) {
                switch (charArray[length - i2]) {
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                        break;
                    case ',':
                        z2 = true;
                        break;
                    default:
                        throw new IllegalArgumentException("invalid permission: " + str);
                }
                length--;
            }
            length -= i2;
        }
        return i3;
    }

    static String getActions(int i2) {
        StringBuilder sb = new StringBuilder();
        boolean z2 = false;
        if ((i2 & 1) == 1) {
            z2 = true;
            sb.append("read");
        }
        if ((i2 & 2) == 2) {
            if (z2) {
                sb.append(',');
            }
            sb.append("write");
        }
        return sb.toString();
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public String getActions() {
        if (this.actions == null) {
            this.actions = getActions(this.mask);
        }
        return this.actions;
    }

    int getMask() {
        return this.mask;
    }

    @Override // java.security.BasicPermission, java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new PropertyPermissionCollection();
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.actions == null) {
            getActions();
        }
        objectOutputStream.defaultWriteObject();
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(getMask(this.actions));
    }
}
