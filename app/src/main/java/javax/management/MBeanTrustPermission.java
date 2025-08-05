package javax.management;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.security.BasicPermission;

/* loaded from: rt.jar:javax/management/MBeanTrustPermission.class */
public class MBeanTrustPermission extends BasicPermission {
    private static final long serialVersionUID = -2952178077029018140L;

    public MBeanTrustPermission(String str) {
        this(str, null);
    }

    public MBeanTrustPermission(String str, String str2) {
        super(str, str2);
        validate(str, str2);
    }

    private static void validate(String str, String str2) {
        if (str2 != null && str2.length() > 0) {
            throw new IllegalArgumentException("MBeanTrustPermission actions must be null: " + str2);
        }
        if (!str.equals("register") && !str.equals("*")) {
            throw new IllegalArgumentException("MBeanTrustPermission: Unknown target name [" + str + "]");
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            validate(super.getName(), super.getActions());
        } catch (IllegalArgumentException e2) {
            throw new InvalidObjectException(e2.getMessage());
        }
    }
}
