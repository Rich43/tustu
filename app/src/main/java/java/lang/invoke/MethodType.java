package java.lang.invoke;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.invoke.util.BytecodeDescriptor;
import sun.invoke.util.VerifyType;
import sun.invoke.util.Wrapper;

/* loaded from: rt.jar:java/lang/invoke/MethodType.class */
public final class MethodType implements Serializable {
    private static final long serialVersionUID = 292;
    private final Class<?> rtype;
    private final Class<?>[] ptypes;

    @Stable
    private MethodTypeForm form;

    @Stable
    private Object wrapAlt;

    @Stable
    private Invokers invokers;

    @Stable
    private String methodDescriptor;
    static final int MAX_JVM_ARITY = 255;
    static final int MAX_MH_ARITY = 254;
    static final int MAX_MH_INVOKER_ARITY = 253;
    static final ConcurrentWeakInternSet<MethodType> internTable;
    static final Class<?>[] NO_PTYPES;
    private static final MethodType[] objectOnlyTypes;
    private static final ObjectStreamField[] serialPersistentFields;
    private static final long rtypeOffset;
    private static final long ptypesOffset;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MethodType.class.desiredAssertionStatus();
        internTable = new ConcurrentWeakInternSet<>();
        NO_PTYPES = new Class[0];
        objectOnlyTypes = new MethodType[20];
        serialPersistentFields = new ObjectStreamField[0];
        try {
            rtypeOffset = MethodHandleStatics.UNSAFE.objectFieldOffset(MethodType.class.getDeclaredField("rtype"));
            ptypesOffset = MethodHandleStatics.UNSAFE.objectFieldOffset(MethodType.class.getDeclaredField("ptypes"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    private MethodType(Class<?> cls, Class<?>[] clsArr, boolean z2) {
        checkRtype(cls);
        checkPtypes(clsArr);
        this.rtype = cls;
        this.ptypes = z2 ? clsArr : (Class[]) Arrays.copyOf(clsArr, clsArr.length);
    }

    private MethodType(Class<?>[] clsArr, Class<?> cls) {
        this.rtype = cls;
        this.ptypes = clsArr;
    }

    MethodTypeForm form() {
        return this.form;
    }

    Class<?> rtype() {
        return this.rtype;
    }

    Class<?>[] ptypes() {
        return this.ptypes;
    }

    void setForm(MethodTypeForm methodTypeForm) {
        this.form = methodTypeForm;
    }

    private static void checkRtype(Class<?> cls) {
        Objects.requireNonNull(cls);
    }

    private static void checkPtype(Class<?> cls) {
        Objects.requireNonNull(cls);
        if (cls == Void.TYPE) {
            throw MethodHandleStatics.newIllegalArgumentException("parameter type cannot be void");
        }
    }

    private static int checkPtypes(Class<?>[] clsArr) {
        int i2 = 0;
        for (Class<?> cls : clsArr) {
            checkPtype(cls);
            if (cls == Double.TYPE || cls == Long.TYPE) {
                i2++;
            }
        }
        checkSlotCount(clsArr.length + i2);
        return i2;
    }

    static void checkSlotCount(int i2) {
        if ((i2 & 255) != i2) {
            throw MethodHandleStatics.newIllegalArgumentException("bad parameter count " + i2);
        }
    }

    private static IndexOutOfBoundsException newIndexOutOfBoundsException(Object obj) {
        if (obj instanceof Integer) {
            obj = "bad index: " + obj;
        }
        return new IndexOutOfBoundsException(obj.toString());
    }

    public static MethodType methodType(Class<?> cls, Class<?>[] clsArr) {
        return makeImpl(cls, clsArr, false);
    }

    public static MethodType methodType(Class<?> cls, List<Class<?>> list) {
        return makeImpl(cls, listToArray(list), false);
    }

    private static Class<?>[] listToArray(List<Class<?>> list) {
        checkSlotCount(list.size());
        return (Class[]) list.toArray(NO_PTYPES);
    }

    public static MethodType methodType(Class<?> cls, Class<?> cls2, Class<?>... clsArr) {
        Class[] clsArr2 = new Class[1 + clsArr.length];
        clsArr2[0] = cls2;
        System.arraycopy(clsArr, 0, clsArr2, 1, clsArr.length);
        return makeImpl(cls, clsArr2, true);
    }

    public static MethodType methodType(Class<?> cls) {
        return makeImpl(cls, NO_PTYPES, true);
    }

    public static MethodType methodType(Class<?> cls, Class<?> cls2) {
        return makeImpl(cls, new Class[]{cls2}, true);
    }

    public static MethodType methodType(Class<?> cls, MethodType methodType) {
        return makeImpl(cls, methodType.ptypes, true);
    }

    static MethodType makeImpl(Class<?> cls, Class<?>[] clsArr, boolean z2) {
        MethodType methodType = internTable.get(new MethodType(clsArr, cls));
        if (methodType != null) {
            return methodType;
        }
        if (clsArr.length == 0) {
            clsArr = NO_PTYPES;
            z2 = true;
        }
        MethodType methodType2 = new MethodType(cls, clsArr, z2);
        methodType2.form = MethodTypeForm.findForm(methodType2);
        return internTable.add(methodType2);
    }

    public static MethodType genericMethodType(int i2, boolean z2) {
        MethodType methodType;
        checkSlotCount(i2);
        int i3 = !z2 ? 0 : 1;
        int i4 = (i2 * 2) + i3;
        if (i4 < objectOnlyTypes.length && (methodType = objectOnlyTypes[i4]) != null) {
            return methodType;
        }
        Class[] clsArr = new Class[i2 + i3];
        Arrays.fill(clsArr, Object.class);
        if (i3 != 0) {
            clsArr[i2] = Object[].class;
        }
        MethodType methodTypeMakeImpl = makeImpl(Object.class, clsArr, true);
        if (i4 < objectOnlyTypes.length) {
            objectOnlyTypes[i4] = methodTypeMakeImpl;
        }
        return methodTypeMakeImpl;
    }

    public static MethodType genericMethodType(int i2) {
        return genericMethodType(i2, false);
    }

    public MethodType changeParameterType(int i2, Class<?> cls) {
        if (parameterType(i2) == cls) {
            return this;
        }
        checkPtype(cls);
        Class[] clsArr = (Class[]) this.ptypes.clone();
        clsArr[i2] = cls;
        return makeImpl(this.rtype, clsArr, true);
    }

    public MethodType insertParameterTypes(int i2, Class<?>... clsArr) {
        int length = this.ptypes.length;
        if (i2 < 0 || i2 > length) {
            throw newIndexOutOfBoundsException(Integer.valueOf(i2));
        }
        checkSlotCount(parameterSlotCount() + clsArr.length + checkPtypes(clsArr));
        int length2 = clsArr.length;
        if (length2 == 0) {
            return this;
        }
        Class[] clsArr2 = (Class[]) Arrays.copyOfRange(this.ptypes, 0, length + length2);
        System.arraycopy(clsArr2, i2, clsArr2, i2 + length2, length - i2);
        System.arraycopy(clsArr, 0, clsArr2, i2, length2);
        return makeImpl(this.rtype, clsArr2, true);
    }

    public MethodType appendParameterTypes(Class<?>... clsArr) {
        return insertParameterTypes(parameterCount(), clsArr);
    }

    public MethodType insertParameterTypes(int i2, List<Class<?>> list) {
        return insertParameterTypes(i2, listToArray(list));
    }

    public MethodType appendParameterTypes(List<Class<?>> list) {
        return insertParameterTypes(parameterCount(), list);
    }

    MethodType replaceParameterTypes(int i2, int i3, Class<?>... clsArr) {
        if (i2 == i3) {
            return insertParameterTypes(i2, clsArr);
        }
        int length = this.ptypes.length;
        if (0 > i2 || i2 > i3 || i3 > length) {
            throw newIndexOutOfBoundsException("start=" + i2 + " end=" + i3);
        }
        if (clsArr.length == 0) {
            return dropParameterTypes(i2, i3);
        }
        return dropParameterTypes(i2, i3).insertParameterTypes(i2, clsArr);
    }

    MethodType asSpreaderType(Class<?> cls, int i2) {
        if (!$assertionsDisabled && parameterCount() < i2) {
            throw new AssertionError();
        }
        int length = this.ptypes.length - i2;
        if (i2 == 0) {
            return this;
        }
        if (cls == Object[].class) {
            if (isGeneric()) {
                return this;
            }
            if (length == 0) {
                MethodType methodTypeGenericMethodType = genericMethodType(i2);
                if (this.rtype != Object.class) {
                    methodTypeGenericMethodType = methodTypeGenericMethodType.changeReturnType(this.rtype);
                }
                return methodTypeGenericMethodType;
            }
        }
        Class<?> componentType = cls.getComponentType();
        if (!$assertionsDisabled && componentType == null) {
            throw new AssertionError();
        }
        for (int i3 = length; i3 < this.ptypes.length; i3++) {
            if (this.ptypes[i3] != componentType) {
                Class[] clsArr = (Class[]) this.ptypes.clone();
                Arrays.fill(clsArr, i3, this.ptypes.length, componentType);
                return methodType(this.rtype, (Class<?>[]) clsArr);
            }
        }
        return this;
    }

    Class<?> leadingReferenceParameter() {
        if (this.ptypes.length != 0) {
            Class<?> cls = this.ptypes[0];
            if (!cls.isPrimitive()) {
                return cls;
            }
        }
        throw MethodHandleStatics.newIllegalArgumentException("no leading reference parameter");
    }

    MethodType asCollectorType(Class<?> cls, int i2) {
        MethodType methodType;
        if (!$assertionsDisabled && parameterCount() < 1) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !lastParameterType().isAssignableFrom(cls)) {
            throw new AssertionError();
        }
        if (cls == Object[].class) {
            methodType = genericMethodType(i2);
            if (this.rtype != Object.class) {
                methodType = methodType.changeReturnType(this.rtype);
            }
        } else {
            Class<?> componentType = cls.getComponentType();
            if (!$assertionsDisabled && componentType == null) {
                throw new AssertionError();
            }
            methodType = methodType(this.rtype, (List<Class<?>>) Collections.nCopies(i2, componentType));
        }
        if (this.ptypes.length == 1) {
            return methodType;
        }
        return methodType.insertParameterTypes(0, parameterList().subList(0, this.ptypes.length - 1));
    }

    public MethodType dropParameterTypes(int i2, int i3) {
        Class<?>[] clsArr;
        int length = this.ptypes.length;
        if (0 > i2 || i2 > i3 || i3 > length) {
            throw newIndexOutOfBoundsException("start=" + i2 + " end=" + i3);
        }
        if (i2 == i3) {
            return this;
        }
        if (i2 == 0) {
            if (i3 == length) {
                clsArr = NO_PTYPES;
            } else {
                clsArr = (Class[]) Arrays.copyOfRange(this.ptypes, i3, length);
            }
        } else if (i3 == length) {
            clsArr = (Class[]) Arrays.copyOfRange(this.ptypes, 0, i2);
        } else {
            int i4 = length - i3;
            clsArr = (Class[]) Arrays.copyOfRange(this.ptypes, 0, i2 + i4);
            System.arraycopy(this.ptypes, i3, clsArr, i2, i4);
        }
        return makeImpl(this.rtype, clsArr, true);
    }

    public MethodType changeReturnType(Class<?> cls) {
        return returnType() == cls ? this : makeImpl(cls, this.ptypes, true);
    }

    public boolean hasPrimitives() {
        return this.form.hasPrimitives();
    }

    public boolean hasWrappers() {
        return unwrap() != this;
    }

    public MethodType erase() {
        return this.form.erasedType();
    }

    MethodType basicType() {
        return this.form.basicType();
    }

    MethodType invokerType() {
        return insertParameterTypes(0, MethodHandle.class);
    }

    public MethodType generic() {
        return genericMethodType(parameterCount());
    }

    boolean isGeneric() {
        return this == erase() && !hasPrimitives();
    }

    public MethodType wrap() {
        return hasPrimitives() ? wrapWithPrims(this) : this;
    }

    public MethodType unwrap() {
        return unwrapWithNoPrims(!hasPrimitives() ? this : wrapWithPrims(this));
    }

    private static MethodType wrapWithPrims(MethodType methodType) {
        if (!$assertionsDisabled && !methodType.hasPrimitives()) {
            throw new AssertionError();
        }
        MethodType methodTypeCanonicalize = (MethodType) methodType.wrapAlt;
        if (methodTypeCanonicalize == null) {
            methodTypeCanonicalize = MethodTypeForm.canonicalize(methodType, 2, 2);
            if (!$assertionsDisabled && methodTypeCanonicalize == null) {
                throw new AssertionError();
            }
            methodType.wrapAlt = methodTypeCanonicalize;
        }
        return methodTypeCanonicalize;
    }

    private static MethodType unwrapWithNoPrims(MethodType methodType) {
        if (!$assertionsDisabled && methodType.hasPrimitives()) {
            throw new AssertionError();
        }
        MethodType methodTypeCanonicalize = (MethodType) methodType.wrapAlt;
        if (methodTypeCanonicalize == null) {
            methodTypeCanonicalize = MethodTypeForm.canonicalize(methodType, 3, 3);
            if (methodTypeCanonicalize == null) {
                methodTypeCanonicalize = methodType;
            }
            methodType.wrapAlt = methodTypeCanonicalize;
        }
        return methodTypeCanonicalize;
    }

    public Class<?> parameterType(int i2) {
        return this.ptypes[i2];
    }

    public int parameterCount() {
        return this.ptypes.length;
    }

    public Class<?> returnType() {
        return this.rtype;
    }

    public List<Class<?>> parameterList() {
        return Collections.unmodifiableList(Arrays.asList((Object[]) this.ptypes.clone()));
    }

    Class<?> lastParameterType() {
        int length = this.ptypes.length;
        return length == 0 ? Void.TYPE : this.ptypes[length - 1];
    }

    public Class<?>[] parameterArray() {
        return (Class[]) this.ptypes.clone();
    }

    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof MethodType) && equals((MethodType) obj));
    }

    private boolean equals(MethodType methodType) {
        return this.rtype == methodType.rtype && Arrays.equals(this.ptypes, methodType.ptypes);
    }

    public int hashCode() {
        int iHashCode = 31 + this.rtype.hashCode();
        for (Class<?> cls : this.ptypes) {
            iHashCode = (31 * iHashCode) + cls.hashCode();
        }
        return iHashCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i2 = 0; i2 < this.ptypes.length; i2++) {
            if (i2 > 0) {
                sb.append(",");
            }
            sb.append(this.ptypes[i2].getSimpleName());
        }
        sb.append(")");
        sb.append(this.rtype.getSimpleName());
        return sb.toString();
    }

    boolean isViewableAs(MethodType methodType, boolean z2) {
        if (!VerifyType.isNullConversion(returnType(), methodType.returnType(), z2)) {
            return false;
        }
        return parametersAreViewableAs(methodType, z2);
    }

    boolean parametersAreViewableAs(MethodType methodType, boolean z2) {
        if ((this.form == methodType.form && this.form.erasedType == this) || this.ptypes == methodType.ptypes) {
            return true;
        }
        int iParameterCount = parameterCount();
        if (iParameterCount != methodType.parameterCount()) {
            return false;
        }
        for (int i2 = 0; i2 < iParameterCount; i2++) {
            if (!VerifyType.isNullConversion(methodType.parameterType(i2), parameterType(i2), z2)) {
                return false;
            }
        }
        return true;
    }

    boolean isConvertibleTo(MethodType methodType) {
        MethodTypeForm methodTypeFormForm = form();
        MethodTypeForm methodTypeFormForm2 = methodType.form();
        if (methodTypeFormForm == methodTypeFormForm2) {
            return true;
        }
        if (!canConvert(returnType(), methodType.returnType())) {
            return false;
        }
        Class<?>[] clsArr = methodType.ptypes;
        Class<?>[] clsArr2 = this.ptypes;
        if (clsArr == clsArr2) {
            return true;
        }
        int length = clsArr.length;
        if (length != clsArr2.length) {
            return false;
        }
        if (length <= 1) {
            if (length == 1 && !canConvert(clsArr[0], clsArr2[0])) {
                return false;
            }
            return true;
        }
        if ((methodTypeFormForm.primitiveParameterCount() == 0 && methodTypeFormForm.erasedType == this) || (methodTypeFormForm2.primitiveParameterCount() == 0 && methodTypeFormForm2.erasedType == methodType)) {
            if ($assertionsDisabled || canConvertParameters(clsArr, clsArr2)) {
                return true;
            }
            throw new AssertionError();
        }
        return canConvertParameters(clsArr, clsArr2);
    }

    boolean explicitCastEquivalentToAsType(MethodType methodType) {
        if (this == methodType) {
            return true;
        }
        if (!explicitCastEquivalentToAsType(this.rtype, methodType.rtype)) {
            return false;
        }
        Class<?>[] clsArr = methodType.ptypes;
        Class<?>[] clsArr2 = this.ptypes;
        if (clsArr2 == clsArr) {
            return true;
        }
        if (!$assertionsDisabled && clsArr2.length != clsArr.length) {
            throw new AssertionError();
        }
        for (int i2 = 0; i2 < clsArr2.length; i2++) {
            if (!explicitCastEquivalentToAsType(clsArr[i2], clsArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    private static boolean explicitCastEquivalentToAsType(Class<?> cls, Class<?> cls2) {
        if (cls == cls2 || cls2 == Object.class || cls2 == Void.TYPE) {
            return true;
        }
        if (cls.isPrimitive()) {
            return canConvert(cls, cls2);
        }
        if (cls2.isPrimitive()) {
            return false;
        }
        return !cls2.isInterface() || cls2.isAssignableFrom(cls);
    }

    private boolean canConvertParameters(Class<?>[] clsArr, Class<?>[] clsArr2) {
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            if (!canConvert(clsArr[i2], clsArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    static boolean canConvert(Class<?> cls, Class<?> cls2) {
        if (cls == cls2 || cls == Object.class || cls2 == Object.class) {
            return true;
        }
        if (cls.isPrimitive()) {
            if (cls == Void.TYPE) {
                return true;
            }
            Wrapper wrapperForPrimitiveType = Wrapper.forPrimitiveType(cls);
            if (cls2.isPrimitive()) {
                return Wrapper.forPrimitiveType(cls2).isConvertibleFrom(wrapperForPrimitiveType);
            }
            return cls2.isAssignableFrom(wrapperForPrimitiveType.wrapperType());
        }
        if (!cls2.isPrimitive() || cls2 == Void.TYPE) {
            return true;
        }
        Wrapper wrapperForPrimitiveType2 = Wrapper.forPrimitiveType(cls2);
        if (cls.isAssignableFrom(wrapperForPrimitiveType2.wrapperType())) {
            return true;
        }
        if (Wrapper.isWrapperType(cls) && wrapperForPrimitiveType2.isConvertibleFrom(Wrapper.forWrapperType(cls))) {
            return true;
        }
        return false;
    }

    int parameterSlotCount() {
        return this.form.parameterSlotCount();
    }

    Invokers invokers() {
        Invokers invokers = this.invokers;
        if (invokers != null) {
            return invokers;
        }
        Invokers invokers2 = new Invokers(this);
        this.invokers = invokers2;
        return invokers2;
    }

    int parameterSlotDepth(int i2) {
        if (i2 < 0 || i2 > this.ptypes.length) {
            parameterType(i2);
        }
        return this.form.parameterToArgSlot(i2 - 1);
    }

    int returnSlotCount() {
        return this.form.returnSlotCount();
    }

    public static MethodType fromMethodDescriptorString(String str, ClassLoader classLoader) throws TypeNotPresentException, IllegalArgumentException {
        if (!str.startsWith("(") || str.indexOf(41) < 0 || str.indexOf(46) >= 0) {
            throw MethodHandleStatics.newIllegalArgumentException("not a method descriptor: " + str);
        }
        List<Class<?>> method = BytecodeDescriptor.parseMethod(str, classLoader);
        Class<?> clsRemove = method.remove(method.size() - 1);
        checkSlotCount(method.size());
        return makeImpl(clsRemove, listToArray(method), true);
    }

    public String toMethodDescriptorString() {
        String strUnparse = this.methodDescriptor;
        if (strUnparse == null) {
            strUnparse = BytecodeDescriptor.unparse(this);
            this.methodDescriptor = strUnparse;
        }
        return strUnparse;
    }

    static String toFieldDescriptorString(Class<?> cls) {
        return BytecodeDescriptor.unparse(cls);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(returnType());
        objectOutputStream.writeObject(parameterArray());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        MethodHandleStatics.UNSAFE.putObject(this, rtypeOffset, Void.TYPE);
        MethodHandleStatics.UNSAFE.putObject(this, ptypesOffset, NO_PTYPES);
        objectInputStream.defaultReadObject();
        this.wrapAlt = new MethodType[]{methodType((Class<?>) objectInputStream.readObject(), (Class<?>[]) objectInputStream.readObject())};
    }

    private Object readResolve() {
        MethodType methodType = ((MethodType[]) this.wrapAlt)[0];
        this.wrapAlt = null;
        return methodType;
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodType$ConcurrentWeakInternSet.class */
    private static class ConcurrentWeakInternSet<T> {
        private final ConcurrentMap<WeakEntry<T>, WeakEntry<T>> map = new ConcurrentHashMap();
        private final ReferenceQueue<T> stale = new ReferenceQueue<>();

        public T get(T t2) {
            T t3;
            if (t2 == null) {
                throw new NullPointerException();
            }
            expungeStaleElements();
            WeakEntry<T> weakEntry = this.map.get(new WeakEntry(t2));
            if (weakEntry != null && (t3 = weakEntry.get()) != null) {
                return t3;
            }
            return null;
        }

        public T add(T t2) {
            T t3;
            if (t2 == null) {
                throw new NullPointerException();
            }
            WeakEntry<T> weakEntry = new WeakEntry<>(t2, this.stale);
            do {
                expungeStaleElements();
                WeakEntry<T> weakEntryPutIfAbsent = this.map.putIfAbsent(weakEntry, weakEntry);
                t3 = weakEntryPutIfAbsent == null ? t2 : weakEntryPutIfAbsent.get();
            } while (t3 == null);
            return t3;
        }

        private void expungeStaleElements() {
            while (true) {
                Reference<? extends T> referencePoll = this.stale.poll();
                if (referencePoll != null) {
                    this.map.remove(referencePoll);
                } else {
                    return;
                }
            }
        }

        /* loaded from: rt.jar:java/lang/invoke/MethodType$ConcurrentWeakInternSet$WeakEntry.class */
        private static class WeakEntry<T> extends WeakReference<T> {
            public final int hashcode;

            public WeakEntry(T t2, ReferenceQueue<T> referenceQueue) {
                super(t2, referenceQueue);
                this.hashcode = t2.hashCode();
            }

            public WeakEntry(T t2) {
                super(t2);
                this.hashcode = t2.hashCode();
            }

            public boolean equals(Object obj) {
                if (obj instanceof WeakEntry) {
                    T t2 = ((WeakEntry) obj).get();
                    T t3 = get();
                    return (t2 == null || t3 == null) ? this == obj : t3.equals(t2);
                }
                return false;
            }

            public int hashCode() {
                return this.hashcode;
            }
        }
    }
}
