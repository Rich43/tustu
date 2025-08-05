package org.omg.CORBA_2_3.portable;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.portable.BoxedValueHelper;

/* loaded from: rt.jar:org/omg/CORBA_2_3/portable/OutputStream.class */
public abstract class OutputStream extends org.omg.CORBA.portable.OutputStream {
    private static final String ALLOW_SUBCLASS_PROP = "jdk.corba.allowOutputStreamSubclass";
    private static final boolean allowSubclass = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: org.omg.CORBA_2_3.portable.OutputStream.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Boolean run() {
            String property = System.getProperty(OutputStream.ALLOW_SUBCLASS_PROP);
            boolean z2 = (property == null || property.equalsIgnoreCase("false")) ? false : true;
            return Boolean.valueOf(z2);
        }
    })).booleanValue();

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && !allowSubclass) {
            securityManager.checkPermission(new SerializablePermission("enableSubclassImplementation"));
            return null;
        }
        return null;
    }

    private OutputStream(Void r3) {
    }

    public OutputStream() {
        this(checkPermission());
    }

    public void write_value(Serializable serializable) {
        throw new NO_IMPLEMENT();
    }

    public void write_value(Serializable serializable, Class cls) {
        throw new NO_IMPLEMENT();
    }

    public void write_value(Serializable serializable, String str) {
        throw new NO_IMPLEMENT();
    }

    public void write_value(Serializable serializable, BoxedValueHelper boxedValueHelper) {
        throw new NO_IMPLEMENT();
    }

    public void write_abstract_interface(Object obj) {
        throw new NO_IMPLEMENT();
    }
}
