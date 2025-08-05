package jdk.jfr.consumer;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jdk.jfr.Timespan;
import jdk.jfr.Timestamp;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.tool.PrettyWriter;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/consumer/RecordedObject.class */
public class RecordedObject {
    private final Object[] objects;
    private final List<ValueDescriptor> descriptors;
    private final TimeConverter timeConverter;

    /* loaded from: jfr.jar:jdk/jfr/consumer/RecordedObject$UnsignedValue.class */
    private static final class UnsignedValue {

        /* renamed from: o, reason: collision with root package name */
        private final Object f12868o;

        UnsignedValue(Object obj) {
            this.f12868o = obj;
        }

        Object value() {
            return this.f12868o;
        }
    }

    RecordedObject(List<ValueDescriptor> list, Object[] objArr, TimeConverter timeConverter) {
        this.descriptors = list;
        this.objects = objArr;
        this.timeConverter = timeConverter;
    }

    final <T> T getTyped(String str, Class<T> cls, T t2) {
        if (!hasField(str)) {
            return t2;
        }
        T t3 = (T) getValue(str);
        if (t3 == null || t3.getClass().isAssignableFrom(cls)) {
            return t3;
        }
        return t2;
    }

    public boolean hasField(String str) {
        RecordedObject recordedObject;
        Objects.requireNonNull(str);
        Iterator<ValueDescriptor> it = this.descriptors.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equals(str)) {
                return true;
            }
        }
        int iIndexOf = str.indexOf(".");
        if (iIndexOf > 0) {
            String strSubstring = str.substring(0, iIndexOf);
            for (ValueDescriptor valueDescriptor : this.descriptors) {
                if (!valueDescriptor.getFields().isEmpty() && valueDescriptor.getName().equals(strSubstring) && (recordedObject = (RecordedObject) getValue(strSubstring)) != null) {
                    return recordedObject.hasField(str.substring(iIndexOf + 1));
                }
            }
            return false;
        }
        return false;
    }

    public final <T> T getValue(String str) {
        return (T) getValue(str, false);
    }

    private Object getValue(String str, boolean z2) {
        Objects.requireNonNull(str);
        int i2 = 0;
        for (ValueDescriptor valueDescriptor : this.descriptors) {
            if (str.equals(valueDescriptor.getName())) {
                Object obj = this.objects[i2];
                if (obj == null) {
                    return null;
                }
                if (valueDescriptor.getFields().isEmpty()) {
                    if (z2 && PrivateAccess.getInstance().isUnsigned(valueDescriptor)) {
                        if ((obj instanceof Character) || (obj instanceof Long)) {
                            return obj;
                        }
                        return new UnsignedValue(obj);
                    }
                    return obj;
                }
                if (obj instanceof RecordedObject) {
                    return obj;
                }
                Object[] objArr = (Object[]) obj;
                if (valueDescriptor.isArray()) {
                    return structifyArray(valueDescriptor, objArr, 0);
                }
                return new RecordedObject(valueDescriptor.getFields(), (Object[]) obj, this.timeConverter);
            }
            i2++;
        }
        int iIndexOf = str.indexOf(".");
        if (iIndexOf > 0) {
            String strSubstring = str.substring(0, iIndexOf);
            for (ValueDescriptor valueDescriptor2 : this.descriptors) {
                if (!valueDescriptor2.getFields().isEmpty() && valueDescriptor2.getName().equals(strSubstring)) {
                    RecordedObject recordedObject = (RecordedObject) getValue(strSubstring);
                    String strSubstring2 = str.substring(iIndexOf + 1);
                    if (recordedObject != null) {
                        return recordedObject.getValue(strSubstring2, z2);
                    }
                    getValueDescriptor(valueDescriptor2.getFields(), strSubstring2, null);
                    throw new NullPointerException("Field value for \"" + strSubstring + "\" was null. Can't access nested field \"" + strSubstring2 + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
            }
        }
        throw new IllegalArgumentException("Could not find field with name " + str);
    }

    private ValueDescriptor getValueDescriptor(List<ValueDescriptor> list, String str, String str2) {
        int iIndexOf = str.indexOf(".");
        if (iIndexOf > 0) {
            String strSubstring = str.substring(0, iIndexOf);
            String strSubstring2 = str.substring(iIndexOf + 1);
            for (ValueDescriptor valueDescriptor : list) {
                if (valueDescriptor.getName().equals(strSubstring) && !valueDescriptor.getFields().isEmpty()) {
                    return getValueDescriptor(valueDescriptor.getFields(), strSubstring2, str2);
                }
            }
            throw new IllegalArgumentException("Attempt to get unknown field \"" + strSubstring + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        for (ValueDescriptor valueDescriptor2 : list) {
            if (valueDescriptor2.getName().equals(str)) {
                if (str2 != null && !valueDescriptor2.getTypeName().equals(str2)) {
                    throw new IllegalArgumentException("Attempt to get " + valueDescriptor2.getTypeName() + " field \"" + str + "\" with illegal data type conversion " + str2);
                }
                return valueDescriptor2;
            }
        }
        throw new IllegalArgumentException("\"Attempt to get unknown field \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    private <T> T getTypedValue(String str, String str2) {
        Objects.requireNonNull(str);
        getValueDescriptor(this.descriptors, str, str2);
        return (T) getValue(str);
    }

    private Object[] structifyArray(ValueDescriptor valueDescriptor, Object[] objArr, int i2) {
        if (objArr == null) {
            return null;
        }
        Object[] objArr2 = new Object[objArr.length];
        for (int i3 = 0; i3 < objArr2.length; i3++) {
            Object obj = objArr[i3];
            if (i2 == 0) {
                if (isStackFrameType(valueDescriptor.getTypeName())) {
                    objArr2[i3] = new RecordedFrame(valueDescriptor.getFields(), (Object[]) obj, this.timeConverter);
                } else {
                    objArr2[i3] = new RecordedObject(valueDescriptor.getFields(), (Object[]) obj, this.timeConverter);
                }
            } else {
                objArr2[i3] = structifyArray(valueDescriptor, (Object[]) obj, i2 - 1);
            }
        }
        return objArr2;
    }

    private boolean isStackFrameType(String str) {
        if ("com.oracle.jfr.types.StackFrame".equals(str) || "jdk.types.StackFrame".equals(str)) {
            return true;
        }
        return false;
    }

    public List<ValueDescriptor> getFields() {
        return this.descriptors;
    }

    public final boolean getBoolean(String str) {
        Object value = getValue(str);
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        throw newIllegalArgumentException(str, "boolean");
    }

    public final byte getByte(String str) {
        Object value = getValue(str);
        if (value instanceof Byte) {
            return ((Byte) value).byteValue();
        }
        throw newIllegalArgumentException(str, SchemaSymbols.ATTVAL_BYTE);
    }

    public final char getChar(String str) {
        Object value = getValue(str);
        if (value instanceof Character) {
            return ((Character) value).charValue();
        }
        throw newIllegalArgumentException(str, "char");
    }

    public final short getShort(String str) {
        Object value = getValue(str, true);
        if (value instanceof Short) {
            return ((Short) value).shortValue();
        }
        if (value instanceof Byte) {
            return ((Byte) value).byteValue();
        }
        if (value instanceof UnsignedValue) {
            Object objValue = ((UnsignedValue) value).value();
            if (objValue instanceof Short) {
                return ((Short) objValue).shortValue();
            }
            if (objValue instanceof Byte) {
                return (short) Byte.toUnsignedInt(((Byte) objValue).byteValue());
            }
        }
        throw newIllegalArgumentException(str, SchemaSymbols.ATTVAL_SHORT);
    }

    public final int getInt(String str) {
        Object value = getValue(str, true);
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        if (value instanceof Short) {
            return ((Short) value).intValue();
        }
        if (value instanceof Character) {
            return ((Character) value).charValue();
        }
        if (value instanceof Byte) {
            return ((Byte) value).intValue();
        }
        if (value instanceof UnsignedValue) {
            Object objValue = ((UnsignedValue) value).value();
            if (objValue instanceof Integer) {
                return ((Integer) objValue).intValue();
            }
            if (objValue instanceof Short) {
                return Short.toUnsignedInt(((Short) objValue).shortValue());
            }
            if (objValue instanceof Byte) {
                return Byte.toUnsignedInt(((Byte) objValue).byteValue());
            }
        }
        throw newIllegalArgumentException(str, "int");
    }

    public final float getFloat(String str) {
        Object value = getValue(str);
        if (value instanceof Float) {
            return ((Float) value).floatValue();
        }
        if (value instanceof Long) {
            return ((Long) value).floatValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).floatValue();
        }
        if (value instanceof Short) {
            return ((Short) value).floatValue();
        }
        if (value instanceof Byte) {
            return ((Byte) value).byteValue();
        }
        if (value instanceof Character) {
            return ((Character) value).charValue();
        }
        throw newIllegalArgumentException(str, SchemaSymbols.ATTVAL_FLOAT);
    }

    public final long getLong(String str) {
        Object value = getValue(str, true);
        if (value instanceof Long) {
            return ((Long) value).longValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        if (value instanceof Short) {
            return ((Short) value).longValue();
        }
        if (value instanceof Character) {
            return ((Character) value).charValue();
        }
        if (value instanceof Byte) {
            return ((Byte) value).longValue();
        }
        if (value instanceof UnsignedValue) {
            Object objValue = ((UnsignedValue) value).value();
            if (objValue instanceof Integer) {
                return Integer.toUnsignedLong(((Integer) objValue).intValue());
            }
            if (objValue instanceof Short) {
                return Short.toUnsignedLong(((Short) objValue).shortValue());
            }
            if (objValue instanceof Byte) {
                return Byte.toUnsignedLong(((Byte) objValue).byteValue());
            }
        }
        throw newIllegalArgumentException(str, SchemaSymbols.ATTVAL_LONG);
    }

    public final double getDouble(String str) {
        Object value = getValue(str);
        if (value instanceof Double) {
            return ((Double) value).doubleValue();
        }
        if (value instanceof Float) {
            return ((Float) value).doubleValue();
        }
        if (value instanceof Long) {
            return ((Long) value).doubleValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        }
        if (value instanceof Short) {
            return ((Short) value).doubleValue();
        }
        if (value instanceof Byte) {
            return ((Byte) value).byteValue();
        }
        if (value instanceof Character) {
            return ((Character) value).charValue();
        }
        throw newIllegalArgumentException(str, SchemaSymbols.ATTVAL_DOUBLE);
    }

    public final String getString(String str) {
        return (String) getTypedValue(str, "java.lang.String");
    }

    public final Duration getDuration(String str) {
        Object value = getValue(str);
        if (value instanceof Long) {
            return getDuration(((Long) value).longValue(), str);
        }
        if (value instanceof Integer) {
            return getDuration(((Integer) value).longValue(), str);
        }
        if (value instanceof Short) {
            return getDuration(((Short) value).longValue(), str);
        }
        if (value instanceof Character) {
            return getDuration(((Character) value).charValue(), str);
        }
        if (value instanceof Byte) {
            return getDuration(((Byte) value).longValue(), str);
        }
        if (value instanceof UnsignedValue) {
            Object objValue = ((UnsignedValue) value).value();
            if (objValue instanceof Integer) {
                return getDuration(Integer.toUnsignedLong(((Integer) objValue).intValue()), str);
            }
            if (objValue instanceof Short) {
                return getDuration(Short.toUnsignedLong(((Short) objValue).shortValue()), str);
            }
            if (objValue instanceof Byte) {
                return getDuration(Short.toUnsignedLong(((Byte) objValue).byteValue()), str);
            }
        }
        throw newIllegalArgumentException(str, "java,time.Duration");
    }

    private Duration getDuration(long j2, String str) throws InternalError {
        ValueDescriptor valueDescriptor = getValueDescriptor(this.descriptors, str, null);
        if (j2 == Long.MIN_VALUE) {
            return Duration.ofSeconds(Long.MIN_VALUE, 0L);
        }
        Timespan timespan = (Timespan) valueDescriptor.getAnnotation(Timespan.class);
        if (timespan != null) {
            switch (timespan.value()) {
                case "MICROSECONDS":
                    return Duration.ofNanos(1000 * j2);
                case "SECONDS":
                    return Duration.ofSeconds(j2);
                case "MILLISECONDS":
                    return Duration.ofMillis(j2);
                case "NANOSECONDS":
                    return Duration.ofNanos(j2);
                case "TICKS":
                    return Duration.ofNanos(this.timeConverter.convertTimespan(j2));
                default:
                    throw new IllegalArgumentException("Attempt to get " + valueDescriptor.getTypeName() + " field \"" + str + "\" with illegal timespan unit " + timespan.value());
            }
        }
        throw new IllegalArgumentException("Attempt to get " + valueDescriptor.getTypeName() + " field \"" + str + "\" with missing @Timespan");
    }

    public final Instant getInstant(String str) {
        Object value = getValue(str, true);
        if (value instanceof Long) {
            return getInstant(((Long) value).longValue(), str);
        }
        if (value instanceof Integer) {
            return getInstant(((Integer) value).longValue(), str);
        }
        if (value instanceof Short) {
            return getInstant(((Short) value).longValue(), str);
        }
        if (value instanceof Character) {
            return getInstant(((Character) value).charValue(), str);
        }
        if (value instanceof Byte) {
            return getInstant(((Byte) value).longValue(), str);
        }
        if (value instanceof UnsignedValue) {
            Object objValue = ((UnsignedValue) value).value();
            if (objValue instanceof Integer) {
                return getInstant(Integer.toUnsignedLong(((Integer) objValue).intValue()), str);
            }
            if (objValue instanceof Short) {
                return getInstant(Short.toUnsignedLong(((Short) objValue).shortValue()), str);
            }
            if (objValue instanceof Byte) {
                return getInstant(Short.toUnsignedLong(((Byte) objValue).byteValue()), str);
            }
        }
        throw newIllegalArgumentException(str, "java.time.Instant");
    }

    private Instant getInstant(long j2, String str) {
        ValueDescriptor valueDescriptor = getValueDescriptor(this.descriptors, str, null);
        Timestamp timestamp = (Timestamp) valueDescriptor.getAnnotation(Timestamp.class);
        if (timestamp == null) {
            throw new IllegalArgumentException("Attempt to get " + valueDescriptor.getTypeName() + " field \"" + str + "\" with missing @Timestamp");
        }
        if (j2 == Long.MIN_VALUE) {
            return Instant.MIN;
        }
        switch (timestamp.value()) {
            case "MILLISECONDS_SINCE_EPOCH":
                return Instant.ofEpochMilli(j2);
            case "TICKS":
                return Instant.ofEpochSecond(0L, this.timeConverter.convertTimestamp(j2));
            default:
                throw new IllegalArgumentException("Attempt to get " + valueDescriptor.getTypeName() + " field \"" + str + "\" with illegal timestamp unit " + timestamp.value());
        }
    }

    public final RecordedClass getClass(String str) {
        return (RecordedClass) getTypedValue(str, "java.lang.Class");
    }

    public final RecordedThread getThread(String str) {
        return (RecordedThread) getTypedValue(str, "java.lang.Thread");
    }

    public final String toString() {
        StringWriter stringWriter = new StringWriter();
        PrettyWriter prettyWriter = new PrettyWriter(new PrintWriter(stringWriter));
        prettyWriter.setStackDepth(5);
        if (this instanceof RecordedEvent) {
            prettyWriter.print((RecordedEvent) this);
        } else {
            prettyWriter.print(this, "");
        }
        prettyWriter.flush(true);
        return stringWriter.toString();
    }

    OffsetDateTime getOffsetDateTime(String str) {
        if (getInstant(str).equals(Instant.MIN)) {
            return OffsetDateTime.MIN;
        }
        return OffsetDateTime.ofInstant(getInstant(str), this.timeConverter.getZoneOffset());
    }

    private static IllegalArgumentException newIllegalArgumentException(String str, String str2) {
        return new IllegalArgumentException("Attempt to get field \"" + str + "\" with illegal data type conversion " + str2);
    }
}
