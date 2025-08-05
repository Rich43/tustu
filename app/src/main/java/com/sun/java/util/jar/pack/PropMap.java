package com.sun.java.util.jar.pack;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.jar.Pack200;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/PropMap.class */
final class PropMap implements SortedMap<String, String> {
    private final TreeMap<String, String> theMap = new TreeMap<>();
    private final List<Object> listenerList = new ArrayList(1);
    private static Map<String, String> defaultProps;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX WARN: Failed to calculate best type for var: r7v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r8v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 7, insn: 0x00e5: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r7 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:24:0x00e5 */
    /* JADX WARN: Not initialized variable reg: 8, insn: 0x00e9: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r8 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:26:0x00e9 */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.Throwable] */
    static {
        $assertionsDisabled = !PropMap.class.desiredAssertionStatus();
        Properties properties = new Properties();
        properties.put("com.sun.java.util.jar.pack.disable.native", String.valueOf(Boolean.getBoolean("com.sun.java.util.jar.pack.disable.native")));
        properties.put("com.sun.java.util.jar.pack.verbose", String.valueOf(Integer.getInteger("com.sun.java.util.jar.pack.verbose", 0)));
        properties.put("com.sun.java.util.jar.pack.default.timezone", String.valueOf(Boolean.getBoolean("com.sun.java.util.jar.pack.default.timezone")));
        properties.put(Pack200.Packer.SEGMENT_LIMIT, "-1");
        properties.put(Pack200.Packer.KEEP_FILE_ORDER, "true");
        properties.put(Pack200.Packer.MODIFICATION_TIME, "keep");
        properties.put(Pack200.Packer.DEFLATE_HINT, "keep");
        properties.put(Pack200.Packer.UNKNOWN_ATTRIBUTE, Pack200.Packer.PASS);
        properties.put("com.sun.java.util.jar.pack.class.format.error", System.getProperty("com.sun.java.util.jar.pack.class.format.error", Pack200.Packer.PASS));
        properties.put(Pack200.Packer.EFFORT, "5");
        try {
            try {
                InputStream resourceAsStream = PackerImpl.class.getResourceAsStream("intrinsic.properties");
                Throwable th = null;
                if (resourceAsStream == null) {
                    throw new RuntimeException("intrinsic.properties cannot be loaded");
                }
                properties.load(resourceAsStream);
                if (resourceAsStream != null) {
                    if (0 != 0) {
                        try {
                            resourceAsStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        resourceAsStream.close();
                    }
                }
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    String str = (String) entry.getKey();
                    String str2 = (String) entry.getValue();
                    if (str.startsWith("attribute.")) {
                        entry.setValue(Attribute.normalizeLayoutString(str2));
                    }
                }
                defaultProps = new HashMap(properties);
            } finally {
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    void addListener(Object obj) {
        if (!$assertionsDisabled && !Beans.isPropertyChangeListener(obj)) {
            throw new AssertionError();
        }
        this.listenerList.add(obj);
    }

    void removeListener(Object obj) {
        if (!$assertionsDisabled && !Beans.isPropertyChangeListener(obj)) {
            throw new AssertionError();
        }
        this.listenerList.remove(obj);
    }

    @Override // java.util.Map
    public String put(String str, String str2) throws IllegalArgumentException {
        String strPut = this.theMap.put(str, str2);
        if (str2 != strPut && !this.listenerList.isEmpty()) {
            if (!$assertionsDisabled && !Beans.isBeansPresent()) {
                throw new AssertionError();
            }
            Object objNewPropertyChangeEvent = Beans.newPropertyChangeEvent(this, str, strPut, str2);
            Iterator<Object> it = this.listenerList.iterator();
            while (it.hasNext()) {
                Beans.invokePropertyChange(it.next(), objNewPropertyChangeEvent);
            }
        }
        return strPut;
    }

    PropMap() {
        this.theMap.putAll(defaultProps);
    }

    SortedMap<String, String> prefixMap(String str) {
        int length = str.length();
        if (length == 0) {
            return this;
        }
        return subMap(str, str.substring(0, length - 1) + ((char) (str.charAt(length - 1) + 1)));
    }

    String getProperty(String str) {
        return get((Object) str);
    }

    String getProperty(String str, String str2) {
        String property = getProperty(str);
        if (property == null) {
            return str2;
        }
        return property;
    }

    String setProperty(String str, String str2) {
        return put(str, str2);
    }

    List<String> getProperties(String str) {
        Collection<String> collectionValues = prefixMap(str).values();
        ArrayList arrayList = new ArrayList(collectionValues.size());
        arrayList.addAll(collectionValues);
        while (arrayList.remove((Object) null)) {
        }
        return arrayList;
    }

    private boolean toBoolean(String str) {
        return Boolean.valueOf(str).booleanValue();
    }

    boolean getBoolean(String str) {
        return toBoolean(getProperty(str));
    }

    boolean setBoolean(String str, boolean z2) {
        return toBoolean(setProperty(str, String.valueOf(z2)));
    }

    int toInteger(String str) {
        return toInteger(str, 0);
    }

    int toInteger(String str, int i2) {
        if (str == null) {
            return i2;
        }
        if ("true".equals(str)) {
            return 1;
        }
        if ("false".equals(str)) {
            return 0;
        }
        return Integer.parseInt(str);
    }

    int getInteger(String str, int i2) {
        return toInteger(getProperty(str), i2);
    }

    int getInteger(String str) {
        return toInteger(getProperty(str));
    }

    int setInteger(String str, int i2) {
        return toInteger(setProperty(str, String.valueOf(i2)));
    }

    long toLong(String str) {
        if (str == null) {
            return 0L;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e2) {
            throw new IllegalArgumentException("Invalid value");
        }
    }

    long getLong(String str) {
        return toLong(getProperty(str));
    }

    long setLong(String str, long j2) {
        return toLong(setProperty(str, String.valueOf(j2)));
    }

    int getTime(String str) {
        String property = getProperty(str, "0");
        if ("now".equals(property)) {
            return (int) ((System.currentTimeMillis() + 500) / 1000);
        }
        long j2 = toLong(property);
        if (j2 < 10000000000L && !"0".equals(property)) {
            Utils.log.warning("Supplied modtime appears to be seconds rather than milliseconds: " + property);
        }
        return (int) ((j2 + 500) / 1000);
    }

    void list(PrintStream printStream) {
        PrintWriter printWriter = new PrintWriter(printStream);
        list(printWriter);
        printWriter.flush();
    }

    void list(PrintWriter printWriter) {
        printWriter.println("#PACK200[");
        Set<Map.Entry<String, String>> setEntrySet = defaultProps.entrySet();
        for (Map.Entry<String, String> entry : this.theMap.entrySet()) {
            if (!setEntrySet.contains(entry)) {
                printWriter.println(sun.security.pkcs11.wrapper.Constants.INDENT + entry.getKey() + " = " + entry.getValue());
            }
        }
        printWriter.println("#]");
    }

    @Override // java.util.Map
    public int size() {
        return this.theMap.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.theMap.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return this.theMap.containsKey(obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.theMap.containsValue(obj);
    }

    @Override // java.util.Map
    public String get(Object obj) {
        return this.theMap.get(obj);
    }

    @Override // java.util.Map
    public String remove(Object obj) {
        return this.theMap.remove(obj);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends String, ? extends String> map) {
        this.theMap.putAll(map);
    }

    @Override // java.util.Map
    public void clear() {
        this.theMap.clear();
    }

    @Override // java.util.SortedMap, java.util.Map
    public Set<String> keySet() {
        return this.theMap.keySet();
    }

    @Override // java.util.SortedMap, java.util.Map
    public Collection<String> values() {
        return this.theMap.values();
    }

    @Override // java.util.SortedMap, java.util.Map
    public Set<Map.Entry<String, String>> entrySet() {
        return this.theMap.entrySet();
    }

    @Override // java.util.SortedMap
    public Comparator<? super String> comparator() {
        return this.theMap.comparator();
    }

    @Override // java.util.SortedMap
    public SortedMap<String, String> subMap(String str, String str2) {
        return this.theMap.subMap(str, str2);
    }

    @Override // java.util.SortedMap
    public SortedMap<String, String> headMap(String str) {
        return this.theMap.headMap(str);
    }

    @Override // java.util.SortedMap
    public SortedMap<String, String> tailMap(String str) {
        return this.theMap.tailMap(str);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedMap
    public String firstKey() {
        return this.theMap.firstKey();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.SortedMap
    public String lastKey() {
        return this.theMap.lastKey();
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/PropMap$Beans.class */
    private static class Beans {
        private static final Class<?> propertyChangeListenerClass = getClass("java.beans.PropertyChangeListener");
        private static final Class<?> propertyChangeEventClass = getClass("java.beans.PropertyChangeEvent");
        private static final Method propertyChangeMethod = getMethod(propertyChangeListenerClass, "propertyChange", propertyChangeEventClass);
        private static final Constructor<?> propertyEventCtor = getConstructor(propertyChangeEventClass, Object.class, String.class, Object.class, Object.class);

        private Beans() {
        }

        private static Class<?> getClass(String str) {
            try {
                return Class.forName(str, true, Beans.class.getClassLoader());
            } catch (ClassNotFoundException e2) {
                return null;
            }
        }

        private static Constructor<?> getConstructor(Class<?> cls, Class<?>... clsArr) {
            if (cls == null) {
                return null;
            }
            try {
                return cls.getDeclaredConstructor(clsArr);
            } catch (NoSuchMethodException e2) {
                throw new AssertionError(e2);
            }
        }

        private static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
            if (cls == null) {
                return null;
            }
            try {
                return cls.getMethod(str, clsArr);
            } catch (NoSuchMethodException e2) {
                throw new AssertionError(e2);
            }
        }

        static boolean isBeansPresent() {
            return (propertyChangeListenerClass == null || propertyChangeEventClass == null) ? false : true;
        }

        static boolean isPropertyChangeListener(Object obj) {
            if (propertyChangeListenerClass == null) {
                return false;
            }
            return propertyChangeListenerClass.isInstance(obj);
        }

        static Object newPropertyChangeEvent(Object obj, String str, Object obj2, Object obj3) {
            try {
                return propertyEventCtor.newInstance(obj, str, obj2, obj3);
            } catch (IllegalAccessException | InstantiationException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new AssertionError(e3);
            }
        }

        static void invokePropertyChange(Object obj, Object obj2) throws IllegalArgumentException {
            try {
                propertyChangeMethod.invoke(obj, obj2);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new AssertionError(e3);
            }
        }
    }
}
