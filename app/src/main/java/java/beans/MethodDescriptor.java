package java.beans;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:java/beans/MethodDescriptor.class */
public class MethodDescriptor extends FeatureDescriptor {
    private final MethodRef methodRef;
    private String[] paramNames;
    private List<WeakReference<Class<?>>> params;
    private ParameterDescriptor[] parameterDescriptors;

    public MethodDescriptor(Method method) {
        this(method, (ParameterDescriptor[]) null);
    }

    public MethodDescriptor(Method method, ParameterDescriptor[] parameterDescriptorArr) {
        this.methodRef = new MethodRef();
        setName(method.getName());
        setMethod(method);
        this.parameterDescriptors = parameterDescriptorArr != null ? (ParameterDescriptor[]) parameterDescriptorArr.clone() : null;
    }

    public synchronized Method getMethod() {
        Method methodFindMethod = this.methodRef.get();
        if (methodFindMethod == null) {
            Class<?> class0 = getClass0();
            String name = getName();
            if (class0 != null && name != null) {
                Class<?>[] params = getParams();
                if (params == null) {
                    for (int i2 = 0; i2 < 3; i2++) {
                        methodFindMethod = Introspector.findMethod(class0, name, i2, null);
                        if (methodFindMethod != null) {
                            break;
                        }
                    }
                } else {
                    methodFindMethod = Introspector.findMethod(class0, name, params.length, params);
                }
                setMethod(methodFindMethod);
            }
        }
        return methodFindMethod;
    }

    private synchronized void setMethod(Method method) {
        if (method == null) {
            return;
        }
        if (getClass0() == null) {
            setClass0(method.getDeclaringClass());
        }
        setParams(getParameterTypes(getClass0(), method));
        this.methodRef.set(method);
    }

    private synchronized void setParams(Class<?>[] clsArr) {
        if (clsArr == null) {
            return;
        }
        this.paramNames = new String[clsArr.length];
        this.params = new ArrayList(clsArr.length);
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            this.paramNames[i2] = clsArr[i2].getName();
            this.params.add(new WeakReference<>(clsArr[i2]));
        }
    }

    String[] getParamNames() {
        return this.paramNames;
    }

    private synchronized Class<?>[] getParams() {
        Class<?>[] clsArr = new Class[this.params.size()];
        for (int i2 = 0; i2 < this.params.size(); i2++) {
            Class<?> cls = this.params.get(i2).get();
            if (cls == null) {
                return null;
            }
            clsArr[i2] = cls;
        }
        return clsArr;
    }

    public ParameterDescriptor[] getParameterDescriptors() {
        if (this.parameterDescriptors != null) {
            return (ParameterDescriptor[]) this.parameterDescriptors.clone();
        }
        return null;
    }

    private static Method resolve(Method method, Method method2) {
        if (method == null) {
            return method2;
        }
        if (method2 == null) {
            return method;
        }
        return (method.isSynthetic() || !method2.isSynthetic()) ? method2 : method;
    }

    MethodDescriptor(MethodDescriptor methodDescriptor, MethodDescriptor methodDescriptor2) {
        super(methodDescriptor, methodDescriptor2);
        this.methodRef = new MethodRef();
        this.methodRef.set(resolve(methodDescriptor.methodRef.get(), methodDescriptor2.methodRef.get()));
        this.params = methodDescriptor.params;
        if (methodDescriptor2.params != null) {
            this.params = methodDescriptor2.params;
        }
        this.paramNames = methodDescriptor.paramNames;
        if (methodDescriptor2.paramNames != null) {
            this.paramNames = methodDescriptor2.paramNames;
        }
        this.parameterDescriptors = methodDescriptor.parameterDescriptors;
        if (methodDescriptor2.parameterDescriptors != null) {
            this.parameterDescriptors = methodDescriptor2.parameterDescriptors;
        }
    }

    MethodDescriptor(MethodDescriptor methodDescriptor) {
        super(methodDescriptor);
        this.methodRef = new MethodRef();
        this.methodRef.set(methodDescriptor.getMethod());
        this.params = methodDescriptor.params;
        this.paramNames = methodDescriptor.paramNames;
        if (methodDescriptor.parameterDescriptors != null) {
            int length = methodDescriptor.parameterDescriptors.length;
            this.parameterDescriptors = new ParameterDescriptor[length];
            for (int i2 = 0; i2 < length; i2++) {
                this.parameterDescriptors[i2] = new ParameterDescriptor(methodDescriptor.parameterDescriptors[i2]);
            }
        }
    }

    @Override // java.beans.FeatureDescriptor
    void appendTo(StringBuilder sb) {
        appendTo(sb, "method", this.methodRef.get());
        if (this.parameterDescriptors != null) {
            sb.append("; parameterDescriptors={");
            for (ParameterDescriptor parameterDescriptor : this.parameterDescriptors) {
                sb.append((Object) parameterDescriptor).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append("}");
        }
    }
}
