package com.sun.jmx.mbeanserver;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.NotCompliantMBeanException;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanAnalyzer.class */
class MBeanAnalyzer<M> {
    private Map<String, List<M>> opMap = Util.newInsertionOrderMap();
    private Map<String, AttrMethods<M>> attrMap = Util.newInsertionOrderMap();

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanAnalyzer$MBeanVisitor.class */
    interface MBeanVisitor<M> {
        void visitAttribute(String str, M m2, M m3);

        void visitOperation(String str, M m2);
    }

    void visit(MBeanVisitor<M> mBeanVisitor) {
        for (Map.Entry<String, AttrMethods<M>> entry : this.attrMap.entrySet()) {
            String key = entry.getKey();
            AttrMethods<M> value = entry.getValue();
            mBeanVisitor.visitAttribute(key, value.getter, value.setter);
        }
        for (Map.Entry<String, List<M>> entry2 : this.opMap.entrySet()) {
            Iterator<M> it = entry2.getValue().iterator();
            while (it.hasNext()) {
                mBeanVisitor.visitOperation(entry2.getKey(), it.next());
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanAnalyzer$AttrMethods.class */
    private static class AttrMethods<M> {
        M getter;
        M setter;

        private AttrMethods() {
        }
    }

    static <M> MBeanAnalyzer<M> analyzer(Class<?> cls, MBeanIntrospector<M> mBeanIntrospector) throws NotCompliantMBeanException {
        return new MBeanAnalyzer<>(cls, mBeanIntrospector);
    }

    private MBeanAnalyzer(Class<?> cls, MBeanIntrospector<M> mBeanIntrospector) throws NotCompliantMBeanException {
        if (!cls.isInterface()) {
            throw new NotCompliantMBeanException("Not an interface: " + cls.getName());
        }
        if (!Modifier.isPublic(cls.getModifiers()) && !Introspector.ALLOW_NONPUBLIC_MBEAN) {
            throw new NotCompliantMBeanException("Interface is not public: " + cls.getName());
        }
        try {
            initMaps(cls, mBeanIntrospector);
        } catch (Exception e2) {
            throw Introspector.throwException(cls, e2);
        }
    }

    private void initMaps(Class<?> cls, MBeanIntrospector<M> mBeanIntrospector) throws Exception {
        for (Method method : eliminateCovariantMethods(mBeanIntrospector.getMethods(cls))) {
            String name = method.getName();
            int length = method.getParameterTypes().length;
            M mMFrom = mBeanIntrospector.mFrom(method);
            String strSubstring = "";
            if (name.startsWith("get")) {
                strSubstring = name.substring(3);
            } else if (name.startsWith(BeanAdapter.IS_PREFIX) && method.getReturnType() == Boolean.TYPE) {
                strSubstring = name.substring(2);
            }
            if (strSubstring.length() != 0 && length == 0 && method.getReturnType() != Void.TYPE) {
                AttrMethods<M> attrMethods = this.attrMap.get(strSubstring);
                if (attrMethods == null) {
                    attrMethods = new AttrMethods<>();
                } else if (attrMethods.getter != null) {
                    throw new NotCompliantMBeanException("Attribute " + strSubstring + " has more than one getter");
                }
                attrMethods.getter = mMFrom;
                this.attrMap.put(strSubstring, attrMethods);
            } else if (name.startsWith("set") && name.length() > 3 && length == 1 && method.getReturnType() == Void.TYPE) {
                String strSubstring2 = name.substring(3);
                AttrMethods<M> attrMethods2 = this.attrMap.get(strSubstring2);
                if (attrMethods2 == null) {
                    attrMethods2 = new AttrMethods<>();
                } else if (attrMethods2.setter != null) {
                    throw new NotCompliantMBeanException("Attribute " + strSubstring2 + " has more than one setter");
                }
                attrMethods2.setter = mMFrom;
                this.attrMap.put(strSubstring2, attrMethods2);
            } else {
                List<M> listNewList = this.opMap.get(name);
                if (listNewList == null) {
                    listNewList = Util.newList();
                }
                listNewList.add(mMFrom);
                this.opMap.put(name, listNewList);
            }
        }
        for (Map.Entry<String, AttrMethods<M>> entry : this.attrMap.entrySet()) {
            AttrMethods<M> value = entry.getValue();
            if (!mBeanIntrospector.consistent(value.getter, value.setter)) {
                throw new NotCompliantMBeanException("Getter and setter for " + entry.getKey() + " have inconsistent types");
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanAnalyzer$MethodOrder.class */
    private static class MethodOrder implements Comparator<Method> {
        public static final MethodOrder instance = new MethodOrder();

        private MethodOrder() {
        }

        @Override // java.util.Comparator
        public int compare(Method method, Method method2) {
            int iCompareTo = method.getName().compareTo(method2.getName());
            if (iCompareTo != 0) {
                return iCompareTo;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?>[] parameterTypes2 = method2.getParameterTypes();
            if (parameterTypes.length != parameterTypes2.length) {
                return parameterTypes.length - parameterTypes2.length;
            }
            if (!Arrays.equals(parameterTypes, parameterTypes2)) {
                return Arrays.toString(parameterTypes).compareTo(Arrays.toString(parameterTypes2));
            }
            Class<?> returnType = method.getReturnType();
            Class<?> returnType2 = method2.getReturnType();
            if (returnType == returnType2) {
                return 0;
            }
            if (returnType.isAssignableFrom(returnType2)) {
                return -1;
            }
            return 1;
        }
    }

    static List<Method> eliminateCovariantMethods(List<Method> list) {
        int size = list.size();
        Method[] methodArr = (Method[]) list.toArray(new Method[size]);
        Arrays.sort(methodArr, MethodOrder.instance);
        Set setNewSet = Util.newSet();
        for (int i2 = 1; i2 < size; i2++) {
            Method method = methodArr[i2 - 1];
            Method method2 = methodArr[i2];
            if (method.getName().equals(method2.getName()) && Arrays.equals(method.getParameterTypes(), method2.getParameterTypes()) && !setNewSet.add(method)) {
                throw new RuntimeException("Internal error: duplicate Method");
            }
        }
        List<Method> listNewList = Util.newList(list);
        listNewList.removeAll(setNewSet);
        return listNewList;
    }
}
