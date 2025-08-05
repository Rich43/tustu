package java.lang.annotation;

/* loaded from: rt.jar:java/lang/annotation/Annotation.class */
public interface Annotation {
    boolean equals(Object obj);

    int hashCode();

    String toString();

    Class<? extends Annotation> annotationType();
}
