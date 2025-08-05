package javax.management.openmbean;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.ObjectStreamException;
import java.lang.reflect.Array;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/management/openmbean/ArrayType.class */
public class ArrayType<T> extends OpenType<T> {
    static final long serialVersionUID = 720504429830309770L;
    private int dimension;
    private OpenType<?> elementType;
    private boolean primitiveArray;
    private transient Integer myHashCode;
    private transient String myToString;
    private static final int PRIMITIVE_WRAPPER_NAME_INDEX = 0;
    private static final int PRIMITIVE_TYPE_NAME_INDEX = 1;
    private static final int PRIMITIVE_TYPE_KEY_INDEX = 2;
    private static final int PRIMITIVE_OPEN_TYPE_INDEX = 3;
    private static final Object[][] PRIMITIVE_ARRAY_TYPES = {new Object[]{Boolean.class.getName(), Boolean.TYPE.getName(), Constants.HASIDCALL_INDEX_SIG, SimpleType.BOOLEAN}, new Object[]{Character.class.getName(), Character.TYPE.getName(), "C", SimpleType.CHARACTER}, new Object[]{Byte.class.getName(), Byte.TYPE.getName(), PdfOps.B_TOKEN, SimpleType.BYTE}, new Object[]{Short.class.getName(), Short.TYPE.getName(), PdfOps.S_TOKEN, SimpleType.SHORT}, new Object[]{Integer.class.getName(), Integer.TYPE.getName(), "I", SimpleType.INTEGER}, new Object[]{Long.class.getName(), Long.TYPE.getName(), "J", SimpleType.LONG}, new Object[]{Float.class.getName(), Float.TYPE.getName(), PdfOps.F_TOKEN, SimpleType.FLOAT}, new Object[]{Double.class.getName(), Double.TYPE.getName(), PdfOps.D_TOKEN, SimpleType.DOUBLE}};

    static boolean isPrimitiveContentType(String str) {
        for (Object[] objArr : PRIMITIVE_ARRAY_TYPES) {
            if (objArr[2].equals(str)) {
                return true;
            }
        }
        return false;
    }

    static String getPrimitiveTypeKey(String str) {
        for (Object[] objArr : PRIMITIVE_ARRAY_TYPES) {
            if (str.equals(objArr[0])) {
                return (String) objArr[2];
            }
        }
        return null;
    }

    static String getPrimitiveTypeName(String str) {
        for (Object[] objArr : PRIMITIVE_ARRAY_TYPES) {
            if (str.equals(objArr[0])) {
                return (String) objArr[1];
            }
        }
        return null;
    }

    static SimpleType<?> getPrimitiveOpenType(String str) {
        for (Object[] objArr : PRIMITIVE_ARRAY_TYPES) {
            if (str.equals(objArr[1])) {
                return (SimpleType) objArr[3];
            }
        }
        return null;
    }

    public ArrayType(int i2, OpenType<?> openType) throws OpenDataException {
        super(buildArrayClassName(i2, openType), buildArrayClassName(i2, openType), buildArrayDescription(i2, openType));
        this.myHashCode = null;
        this.myToString = null;
        if (openType.isArray()) {
            ArrayType arrayType = (ArrayType) openType;
            this.dimension = arrayType.getDimension() + i2;
            this.elementType = arrayType.getElementOpenType();
            this.primitiveArray = arrayType.isPrimitiveArray();
            return;
        }
        this.dimension = i2;
        this.elementType = openType;
        this.primitiveArray = false;
    }

    public ArrayType(SimpleType<?> simpleType, boolean z2) throws OpenDataException {
        super(buildArrayClassName(1, simpleType, z2), buildArrayClassName(1, simpleType, z2), buildArrayDescription(1, simpleType, z2), true);
        this.myHashCode = null;
        this.myToString = null;
        this.dimension = 1;
        this.elementType = simpleType;
        this.primitiveArray = z2;
    }

    ArrayType(String str, String str2, String str3, int i2, OpenType<?> openType, boolean z2) {
        super(str, str2, str3, true);
        this.myHashCode = null;
        this.myToString = null;
        this.dimension = i2;
        this.elementType = openType;
        this.primitiveArray = z2;
    }

    private static String buildArrayClassName(int i2, OpenType<?> openType) throws OpenDataException {
        boolean zIsPrimitiveArray = false;
        if (openType.isArray()) {
            zIsPrimitiveArray = ((ArrayType) openType).isPrimitiveArray();
        }
        return buildArrayClassName(i2, openType, zIsPrimitiveArray);
    }

    private static String buildArrayClassName(int i2, OpenType<?> openType, boolean z2) throws OpenDataException {
        if (i2 < 1) {
            throw new IllegalArgumentException("Value of argument dimension must be greater than 0");
        }
        StringBuilder sb = new StringBuilder();
        String className = openType.getClassName();
        for (int i3 = 1; i3 <= i2; i3++) {
            sb.append('[');
        }
        if (openType.isArray()) {
            sb.append(className);
        } else if (z2) {
            String primitiveTypeKey = getPrimitiveTypeKey(className);
            if (primitiveTypeKey == null) {
                throw new OpenDataException("Element type is not primitive: " + className);
            }
            sb.append(primitiveTypeKey);
        } else {
            sb.append("L");
            sb.append(className);
            sb.append(';');
        }
        return sb.toString();
    }

    private static String buildArrayDescription(int i2, OpenType<?> openType) throws OpenDataException {
        boolean zIsPrimitiveArray = false;
        if (openType.isArray()) {
            zIsPrimitiveArray = ((ArrayType) openType).isPrimitiveArray();
        }
        return buildArrayDescription(i2, openType, zIsPrimitiveArray);
    }

    private static String buildArrayDescription(int i2, OpenType<?> openType, boolean z2) throws OpenDataException {
        if (openType.isArray()) {
            ArrayType arrayType = (ArrayType) openType;
            i2 += arrayType.getDimension();
            openType = arrayType.getElementOpenType();
            z2 = arrayType.isPrimitiveArray();
        }
        StringBuilder sb = new StringBuilder(i2 + "-dimension array of ");
        String className = openType.getClassName();
        if (z2) {
            String primitiveTypeName = getPrimitiveTypeName(className);
            if (primitiveTypeName == null) {
                throw new OpenDataException("Element is not a primitive type: " + className);
            }
            sb.append(primitiveTypeName);
        } else {
            sb.append(className);
        }
        return sb.toString();
    }

    public int getDimension() {
        return this.dimension;
    }

    public OpenType<?> getElementOpenType() {
        return this.elementType;
    }

    public boolean isPrimitiveArray() {
        return this.primitiveArray;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean isValue(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        String name = cls.getName();
        if (!cls.isArray()) {
            return false;
        }
        if (getClassName().equals(name)) {
            return true;
        }
        if (this.elementType.getClassName().equals(TabularData.class.getName()) || this.elementType.getClassName().equals(CompositeData.class.getName())) {
            boolean zEquals = this.elementType.getClassName().equals(TabularData.class.getName());
            if (!Array.newInstance((Class<?>) (zEquals ? TabularData.class : CompositeData.class), new int[getDimension()]).getClass().isAssignableFrom(cls) || !checkElementsType((Object[]) obj, this.dimension)) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean checkElementsType(Object[] objArr, int i2) {
        if (i2 > 1) {
            for (Object obj : objArr) {
                if (!checkElementsType((Object[]) obj, i2 - 1)) {
                    return false;
                }
            }
            return true;
        }
        for (int i3 = 0; i3 < objArr.length; i3++) {
            if (objArr[i3] != null && !getElementOpenType().isValue(objArr[i3])) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.management.openmbean.OpenType
    boolean isAssignableFrom(OpenType<?> openType) {
        if (!(openType instanceof ArrayType)) {
            return false;
        }
        ArrayType arrayType = (ArrayType) openType;
        return arrayType.getDimension() == getDimension() && arrayType.isPrimitiveArray() == isPrimitiveArray() && arrayType.getElementOpenType().isAssignableFrom(getElementOpenType());
    }

    @Override // javax.management.openmbean.OpenType
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ArrayType)) {
            return false;
        }
        ArrayType arrayType = (ArrayType) obj;
        return this.dimension == arrayType.dimension && this.elementType.equals(arrayType.elementType) && this.primitiveArray == arrayType.primitiveArray;
    }

    @Override // javax.management.openmbean.OpenType
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = Integer.valueOf(0 + this.dimension + this.elementType.hashCode() + Boolean.valueOf(this.primitiveArray).hashCode());
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenType
    public String toString() {
        if (this.myToString == null) {
            this.myToString = getClass().getName() + "(name=" + getTypeName() + ",dimension=" + this.dimension + ",elementType=" + ((Object) this.elementType) + ",primitiveArray=" + this.primitiveArray + ")";
        }
        return this.myToString;
    }

    public static <E> ArrayType<E[]> getArrayType(OpenType<E> openType) throws OpenDataException {
        return new ArrayType<>(1, (OpenType<?>) openType);
    }

    public static <T> ArrayType<T> getPrimitiveArrayType(Class<T> cls) {
        Class<?> cls2;
        if (!cls.isArray()) {
            throw new IllegalArgumentException("arrayClass must be an array");
        }
        int i2 = 1;
        Class<?> componentType = cls.getComponentType();
        while (true) {
            cls2 = componentType;
            if (!cls2.isArray()) {
                break;
            }
            i2++;
            componentType = cls2.getComponentType();
        }
        String name = cls2.getName();
        if (!cls2.isPrimitive()) {
            throw new IllegalArgumentException("component type of the array must be a primitive type");
        }
        try {
            ArrayType<T> arrayType = new ArrayType<>(getPrimitiveOpenType(name), true);
            if (i2 > 1) {
                arrayType = new ArrayType<>(i2 - 1, arrayType);
            }
            return arrayType;
        } catch (OpenDataException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    private Object readResolve() throws ObjectStreamException {
        if (this.primitiveArray) {
            return convertFromWrapperToPrimitiveTypes();
        }
        return this;
    }

    private <T> ArrayType<T> convertFromWrapperToPrimitiveTypes() {
        String className = getClassName();
        String typeName = getTypeName();
        String description = getDescription();
        Object[][] objArr = PRIMITIVE_ARRAY_TYPES;
        int length = objArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            Object[] objArr2 = objArr[i2];
            if (className.indexOf((String) objArr2[0]) == -1) {
                i2++;
            } else {
                className = className.replaceFirst("L" + objArr2[0] + ";", (String) objArr2[2]);
                typeName = typeName.replaceFirst("L" + objArr2[0] + ";", (String) objArr2[2]);
                description = description.replaceFirst((String) objArr2[0], (String) objArr2[1]);
                break;
            }
        }
        return new ArrayType<>(className, typeName, description, this.dimension, this.elementType, this.primitiveArray);
    }

    private Object writeReplace() throws ObjectStreamException {
        if (this.primitiveArray) {
            return convertFromPrimitiveToWrapperTypes();
        }
        return this;
    }

    private <T> ArrayType<T> convertFromPrimitiveToWrapperTypes() {
        String className = getClassName();
        String typeName = getTypeName();
        String description = getDescription();
        Object[][] objArr = PRIMITIVE_ARRAY_TYPES;
        int length = objArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            Object[] objArr2 = objArr[i2];
            if (className.indexOf((String) objArr2[2]) == -1) {
                i2++;
            } else {
                className = className.replaceFirst((String) objArr2[2], "L" + objArr2[0] + ";");
                typeName = typeName.replaceFirst((String) objArr2[2], "L" + objArr2[0] + ";");
                description = description.replaceFirst((String) objArr2[1], (String) objArr2[0]);
                break;
            }
        }
        return new ArrayType<>(className, typeName, description, this.dimension, this.elementType, this.primitiveArray);
    }
}
