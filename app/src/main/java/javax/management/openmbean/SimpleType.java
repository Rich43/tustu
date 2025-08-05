package javax.management.openmbean;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/openmbean/SimpleType.class */
public final class SimpleType<T> extends OpenType<T> {
    static final long serialVersionUID = 2215577471957694503L;
    private transient Integer myHashCode;
    private transient String myToString;
    public static final SimpleType<Void> VOID = new SimpleType<>(Void.class);
    public static final SimpleType<Boolean> BOOLEAN = new SimpleType<>(Boolean.class);
    public static final SimpleType<Character> CHARACTER = new SimpleType<>(Character.class);
    public static final SimpleType<Byte> BYTE = new SimpleType<>(Byte.class);
    public static final SimpleType<Short> SHORT = new SimpleType<>(Short.class);
    public static final SimpleType<Integer> INTEGER = new SimpleType<>(Integer.class);
    public static final SimpleType<Long> LONG = new SimpleType<>(Long.class);
    public static final SimpleType<Float> FLOAT = new SimpleType<>(Float.class);
    public static final SimpleType<Double> DOUBLE = new SimpleType<>(Double.class);
    public static final SimpleType<String> STRING = new SimpleType<>(String.class);
    public static final SimpleType<BigDecimal> BIGDECIMAL = new SimpleType<>(BigDecimal.class);
    public static final SimpleType<BigInteger> BIGINTEGER = new SimpleType<>(BigInteger.class);
    public static final SimpleType<Date> DATE = new SimpleType<>(Date.class);
    public static final SimpleType<ObjectName> OBJECTNAME = new SimpleType<>(ObjectName.class);
    private static final SimpleType<?>[] typeArray = {VOID, BOOLEAN, CHARACTER, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, STRING, BIGDECIMAL, BIGINTEGER, DATE, OBJECTNAME};
    private static final Map<SimpleType<?>, SimpleType<?>> canonicalTypes = new HashMap();

    static {
        for (int i2 = 0; i2 < typeArray.length; i2++) {
            SimpleType<?> simpleType = typeArray[i2];
            canonicalTypes.put(simpleType, simpleType);
        }
    }

    private SimpleType(Class<T> cls) {
        super(cls.getName(), cls.getName(), cls.getName(), false);
        this.myHashCode = null;
        this.myToString = null;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean isValue(Object obj) {
        if (obj == null) {
            return false;
        }
        return getClassName().equals(obj.getClass().getName());
    }

    @Override // javax.management.openmbean.OpenType
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleType)) {
            return false;
        }
        return getClassName().equals(((SimpleType) obj).getClassName());
    }

    @Override // javax.management.openmbean.OpenType
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = Integer.valueOf(getClassName().hashCode());
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenType
    public String toString() {
        if (this.myToString == null) {
            this.myToString = getClass().getName() + "(name=" + getTypeName() + ")";
        }
        return this.myToString;
    }

    public Object readResolve() throws ObjectStreamException {
        SimpleType<?> simpleType = canonicalTypes.get(this);
        if (simpleType == null) {
            throw new InvalidObjectException("Invalid SimpleType: " + ((Object) this));
        }
        return simpleType;
    }
}
