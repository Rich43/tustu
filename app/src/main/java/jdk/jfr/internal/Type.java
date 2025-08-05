package jdk.jfr.internal;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jdk.jfr.AnnotationElement;
import jdk.jfr.Event;
import jdk.jfr.SettingControl;
import jdk.jfr.ValueDescriptor;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: jfr.jar:jdk/jfr/internal/Type.class */
public class Type implements Comparable<Type> {
    public static final String EVENT_NAME_PREFIX = "jdk.";
    public static final String TYPES_PREFIX = "jdk.types.";
    public static final String SETTINGS_PREFIX = "jdk.settings.";
    private final AnnotationConstruct annos;
    private final String name;
    private final String superType;
    private final boolean constantPool;
    private final long id;
    private List<ValueDescriptor> fields;
    private Boolean simpleType;
    private boolean remove;
    public static final String SUPER_TYPE_ANNOTATION = Annotation.class.getName();
    public static final String SUPER_TYPE_SETTING = SettingControl.class.getName();
    public static final String SUPER_TYPE_EVENT = Event.class.getName();
    private static final Map<Type, Class<?>> knownTypes = new HashMap();
    static final Type BOOLEAN = register(Boolean.TYPE, new Type("boolean", null, 4));
    static final Type CHAR = register(Character.TYPE, new Type("char", null, 5));
    static final Type FLOAT = register(Float.TYPE, new Type(SchemaSymbols.ATTVAL_FLOAT, null, 6));
    static final Type DOUBLE = register(Double.TYPE, new Type(SchemaSymbols.ATTVAL_DOUBLE, null, 7));
    static final Type BYTE = register(Byte.TYPE, new Type(SchemaSymbols.ATTVAL_BYTE, null, 8));
    static final Type SHORT = register(Short.TYPE, new Type(SchemaSymbols.ATTVAL_SHORT, null, 9));
    static final Type INT = register(Integer.TYPE, new Type("int", null, 10));
    static final Type LONG = register(Long.TYPE, new Type(SchemaSymbols.ATTVAL_LONG, null, 11));
    static final Type CLASS = register(Class.class, new Type("java.lang.Class", null, 20));
    static final Type STRING = register(String.class, new Type("java.lang.String", null, 21));
    static final Type THREAD = register(Thread.class, new Type("java.lang.Thread", null, 22));
    static final Type STACK_TRACE = register(null, new Type("jdk.types.StackTrace", null, 23));

    public Type(String str, String str2, long j2) {
        this(str, str2, j2, false);
    }

    Type(String str, String str2, long j2, boolean z2) {
        this(str, str2, j2, z2, null);
    }

    Type(String str, String str2, long j2, boolean z2, Boolean bool) {
        this.annos = new AnnotationConstruct();
        this.fields = new ArrayList();
        this.remove = true;
        Objects.requireNonNull(str);
        if (!isValidJavaIdentifier(str)) {
            throw new IllegalArgumentException(str + " is not a valid Java identifier");
        }
        this.constantPool = z2;
        this.superType = str2;
        this.name = str;
        this.id = j2;
        this.simpleType = bool;
    }

    static boolean isDefinedByJVM(long j2) {
        return j2 < 400;
    }

    public static long getTypeId(Class<?> cls) {
        Type knownType = getKnownType(cls);
        return knownType == null ? JVM.getJVM().getTypeId(cls) : knownType.getId();
    }

    static Collection<Type> getKnownTypes() {
        return knownTypes.keySet();
    }

    public static boolean isValidJavaIdentifier(String str) {
        if (str.isEmpty() || !Character.isJavaIdentifierStart(str.charAt(0))) {
            return false;
        }
        for (int i2 = 1; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt != '.' && !Character.isJavaIdentifierPart(cCharAt)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidJavaFieldType(String str) {
        Iterator<Map.Entry<Type, Class<?>>> it = knownTypes.entrySet().iterator();
        while (it.hasNext()) {
            Class<?> value = it.next().getValue();
            if (value != null && str.equals(value.getName())) {
                return true;
            }
        }
        return false;
    }

    public static Type getKnownType(String str) {
        for (Type type : knownTypes.keySet()) {
            if (type.getName().equals(str)) {
                return type;
            }
        }
        return null;
    }

    static boolean isKnownType(Class<?> cls) {
        if (cls.isPrimitive() || cls.equals(Class.class) || cls.equals(Thread.class) || cls.equals(String.class)) {
            return true;
        }
        return false;
    }

    public static Type getKnownType(Class<?> cls) {
        for (Map.Entry<Type, Class<?>> entry : knownTypes.entrySet()) {
            if (cls != null && cls.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getLogName() {
        return getName() + "(" + getId() + ")";
    }

    public List<ValueDescriptor> getFields() {
        if (this.fields instanceof ArrayList) {
            ((ArrayList) this.fields).trimToSize();
            this.fields = Collections.unmodifiableList(this.fields);
        }
        return this.fields;
    }

    public boolean isSimpleType() {
        if (this.simpleType == null) {
            this.simpleType = Boolean.valueOf(calculateSimpleType());
        }
        return this.simpleType.booleanValue();
    }

    private boolean calculateSimpleType() {
        return this.fields.size() == 1 && this.superType == null;
    }

    public boolean isDefinedByJVM() {
        return this.id < 400;
    }

    private static Type register(Class<?> cls, Type type) {
        knownTypes.put(type, cls);
        return type;
    }

    public void add(ValueDescriptor valueDescriptor) {
        Objects.requireNonNull(valueDescriptor);
        this.fields.add(valueDescriptor);
    }

    void trimFields() {
        getFields();
    }

    void setAnnotations(List<AnnotationElement> list) {
        this.annos.setAnnotationElements(list);
    }

    public String getSuperType() {
        return this.superType;
    }

    public long getId() {
        return this.id;
    }

    public boolean isConstantPool() {
        return this.constantPool;
    }

    public String getLabel() {
        return this.annos.getLabel();
    }

    public List<AnnotationElement> getAnnotationElements() {
        return this.annos.getUnmodifiableAnnotationElements();
    }

    public <T> T getAnnotation(Class<? extends Annotation> cls) {
        return (T) this.annos.getAnnotation(cls);
    }

    public String getDescription() {
        return this.annos.getDescription();
    }

    public int hashCode() {
        return Long.hashCode(this.id);
    }

    public boolean equals(Object obj) {
        return (obj instanceof Type) && ((Type) obj).id == this.id;
    }

    @Override // java.lang.Comparable
    public int compareTo(Type type) {
        return Long.compare(this.id, type.id);
    }

    void log(String str, LogTag logTag, LogLevel logLevel) {
        if (Logger.shouldLog(logTag, logLevel) && !isSimpleType()) {
            Logger.log(logTag, LogLevel.TRACE, str + " " + typeText() + " " + getLogName() + " {");
            for (ValueDescriptor valueDescriptor : getFields()) {
                Logger.log(logTag, LogLevel.TRACE, Constants.INDENT + valueDescriptor.getTypeName() + (valueDescriptor.isArray() ? "[]" : "") + " " + valueDescriptor.getName() + ";");
            }
            Logger.log(logTag, LogLevel.TRACE, "}");
            return;
        }
        if (Logger.shouldLog(logTag, LogLevel.INFO) && !isSimpleType()) {
            Logger.log(logTag, LogLevel.INFO, str + " " + typeText() + " " + getLogName());
        }
    }

    private String typeText() {
        if (this instanceof PlatformEventType) {
            return "event type";
        }
        if (SUPER_TYPE_SETTING.equals(this.superType)) {
            return "setting type";
        }
        if (SUPER_TYPE_ANNOTATION.equals(this.superType)) {
            return "annotation type";
        }
        return "type";
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getLogName());
        if (!getFields().isEmpty()) {
            sb.append(" {\n");
            for (ValueDescriptor valueDescriptor : getFields()) {
                sb.append("  type=" + valueDescriptor.getTypeName() + "(" + valueDescriptor.getTypeId() + ") name=" + valueDescriptor.getName() + "\n");
            }
            sb.append("}\n");
        }
        return sb.toString();
    }

    public void setRemove(boolean z2) {
        this.remove = z2;
    }

    public boolean getRemove() {
        return this.remove;
    }
}
