package jdk.jfr;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jdk.jfr.internal.AnnotationConstruct;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;

/* loaded from: jfr.jar:jdk/jfr/ValueDescriptor.class */
public final class ValueDescriptor {
    private final AnnotationConstruct annotationConstruct;
    private final Type type;
    private final String name;
    private final boolean isArray;
    private final boolean constantPool;
    private final String javaFieldName;

    ValueDescriptor(Type type, String str, List<AnnotationElement> list, int i2, boolean z2, String str2) {
        Objects.requireNonNull(list);
        if (i2 < 0) {
            throw new IllegalArgumentException("Dimension must be positive");
        }
        this.name = (String) Objects.requireNonNull(str, "Name of value descriptor can't be null");
        this.type = (Type) Objects.requireNonNull(type);
        this.isArray = i2 > 0;
        this.constantPool = z2;
        this.annotationConstruct = new AnnotationConstruct(list);
        this.javaFieldName = str2;
    }

    public ValueDescriptor(Class<?> cls, String str) {
        this(cls, str, Collections.emptyList());
    }

    public ValueDescriptor(Class<?> cls, String str, List<AnnotationElement> list) {
        this(cls, str, new ArrayList(list), false);
    }

    ValueDescriptor(Class<?> cls, String str, List<AnnotationElement> list, boolean z2) throws SecurityException {
        Objects.requireNonNull(list);
        Utils.checkRegisterPermission();
        if (!z2 && cls.isArray()) {
            throw new IllegalArgumentException("Array types are not allowed");
        }
        this.name = (String) Objects.requireNonNull(str, "Name of value descriptor can't be null");
        this.type = (Type) Objects.requireNonNull(Utils.getValidType((Class) Objects.requireNonNull(cls), (String) Objects.requireNonNull(str)));
        this.annotationConstruct = new AnnotationConstruct(list);
        this.javaFieldName = str;
        this.isArray = cls.isArray();
        this.constantPool = cls == Class.class || cls == Thread.class;
    }

    public String getLabel() {
        return this.annotationConstruct.getLabel();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.annotationConstruct.getDescription();
    }

    public String getContentType() {
        for (AnnotationElement annotationElement : getAnnotationElements()) {
            Iterator<AnnotationElement> it = annotationElement.getAnnotationElements().iterator();
            while (it.hasNext()) {
                if (it.next().getTypeName().equals(ContentType.class.getName())) {
                    return annotationElement.getTypeName();
                }
            }
        }
        return null;
    }

    public String getTypeName() {
        if (this.type.isSimpleType()) {
            return this.type.getFields().get(0).getTypeName();
        }
        return this.type.getName();
    }

    public long getTypeId() {
        return this.type.getId();
    }

    public boolean isArray() {
        return this.isArray;
    }

    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        Objects.requireNonNull(cls);
        return (A) this.annotationConstruct.getAnnotation(cls);
    }

    public List<AnnotationElement> getAnnotationElements() {
        return this.annotationConstruct.getUnmodifiableAnnotationElements();
    }

    public List<ValueDescriptor> getFields() {
        if (this.type.isSimpleType()) {
            return Collections.emptyList();
        }
        return this.type.getFields();
    }

    Type getType() {
        return this.type;
    }

    void setAnnotations(List<AnnotationElement> list) {
        this.annotationConstruct.setAnnotationElements(list);
    }

    boolean isConstantPool() {
        return this.constantPool;
    }

    String getJavaFieldName() {
        return this.javaFieldName;
    }

    boolean isUnsigned() {
        return this.annotationConstruct.hasUnsigned();
    }
}
