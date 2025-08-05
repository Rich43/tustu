package com.sun.jmx.mbeanserver;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.jmx.mbeanserver.MBeanAnalyzer;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/PerInterface.class */
final class PerInterface<M> {
    private final Class<?> mbeanInterface;
    private final MBeanIntrospector<M> introspector;
    private final MBeanInfo mbeanInfo;
    private final Map<String, M> getters = Util.newMap();
    private final Map<String, M> setters = Util.newMap();
    private final Map<String, List<PerInterface<M>.MethodAndSig>> ops = Util.newMap();

    PerInterface(Class<?> cls, MBeanIntrospector<M> mBeanIntrospector, MBeanAnalyzer<M> mBeanAnalyzer, MBeanInfo mBeanInfo) {
        this.mbeanInterface = cls;
        this.introspector = mBeanIntrospector;
        this.mbeanInfo = mBeanInfo;
        mBeanAnalyzer.visit(new InitMaps());
    }

    Class<?> getMBeanInterface() {
        return this.mbeanInterface;
    }

    MBeanInfo getMBeanInfo() {
        return this.mbeanInfo;
    }

    boolean isMXBean() {
        return this.introspector.isMXBean();
    }

    Object getAttribute(Object obj, String str, Object obj2) throws MBeanException, AttributeNotFoundException, ReflectionException {
        String str2;
        M m2 = this.getters.get(str);
        if (m2 == null) {
            if (this.setters.containsKey(str)) {
                str2 = "Write-only attribute: " + str;
            } else {
                str2 = "No such attribute: " + str;
            }
            throw new AttributeNotFoundException(str2);
        }
        return this.introspector.invokeM(m2, obj, (Object[]) null, obj2);
    }

    void setAttribute(Object obj, String str, Object obj2, Object obj3) throws MBeanException, InvalidAttributeValueException, AttributeNotFoundException, ReflectionException {
        String str2;
        M m2 = this.setters.get(str);
        if (m2 == null) {
            if (this.getters.containsKey(str)) {
                str2 = "Read-only attribute: " + str;
            } else {
                str2 = "No such attribute: " + str;
            }
            throw new AttributeNotFoundException(str2);
        }
        this.introspector.invokeSetter(str, m2, obj, obj2, obj3);
    }

    Object invoke(Object obj, String str, Object[] objArr, String[] strArr, Object obj2) throws MBeanException, ReflectionException {
        String str2;
        List<PerInterface<M>.MethodAndSig> list = this.ops.get(str);
        if (list == null) {
            return noSuchMethod("No such operation: " + str, obj, str, objArr, strArr, obj2);
        }
        if (strArr == null) {
            strArr = new String[0];
        }
        PerInterface<M>.MethodAndSig methodAndSig = null;
        Iterator<PerInterface<M>.MethodAndSig> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            PerInterface<M>.MethodAndSig next = it.next();
            if (Arrays.equals(next.signature, strArr)) {
                methodAndSig = next;
                break;
            }
        }
        if (methodAndSig == null) {
            String strSigString = sigString(strArr);
            if (list.size() == 1) {
                str2 = "Signature mismatch for operation " + str + ": " + strSigString + " should be " + sigString(list.get(0).signature);
            } else {
                str2 = "Operation " + str + " exists but not with this signature: " + strSigString;
            }
            return noSuchMethod(str2, obj, str, objArr, strArr, obj2);
        }
        return this.introspector.invokeM(methodAndSig.method, obj, objArr, obj2);
    }

    private Object noSuchMethod(String str, Object obj, String str2, Object[] objArr, String[] strArr, Object obj2) throws MBeanException, ReflectionException {
        String str3;
        ReflectionException reflectionException = new ReflectionException(new NoSuchMethodException(str2 + sigString(strArr)), str);
        if (this.introspector.isMXBean()) {
            throw reflectionException;
        }
        try {
            str3 = (String) AccessController.doPrivileged(new GetPropertyAction("jmx.invoke.getters"));
        } catch (Exception e2) {
            str3 = null;
        }
        if (str3 == null) {
            throw reflectionException;
        }
        int i2 = 0;
        Map<String, M> map = null;
        if (strArr == null || strArr.length == 0) {
            if (str2.startsWith("get")) {
                i2 = 3;
            } else if (str2.startsWith(BeanAdapter.IS_PREFIX)) {
                i2 = 2;
            }
            if (i2 != 0) {
                map = this.getters;
            }
        } else if (strArr.length == 1 && str2.startsWith("set")) {
            i2 = 3;
            map = this.setters;
        }
        if (i2 != 0) {
            M m2 = map.get(str2.substring(i2));
            if (m2 != null && this.introspector.getName(m2).equals(str2)) {
                String[] signature = this.introspector.getSignature(m2);
                if ((strArr == null && signature.length == 0) || Arrays.equals(strArr, signature)) {
                    return this.introspector.invokeM(m2, obj, objArr, obj2);
                }
            }
        }
        throw reflectionException;
    }

    private String sigString(String[] strArr) {
        StringBuilder sb = new StringBuilder("(");
        if (strArr != null) {
            for (String str : strArr) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append(str);
            }
        }
        return sb.append(")").toString();
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/PerInterface$InitMaps.class */
    private class InitMaps implements MBeanAnalyzer.MBeanVisitor<M> {
        static final /* synthetic */ boolean $assertionsDisabled;

        private InitMaps() {
        }

        static {
            $assertionsDisabled = !PerInterface.class.desiredAssertionStatus();
        }

        @Override // com.sun.jmx.mbeanserver.MBeanAnalyzer.MBeanVisitor
        public void visitAttribute(String str, M m2, M m3) {
            if (m2 != null) {
                PerInterface.this.introspector.checkMethod(m2);
                Object objPut = PerInterface.this.getters.put(str, m2);
                if (!$assertionsDisabled && objPut != null) {
                    throw new AssertionError();
                }
            }
            if (m3 != null) {
                PerInterface.this.introspector.checkMethod(m3);
                Object objPut2 = PerInterface.this.setters.put(str, m3);
                if (!$assertionsDisabled && objPut2 != null) {
                    throw new AssertionError();
                }
            }
        }

        @Override // com.sun.jmx.mbeanserver.MBeanAnalyzer.MBeanVisitor
        public void visitOperation(String str, M m2) {
            PerInterface.this.introspector.checkMethod(m2);
            String[] signature = PerInterface.this.introspector.getSignature(m2);
            MethodAndSig methodAndSig = new MethodAndSig();
            methodAndSig.method = m2;
            methodAndSig.signature = signature;
            List listNewList = (List) PerInterface.this.ops.get(str);
            if (listNewList == null) {
                listNewList = Collections.singletonList(methodAndSig);
            } else {
                if (listNewList.size() == 1) {
                    listNewList = Util.newList(listNewList);
                }
                listNewList.add(methodAndSig);
            }
            PerInterface.this.ops.put(str, listNewList);
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/PerInterface$MethodAndSig.class */
    private class MethodAndSig {
        M method;
        String[] signature;

        private MethodAndSig() {
        }
    }
}
