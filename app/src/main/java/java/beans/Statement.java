package java.beans;

import com.sun.beans.finder.ClassFinder;
import com.sun.beans.finder.ConstructorFinder;
import com.sun.beans.finder.MethodFinder;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.reflect.misc.MethodUtil;

/* loaded from: rt.jar:java/beans/Statement.class */
public class Statement {
    private static Object[] emptyArray = new Object[0];
    static ExceptionListener defaultExceptionListener = new ExceptionListener() { // from class: java.beans.Statement.1
        @Override // java.beans.ExceptionListener
        public void exceptionThrown(Exception exc) {
            System.err.println(exc);
            System.err.println("Continuing ...");
        }
    };
    private final AccessControlContext acc = AccessController.getContext();
    private final Object target;
    private final String methodName;
    private final Object[] arguments;
    ClassLoader loader;

    @ConstructorProperties({"target", "methodName", "arguments"})
    public Statement(Object obj, String str, Object[] objArr) {
        this.target = obj;
        this.methodName = str;
        this.arguments = objArr == null ? emptyArray : (Object[]) objArr.clone();
    }

    public Object getTarget() {
        return this.target;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public Object[] getArguments() {
        return (Object[]) this.arguments.clone();
    }

    public void execute() throws Exception {
        invoke();
    }

    Object invoke() throws Exception {
        AccessControlContext accessControlContext = this.acc;
        if (accessControlContext == null && System.getSecurityManager() != null) {
            throw new SecurityException("AccessControlContext is not set");
        }
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: java.beans.Statement.2
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    return Statement.this.invokeInternal();
                }
            }, accessControlContext);
        } catch (PrivilegedActionException e2) {
            throw e2.getException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object invokeInternal() throws Exception {
        Object target = getTarget();
        String methodName = getMethodName();
        if (target == null || methodName == null) {
            throw new NullPointerException((target == null ? "target" : "methodName") + " should not be null");
        }
        Object[] arguments = getArguments();
        if (arguments == null) {
            arguments = emptyArray;
        }
        if (target == Class.class && methodName.equals("forName")) {
            return ClassFinder.resolveClass((String) arguments[0], this.loader);
        }
        Class[] clsArr = new Class[arguments.length];
        for (int i2 = 0; i2 < arguments.length; i2++) {
            clsArr[i2] = arguments[i2] == null ? null : arguments[i2].getClass();
        }
        GenericDeclaration method = null;
        if (target instanceof Class) {
            if (methodName.equals("new")) {
                methodName = "newInstance";
            }
            if (methodName.equals("newInstance") && ((Class) target).isArray()) {
                Object objNewInstance = Array.newInstance(((Class) target).getComponentType(), arguments.length);
                for (int i3 = 0; i3 < arguments.length; i3++) {
                    Array.set(objNewInstance, i3, arguments[i3]);
                }
                return objNewInstance;
            }
            if (methodName.equals("newInstance") && arguments.length != 0) {
                if (target == Character.class && arguments.length == 1 && clsArr[0] == String.class) {
                    return new Character(((String) arguments[0]).charAt(0));
                }
                try {
                    method = ConstructorFinder.findConstructor((Class) target, clsArr);
                } catch (NoSuchMethodException e2) {
                    method = null;
                }
            }
            if (method == null && target != Class.class) {
                method = getMethod((Class) target, methodName, clsArr);
            }
            if (method == null) {
                method = getMethod(Class.class, methodName, clsArr);
            }
        } else {
            if (target.getClass().isArray() && (methodName.equals("set") || methodName.equals("get"))) {
                int iIntValue = ((Integer) arguments[0]).intValue();
                if (methodName.equals("get")) {
                    return Array.get(target, iIntValue);
                }
                Array.set(target, iIntValue, arguments[1]);
                return null;
            }
            method = getMethod(target.getClass(), methodName, clsArr);
        }
        if (method != null) {
            try {
                if (method instanceof Method) {
                    return MethodUtil.invoke((Method) method, target, arguments);
                }
                return ((Constructor) method).newInstance(arguments);
            } catch (IllegalAccessException e3) {
                throw new Exception("Statement cannot invoke: " + methodName + " on " + ((Object) target.getClass()), e3);
            } catch (InvocationTargetException e4) {
                Throwable targetException = e4.getTargetException();
                if (targetException instanceof Exception) {
                    throw ((Exception) targetException);
                }
                throw e4;
            }
        }
        throw new NoSuchMethodException(toString());
    }

    String instanceName(Object obj) {
        if (obj == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        if (obj.getClass() == String.class) {
            return PdfOps.DOUBLE_QUOTE__TOKEN + ((String) obj) + PdfOps.DOUBLE_QUOTE__TOKEN;
        }
        return NameGenerator.unqualifiedClassName(obj.getClass());
    }

    public String toString() {
        Object target = getTarget();
        String methodName = getMethodName();
        Object[] arguments = getArguments();
        if (arguments == null) {
            arguments = emptyArray;
        }
        StringBuffer stringBuffer = new StringBuffer(instanceName(target) + "." + methodName + "(");
        int length = arguments.length;
        for (int i2 = 0; i2 < length; i2++) {
            stringBuffer.append(instanceName(arguments[i2]));
            if (i2 != length - 1) {
                stringBuffer.append(", ");
            }
        }
        stringBuffer.append(");");
        return stringBuffer.toString();
    }

    static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
        try {
            return MethodFinder.findMethod(cls, str, clsArr);
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }
}
