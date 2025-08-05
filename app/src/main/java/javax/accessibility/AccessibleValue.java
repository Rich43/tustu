package javax.accessibility;

/* loaded from: rt.jar:javax/accessibility/AccessibleValue.class */
public interface AccessibleValue {
    Number getCurrentAccessibleValue();

    boolean setCurrentAccessibleValue(Number number);

    Number getMinimumAccessibleValue();

    Number getMaximumAccessibleValue();
}
