package javax.accessibility;

/* loaded from: rt.jar:javax/accessibility/AccessibleSelection.class */
public interface AccessibleSelection {
    int getAccessibleSelectionCount();

    Accessible getAccessibleSelection(int i2);

    boolean isAccessibleChildSelected(int i2);

    void addAccessibleSelection(int i2);

    void removeAccessibleSelection(int i2);

    void clearAccessibleSelection();

    void selectAllAccessibleSelection();
}
