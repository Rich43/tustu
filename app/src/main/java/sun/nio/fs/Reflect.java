package sun.nio.fs;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/nio/fs/Reflect.class */
class Reflect {
    private Reflect() {
    }

    private static void setAccessible(final AccessibleObject accessibleObject) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.nio.fs.Reflect.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() throws SecurityException {
                accessibleObject.setAccessible(true);
                return null;
            }
        });
    }

    static Field lookupField(String str, String str2) throws SecurityException {
        try {
            Field declaredField = Class.forName(str).getDeclaredField(str2);
            setAccessible(declaredField);
            return declaredField;
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        } catch (NoSuchFieldException e3) {
            throw new AssertionError(e3);
        }
    }
}
