package com.sun.jmx.mbeanserver;

import com.sun.jmx.mbeanserver.MBeanAnalyzer;
import java.lang.reflect.Method;
import java.util.Map;
import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanProxy.class */
public class MXBeanProxy {
    private final Map<Method, Handler> handlerMap = Util.newMap();

    public MXBeanProxy(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Null parameter");
        }
        try {
            MXBeanIntrospector.getInstance().getAnalyzer(cls).visit(new Visitor());
        } catch (NotCompliantMBeanException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanProxy$Visitor.class */
    private class Visitor implements MBeanAnalyzer.MBeanVisitor<ConvertingMethod> {
        private Visitor() {
        }

        @Override // com.sun.jmx.mbeanserver.MBeanAnalyzer.MBeanVisitor
        public void visitAttribute(String str, ConvertingMethod convertingMethod, ConvertingMethod convertingMethod2) {
            if (convertingMethod != null) {
                convertingMethod.checkCallToOpen();
                MXBeanProxy.this.handlerMap.put(convertingMethod.getMethod(), new GetHandler(str, convertingMethod));
            }
            if (convertingMethod2 != null) {
                MXBeanProxy.this.handlerMap.put(convertingMethod2.getMethod(), new SetHandler(str, convertingMethod2));
            }
        }

        @Override // com.sun.jmx.mbeanserver.MBeanAnalyzer.MBeanVisitor
        public void visitOperation(String str, ConvertingMethod convertingMethod) {
            convertingMethod.checkCallToOpen();
            MXBeanProxy.this.handlerMap.put(convertingMethod.getMethod(), new InvokeHandler(str, convertingMethod.getOpenSignature(), convertingMethod));
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanProxy$Handler.class */
    private static abstract class Handler {
        private final String name;
        private final ConvertingMethod convertingMethod;

        abstract Object invoke(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Object[] objArr) throws Exception;

        Handler(String str, ConvertingMethod convertingMethod) {
            this.name = str;
            this.convertingMethod = convertingMethod;
        }

        String getName() {
            return this.name;
        }

        ConvertingMethod getConvertingMethod() {
            return this.convertingMethod;
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanProxy$GetHandler.class */
    private static class GetHandler extends Handler {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !MXBeanProxy.class.desiredAssertionStatus();
        }

        GetHandler(String str, ConvertingMethod convertingMethod) {
            super(str, convertingMethod);
        }

        @Override // com.sun.jmx.mbeanserver.MXBeanProxy.Handler
        Object invoke(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Object[] objArr) throws Exception {
            if ($assertionsDisabled || objArr == null || objArr.length == 0) {
                return mBeanServerConnection.getAttribute(objectName, getName());
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanProxy$SetHandler.class */
    private static class SetHandler extends Handler {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !MXBeanProxy.class.desiredAssertionStatus();
        }

        SetHandler(String str, ConvertingMethod convertingMethod) {
            super(str, convertingMethod);
        }

        @Override // com.sun.jmx.mbeanserver.MXBeanProxy.Handler
        Object invoke(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Object[] objArr) throws Exception {
            if (!$assertionsDisabled && objArr.length != 1) {
                throw new AssertionError();
            }
            mBeanServerConnection.setAttribute(objectName, new Attribute(getName(), objArr[0]));
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanProxy$InvokeHandler.class */
    private static class InvokeHandler extends Handler {
        private final String[] signature;

        InvokeHandler(String str, String[] strArr, ConvertingMethod convertingMethod) {
            super(str, convertingMethod);
            this.signature = strArr;
        }

        @Override // com.sun.jmx.mbeanserver.MXBeanProxy.Handler
        Object invoke(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Object[] objArr) throws Exception {
            return mBeanServerConnection.invoke(objectName, getName(), objArr, this.signature);
        }
    }

    public Object invoke(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Method method, Object[] objArr) throws Throwable {
        Handler handler = this.handlerMap.get(method);
        ConvertingMethod convertingMethod = handler.getConvertingMethod();
        MXBeanLookup mXBeanLookupLookupFor = MXBeanLookup.lookupFor(mBeanServerConnection);
        MXBeanLookup lookup = MXBeanLookup.getLookup();
        try {
            MXBeanLookup.setLookup(mXBeanLookupLookupFor);
            Object objFromOpenReturnValue = convertingMethod.fromOpenReturnValue(mXBeanLookupLookupFor, handler.invoke(mBeanServerConnection, objectName, convertingMethod.toOpenParameters(mXBeanLookupLookupFor, objArr)));
            MXBeanLookup.setLookup(lookup);
            return objFromOpenReturnValue;
        } catch (Throwable th) {
            MXBeanLookup.setLookup(lookup);
            throw th;
        }
    }
}
