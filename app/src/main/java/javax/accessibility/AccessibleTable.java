package javax.accessibility;

/* loaded from: rt.jar:javax/accessibility/AccessibleTable.class */
public interface AccessibleTable {
    Accessible getAccessibleCaption();

    void setAccessibleCaption(Accessible accessible);

    Accessible getAccessibleSummary();

    void setAccessibleSummary(Accessible accessible);

    int getAccessibleRowCount();

    int getAccessibleColumnCount();

    Accessible getAccessibleAt(int i2, int i3);

    int getAccessibleRowExtentAt(int i2, int i3);

    int getAccessibleColumnExtentAt(int i2, int i3);

    AccessibleTable getAccessibleRowHeader();

    void setAccessibleRowHeader(AccessibleTable accessibleTable);

    AccessibleTable getAccessibleColumnHeader();

    void setAccessibleColumnHeader(AccessibleTable accessibleTable);

    Accessible getAccessibleRowDescription(int i2);

    void setAccessibleRowDescription(int i2, Accessible accessible);

    Accessible getAccessibleColumnDescription(int i2);

    void setAccessibleColumnDescription(int i2, Accessible accessible);

    boolean isAccessibleSelected(int i2, int i3);

    boolean isAccessibleRowSelected(int i2);

    boolean isAccessibleColumnSelected(int i2);

    int[] getSelectedAccessibleRows();

    int[] getSelectedAccessibleColumns();
}
