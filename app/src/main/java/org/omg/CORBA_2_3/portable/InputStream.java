package org.omg.CORBA_2_3.portable;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.portable.BoxedValueHelper;

/* loaded from: rt.jar:org/omg/CORBA_2_3/portable/InputStream.class */
public abstract class InputStream extends org.omg.CORBA.portable.InputStream {
    private static final String ALLOW_SUBCLASS_PROP = "jdk.corba.allowInputStreamSubclass";
    private static final boolean allowSubclass = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: org.omg.CORBA_2_3.portable.InputStream.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Boolean run() {
            String property = System.getProperty(InputStream.ALLOW_SUBCLASS_PROP);
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

    private InputStream(Void r3) {
    }

    public InputStream() {
        this(checkPermission());
    }

    public Serializable read_value() {
        throw new NO_IMPLEMENT();
    }

    public Serializable read_value(Class cls) {
        throw new NO_IMPLEMENT();
    }

    public Serializable read_value(BoxedValueHelper boxedValueHelper) {
        throw new NO_IMPLEMENT();
    }

    public Serializable read_value(String str) {
        throw new NO_IMPLEMENT();
    }

    public Serializable read_value(Serializable serializable) {
        throw new NO_IMPLEMENT();
    }

    public Object read_abstract_interface() {
        throw new NO_IMPLEMENT();
    }

    public Object read_abstract_interface(Class cls) {
        throw new NO_IMPLEMENT();
    }
}
