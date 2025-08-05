package javax.lang.model.element;

/* loaded from: rt.jar:javax/lang/model/element/Name.class */
public interface Name extends CharSequence {
    boolean equals(Object obj);

    int hashCode();

    boolean contentEquals(CharSequence charSequence);
}
