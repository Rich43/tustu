package java.beans;

import java.lang.ref.Reference;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:java/beans/EventSetDescriptor.class */
public class EventSetDescriptor extends FeatureDescriptor {
    private MethodDescriptor[] listenerMethodDescriptors;
    private MethodDescriptor addMethodDescriptor;
    private MethodDescriptor removeMethodDescriptor;
    private MethodDescriptor getMethodDescriptor;
    private Reference<Method[]> listenerMethodsRef;
    private Reference<? extends Class<?>> listenerTypeRef;
    private boolean unicast;
    private boolean inDefaultEventSet;

    public EventSetDescriptor(Class<?> cls, String str, Class<?> cls2, String str2) throws IntrospectionException {
        this(cls, str, cls2, new String[]{str2}, "add" + getListenerClassName(cls2), "remove" + getListenerClassName(cls2), "get" + getListenerClassName(cls2) + PdfOps.s_TOKEN);
        String str3 = NameGenerator.capitalize(str) + "Event";
        Method[] listenerMethods = getListenerMethods();
        if (listenerMethods.length > 0) {
            Class<?>[] parameterTypes = getParameterTypes(getClass0(), listenerMethods[0]);
            if (!"vetoableChange".equals(str) && !parameterTypes[0].getName().endsWith(str3)) {
                throw new IntrospectionException("Method \"" + str2 + "\" should have argument \"" + str3 + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        }
    }

    private static String getListenerClassName(Class<?> cls) {
        String name = cls.getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }

    public EventSetDescriptor(Class<?> cls, String str, Class<?> cls2, String[] strArr, String str2, String str3) throws IntrospectionException {
        this(cls, str, cls2, strArr, str2, str3, null);
    }

    public EventSetDescriptor(Class<?> cls, String str, Class<?> cls2, String[] strArr, String str2, String str3, String str4) throws IntrospectionException {
        this.inDefaultEventSet = true;
        if (cls == null || str == null || cls2 == null) {
            throw new NullPointerException();
        }
        setName(str);
        setClass0(cls);
        setListenerType(cls2);
        Method[] methodArr = new Method[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2] == null) {
                throw new NullPointerException();
            }
            methodArr[i2] = getMethod(cls2, strArr[i2], 1);
        }
        setListenerMethods(methodArr);
        setAddListenerMethod(getMethod(cls, str2, 1));
        setRemoveListenerMethod(getMethod(cls, str3, 1));
        Method methodFindMethod = Introspector.findMethod(cls, str4, 0);
        if (methodFindMethod != null) {
            setGetListenerMethod(methodFindMethod);
        }
    }

    private static Method getMethod(Class<?> cls, String str, int i2) throws IntrospectionException {
        if (str == null) {
            return null;
        }
        Method methodFindMethod = Introspector.findMethod(cls, str, i2);
        if (methodFindMethod == null || Modifier.isStatic(methodFindMethod.getModifiers())) {
            throw new IntrospectionException("Method not found: " + str + " on class " + cls.getName());
        }
        return methodFindMethod;
    }

    public EventSetDescriptor(String str, Class<?> cls, Method[] methodArr, Method method, Method method2) throws IntrospectionException {
        this(str, cls, methodArr, method, method2, (Method) null);
    }

    public EventSetDescriptor(String str, Class<?> cls, Method[] methodArr, Method method, Method method2, Method method3) throws IntrospectionException {
        this.inDefaultEventSet = true;
        setName(str);
        setListenerMethods(methodArr);
        setAddListenerMethod(method);
        setRemoveListenerMethod(method2);
        setGetListenerMethod(method3);
        setListenerType(cls);
    }

    public EventSetDescriptor(String str, Class<?> cls, MethodDescriptor[] methodDescriptorArr, Method method, Method method2) throws IntrospectionException {
        this.inDefaultEventSet = true;
        setName(str);
        this.listenerMethodDescriptors = methodDescriptorArr != null ? (MethodDescriptor[]) methodDescriptorArr.clone() : null;
        setAddListenerMethod(method);
        setRemoveListenerMethod(method2);
        setListenerType(cls);
    }

    public Class<?> getListenerType() {
        if (this.listenerTypeRef != null) {
            return this.listenerTypeRef.get();
        }
        return null;
    }

    private void setListenerType(Class<?> cls) {
        this.listenerTypeRef = getWeakReference(cls);
    }

    public synchronized Method[] getListenerMethods() {
        Method[] listenerMethods0 = getListenerMethods0();
        if (listenerMethods0 == null) {
            if (this.listenerMethodDescriptors != null) {
                listenerMethods0 = new Method[this.listenerMethodDescriptors.length];
                for (int i2 = 0; i2 < listenerMethods0.length; i2++) {
                    listenerMethods0[i2] = this.listenerMethodDescriptors[i2].getMethod();
                }
            }
            setListenerMethods(listenerMethods0);
        }
        return listenerMethods0;
    }

    private void setListenerMethods(Method[] methodArr) {
        if (methodArr == null) {
            return;
        }
        if (this.listenerMethodDescriptors == null) {
            this.listenerMethodDescriptors = new MethodDescriptor[methodArr.length];
            for (int i2 = 0; i2 < methodArr.length; i2++) {
                this.listenerMethodDescriptors[i2] = new MethodDescriptor(methodArr[i2]);
            }
        }
        this.listenerMethodsRef = getSoftReference(methodArr);
    }

    private Method[] getListenerMethods0() {
        if (this.listenerMethodsRef != null) {
            return this.listenerMethodsRef.get();
        }
        return null;
    }

    public synchronized MethodDescriptor[] getListenerMethodDescriptors() {
        if (this.listenerMethodDescriptors != null) {
            return (MethodDescriptor[]) this.listenerMethodDescriptors.clone();
        }
        return null;
    }

    public synchronized Method getAddListenerMethod() {
        return getMethod(this.addMethodDescriptor);
    }

    private synchronized void setAddListenerMethod(Method method) {
        if (method == null) {
            return;
        }
        if (getClass0() == null) {
            setClass0(method.getDeclaringClass());
        }
        this.addMethodDescriptor = new MethodDescriptor(method);
        setTransient((Transient) method.getAnnotation(Transient.class));
    }

    public synchronized Method getRemoveListenerMethod() {
        return getMethod(this.removeMethodDescriptor);
    }

    private synchronized void setRemoveListenerMethod(Method method) {
        if (method == null) {
            return;
        }
        if (getClass0() == null) {
            setClass0(method.getDeclaringClass());
        }
        this.removeMethodDescriptor = new MethodDescriptor(method);
        setTransient((Transient) method.getAnnotation(Transient.class));
    }

    public synchronized Method getGetListenerMethod() {
        return getMethod(this.getMethodDescriptor);
    }

    private synchronized void setGetListenerMethod(Method method) {
        if (method == null) {
            return;
        }
        if (getClass0() == null) {
            setClass0(method.getDeclaringClass());
        }
        this.getMethodDescriptor = new MethodDescriptor(method);
        setTransient((Transient) method.getAnnotation(Transient.class));
    }

    public void setUnicast(boolean z2) {
        this.unicast = z2;
    }

    public boolean isUnicast() {
        return this.unicast;
    }

    public void setInDefaultEventSet(boolean z2) {
        this.inDefaultEventSet = z2;
    }

    public boolean isInDefaultEventSet() {
        return this.inDefaultEventSet;
    }

    EventSetDescriptor(EventSetDescriptor eventSetDescriptor, EventSetDescriptor eventSetDescriptor2) {
        super(eventSetDescriptor, eventSetDescriptor2);
        this.inDefaultEventSet = true;
        this.listenerMethodDescriptors = eventSetDescriptor.listenerMethodDescriptors;
        if (eventSetDescriptor2.listenerMethodDescriptors != null) {
            this.listenerMethodDescriptors = eventSetDescriptor2.listenerMethodDescriptors;
        }
        this.listenerTypeRef = eventSetDescriptor.listenerTypeRef;
        if (eventSetDescriptor2.listenerTypeRef != null) {
            this.listenerTypeRef = eventSetDescriptor2.listenerTypeRef;
        }
        this.addMethodDescriptor = eventSetDescriptor.addMethodDescriptor;
        if (eventSetDescriptor2.addMethodDescriptor != null) {
            this.addMethodDescriptor = eventSetDescriptor2.addMethodDescriptor;
        }
        this.removeMethodDescriptor = eventSetDescriptor.removeMethodDescriptor;
        if (eventSetDescriptor2.removeMethodDescriptor != null) {
            this.removeMethodDescriptor = eventSetDescriptor2.removeMethodDescriptor;
        }
        this.getMethodDescriptor = eventSetDescriptor.getMethodDescriptor;
        if (eventSetDescriptor2.getMethodDescriptor != null) {
            this.getMethodDescriptor = eventSetDescriptor2.getMethodDescriptor;
        }
        this.unicast = eventSetDescriptor2.unicast;
        if (!eventSetDescriptor.inDefaultEventSet || !eventSetDescriptor2.inDefaultEventSet) {
            this.inDefaultEventSet = false;
        }
    }

    EventSetDescriptor(EventSetDescriptor eventSetDescriptor) {
        super(eventSetDescriptor);
        this.inDefaultEventSet = true;
        if (eventSetDescriptor.listenerMethodDescriptors != null) {
            int length = eventSetDescriptor.listenerMethodDescriptors.length;
            this.listenerMethodDescriptors = new MethodDescriptor[length];
            for (int i2 = 0; i2 < length; i2++) {
                this.listenerMethodDescriptors[i2] = new MethodDescriptor(eventSetDescriptor.listenerMethodDescriptors[i2]);
            }
        }
        this.listenerTypeRef = eventSetDescriptor.listenerTypeRef;
        this.addMethodDescriptor = eventSetDescriptor.addMethodDescriptor;
        this.removeMethodDescriptor = eventSetDescriptor.removeMethodDescriptor;
        this.getMethodDescriptor = eventSetDescriptor.getMethodDescriptor;
        this.unicast = eventSetDescriptor.unicast;
        this.inDefaultEventSet = eventSetDescriptor.inDefaultEventSet;
    }

    @Override // java.beans.FeatureDescriptor
    void appendTo(StringBuilder sb) {
        appendTo(sb, "unicast", this.unicast);
        appendTo(sb, "inDefaultEventSet", this.inDefaultEventSet);
        appendTo(sb, "listenerType", (Reference<?>) this.listenerTypeRef);
        appendTo(sb, "getListenerMethod", getMethod(this.getMethodDescriptor));
        appendTo(sb, "addListenerMethod", getMethod(this.addMethodDescriptor));
        appendTo(sb, "removeListenerMethod", getMethod(this.removeMethodDescriptor));
    }

    private static Method getMethod(MethodDescriptor methodDescriptor) {
        if (methodDescriptor != null) {
            return methodDescriptor.getMethod();
        }
        return null;
    }
}
