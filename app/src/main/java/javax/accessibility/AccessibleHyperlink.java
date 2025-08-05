package javax.accessibility;

/* loaded from: rt.jar:javax/accessibility/AccessibleHyperlink.class */
public abstract class AccessibleHyperlink implements AccessibleAction {
    public abstract boolean isValid();

    @Override // javax.accessibility.AccessibleAction
    public abstract int getAccessibleActionCount();

    @Override // javax.accessibility.AccessibleAction
    public abstract boolean doAccessibleAction(int i2);

    @Override // javax.accessibility.AccessibleAction
    public abstract String getAccessibleActionDescription(int i2);

    public abstract Object getAccessibleActionObject(int i2);

    public abstract Object getAccessibleActionAnchor(int i2);

    public abstract int getStartIndex();

    public abstract int getEndIndex();
}
