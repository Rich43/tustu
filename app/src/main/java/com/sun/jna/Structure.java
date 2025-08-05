package com.sun.jna;

import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.org.glassfish.external.statistics.impl.StatisticImpl;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure.class */
public abstract class Structure {
    private static final boolean REVERSE_FIELDS;
    private static final boolean REQUIRES_FIELD_ORDER;
    static final boolean isPPC;
    static final boolean isSPARC;
    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_NONE = 1;
    public static final int ALIGN_GNUC = 2;
    public static final int ALIGN_MSVC = 3;
    private static final int MAX_GNUC_ALIGNMENT;
    protected static final int CALCULATE_SIZE = -1;
    private Pointer memory;
    private int size;
    private int alignType;
    private int structAlignment;
    private final Map structFields;
    private final Map nativeStrings;
    private TypeMapper typeMapper;
    private long typeInfo;
    private List fieldOrder;
    private boolean autoRead;
    private boolean autoWrite;
    private Structure[] array;
    private static final ThreadLocal reads;
    private static final ThreadLocal busy;
    static Class class$com$sun$jna$Structure$MemberOrder;
    static Class class$com$sun$jna$Structure;
    static Class class$com$sun$jna$Callback;
    static Class class$java$nio$Buffer;
    static Class class$com$sun$jna$Pointer;
    static Class class$com$sun$jna$NativeMapped;
    static Class class$java$lang$String;
    static Class class$com$sun$jna$WString;
    static Class class$com$sun$jna$Structure$ByReference;
    static Class class$java$lang$Long;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Short;
    static Class class$java$lang$Character;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$java$lang$Void;

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure$ByReference.class */
    public interface ByReference {
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure$ByValue.class */
    public interface ByValue {
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure$MemberOrder.class */
    private static class MemberOrder {
        private static final String[] FIELDS = {"first", StatisticImpl.UNIT_SECOND, "middle", "penultimate", Keywords.FUNC_LAST_STRING};
        public int first;
        public int second;
        public int middle;
        public int penultimate;
        public int last;

        private MemberOrder() {
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    static {
        Class clsClass$;
        if (class$com$sun$jna$Structure$MemberOrder == null) {
            clsClass$ = class$("com.sun.jna.Structure$MemberOrder");
            class$com$sun$jna$Structure$MemberOrder = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Structure$MemberOrder;
        }
        Field[] fields = clsClass$.getFields();
        List names = new ArrayList();
        for (Field field : fields) {
            names.add(field.getName());
        }
        List expected = Arrays.asList(MemberOrder.FIELDS);
        List reversed = new ArrayList(expected);
        Collections.reverse(reversed);
        REVERSE_FIELDS = names.equals(reversed);
        REQUIRES_FIELD_ORDER = (names.equals(expected) || REVERSE_FIELDS) ? false : true;
        String arch = System.getProperty("os.arch").toLowerCase();
        isPPC = "ppc".equals(arch) || "powerpc".equals(arch);
        isSPARC = "sparc".equals(arch);
        MAX_GNUC_ALIGNMENT = isSPARC ? 8 : Native.LONG_SIZE;
        reads = new ThreadLocal() { // from class: com.sun.jna.Structure.1
            @Override // java.lang.ThreadLocal
            protected synchronized Object initialValue() {
                return new HashMap();
            }
        };
        busy = new ThreadLocal() { // from class: com.sun.jna.Structure.2

            /* renamed from: com.sun.jna.Structure$2$StructureSet */
            /* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure$2$StructureSet.class */
            class StructureSet extends AbstractCollection implements Set {
                private Structure[] elements;
                private int count;
                private final AnonymousClass2 this$0;

                StructureSet(AnonymousClass2 anonymousClass2) {
                    this.this$0 = anonymousClass2;
                }

                private void ensureCapacity(int size) {
                    if (this.elements == null) {
                        this.elements = new Structure[(size * 3) / 2];
                    } else if (this.elements.length < size) {
                        Structure[] e2 = new Structure[(size * 3) / 2];
                        System.arraycopy(this.elements, 0, e2, 0, this.elements.length);
                        this.elements = e2;
                    }
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return this.count;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean contains(Object o2) {
                    return indexOf(o2) != -1;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public boolean add(Object o2) {
                    if (!contains(o2)) {
                        ensureCapacity(this.count + 1);
                        Structure[] structureArr = this.elements;
                        int i2 = this.count;
                        this.count = i2 + 1;
                        structureArr[i2] = (Structure) o2;
                        return true;
                    }
                    return true;
                }

                private int indexOf(Object o2) {
                    Structure s1 = (Structure) o2;
                    for (int i2 = 0; i2 < this.count; i2++) {
                        Structure s2 = this.elements[i2];
                        if (s1 == s2 || (s1.getClass() == s2.getClass() && s1.size() == s2.size() && s1.getPointer().equals(s2.getPointer()))) {
                            return i2;
                        }
                    }
                    return -1;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean remove(Object o2) {
                    int idx = indexOf(o2);
                    if (idx != -1) {
                        int i2 = this.count - 1;
                        this.count = i2;
                        if (i2 > 0) {
                            this.elements[idx] = this.elements[this.count];
                            this.elements[this.count] = null;
                            return true;
                        }
                        return true;
                    }
                    return false;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                public Iterator iterator() {
                    Structure[] e2 = new Structure[this.count];
                    System.arraycopy(this.elements, 0, e2, 0, this.count);
                    return Arrays.asList(e2).iterator();
                }
            }

            @Override // java.lang.ThreadLocal
            protected synchronized Object initialValue() {
                return new StructureSet(this);
            }
        };
    }

    protected Structure() {
        this((Pointer) null);
    }

    protected Structure(TypeMapper mapper) {
        this((Pointer) null, 0, mapper);
    }

    protected Structure(Pointer p2) {
        this(p2, 0);
    }

    protected Structure(Pointer p2, int alignment) {
        this(p2, alignment, null);
    }

    protected Structure(Pointer p2, int alignment, TypeMapper mapper) throws Throwable {
        this.size = -1;
        this.structFields = new LinkedHashMap();
        this.nativeStrings = new HashMap();
        this.autoRead = true;
        this.autoWrite = true;
        setAlignType(alignment);
        setTypeMapper(mapper);
        if (p2 != null) {
            useMemory(p2);
        } else {
            allocateMemory(-1);
        }
    }

    Map fields() {
        return this.structFields;
    }

    protected void setTypeMapper(TypeMapper mapper) {
        Class declaring;
        if (mapper == null && (declaring = getClass().getDeclaringClass()) != null) {
            mapper = Native.getTypeMapper(declaring);
        }
        this.typeMapper = mapper;
        this.size = -1;
        if (this.memory instanceof AutoAllocated) {
            this.memory = null;
        }
    }

    protected void setAlignType(int alignType) throws SecurityException {
        if (alignType == 0) {
            Class declaring = getClass().getDeclaringClass();
            if (declaring != null) {
                alignType = Native.getStructureAlignment(declaring);
            }
            if (alignType == 0) {
                if (Platform.isWindows()) {
                    alignType = 3;
                } else {
                    alignType = 2;
                }
            }
        }
        this.alignType = alignType;
        this.size = -1;
        if (this.memory instanceof AutoAllocated) {
            this.memory = null;
        }
    }

    protected Memory autoAllocate(int size) {
        return new AutoAllocated(this, size);
    }

    protected void useMemory(Pointer m2) {
        useMemory(m2, 0);
    }

    protected void useMemory(Pointer m2, int offset) {
        try {
            this.memory = m2;
            if (this.size == -1) {
                this.size = calculateSize(false);
            }
            if (this.size != -1) {
                this.memory = m2.share(offset, this.size);
            }
            this.array = null;
        } catch (IndexOutOfBoundsException e2) {
            throw new IllegalArgumentException("Structure exceeds provided memory bounds");
        }
    }

    protected void ensureAllocated() throws Throwable {
        if (this.memory == null) {
            allocateMemory();
        } else if (this.size == -1) {
            this.size = calculateSize(true);
        }
    }

    protected void allocateMemory() throws Throwable {
        allocateMemory(calculateSize(true));
    }

    protected void allocateMemory(int size) throws Throwable {
        if (size == -1) {
            size = calculateSize(false);
        } else if (size <= 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Structure size must be greater than zero: ").append(size).toString());
        }
        if (size != -1) {
            if (this.memory == null || (this.memory instanceof AutoAllocated)) {
                this.memory = autoAllocate(size);
            }
            this.size = size;
        }
    }

    public int size() throws Throwable {
        ensureAllocated();
        if (this.size == -1) {
            this.size = calculateSize(true);
        }
        return this.size;
    }

    public void clear() {
        this.memory.clear(size());
    }

    public Pointer getPointer() throws Throwable {
        ensureAllocated();
        return this.memory;
    }

    static Set busy() {
        return (Set) busy.get();
    }

    static Map reading() {
        return (Map) reads.get();
    }

    public void read() {
        ensureAllocated();
        if (busy().contains(this)) {
            return;
        }
        busy().add(this);
        if (this instanceof ByReference) {
            reading().put(getPointer(), this);
        }
        try {
            Iterator i2 = this.structFields.values().iterator();
            while (i2.hasNext()) {
                readField((StructField) i2.next());
            }
            busy().remove(this);
            if (reading().get(getPointer()) == this) {
                reading().remove(getPointer());
            }
        } catch (Throwable th) {
            busy().remove(this);
            if (reading().get(getPointer()) == this) {
                reading().remove(getPointer());
            }
            throw th;
        }
    }

    public Object readField(String name) throws Throwable {
        ensureAllocated();
        StructField f2 = (StructField) this.structFields.get(name);
        if (f2 == null) {
            throw new IllegalArgumentException(new StringBuffer().append("No such field: ").append(name).toString());
        }
        return readField(f2);
    }

    Object getField(StructField structField) {
        try {
            return structField.field.get(this);
        } catch (Exception e2) {
            throw new Error(new StringBuffer().append("Exception reading field '").append(structField.name).append("' in ").append((Object) getClass()).append(": ").append((Object) e2).toString());
        }
    }

    void setField(StructField structField, Object value) throws IllegalArgumentException {
        try {
            structField.field.set(this, value);
        } catch (IllegalAccessException e2) {
            throw new Error(new StringBuffer().append("Unexpectedly unable to write to field '").append(structField.name).append("' within ").append((Object) getClass()).append(": ").append((Object) e2).toString());
        }
    }

    static Structure updateStructureByReference(Class type, Structure s2, Pointer address) {
        if (address == null) {
            s2 = null;
        } else {
            if (s2 == null || !address.equals(s2.getPointer())) {
                Structure s1 = (Structure) reading().get(address);
                if (s1 != null && type.equals(s1.getClass())) {
                    s2 = s1;
                } else {
                    s2 = newInstance(type);
                    s2.useMemory(address);
                }
            }
            s2.autoRead();
        }
        return s2;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00b0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.Object readField(com.sun.jna.Structure.StructField r7) throws java.lang.Throwable {
        /*
            r6 = this;
            r0 = r7
            int r0 = r0.offset
            r8 = r0
            r0 = r7
            java.lang.Class r0 = r0.type
            r9 = r0
            r0 = r7
            com.sun.jna.FromNativeConverter r0 = r0.readConverter
            r10 = r0
            r0 = r10
            if (r0 == 0) goto L1d
            r0 = r10
            java.lang.Class r0 = r0.nativeType()
            r9 = r0
        L1d:
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Structure
            if (r0 != 0) goto L2f
            java.lang.String r0 = "com.sun.jna.Structure"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            com.sun.jna.Structure.class$com$sun$jna$Structure = r1
            goto L32
        L2f:
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Structure
        L32:
            r1 = r9
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto Lb0
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Callback
            if (r0 != 0) goto L4b
            java.lang.String r0 = "com.sun.jna.Callback"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            com.sun.jna.Structure.class$com$sun$jna$Callback = r1
            goto L4e
        L4b:
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Callback
        L4e:
            r1 = r9
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto Lb0
            java.lang.Class r0 = com.sun.jna.Structure.class$java$nio$Buffer
            if (r0 != 0) goto L67
            java.lang.String r0 = "java.nio.Buffer"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            com.sun.jna.Structure.class$java$nio$Buffer = r1
            goto L6a
        L67:
            java.lang.Class r0 = com.sun.jna.Structure.class$java$nio$Buffer
        L6a:
            r1 = r9
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto Lb0
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Pointer
            if (r0 != 0) goto L83
            java.lang.String r0 = "com.sun.jna.Pointer"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            com.sun.jna.Structure.class$com$sun$jna$Pointer = r1
            goto L86
        L83:
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$Pointer
        L86:
            r1 = r9
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto Lb0
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$NativeMapped
            if (r0 != 0) goto L9f
            java.lang.String r0 = "com.sun.jna.NativeMapped"
            java.lang.Class r0 = class$(r0)
            r1 = r0
            com.sun.jna.Structure.class$com$sun$jna$NativeMapped = r1
            goto La2
        L9f:
            java.lang.Class r0 = com.sun.jna.Structure.class$com$sun$jna$NativeMapped
        La2:
            r1 = r9
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 != 0) goto Lb0
            r0 = r9
            boolean r0 = r0.isArray()
            if (r0 == 0) goto Lb8
        Lb0:
            r0 = r6
            r1 = r7
            java.lang.Object r0 = r0.getField(r1)
            goto Lb9
        Lb8:
            r0 = 0
        Lb9:
            r11 = r0
            r0 = r6
            com.sun.jna.Pointer r0 = r0.memory
            r1 = r8
            long r1 = (long) r1
            r2 = r9
            r3 = r11
            java.lang.Object r0 = r0.getValue(r1, r2, r3)
            r12 = r0
            r0 = r10
            if (r0 == 0) goto Ldd
            r0 = r10
            r1 = r12
            r2 = r7
            com.sun.jna.FromNativeContext r2 = r2.context
            java.lang.Object r0 = r0.fromNative(r1, r2)
            r12 = r0
        Ldd:
            r0 = r6
            r1 = r7
            r2 = r12
            r0.setField(r1, r2)
            r0 = r12
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Structure.readField(com.sun.jna.Structure$StructField):java.lang.Object");
    }

    public void write() {
        ensureAllocated();
        if (this instanceof ByValue) {
            getTypeInfo();
        }
        if (busy().contains(this)) {
            return;
        }
        busy().add(this);
        try {
            for (StructField sf : this.structFields.values()) {
                if (!sf.isVolatile) {
                    writeField(sf);
                }
            }
            busy().remove(this);
        } catch (Throwable th) {
            busy().remove(this);
            throw th;
        }
    }

    public void writeField(String name) throws Throwable {
        ensureAllocated();
        StructField f2 = (StructField) this.structFields.get(name);
        if (f2 == null) {
            throw new IllegalArgumentException(new StringBuffer().append("No such field: ").append(name).toString());
        }
        writeField(f2);
    }

    public void writeField(String name, Object value) throws Throwable {
        ensureAllocated();
        StructField f2 = (StructField) this.structFields.get(name);
        if (f2 == null) {
            throw new IllegalArgumentException(new StringBuffer().append("No such field: ").append(name).toString());
        }
        setField(f2, value);
        writeField(f2);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0076  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void writeField(com.sun.jna.Structure.StructField r8) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 327
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Structure.writeField(com.sun.jna.Structure$StructField):void");
    }

    private boolean hasFieldOrder() {
        boolean z2;
        synchronized (this) {
            z2 = this.fieldOrder != null;
        }
        return z2;
    }

    protected List getFieldOrder() {
        List list;
        synchronized (this) {
            if (this.fieldOrder == null) {
                this.fieldOrder = new ArrayList();
            }
            list = this.fieldOrder;
        }
        return list;
    }

    protected void setFieldOrder(String[] fields) {
        getFieldOrder().addAll(Arrays.asList(fields));
        this.size = -1;
        if (this.memory instanceof AutoAllocated) {
            this.memory = null;
        }
    }

    protected void sortFields(List fields, List names) {
        for (int i2 = 0; i2 < names.size(); i2++) {
            String name = (String) names.get(i2);
            int f2 = 0;
            while (true) {
                if (f2 < fields.size()) {
                    Field field = (Field) fields.get(f2);
                    if (!name.equals(field.getName())) {
                        f2++;
                    } else {
                        Collections.swap((List<?>) fields, i2, f2);
                        break;
                    }
                }
            }
        }
    }

    protected List getFields(boolean force) throws Throwable {
        Class clsClass$;
        List flist = new ArrayList();
        Class superclass = getClass();
        while (true) {
            Class cls = superclass;
            if (class$com$sun$jna$Structure == null) {
                clsClass$ = class$("com.sun.jna.Structure");
                class$com$sun$jna$Structure = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$Structure;
            }
            if (cls.equals(clsClass$)) {
                break;
            }
            ArrayList arrayList = new ArrayList();
            Field[] fields = cls.getDeclaredFields();
            for (int i2 = 0; i2 < fields.length; i2++) {
                int modifiers = fields[i2].getModifiers();
                if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
                    arrayList.add(fields[i2]);
                }
            }
            if (REVERSE_FIELDS) {
                Collections.reverse(arrayList);
            }
            flist.addAll(0, arrayList);
            superclass = cls.getSuperclass();
        }
        if (REQUIRES_FIELD_ORDER || hasFieldOrder()) {
            List fieldOrder = getFieldOrder();
            if (fieldOrder.size() < flist.size()) {
                if (force) {
                    throw new Error(new StringBuffer().append("This VM does not store fields in a predictable order; you must use Structure.setFieldOrder to explicitly indicate the field order: ").append(System.getProperty("java.vendor")).append(", ").append(System.getProperty("java.version")).toString());
                }
                return null;
            }
            sortFields(flist, fieldOrder);
        }
        return flist;
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x0194  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    int calculateSize(boolean r8) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 984
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Structure.calculateSize(boolean):int");
    }

    int calculateAlignedSize(int calculatedSize) {
        if (this.alignType != 1 && calculatedSize % this.structAlignment != 0) {
            calculatedSize += this.structAlignment - (calculatedSize % this.structAlignment);
        }
        return calculatedSize;
    }

    protected int getStructAlignment() throws Throwable {
        if (this.size == -1) {
            calculateSize(true);
        }
        return this.structAlignment;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:59:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0199  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected int getNativeAlignment(java.lang.Class r6, java.lang.Object r7, boolean r8) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 632
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Structure.getNativeAlignment(java.lang.Class, java.lang.Object, boolean):int");
    }

    public String toString() {
        return toString(0, true);
    }

    private String format(Class type) {
        String s2 = type.getName();
        int dot = s2.lastIndexOf(".");
        return s2.substring(dot + 1);
    }

    private String toString(int indent, boolean showContents) {
        String contents;
        String LS = System.getProperty("line.separator");
        String name = new StringBuffer().append(format(getClass())).append("(").append((Object) getPointer()).append(")").toString();
        if (!(getPointer() instanceof Memory)) {
            name = new StringBuffer().append(name).append(" (").append(size()).append(" bytes)").toString();
        }
        String prefix = "";
        for (int idx = 0; idx < indent; idx++) {
            prefix = new StringBuffer().append(prefix).append(Constants.INDENT).toString();
        }
        String contents2 = LS;
        if (!showContents) {
            contents2 = "...}";
        } else {
            Iterator i2 = this.structFields.values().iterator();
            while (i2.hasNext()) {
                StructField sf = (StructField) i2.next();
                Object value = getField(sf);
                String type = format(sf.type);
                String index = "";
                String contents3 = new StringBuffer().append(contents2).append(prefix).toString();
                if (sf.type.isArray() && value != null) {
                    type = format(sf.type.getComponentType());
                    index = new StringBuffer().append("[").append(Array.getLength(value)).append("]").toString();
                }
                String contents4 = new StringBuffer().append(contents3).append(Constants.INDENT).append(type).append(" ").append(sf.name).append(index).append("@").append(Integer.toHexString(sf.offset)).toString();
                if (value instanceof Structure) {
                    value = ((Structure) value).toString(indent + 1, !(value instanceof ByReference));
                }
                String contents5 = new StringBuffer().append(contents4).append("=").toString();
                if (value instanceof Long) {
                    contents = new StringBuffer().append(contents5).append(Long.toHexString(((Long) value).longValue())).toString();
                } else if (value instanceof Integer) {
                    contents = new StringBuffer().append(contents5).append(Integer.toHexString(((Integer) value).intValue())).toString();
                } else if (value instanceof Short) {
                    contents = new StringBuffer().append(contents5).append(Integer.toHexString(((Short) value).shortValue())).toString();
                } else if (value instanceof Byte) {
                    contents = new StringBuffer().append(contents5).append(Integer.toHexString(((Byte) value).byteValue())).toString();
                } else {
                    contents = new StringBuffer().append(contents5).append(String.valueOf(value).trim()).toString();
                }
                contents2 = new StringBuffer().append(contents).append(LS).toString();
                if (!i2.hasNext()) {
                    contents2 = new StringBuffer().append(contents2).append(prefix).append("}").toString();
                }
            }
        }
        if (indent == 0 && Boolean.getBoolean("jna.dump_memory")) {
            byte[] buf = getPointer().getByteArray(0L, size());
            String contents6 = new StringBuffer().append(contents2).append(LS).append("memory dump").append(LS).toString();
            for (int i3 = 0; i3 < buf.length; i3++) {
                if (i3 % 4 == 0) {
                    contents6 = new StringBuffer().append(contents6).append("[").toString();
                }
                if (buf[i3] >= 0 && buf[i3] < 16) {
                    contents6 = new StringBuffer().append(contents6).append("0").toString();
                }
                contents6 = new StringBuffer().append(contents6).append(Integer.toHexString(buf[i3] & 255)).toString();
                if (i3 % 4 == 3 && i3 < buf.length - 1) {
                    contents6 = new StringBuffer().append(contents6).append("]").append(LS).toString();
                }
            }
            contents2 = new StringBuffer().append(contents6).append("]").toString();
        }
        return new StringBuffer().append(name).append(" {").append(contents2).toString();
    }

    public Structure[] toArray(Structure[] array) throws Throwable {
        ensureAllocated();
        if (this.memory instanceof AutoAllocated) {
            Memory m2 = (Memory) this.memory;
            int requiredSize = array.length * size();
            if (m2.size() < requiredSize) {
                useMemory(autoAllocate(requiredSize));
            }
        }
        array[0] = this;
        int size = size();
        for (int i2 = 1; i2 < array.length; i2++) {
            array[i2] = newInstance(getClass());
            array[i2].useMemory(this.memory.share(i2 * size, size));
            array[i2].read();
        }
        if (!(this instanceof ByValue)) {
            this.array = array;
        }
        return array;
    }

    public Structure[] toArray(int size) {
        return toArray((Structure[]) Array.newInstance(getClass(), size));
    }

    private Class baseClass() throws Throwable {
        Class clsClass$;
        if ((this instanceof ByReference) || (this instanceof ByValue)) {
            if (class$com$sun$jna$Structure == null) {
                clsClass$ = class$("com.sun.jna.Structure");
                class$com$sun$jna$Structure = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$Structure;
            }
            if (clsClass$.isAssignableFrom(getClass().getSuperclass())) {
                return getClass().getSuperclass();
            }
        }
        return getClass();
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof Structure)) {
            return false;
        }
        if (o2.getClass() != getClass() && ((Structure) o2).baseClass() != baseClass()) {
            return false;
        }
        Structure s2 = (Structure) o2;
        if (s2.size() == size()) {
            clear();
            write();
            byte[] buf = getPointer().getByteArray(0L, size());
            s2.clear();
            s2.write();
            byte[] sbuf = s2.getPointer().getByteArray(0L, s2.size());
            return Arrays.equals(buf, sbuf);
        }
        return false;
    }

    public int hashCode() {
        clear();
        write();
        return Arrays.hashCode(getPointer().getByteArray(0L, size()));
    }

    protected void cacheTypeInfo(Pointer p2) {
        this.typeInfo = p2.peer;
    }

    Pointer getTypeInfo() {
        Pointer p2 = getTypeInfo(this);
        cacheTypeInfo(p2);
        return p2;
    }

    public void setAutoSynch(boolean auto) {
        setAutoRead(auto);
        setAutoWrite(auto);
    }

    public void setAutoRead(boolean auto) {
        this.autoRead = auto;
    }

    public boolean getAutoRead() {
        return this.autoRead;
    }

    public void setAutoWrite(boolean auto) {
        this.autoWrite = auto;
    }

    public boolean getAutoWrite() {
        return this.autoWrite;
    }

    static Pointer getTypeInfo(Object obj) {
        return FFIType.get(obj);
    }

    public static Structure newInstance(Class type) throws IllegalArgumentException {
        try {
            Structure s2 = (Structure) type.newInstance();
            if (s2 instanceof ByValue) {
                s2.allocateMemory();
            }
            return s2;
        } catch (IllegalAccessException e2) {
            String msg = new StringBuffer().append("Instantiation of ").append((Object) type).append(" not allowed, is it public? (").append((Object) e2).append(")").toString();
            throw new IllegalArgumentException(msg);
        } catch (InstantiationException e3) {
            String msg2 = new StringBuffer().append("Can't instantiate ").append((Object) type).append(" (").append((Object) e3).append(")").toString();
            throw new IllegalArgumentException(msg2);
        }
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure$StructField.class */
    class StructField {
        public String name;
        public Class type;
        public Field field;
        public int size = -1;
        public int offset = -1;
        public boolean isVolatile;
        public boolean isReadOnly;
        public FromNativeConverter readConverter;
        public ToNativeConverter writeConverter;
        public FromNativeContext context;
        private final Structure this$0;

        StructField(Structure structure) {
            this.this$0 = structure;
        }
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure$FFIType.class */
    static class FFIType extends Structure {
        private static Map typeInfoMap = new WeakHashMap();
        private static final int FFI_TYPE_STRUCT = 13;
        public size_t size;
        public short alignment;
        public short type = 13;
        public Pointer elements;

        /* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure$FFIType$size_t.class */
        public static class size_t extends IntegerType {
            public size_t() {
                this(0L);
            }

            public size_t(long value) {
                super(Native.POINTER_SIZE, value);
            }
        }

        static {
            Class clsClass$;
            Class clsClass$2;
            Class clsClass$3;
            Class clsClass$4;
            Class clsClass$5;
            Class clsClass$6;
            Class clsClass$7;
            Class clsClass$8;
            Class clsClass$9;
            Class clsClass$10;
            Class clsClass$11;
            Class clsClass$12;
            if (Native.POINTER_SIZE != 0) {
                if (FFITypes.ffi_type_void != null) {
                    typeInfoMap.put(Void.TYPE, FFITypes.ffi_type_void);
                    Map map = typeInfoMap;
                    if (Structure.class$java$lang$Void == null) {
                        clsClass$ = Structure.class$("java.lang.Void");
                        Structure.class$java$lang$Void = clsClass$;
                    } else {
                        clsClass$ = Structure.class$java$lang$Void;
                    }
                    map.put(clsClass$, FFITypes.ffi_type_void);
                    typeInfoMap.put(Float.TYPE, FFITypes.ffi_type_float);
                    Map map2 = typeInfoMap;
                    if (Structure.class$java$lang$Float == null) {
                        clsClass$2 = Structure.class$("java.lang.Float");
                        Structure.class$java$lang$Float = clsClass$2;
                    } else {
                        clsClass$2 = Structure.class$java$lang$Float;
                    }
                    map2.put(clsClass$2, FFITypes.ffi_type_float);
                    typeInfoMap.put(Double.TYPE, FFITypes.ffi_type_double);
                    Map map3 = typeInfoMap;
                    if (Structure.class$java$lang$Double == null) {
                        clsClass$3 = Structure.class$(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.DOUBLE_CLASS);
                        Structure.class$java$lang$Double = clsClass$3;
                    } else {
                        clsClass$3 = Structure.class$java$lang$Double;
                    }
                    map3.put(clsClass$3, FFITypes.ffi_type_double);
                    typeInfoMap.put(Long.TYPE, FFITypes.ffi_type_sint64);
                    Map map4 = typeInfoMap;
                    if (Structure.class$java$lang$Long == null) {
                        clsClass$4 = Structure.class$("java.lang.Long");
                        Structure.class$java$lang$Long = clsClass$4;
                    } else {
                        clsClass$4 = Structure.class$java$lang$Long;
                    }
                    map4.put(clsClass$4, FFITypes.ffi_type_sint64);
                    typeInfoMap.put(Integer.TYPE, FFITypes.ffi_type_sint32);
                    Map map5 = typeInfoMap;
                    if (Structure.class$java$lang$Integer == null) {
                        clsClass$5 = Structure.class$(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.INTEGER_CLASS);
                        Structure.class$java$lang$Integer = clsClass$5;
                    } else {
                        clsClass$5 = Structure.class$java$lang$Integer;
                    }
                    map5.put(clsClass$5, FFITypes.ffi_type_sint32);
                    typeInfoMap.put(Short.TYPE, FFITypes.ffi_type_sint16);
                    Map map6 = typeInfoMap;
                    if (Structure.class$java$lang$Short == null) {
                        clsClass$6 = Structure.class$("java.lang.Short");
                        Structure.class$java$lang$Short = clsClass$6;
                    } else {
                        clsClass$6 = Structure.class$java$lang$Short;
                    }
                    map6.put(clsClass$6, FFITypes.ffi_type_sint16);
                    Pointer ctype = Native.WCHAR_SIZE == 2 ? FFITypes.ffi_type_uint16 : FFITypes.ffi_type_uint32;
                    typeInfoMap.put(Character.TYPE, ctype);
                    Map map7 = typeInfoMap;
                    if (Structure.class$java$lang$Character == null) {
                        clsClass$7 = Structure.class$("java.lang.Character");
                        Structure.class$java$lang$Character = clsClass$7;
                    } else {
                        clsClass$7 = Structure.class$java$lang$Character;
                    }
                    map7.put(clsClass$7, ctype);
                    typeInfoMap.put(Byte.TYPE, FFITypes.ffi_type_sint8);
                    Map map8 = typeInfoMap;
                    if (Structure.class$java$lang$Byte == null) {
                        clsClass$8 = Structure.class$("java.lang.Byte");
                        Structure.class$java$lang$Byte = clsClass$8;
                    } else {
                        clsClass$8 = Structure.class$java$lang$Byte;
                    }
                    map8.put(clsClass$8, FFITypes.ffi_type_sint8);
                    typeInfoMap.put(Boolean.TYPE, FFITypes.ffi_type_uint32);
                    Map map9 = typeInfoMap;
                    if (Structure.class$java$lang$Boolean == null) {
                        clsClass$9 = Structure.class$(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.BOOLEAN_CLASS);
                        Structure.class$java$lang$Boolean = clsClass$9;
                    } else {
                        clsClass$9 = Structure.class$java$lang$Boolean;
                    }
                    map9.put(clsClass$9, FFITypes.ffi_type_uint32);
                    Map map10 = typeInfoMap;
                    if (Structure.class$com$sun$jna$Pointer == null) {
                        clsClass$10 = Structure.class$("com.sun.jna.Pointer");
                        Structure.class$com$sun$jna$Pointer = clsClass$10;
                    } else {
                        clsClass$10 = Structure.class$com$sun$jna$Pointer;
                    }
                    map10.put(clsClass$10, FFITypes.ffi_type_pointer);
                    Map map11 = typeInfoMap;
                    if (Structure.class$java$lang$String == null) {
                        clsClass$11 = Structure.class$("java.lang.String");
                        Structure.class$java$lang$String = clsClass$11;
                    } else {
                        clsClass$11 = Structure.class$java$lang$String;
                    }
                    map11.put(clsClass$11, FFITypes.ffi_type_pointer);
                    Map map12 = typeInfoMap;
                    if (Structure.class$com$sun$jna$WString == null) {
                        clsClass$12 = Structure.class$("com.sun.jna.WString");
                        Structure.class$com$sun$jna$WString = clsClass$12;
                    } else {
                        clsClass$12 = Structure.class$com$sun$jna$WString;
                    }
                    map12.put(clsClass$12, FFITypes.ffi_type_pointer);
                    return;
                }
                throw new Error("FFI types not initialized");
            }
            throw new Error("Native library not initialized");
        }

        /* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure$FFIType$FFITypes.class */
        private static class FFITypes {
            private static Pointer ffi_type_void;
            private static Pointer ffi_type_float;
            private static Pointer ffi_type_double;
            private static Pointer ffi_type_longdouble;
            private static Pointer ffi_type_uint8;
            private static Pointer ffi_type_sint8;
            private static Pointer ffi_type_uint16;
            private static Pointer ffi_type_sint16;
            private static Pointer ffi_type_uint32;
            private static Pointer ffi_type_sint32;
            private static Pointer ffi_type_uint64;
            private static Pointer ffi_type_sint64;
            private static Pointer ffi_type_pointer;

            private FFITypes() {
            }
        }

        private FFIType(Structure ref) {
            Pointer[] els;
            if (ref instanceof Union) {
                StructField sf = ((Union) ref).biggestField;
                els = new Pointer[]{get(ref.getField(sf), sf.type), null};
            } else {
                els = new Pointer[ref.fields().size() + 1];
                int idx = 0;
                for (StructField sf2 : ref.fields().values()) {
                    int i2 = idx;
                    idx++;
                    els[i2] = get(ref.getField(sf2), sf2.type);
                }
            }
            init(els);
        }

        private FFIType(Object array, Class type) throws IllegalArgumentException {
            int length = Array.getLength(array);
            Pointer[] els = new Pointer[length + 1];
            Pointer p2 = get(null, type.getComponentType());
            for (int i2 = 0; i2 < length; i2++) {
                els[i2] = p2;
            }
            init(els);
        }

        private void init(Pointer[] els) {
            this.elements = new Memory(Pointer.SIZE * els.length);
            this.elements.write(0L, els, 0, els.length);
            write();
        }

        static Pointer get(Object obj) {
            if (obj == null) {
                return FFITypes.ffi_type_pointer;
            }
            if (obj instanceof Class) {
                return get(null, (Class) obj);
            }
            return get(obj, obj.getClass());
        }

        private static Pointer get(Object obj, Class cls) {
            Class clsClass$;
            Class clsClass$2;
            Class clsClass$3;
            Class clsClass$4;
            Class clsClass$5;
            synchronized (typeInfoMap) {
                Object o2 = typeInfoMap.get(cls);
                if (o2 instanceof Pointer) {
                    return (Pointer) o2;
                }
                if (o2 instanceof FFIType) {
                    return ((FFIType) o2).getPointer();
                }
                if (Structure.class$java$nio$Buffer == null) {
                    clsClass$ = Structure.class$("java.nio.Buffer");
                    Structure.class$java$nio$Buffer = clsClass$;
                } else {
                    clsClass$ = Structure.class$java$nio$Buffer;
                }
                if (!clsClass$.isAssignableFrom(cls)) {
                    if (Structure.class$com$sun$jna$Callback == null) {
                        clsClass$2 = Structure.class$("com.sun.jna.Callback");
                        Structure.class$com$sun$jna$Callback = clsClass$2;
                    } else {
                        clsClass$2 = Structure.class$com$sun$jna$Callback;
                    }
                    if (!clsClass$2.isAssignableFrom(cls)) {
                        if (Structure.class$com$sun$jna$Structure == null) {
                            clsClass$3 = Structure.class$("com.sun.jna.Structure");
                            Structure.class$com$sun$jna$Structure = clsClass$3;
                        } else {
                            clsClass$3 = Structure.class$com$sun$jna$Structure;
                        }
                        if (clsClass$3.isAssignableFrom(cls)) {
                            if (obj == null) {
                                obj = newInstance(cls);
                            }
                            if (Structure.class$com$sun$jna$Structure$ByReference == null) {
                                clsClass$5 = Structure.class$("com.sun.jna.Structure$ByReference");
                                Structure.class$com$sun$jna$Structure$ByReference = clsClass$5;
                            } else {
                                clsClass$5 = Structure.class$com$sun$jna$Structure$ByReference;
                            }
                            if (clsClass$5.isAssignableFrom(cls)) {
                                typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
                                return FFITypes.ffi_type_pointer;
                            }
                            FFIType type = new FFIType((Structure) obj);
                            typeInfoMap.put(cls, type);
                            return type.getPointer();
                        }
                        if (Structure.class$com$sun$jna$NativeMapped == null) {
                            clsClass$4 = Structure.class$("com.sun.jna.NativeMapped");
                            Structure.class$com$sun$jna$NativeMapped = clsClass$4;
                        } else {
                            clsClass$4 = Structure.class$com$sun$jna$NativeMapped;
                        }
                        if (clsClass$4.isAssignableFrom(cls)) {
                            NativeMappedConverter c2 = NativeMappedConverter.getInstance(cls);
                            return get(c2.toNative(obj, new ToNativeContext()), c2.nativeType());
                        }
                        if (cls.isArray()) {
                            FFIType type2 = new FFIType(obj, cls);
                            typeInfoMap.put(obj, type2);
                            return type2.getPointer();
                        }
                        throw new IllegalArgumentException(new StringBuffer().append("Unsupported type ").append((Object) cls).toString());
                    }
                }
                typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
                return FFITypes.ffi_type_pointer;
            }
        }
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Structure$AutoAllocated.class */
    private class AutoAllocated extends Memory {
        private final Structure this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AutoAllocated(Structure structure, int size) {
            super(size);
            this.this$0 = structure;
            super.clear();
        }
    }

    private static void structureArrayCheck(Structure[] ss) throws Throwable {
        Pointer base = ss[0].getPointer();
        int size = ss[0].size();
        for (int si = 1; si < ss.length; si++) {
            if (ss[si].getPointer().peer != base.peer + (size * si)) {
                String msg = new StringBuffer().append("Structure array elements must use contiguous memory (bad backing address at Structure array index ").append(si).append(")").toString();
                throw new IllegalArgumentException(msg);
            }
        }
    }

    public static void autoRead(Structure[] ss) throws Throwable {
        structureArrayCheck(ss);
        if (ss[0].array == ss) {
            ss[0].autoRead();
            return;
        }
        for (Structure structure : ss) {
            structure.autoRead();
        }
    }

    public void autoRead() {
        if (getAutoRead()) {
            read();
            if (this.array != null) {
                for (int i2 = 1; i2 < this.array.length; i2++) {
                    this.array[i2].autoRead();
                }
            }
        }
    }

    public static void autoWrite(Structure[] ss) throws Throwable {
        structureArrayCheck(ss);
        if (ss[0].array == ss) {
            ss[0].autoWrite();
            return;
        }
        for (Structure structure : ss) {
            structure.autoWrite();
        }
    }

    public void autoWrite() {
        if (getAutoWrite()) {
            write();
            if (this.array != null) {
                for (int i2 = 1; i2 < this.array.length; i2++) {
                    this.array[i2].autoWrite();
                }
            }
        }
    }
}
