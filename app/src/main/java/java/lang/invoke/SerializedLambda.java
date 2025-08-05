package java.lang.invoke;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Objects;

/* loaded from: rt.jar:java/lang/invoke/SerializedLambda.class */
public final class SerializedLambda implements Serializable {
    private static final long serialVersionUID = 8025925345765570181L;
    private final Class<?> capturingClass;
    private final String functionalInterfaceClass;
    private final String functionalInterfaceMethodName;
    private final String functionalInterfaceMethodSignature;
    private final String implClass;
    private final String implMethodName;
    private final String implMethodSignature;
    private final int implMethodKind;
    private final String instantiatedMethodType;
    private final Object[] capturedArgs;

    public SerializedLambda(Class<?> cls, String str, String str2, String str3, int i2, String str4, String str5, String str6, String str7, Object[] objArr) {
        this.capturingClass = cls;
        this.functionalInterfaceClass = str;
        this.functionalInterfaceMethodName = str2;
        this.functionalInterfaceMethodSignature = str3;
        this.implMethodKind = i2;
        this.implClass = str4;
        this.implMethodName = str5;
        this.implMethodSignature = str6;
        this.instantiatedMethodType = str7;
        this.capturedArgs = (Object[]) ((Object[]) Objects.requireNonNull(objArr)).clone();
    }

    public String getCapturingClass() {
        return this.capturingClass.getName().replace('.', '/');
    }

    public String getFunctionalInterfaceClass() {
        return this.functionalInterfaceClass;
    }

    public String getFunctionalInterfaceMethodName() {
        return this.functionalInterfaceMethodName;
    }

    public String getFunctionalInterfaceMethodSignature() {
        return this.functionalInterfaceMethodSignature;
    }

    public String getImplClass() {
        return this.implClass;
    }

    public String getImplMethodName() {
        return this.implMethodName;
    }

    public String getImplMethodSignature() {
        return this.implMethodSignature;
    }

    public int getImplMethodKind() {
        return this.implMethodKind;
    }

    public final String getInstantiatedMethodType() {
        return this.instantiatedMethodType;
    }

    public int getCapturedArgCount() {
        return this.capturedArgs.length;
    }

    public Object getCapturedArg(int i2) {
        return this.capturedArgs[i2];
    }

    private Object readResolve() throws ReflectiveOperationException {
        try {
            return ((Method) AccessController.doPrivileged(new PrivilegedExceptionAction<Method>() { // from class: java.lang.invoke.SerializedLambda.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Method run() throws Exception {
                    Method declaredMethod = SerializedLambda.this.capturingClass.getDeclaredMethod("$deserializeLambda$", SerializedLambda.class);
                    declaredMethod.setAccessible(true);
                    return declaredMethod;
                }
            })).invoke(null, this);
        } catch (PrivilegedActionException e2) {
            Exception exception = e2.getException();
            if (exception instanceof ReflectiveOperationException) {
                throw ((ReflectiveOperationException) exception);
            }
            if (exception instanceof RuntimeException) {
                throw ((RuntimeException) exception);
            }
            throw new RuntimeException("Exception in SerializedLambda.readResolve", e2);
        }
    }

    public String toString() {
        return String.format("SerializedLambda[%s=%s, %s=%s.%s:%s, %s=%s %s.%s:%s, %s=%s, %s=%d]", "capturingClass", this.capturingClass, "functionalInterfaceMethod", this.functionalInterfaceClass, this.functionalInterfaceMethodName, this.functionalInterfaceMethodSignature, DeploymentDescriptorParser.ATTR_IMPLEMENTATION, MethodHandleInfo.referenceKindToString(this.implMethodKind), this.implClass, this.implMethodName, this.implMethodSignature, "instantiatedMethodType", this.instantiatedMethodType, "numCaptured", Integer.valueOf(this.capturedArgs.length));
    }
}
