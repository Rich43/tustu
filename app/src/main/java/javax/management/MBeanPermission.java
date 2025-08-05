package javax.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Permission;
import javafx.fxml.FXMLLoader;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/management/MBeanPermission.class */
public class MBeanPermission extends Permission {
    private static final long serialVersionUID = -2416928705275160661L;
    private static final int AddNotificationListener = 1;
    private static final int GetAttribute = 2;
    private static final int GetClassLoader = 4;
    private static final int GetClassLoaderFor = 8;
    private static final int GetClassLoaderRepository = 16;
    private static final int GetDomains = 32;
    private static final int GetMBeanInfo = 64;
    private static final int GetObjectInstance = 128;
    private static final int Instantiate = 256;
    private static final int Invoke = 512;
    private static final int IsInstanceOf = 1024;
    private static final int QueryMBeans = 2048;
    private static final int QueryNames = 4096;
    private static final int RegisterMBean = 8192;
    private static final int RemoveNotificationListener = 16384;
    private static final int SetAttribute = 32768;
    private static final int UnregisterMBean = 65536;
    private static final int NONE = 0;
    private static final int ALL = 131071;
    private String actions;
    private transient int mask;
    private transient String classNamePrefix;
    private transient boolean classNameExactMatch;
    private transient String member;
    private transient ObjectName objectName;

    private void parseActions() {
        if (this.actions == null) {
            throw new IllegalArgumentException("MBeanPermission: actions can't be null");
        }
        if (this.actions.equals("")) {
            throw new IllegalArgumentException("MBeanPermission: actions can't be empty");
        }
        int mask = getMask(this.actions);
        if ((mask & ALL) != mask) {
            throw new IllegalArgumentException("Invalid actions mask");
        }
        if (mask == 0) {
            throw new IllegalArgumentException("Invalid actions mask");
        }
        this.mask = mask;
    }

    private void parseName() {
        String name = getName();
        if (name == null) {
            throw new IllegalArgumentException("MBeanPermission name cannot be null");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("MBeanPermission name cannot be empty");
        }
        int iIndexOf = name.indexOf("[");
        if (iIndexOf == -1) {
            this.objectName = ObjectName.WILDCARD;
        } else {
            if (!name.endsWith("]")) {
                throw new IllegalArgumentException("MBeanPermission: The ObjectName in the target name must be included in square brackets");
            }
            try {
                String strSubstring = name.substring(iIndexOf + 1, name.length() - 1);
                if (strSubstring.equals("")) {
                    this.objectName = ObjectName.WILDCARD;
                } else if (strSubstring.equals(LanguageTag.SEP)) {
                    this.objectName = null;
                } else {
                    this.objectName = new ObjectName(strSubstring);
                }
                name = name.substring(0, iIndexOf);
            } catch (MalformedObjectNameException e2) {
                throw new IllegalArgumentException("MBeanPermission: The target name does not specify a valid ObjectName", e2);
            }
        }
        int iIndexOf2 = name.indexOf(FXMLLoader.CONTROLLER_METHOD_PREFIX);
        if (iIndexOf2 == -1) {
            setMember("*");
        } else {
            setMember(name.substring(iIndexOf2 + 1));
            name = name.substring(0, iIndexOf2);
        }
        setClassName(name);
    }

    private void initName(String str, String str2, ObjectName objectName) {
        setClassName(str);
        setMember(str2);
        this.objectName = objectName;
    }

    private void setClassName(String str) {
        if (str == null || str.equals(LanguageTag.SEP)) {
            this.classNamePrefix = null;
            this.classNameExactMatch = false;
            return;
        }
        if (str.equals("") || str.equals("*")) {
            this.classNamePrefix = "";
            this.classNameExactMatch = false;
        } else if (str.endsWith(".*")) {
            this.classNamePrefix = str.substring(0, str.length() - 1);
            this.classNameExactMatch = false;
        } else {
            this.classNamePrefix = str;
            this.classNameExactMatch = true;
        }
    }

    private void setMember(String str) {
        if (str == null || str.equals(LanguageTag.SEP)) {
            this.member = null;
        } else if (str.equals("")) {
            this.member = "*";
        } else {
            this.member = str;
        }
    }

    public MBeanPermission(String str, String str2) {
        super(str);
        parseName();
        this.actions = str2;
        parseActions();
    }

    public MBeanPermission(String str, String str2, ObjectName objectName, String str3) {
        super(makeName(str, str2, objectName));
        initName(str, str2, objectName);
        this.actions = str3;
        parseActions();
    }

    private static String makeName(String str, String str2, ObjectName objectName) {
        StringBuilder sb = new StringBuilder();
        if (str == null) {
            str = LanguageTag.SEP;
        }
        sb.append(str);
        if (str2 == null) {
            str2 = LanguageTag.SEP;
        }
        sb.append(FXMLLoader.CONTROLLER_METHOD_PREFIX + str2);
        if (objectName == null) {
            sb.append("[-]");
        } else {
            sb.append("[").append(objectName.getCanonicalName()).append("]");
        }
        if (sb.length() == 0) {
            return "*";
        }
        return sb.toString();
    }

    @Override // java.security.Permission
    public String getActions() {
        if (this.actions == null) {
            this.actions = getActions(this.mask);
        }
        return this.actions;
    }

    private static String getActions(int i2) {
        StringBuilder sb = new StringBuilder();
        boolean z2 = false;
        if ((i2 & 1) == 1) {
            z2 = true;
            sb.append("addNotificationListener");
        }
        if ((i2 & 2) == 2) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("getAttribute");
        }
        if ((i2 & 4) == 4) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("getClassLoader");
        }
        if ((i2 & 8) == 8) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("getClassLoaderFor");
        }
        if ((i2 & 16) == 16) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("getClassLoaderRepository");
        }
        if ((i2 & 32) == 32) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("getDomains");
        }
        if ((i2 & 64) == 64) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("getMBeanInfo");
        }
        if ((i2 & 128) == 128) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("getObjectInstance");
        }
        if ((i2 & 256) == 256) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("instantiate");
        }
        if ((i2 & 512) == 512) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("invoke");
        }
        if ((i2 & 1024) == 1024) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("isInstanceOf");
        }
        if ((i2 & 2048) == 2048) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("queryMBeans");
        }
        if ((i2 & 4096) == 4096) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("queryNames");
        }
        if ((i2 & 8192) == 8192) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("registerMBean");
        }
        if ((i2 & 16384) == 16384) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("removeNotificationListener");
        }
        if ((i2 & 32768) == 32768) {
            if (z2) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("setAttribute");
        }
        if ((i2 & 65536) == 65536) {
            if (z2) {
                sb.append(',');
            }
            sb.append("unregisterMBean");
        }
        return sb.toString();
    }

    @Override // java.security.Permission
    public int hashCode() {
        return getName().hashCode() + getActions().hashCode();
    }

    private static int getMask(String str) {
        int i2;
        char c2;
        int i3 = 0;
        if (str == null) {
            return 0;
        }
        if (str.equals("*")) {
            return ALL;
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
            if (length >= 25 && charArray[length - 25] == 'r' && charArray[length - 24] == 'e' && charArray[length - 23] == 'm' && charArray[length - 22] == 'o' && charArray[length - 21] == 'v' && charArray[length - 20] == 'e' && charArray[length - 19] == 'N' && charArray[length - 18] == 'o' && charArray[length - 17] == 't' && charArray[length - 16] == 'i' && charArray[length - 15] == 'f' && charArray[length - 14] == 'i' && charArray[length - 13] == 'c' && charArray[length - 12] == 'a' && charArray[length - 11] == 't' && charArray[length - 10] == 'i' && charArray[length - 9] == 'o' && charArray[length - 8] == 'n' && charArray[length - 7] == 'L' && charArray[length - 6] == 'i' && charArray[length - 5] == 's' && charArray[length - 4] == 't' && charArray[length - 3] == 'e' && charArray[length - 2] == 'n' && charArray[length - 1] == 'e' && charArray[length] == 'r') {
                i2 = 26;
                i3 |= 16384;
            } else if (length >= 23 && charArray[length - 23] == 'g' && charArray[length - 22] == 'e' && charArray[length - 21] == 't' && charArray[length - 20] == 'C' && charArray[length - 19] == 'l' && charArray[length - 18] == 'a' && charArray[length - 17] == 's' && charArray[length - 16] == 's' && charArray[length - 15] == 'L' && charArray[length - 14] == 'o' && charArray[length - 13] == 'a' && charArray[length - 12] == 'd' && charArray[length - 11] == 'e' && charArray[length - 10] == 'r' && charArray[length - 9] == 'R' && charArray[length - 8] == 'e' && charArray[length - 7] == 'p' && charArray[length - 6] == 'o' && charArray[length - 5] == 's' && charArray[length - 4] == 'i' && charArray[length - 3] == 't' && charArray[length - 2] == 'o' && charArray[length - 1] == 'r' && charArray[length] == 'y') {
                i2 = 24;
                i3 |= 16;
            } else if (length >= 22 && charArray[length - 22] == 'a' && charArray[length - 21] == 'd' && charArray[length - 20] == 'd' && charArray[length - 19] == 'N' && charArray[length - 18] == 'o' && charArray[length - 17] == 't' && charArray[length - 16] == 'i' && charArray[length - 15] == 'f' && charArray[length - 14] == 'i' && charArray[length - 13] == 'c' && charArray[length - 12] == 'a' && charArray[length - 11] == 't' && charArray[length - 10] == 'i' && charArray[length - 9] == 'o' && charArray[length - 8] == 'n' && charArray[length - 7] == 'L' && charArray[length - 6] == 'i' && charArray[length - 5] == 's' && charArray[length - 4] == 't' && charArray[length - 3] == 'e' && charArray[length - 2] == 'n' && charArray[length - 1] == 'e' && charArray[length] == 'r') {
                i2 = 23;
                i3 |= 1;
            } else if (length >= 16 && charArray[length - 16] == 'g' && charArray[length - 15] == 'e' && charArray[length - 14] == 't' && charArray[length - 13] == 'C' && charArray[length - 12] == 'l' && charArray[length - 11] == 'a' && charArray[length - 10] == 's' && charArray[length - 9] == 's' && charArray[length - 8] == 'L' && charArray[length - 7] == 'o' && charArray[length - 6] == 'a' && charArray[length - 5] == 'd' && charArray[length - 4] == 'e' && charArray[length - 3] == 'r' && charArray[length - 2] == 'F' && charArray[length - 1] == 'o' && charArray[length] == 'r') {
                i2 = 17;
                i3 |= 8;
            } else if (length >= 16 && charArray[length - 16] == 'g' && charArray[length - 15] == 'e' && charArray[length - 14] == 't' && charArray[length - 13] == 'O' && charArray[length - 12] == 'b' && charArray[length - 11] == 'j' && charArray[length - 10] == 'e' && charArray[length - 9] == 'c' && charArray[length - 8] == 't' && charArray[length - 7] == 'I' && charArray[length - 6] == 'n' && charArray[length - 5] == 's' && charArray[length - 4] == 't' && charArray[length - 3] == 'a' && charArray[length - 2] == 'n' && charArray[length - 1] == 'c' && charArray[length] == 'e') {
                i2 = 17;
                i3 |= 128;
            } else if (length >= 14 && charArray[length - 14] == 'u' && charArray[length - 13] == 'n' && charArray[length - 12] == 'r' && charArray[length - 11] == 'e' && charArray[length - 10] == 'g' && charArray[length - 9] == 'i' && charArray[length - 8] == 's' && charArray[length - 7] == 't' && charArray[length - 6] == 'e' && charArray[length - 5] == 'r' && charArray[length - 4] == 'M' && charArray[length - 3] == 'B' && charArray[length - 2] == 'e' && charArray[length - 1] == 'a' && charArray[length] == 'n') {
                i2 = 15;
                i3 |= 65536;
            } else if (length >= 13 && charArray[length - 13] == 'g' && charArray[length - 12] == 'e' && charArray[length - 11] == 't' && charArray[length - 10] == 'C' && charArray[length - 9] == 'l' && charArray[length - 8] == 'a' && charArray[length - 7] == 's' && charArray[length - 6] == 's' && charArray[length - 5] == 'L' && charArray[length - 4] == 'o' && charArray[length - 3] == 'a' && charArray[length - 2] == 'd' && charArray[length - 1] == 'e' && charArray[length] == 'r') {
                i2 = 14;
                i3 |= 4;
            } else if (length >= 12 && charArray[length - 12] == 'r' && charArray[length - 11] == 'e' && charArray[length - 10] == 'g' && charArray[length - 9] == 'i' && charArray[length - 8] == 's' && charArray[length - 7] == 't' && charArray[length - 6] == 'e' && charArray[length - 5] == 'r' && charArray[length - 4] == 'M' && charArray[length - 3] == 'B' && charArray[length - 2] == 'e' && charArray[length - 1] == 'a' && charArray[length] == 'n') {
                i2 = 13;
                i3 |= 8192;
            } else if (length >= 11 && charArray[length - 11] == 'g' && charArray[length - 10] == 'e' && charArray[length - 9] == 't' && charArray[length - 8] == 'A' && charArray[length - 7] == 't' && charArray[length - 6] == 't' && charArray[length - 5] == 'r' && charArray[length - 4] == 'i' && charArray[length - 3] == 'b' && charArray[length - 2] == 'u' && charArray[length - 1] == 't' && charArray[length] == 'e') {
                i2 = 12;
                i3 |= 2;
            } else if (length >= 11 && charArray[length - 11] == 'g' && charArray[length - 10] == 'e' && charArray[length - 9] == 't' && charArray[length - 8] == 'M' && charArray[length - 7] == 'B' && charArray[length - 6] == 'e' && charArray[length - 5] == 'a' && charArray[length - 4] == 'n' && charArray[length - 3] == 'I' && charArray[length - 2] == 'n' && charArray[length - 1] == 'f' && charArray[length] == 'o') {
                i2 = 12;
                i3 |= 64;
            } else if (length >= 11 && charArray[length - 11] == 'i' && charArray[length - 10] == 's' && charArray[length - 9] == 'I' && charArray[length - 8] == 'n' && charArray[length - 7] == 's' && charArray[length - 6] == 't' && charArray[length - 5] == 'a' && charArray[length - 4] == 'n' && charArray[length - 3] == 'c' && charArray[length - 2] == 'e' && charArray[length - 1] == 'O' && charArray[length] == 'f') {
                i2 = 12;
                i3 |= 1024;
            } else if (length >= 11 && charArray[length - 11] == 's' && charArray[length - 10] == 'e' && charArray[length - 9] == 't' && charArray[length - 8] == 'A' && charArray[length - 7] == 't' && charArray[length - 6] == 't' && charArray[length - 5] == 'r' && charArray[length - 4] == 'i' && charArray[length - 3] == 'b' && charArray[length - 2] == 'u' && charArray[length - 1] == 't' && charArray[length] == 'e') {
                i2 = 12;
                i3 |= 32768;
            } else if (length >= 10 && charArray[length - 10] == 'i' && charArray[length - 9] == 'n' && charArray[length - 8] == 's' && charArray[length - 7] == 't' && charArray[length - 6] == 'a' && charArray[length - 5] == 'n' && charArray[length - 4] == 't' && charArray[length - 3] == 'i' && charArray[length - 2] == 'a' && charArray[length - 1] == 't' && charArray[length] == 'e') {
                i2 = 11;
                i3 |= 256;
            } else if (length >= 10 && charArray[length - 10] == 'q' && charArray[length - 9] == 'u' && charArray[length - 8] == 'e' && charArray[length - 7] == 'r' && charArray[length - 6] == 'y' && charArray[length - 5] == 'M' && charArray[length - 4] == 'B' && charArray[length - 3] == 'e' && charArray[length - 2] == 'a' && charArray[length - 1] == 'n' && charArray[length] == 's') {
                i2 = 11;
                i3 |= 2048;
            } else if (length >= 9 && charArray[length - 9] == 'g' && charArray[length - 8] == 'e' && charArray[length - 7] == 't' && charArray[length - 6] == 'D' && charArray[length - 5] == 'o' && charArray[length - 4] == 'm' && charArray[length - 3] == 'a' && charArray[length - 2] == 'i' && charArray[length - 1] == 'n' && charArray[length] == 's') {
                i2 = 10;
                i3 |= 32;
            } else if (length >= 9 && charArray[length - 9] == 'q' && charArray[length - 8] == 'u' && charArray[length - 7] == 'e' && charArray[length - 6] == 'r' && charArray[length - 5] == 'y' && charArray[length - 4] == 'N' && charArray[length - 3] == 'a' && charArray[length - 2] == 'm' && charArray[length - 1] == 'e' && charArray[length] == 's') {
                i2 = 10;
                i3 |= 4096;
            } else if (length >= 5 && charArray[length - 5] == 'i' && charArray[length - 4] == 'n' && charArray[length - 3] == 'v' && charArray[length - 2] == 'o' && charArray[length - 1] == 'k' && charArray[length] == 'e') {
                i2 = 6;
                i3 |= 512;
            } else {
                throw new IllegalArgumentException("Invalid permission: " + str);
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
                        throw new IllegalArgumentException("Invalid permission: " + str);
                }
                length--;
            }
            length -= i2;
        }
        return i3;
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof MBeanPermission)) {
            return false;
        }
        MBeanPermission mBeanPermission = (MBeanPermission) permission;
        if ((this.mask & 2048) == 2048) {
            if (((this.mask | 4096) & mBeanPermission.mask) != mBeanPermission.mask) {
                return false;
            }
        } else if ((this.mask & mBeanPermission.mask) != mBeanPermission.mask) {
            return false;
        }
        if (mBeanPermission.classNamePrefix != null) {
            if (this.classNamePrefix == null) {
                return false;
            }
            if (this.classNameExactMatch) {
                if (!mBeanPermission.classNameExactMatch || !mBeanPermission.classNamePrefix.equals(this.classNamePrefix)) {
                    return false;
                }
            } else if (!mBeanPermission.classNamePrefix.startsWith(this.classNamePrefix)) {
                return false;
            }
        }
        if (mBeanPermission.member != null) {
            if (this.member == null) {
                return false;
            }
            if (!this.member.equals("*") && !this.member.equals(mBeanPermission.member)) {
                return false;
            }
        }
        if (mBeanPermission.objectName != null) {
            if (this.objectName == null) {
                return false;
            }
            if (!this.objectName.apply(mBeanPermission.objectName) && !this.objectName.equals(mBeanPermission.objectName)) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanPermission)) {
            return false;
        }
        MBeanPermission mBeanPermission = (MBeanPermission) obj;
        return this.mask == mBeanPermission.mask && getName().equals(mBeanPermission.getName());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        parseName();
        parseActions();
    }
}
