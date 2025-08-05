package javax.smartcardio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Permission;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/smartcardio/CardPermission.class */
public class CardPermission extends Permission {
    private static final long serialVersionUID = 7146787880530705613L;
    private static final int A_CONNECT = 1;
    private static final int A_EXCLUSIVE = 2;
    private static final int A_GET_BASIC_CHANNEL = 4;
    private static final int A_OPEN_LOGICAL_CHANNEL = 8;
    private static final int A_RESET = 16;
    private static final int A_TRANSMIT_CONTROL = 32;
    private static final int A_ALL = 63;
    private static final String S_CONNECT = "connect";
    private static final String S_RESET = "reset";
    private static final String S_ALL = "*";
    private transient int mask;
    private volatile String actions;
    private static final int[] ARRAY_MASKS = {63, 1, 2, 4, 8, 16, 32};
    private static final String S_EXCLUSIVE = "exclusive";
    private static final String S_GET_BASIC_CHANNEL = "getBasicChannel";
    private static final String S_OPEN_LOGICAL_CHANNEL = "openLogicalChannel";
    private static final String S_TRANSMIT_CONTROL = "transmitControl";
    private static final String[] ARRAY_STRINGS = {"*", "connect", S_EXCLUSIVE, S_GET_BASIC_CHANNEL, S_OPEN_LOGICAL_CHANNEL, "reset", S_TRANSMIT_CONTROL};

    public CardPermission(String str, String str2) {
        super(str);
        if (str == null) {
            throw new NullPointerException();
        }
        this.mask = getMask(str2);
    }

    private static int getMask(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("actions must not be empty");
        }
        for (int i2 = 0; i2 < ARRAY_STRINGS.length; i2++) {
            if (str == ARRAY_STRINGS[i2]) {
                return ARRAY_MASKS[i2];
            }
        }
        if (str.endsWith(",")) {
            throw new IllegalArgumentException("Invalid actions: '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        int i3 = 0;
        for (String str2 : str.split(",")) {
            for (int i4 = 0; i4 < ARRAY_STRINGS.length; i4++) {
                if (ARRAY_STRINGS[i4].equalsIgnoreCase(str2)) {
                    i3 |= ARRAY_MASKS[i4];
                }
            }
            throw new IllegalArgumentException("Invalid action: '" + str2 + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        return i3;
    }

    private static String getActions(int i2) {
        if (i2 == 63) {
            return "*";
        }
        boolean z2 = true;
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < ARRAY_MASKS.length; i3++) {
            int i4 = ARRAY_MASKS[i3];
            if ((i2 & i4) == i4) {
                if (!z2) {
                    sb.append(",");
                } else {
                    z2 = false;
                }
                sb.append(ARRAY_STRINGS[i3]);
            }
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

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof CardPermission)) {
            return false;
        }
        CardPermission cardPermission = (CardPermission) permission;
        if ((this.mask & cardPermission.mask) != cardPermission.mask) {
            return false;
        }
        String name = getName();
        if (name.equals("*") || name.equals(cardPermission.getName())) {
            return true;
        }
        return false;
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CardPermission)) {
            return false;
        }
        CardPermission cardPermission = (CardPermission) obj;
        return getName().equals(cardPermission.getName()) && this.mask == cardPermission.mask;
    }

    @Override // java.security.Permission
    public int hashCode() {
        return getName().hashCode() + (31 * this.mask);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.actions == null) {
            getActions();
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.mask = getMask(this.actions);
    }
}
