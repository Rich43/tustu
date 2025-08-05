package jdk.jfr;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jdk.jfr.internal.AnnotationConstruct;
import jdk.jfr.internal.Type;

/* loaded from: jfr.jar:jdk/jfr/SettingDescriptor.class */
public final class SettingDescriptor {
    private final AnnotationConstruct annotationConstruct;
    private final Type type;
    private final String name;
    private final String defaultValue;

    SettingDescriptor(Type type, String str, String str2, List<AnnotationElement> list) {
        Objects.requireNonNull(list);
        this.name = (String) Objects.requireNonNull(str, "Name of value descriptor can't be null");
        this.type = (Type) Objects.requireNonNull(type);
        this.annotationConstruct = new AnnotationConstruct(list);
        this.defaultValue = (String) Objects.requireNonNull(str2);
    }

    void setAnnotations(List<AnnotationElement> list) {
        this.annotationConstruct.setAnnotationElements(list);
    }

    public String getName() {
        return this.name;
    }

    public String getLabel() {
        String label = this.annotationConstruct.getLabel();
        if (label == null) {
            label = this.type.getLabel();
        }
        return label;
    }

    public String getDescription() {
        String description = this.annotationConstruct.getDescription();
        if (description == null) {
            description = this.type.getDescription();
        }
        return description;
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
        for (AnnotationElement annotationElement2 : this.type.getAnnotationElements()) {
            Iterator<AnnotationElement> it2 = annotationElement2.getAnnotationElements().iterator();
            while (it2.hasNext()) {
                if (it2.next().getTypeName().equals(ContentType.class.getName())) {
                    return annotationElement2.getTypeName();
                }
            }
        }
        return null;
    }

    public String getTypeName() {
        return this.type.getName();
    }

    public long getTypeId() {
        return this.type.getId();
    }

    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        Objects.requireNonNull(cls);
        return (A) this.annotationConstruct.getAnnotation(cls);
    }

    public List<AnnotationElement> getAnnotationElements() {
        return Collections.unmodifiableList(this.annotationConstruct.getUnmodifiableAnnotationElements());
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    Type getType() {
        return this.type;
    }
}
