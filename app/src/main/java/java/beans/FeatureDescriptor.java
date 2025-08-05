package java.beans;

import com.sun.beans.TypeResolver;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:java/beans/FeatureDescriptor.class */
public class FeatureDescriptor {
    private static final String TRANSIENT = "transient";
    private Reference<? extends Class<?>> classRef;
    private boolean expert;
    private boolean hidden;
    private boolean preferred;
    private String shortDescription;
    private String name;
    private String displayName;
    private Hashtable<String, Object> table;

    public FeatureDescriptor() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getDisplayName() {
        if (this.displayName == null) {
            return getName();
        }
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public boolean isExpert() {
        return this.expert;
    }

    public void setExpert(boolean z2) {
        this.expert = z2;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean z2) {
        this.hidden = z2;
    }

    public boolean isPreferred() {
        return this.preferred;
    }

    public void setPreferred(boolean z2) {
        this.preferred = z2;
    }

    public String getShortDescription() {
        if (this.shortDescription == null) {
            return getDisplayName();
        }
        return this.shortDescription;
    }

    public void setShortDescription(String str) {
        this.shortDescription = str;
    }

    public void setValue(String str, Object obj) {
        getTable().put(str, obj);
    }

    public Object getValue(String str) {
        if (this.table != null) {
            return this.table.get(str);
        }
        return null;
    }

    public Enumeration<String> attributeNames() {
        return getTable().keys();
    }

    FeatureDescriptor(FeatureDescriptor featureDescriptor, FeatureDescriptor featureDescriptor2) {
        this.expert = featureDescriptor.expert | featureDescriptor2.expert;
        this.hidden = featureDescriptor.hidden | featureDescriptor2.hidden;
        this.preferred = featureDescriptor.preferred | featureDescriptor2.preferred;
        this.name = featureDescriptor2.name;
        this.shortDescription = featureDescriptor.shortDescription;
        if (featureDescriptor2.shortDescription != null) {
            this.shortDescription = featureDescriptor2.shortDescription;
        }
        this.displayName = featureDescriptor.displayName;
        if (featureDescriptor2.displayName != null) {
            this.displayName = featureDescriptor2.displayName;
        }
        this.classRef = featureDescriptor.classRef;
        if (featureDescriptor2.classRef != null) {
            this.classRef = featureDescriptor2.classRef;
        }
        addTable(featureDescriptor.table);
        addTable(featureDescriptor2.table);
    }

    FeatureDescriptor(FeatureDescriptor featureDescriptor) {
        this.expert = featureDescriptor.expert;
        this.hidden = featureDescriptor.hidden;
        this.preferred = featureDescriptor.preferred;
        this.name = featureDescriptor.name;
        this.shortDescription = featureDescriptor.shortDescription;
        this.displayName = featureDescriptor.displayName;
        this.classRef = featureDescriptor.classRef;
        addTable(featureDescriptor.table);
    }

    private void addTable(Hashtable<String, Object> hashtable) {
        if (hashtable != null && !hashtable.isEmpty()) {
            getTable().putAll(hashtable);
        }
    }

    private Hashtable<String, Object> getTable() {
        if (this.table == null) {
            this.table = new Hashtable<>();
        }
        return this.table;
    }

    void setTransient(Transient r5) {
        if (r5 != null && null == getValue(TRANSIENT)) {
            setValue(TRANSIENT, Boolean.valueOf(r5.value()));
        }
    }

    boolean isTransient() {
        Object value = getValue(TRANSIENT);
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return false;
    }

    void setClass0(Class<?> cls) {
        this.classRef = getWeakReference(cls);
    }

    Class<?> getClass0() {
        if (this.classRef != null) {
            return this.classRef.get();
        }
        return null;
    }

    static <T> Reference<T> getSoftReference(T t2) {
        if (t2 != null) {
            return new SoftReference(t2);
        }
        return null;
    }

    static <T> Reference<T> getWeakReference(T t2) {
        if (t2 != null) {
            return new WeakReference(t2);
        }
        return null;
    }

    static Class<?> getReturnType(Class<?> cls, Method method) {
        if (cls == null) {
            cls = method.getDeclaringClass();
        }
        return TypeResolver.erase(TypeResolver.resolveInClass(cls, method.getGenericReturnType()));
    }

    static Class<?>[] getParameterTypes(Class<?> cls, Method method) {
        if (cls == null) {
            cls = method.getDeclaringClass();
        }
        return TypeResolver.erase(TypeResolver.resolveInClass(cls, method.getGenericParameterTypes()));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append("[name=").append(this.name);
        appendTo(sb, "displayName", this.displayName);
        appendTo(sb, "shortDescription", this.shortDescription);
        appendTo(sb, "preferred", this.preferred);
        appendTo(sb, "hidden", this.hidden);
        appendTo(sb, "expert", this.expert);
        if (this.table != null && !this.table.isEmpty()) {
            sb.append("; values={");
            for (Map.Entry<String, Object> entry : this.table.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append(VectorFormat.DEFAULT_SEPARATOR);
            }
            sb.setLength(sb.length() - 2);
            sb.append("}");
        }
        appendTo(sb);
        return sb.append("]").toString();
    }

    void appendTo(StringBuilder sb) {
    }

    static void appendTo(StringBuilder sb, String str, Reference<?> reference) {
        if (reference != null) {
            appendTo(sb, str, reference.get());
        }
    }

    static void appendTo(StringBuilder sb, String str, Object obj) {
        if (obj != null) {
            sb.append(VectorFormat.DEFAULT_SEPARATOR).append(str).append("=").append(obj);
        }
    }

    static void appendTo(StringBuilder sb, String str, boolean z2) {
        if (z2) {
            sb.append(VectorFormat.DEFAULT_SEPARATOR).append(str);
        }
    }
}
