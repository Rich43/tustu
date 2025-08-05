package javax.management.openmbean;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.JMX;

/* loaded from: rt.jar:javax/management/openmbean/OpenType.class */
public abstract class OpenType<T> implements Serializable {
    static final long serialVersionUID = -9195195325186646468L;
    public static final List<String> ALLOWED_CLASSNAMES_LIST = Collections.unmodifiableList(Arrays.asList("java.lang.Void", Constants.BOOLEAN_CLASS, "java.lang.Character", "java.lang.Byte", "java.lang.Short", Constants.INTEGER_CLASS, "java.lang.Long", "java.lang.Float", Constants.DOUBLE_CLASS, "java.lang.String", "java.math.BigDecimal", "java.math.BigInteger", "java.util.Date", "javax.management.ObjectName", CompositeData.class.getName(), TabularData.class.getName()));

    @Deprecated
    public static final String[] ALLOWED_CLASSNAMES = (String[]) ALLOWED_CLASSNAMES_LIST.toArray(new String[0]);
    private String className;
    private String description;
    private String typeName;
    private transient boolean isArray;
    private transient Descriptor descriptor;

    public abstract boolean isValue(Object obj);

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract String toString();

    protected OpenType(String str, String str2, String str3) throws OpenDataException, SecurityException {
        this.isArray = false;
        checkClassNameOverride();
        this.typeName = valid("typeName", str2);
        this.description = valid("description", str3);
        this.className = validClassName(str);
        this.isArray = this.className != null && this.className.startsWith("[");
    }

    OpenType(String str, String str2, String str3, boolean z2) {
        this.isArray = false;
        this.className = valid("className", str);
        this.typeName = valid("typeName", str2);
        this.description = valid("description", str3);
        this.isArray = z2;
    }

    private void checkClassNameOverride() throws SecurityException {
        if (getClass().getClassLoader() != null && overridesGetClassName(getClass()) && AccessController.doPrivileged(new GetPropertyAction("jmx.extend.open.types")) == null) {
            throw new SecurityException("Cannot override getClassName() unless -Djmx.extend.open.types");
        }
    }

    private static boolean overridesGetClassName(final Class<?> cls) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: javax.management.openmbean.OpenType.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() {
                try {
                    return Boolean.valueOf(cls.getMethod("getClassName", new Class[0]).getDeclaringClass() != OpenType.class);
                } catch (Exception e2) {
                    return true;
                }
            }
        })).booleanValue();
    }

    private static String validClassName(String str) throws OpenDataException {
        String strSubstring;
        boolean zContains;
        String strValid = valid("className", str);
        int i2 = 0;
        while (strValid.startsWith("[", i2)) {
            i2++;
        }
        boolean z2 = false;
        if (i2 > 0) {
            if (strValid.startsWith("L", i2) && strValid.endsWith(";")) {
                strSubstring = strValid.substring(i2 + 1, strValid.length() - 1);
            } else if (i2 == strValid.length() - 1) {
                strSubstring = strValid.substring(i2, strValid.length());
                z2 = true;
            } else {
                throw new OpenDataException("Argument className=\"" + strValid + "\" is not a valid class name");
            }
        } else {
            strSubstring = strValid;
        }
        if (z2) {
            zContains = ArrayType.isPrimitiveContentType(strSubstring);
        } else {
            zContains = ALLOWED_CLASSNAMES_LIST.contains(strSubstring);
        }
        if (!zContains) {
            throw new OpenDataException("Argument className=\"" + strValid + "\" is not one of the allowed Java class names for open data.");
        }
        return strValid;
    }

    private static String valid(String str, String str2) {
        if (str2 != null) {
            String strTrim = str2.trim();
            if (!strTrim.equals("")) {
                return strTrim;
            }
        }
        throw new IllegalArgumentException("Argument " + str + " cannot be null or empty");
    }

    synchronized Descriptor getDescriptor() {
        if (this.descriptor == null) {
            this.descriptor = new ImmutableDescriptor(new String[]{JMX.OPEN_TYPE_FIELD}, new Object[]{this});
        }
        return this.descriptor;
    }

    public String getClassName() {
        return this.className;
    }

    String safeGetClassName() {
        return this.className;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isArray() {
        return this.isArray;
    }

    boolean isAssignableFrom(OpenType<?> openType) {
        return equals(openType);
    }

    private void readObject(ObjectInputStream objectInputStream) throws SecurityException, IOException, ClassNotFoundException {
        checkClassNameOverride();
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        try {
            String strValidClassName = validClassName((String) fields.get("className", (Object) null));
            String strValid = valid("description", (String) fields.get("description", (Object) null));
            String strValid2 = valid("typeName", (String) fields.get("typeName", (Object) null));
            this.className = strValidClassName;
            this.description = strValid;
            this.typeName = strValid2;
            this.isArray = this.className.startsWith("[");
        } catch (Exception e2) {
            InvalidObjectException invalidObjectException = new InvalidObjectException(e2.getMessage());
            invalidObjectException.initCause(e2);
            throw invalidObjectException;
        }
    }
}
