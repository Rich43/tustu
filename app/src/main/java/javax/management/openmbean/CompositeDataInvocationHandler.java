package javax.management.openmbean;

import com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory;
import com.sun.jmx.mbeanserver.MXBeanLookup;
import com.sun.jmx.mbeanserver.MXBeanMappingFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/* loaded from: rt.jar:javax/management/openmbean/CompositeDataInvocationHandler.class */
public class CompositeDataInvocationHandler implements InvocationHandler {
    private final CompositeData compositeData;
    private final MXBeanLookup lookup;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CompositeDataInvocationHandler.class.desiredAssertionStatus();
    }

    public CompositeDataInvocationHandler(CompositeData compositeData) {
        this(compositeData, null);
    }

    CompositeDataInvocationHandler(CompositeData compositeData, MXBeanLookup mXBeanLookup) {
        if (compositeData == null) {
            throw new IllegalArgumentException("compositeData");
        }
        this.compositeData = compositeData;
        this.lookup = mXBeanLookup;
    }

    public CompositeData getCompositeData() {
        if ($assertionsDisabled || this.compositeData != null) {
            return this.compositeData;
        }
        throw new AssertionError();
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        Object obj2;
        String name = method.getName();
        if (method.getDeclaringClass() == Object.class) {
            if (name.equals("toString") && objArr == null) {
                return "Proxy[" + ((Object) this.compositeData) + "]";
            }
            if (name.equals("hashCode") && objArr == null) {
                return Integer.valueOf(this.compositeData.hashCode() + 1128548680);
            }
            if (name.equals("equals") && objArr.length == 1 && method.getParameterTypes()[0] == Object.class) {
                return Boolean.valueOf(equals(obj, objArr[0]));
            }
            return method.invoke(this, objArr);
        }
        String strPropertyName = DefaultMXBeanMappingFactory.propertyName(method);
        if (strPropertyName == null) {
            throw new IllegalArgumentException("Method is not getter: " + method.getName());
        }
        if (this.compositeData.containsKey(strPropertyName)) {
            obj2 = this.compositeData.get(strPropertyName);
        } else {
            String strDecapitalize = DefaultMXBeanMappingFactory.decapitalize(strPropertyName);
            if (this.compositeData.containsKey(strDecapitalize)) {
                obj2 = this.compositeData.get(strDecapitalize);
            } else {
                throw new IllegalArgumentException("No CompositeData item " + strPropertyName + (strDecapitalize.equals(strPropertyName) ? "" : " or " + strDecapitalize) + " to match " + name);
            }
        }
        return MXBeanMappingFactory.DEFAULT.mappingForType(method.getGenericReturnType(), MXBeanMappingFactory.DEFAULT).fromOpenValue(obj2);
    }

    private boolean equals(Object obj, Object obj2) throws IllegalArgumentException {
        if (obj2 == null || obj.getClass() != obj2.getClass()) {
            return false;
        }
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(obj2);
        if (!(invocationHandler instanceof CompositeDataInvocationHandler)) {
            return false;
        }
        return this.compositeData.equals(((CompositeDataInvocationHandler) invocationHandler).compositeData);
    }
}
