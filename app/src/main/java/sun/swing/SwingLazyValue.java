package sun.swing;

import java.awt.Color;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:sun/swing/SwingLazyValue.class */
public class SwingLazyValue implements UIDefaults.LazyValue {
    private String className;
    private String methodName;
    private Object[] args;

    public SwingLazyValue(String str) {
        this(str, (String) null);
    }

    public SwingLazyValue(String str, String str2) {
        this(str, str2, null);
    }

    public SwingLazyValue(String str, Object[] objArr) {
        this(str, null, objArr);
    }

    public SwingLazyValue(String str, String str2, Object[] objArr) {
        this.className = str;
        this.methodName = str2;
        if (objArr != null) {
            this.args = (Object[]) objArr.clone();
        }
    }

    @Override // javax.swing.UIDefaults.LazyValue
    public Object createValue(UIDefaults uIDefaults) {
        try {
            ReflectUtil.checkPackageAccess(this.className);
            Class<?> cls = Class.forName(this.className, true, null);
            if (this.methodName != null) {
                Method method = cls.getMethod(this.methodName, getClassArray(this.args));
                makeAccessible(method);
                return method.invoke(cls, this.args);
            }
            Constructor<?> constructor = cls.getConstructor(getClassArray(this.args));
            makeAccessible(constructor);
            return constructor.newInstance(this.args);
        } catch (Exception e2) {
            return null;
        }
    }

    private void makeAccessible(final AccessibleObject accessibleObject) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.swing.SwingLazyValue.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws SecurityException {
                accessibleObject.setAccessible(true);
                return null;
            }
        });
    }

    private Class[] getClassArray(Object[] objArr) {
        Class[] clsArr = null;
        if (objArr != null) {
            clsArr = new Class[objArr.length];
            for (int i2 = 0; i2 < objArr.length; i2++) {
                if (objArr[i2] instanceof Integer) {
                    clsArr[i2] = Integer.TYPE;
                } else if (objArr[i2] instanceof Boolean) {
                    clsArr[i2] = Boolean.TYPE;
                } else if (objArr[i2] instanceof ColorUIResource) {
                    clsArr[i2] = Color.class;
                } else {
                    clsArr[i2] = objArr[i2].getClass();
                }
            }
        }
        return clsArr;
    }
}
